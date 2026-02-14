package com.hpy.uam.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.hpy.rest.exception.ConflictException;
import com.hpy.rest.exception.InternalServerErrorException;
import com.hpy.uam.dto.AggUserRepresentationRequestDto;
import com.hpy.uam.dto.BooleanResponseDto;
import com.hpy.uam.dto.BulkUserCreationRequestDto;
import com.hpy.uam.dto.BulkUserCreationResponseDto;
import com.hpy.uam.dto.BulkUserRequestDto;
import com.hpy.uam.dto.ClientRepresentationListResponseDto;
import com.hpy.uam.dto.ClientRepresentationResponseDto;
import com.hpy.uam.dto.GenericMessageResponseDto;
import com.hpy.uam.dto.OrganizationHierarchyListDto;
import com.hpy.uam.dto.PortalPersonalDetailsDto;
import com.hpy.uam.dto.PortalWorkMetricsDto;
import com.hpy.uam.dto.ProfilePictureResponseDto;
import com.hpy.uam.dto.SetPasswordRequestDto;
import com.hpy.uam.dto.StatusCodeResponseDto;
import com.hpy.uam.dto.UserMobileResponseDto;
import com.hpy.uam.dto.UserRepresentationListResponseDto;
import com.hpy.uam.dto.UserSessionListResponseDto;
import com.hpy.uam.dto.UsernameRequestDto;
import com.hpy.uam.entity.Image;
import com.hpy.uam.entity.OrganizationHierarchy;
import com.hpy.uam.entity.UserReportingHierarchy;
import com.hpy.uam.repo.ImageRepository;
import com.hpy.uam.repo.OrganizationHierarchyRepository;
import com.hpy.uam.repo.UserDetailsRepo;
import com.hpy.uam.repo.UserReportingHierarchyRepository;
import com.hpy.uam.util.MethodUtil;
import com.hpy.uam.util.PasswordUtils;
import com.hpy.uam.util.UploadUtil;

import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserRegistrationService {

	@Value("${keycloak.realm}")
	private String realm;

	@Value("${keycloak.adminClientId}")
	private String adminClientId;

	@Value("${response.columns}")
	private String responseColumns;

	private MethodUtil methodUtil;

	private Keycloak keycloak;

	private UploadUtil uploadUtil;

	private PasswordUtils passwordUtils;

	private EmailService emailService;

	private GroupService groupService;

	private String pwd = "";

	@Autowired
	private MethodUtil utilmethod;

	@Autowired
	private UserDetailsRepo repo;

	private String randomPassword;

	@Autowired
	private OrganizationHierarchyRepository hierarchyRepository;

	@Autowired
	private UserReportingHierarchyRepository userReportingHierarchyRepository;

	@Autowired
	private ImageRepository imageRepository;

	public UserRegistrationService(MethodUtil methodUtil, Keycloak keycloak, PasswordUtils passwordUtils,
			EmailService emailService, GroupService groupService, UploadUtil uploadUtil) {
		this.methodUtil = methodUtil;
		this.keycloak = keycloak;
		this.passwordUtils = passwordUtils;
		this.emailService = emailService;
		this.groupService = groupService;
		this.uploadUtil = uploadUtil;
	}

	public StatusCodeResponseDto createUser(BulkUserCreationRequestDto request) {// throws ForbiddenException,
																					// ConflictException,
																					// InternalServerErrorException {
		StatusCodeResponseDto status = new StatusCodeResponseDto();
		log.info("userRegistration:{}", request);
		UserRepresentation user = new UserRepresentation();
		buildUserObject(request, user);
		Response response = methodUtil.getUsersResource().create(user);
		if (response.getStatus() == 201) {
			String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
			user.setId(userId);
			UserRepresentation userRepresentation = methodUtil.getUsersResource().get(userId).toRepresentation();
//			String username=userRepresentation.getUsername();
			setPassword(user, false);
			log.info("pwd created " + pwd);
			status.setCode(response.getStatus());
			status.setMessage("User Created: " + userId);
			return status;
		} else if (response.getStatus() == 403) {
			status.setCode(response.getStatus());
			status.setMessage("Permission Denied!!..");
			throw new AccessDeniedException("Access Denied!!");
//			return status;
		} else if (response.getStatus() == 409) {
			status.setCode(response.getStatus());
			status.setMessage("User Already Exist..");
			throw new ConflictException("User Already Exist");
//			return status;
		} else if (response.getStatus() == 500) {
			status.setCode(response.getStatus());
			status.setMessage("Could not find any group with this name!!");
			throw new InternalServerErrorException("Could not find any group with this name!!");
//			return status;
		}
		return status;
	}

	private void buildUserObject(BulkUserCreationRequestDto request, UserRepresentation user) {
		user.setEnabled(true);
		user.setUsername(request.getUsername().toLowerCase());
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());
		user.setEmailVerified(false);
		List<String> groupName = new ArrayList<>();
		groupName.add(request.getGroupName());
		Map<String, List<String>> attributes = new HashMap<>();
		attributes.put("Mobile", Collections.singletonList(request.getMobile()));
		user.setGroups(groupName);
		user.setAttributes(attributes);
	}

//	public void setUsernameAsPassword(UserRepresentation user, boolean isTemporary, String password) {
//		CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
//		pwd=password;
//		log.info(pwd);
//		credentialRepresentation.setValue(pwd);
//		credentialRepresentation.setTemporary(isTemporary);
//		credentialRepresentation.setType(CredentialRepresentation.PASSWORD); 
//		List<CredentialRepresentation> list = new ArrayList<>();
//		list.add(credentialRepresentation);
//		user.setCredentials(list);
//		methodUtil.getUsersResource().get(user.getId()).update(user);
//	}

	// forcefully, password set by admin for any user
	public GenericMessageResponseDto setPasswordByAdmin(SetPasswordRequestDto setPasswordDto) {
		String userId = methodUtil.getUserIdByUsername(setPasswordDto.getUsername());
		UserRepresentation user = methodUtil.getUsersResource().get(userId).toRepresentation();
		setPassword(user, false);
		emailService.sendResetPasswordEmail(user.getEmail(), pwd);
		GenericMessageResponseDto response = new GenericMessageResponseDto();
		response.setMessage("password set successfully!!");
		return response;
	}

//	public BooleanResponseDto isUserExists(String username) {
//		BooleanResponseDto booleanResponse=new BooleanResponseDto();
//		List<UserRepresentation> userRepresentation = methodUtil.getUsersResource().search(username);
//		if (!userRepresentation.isEmpty() && userRepresentation.get(0).getUsername().equals(username)) {
//			booleanResponse.setMessage(true);
//			return booleanResponse;
//		} else {
//			booleanResponse.setMessage(false);
//			return booleanResponse;
//		}
//	}

	public BooleanResponseDto getUserStatus(String username) {
		BooleanResponseDto response = new BooleanResponseDto();
		String userId = methodUtil.getUserIdByUsername(username);
		UserResource userResource = methodUtil.getUsersResource().get(userId);
		UserRepresentation user = userResource.toRepresentation();
		response.setStatus(user.isEnabled());
		return response;
	}

	public AggUserRepresentationRequestDto getUserWithRolesAndGroups(String username) {
		String id = methodUtil.getUserIdByUsername(username);
		UserRepresentation user = methodUtil.getUsersResource().get(id).toRepresentation();
		List<GroupRepresentation> group = keycloak.realm(realm).users().get(id).groups();
		String groupId = group.stream().map(GroupRepresentation::getId).toList().get(0);
		List<RoleRepresentation> realmRole = keycloak.realm(realm).groups().group(groupId).roles().realmLevel()
				.listAll();
		return new AggUserRepresentationRequestDto(user, realmRole, group);

	}

	public GenericMessageResponseDto deleteUser(UsernameRequestDto requestDto) {
		GenericMessageResponseDto responseDto = new GenericMessageResponseDto();
		String userId = methodUtil.getUserIdByUsername(requestDto.getUsername());
		methodUtil.getUsersResource().get(userId).remove();
		responseDto.setMessage("user deleted successfully..");
		return responseDto;
	}

	public GenericMessageResponseDto updateUser(String username, BulkUserCreationRequestDto request) {
		log.info("userRegistration:{}", request);
		String userId = methodUtil.getUserIdByUsername(username);
		GenericMessageResponseDto status = new GenericMessageResponseDto();
		UserRepresentation existUser = keycloak.realm(realm).users().get(userId).toRepresentation();
		if (Boolean.TRUE.equals(existUser.isEnabled())) {
			existUser.setFirstName(request.getFirstName());
			existUser.setLastName(request.getLastName());
			existUser.setEmail(request.getEmail());
			Map<String, List<String>> attributes = existUser.getAttributes();
			if (attributes == null) {
				attributes = new HashMap<>();
			}
			attributes.put("Mobile", Collections.singletonList(request.getMobile()));
			log.info("attributes:{}", attributes);
			existUser.setAttributes(attributes);
			String groupId = methodUtil.getGroupIdByGroupName(request.getGroupName());
			groupService.addingUserToGroup(userId, groupId);
			methodUtil.getUsersResource().get(userId).update(existUser);
			status.setMessage("details updated successfully..");
			return status;
		} else {
			status.setMessage("Group not found!!");
		}
		return status;
	}

	public void setPassword(UserRepresentation user, boolean isTemporary) {
		setPassword(user, isTemporary, null);
	}

	public void setPassword(UserRepresentation user, boolean isTemporary, String password) {
		CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
		if (password == null) {
			pwd = passwordUtils.generatedRandomPassword();
			// pwd = "randomPassword";
		} else {
			pwd = password;
		}
		setRandomPassword(pwd);
		credentialRepresentation.setValue(pwd);
		credentialRepresentation.setTemporary(isTemporary);
		credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
		LocalDateTime localDateTime = LocalDateTime.now();
		long epochMilli = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		credentialRepresentation.setCreatedDate(epochMilli);
		log.info("Password creation time:: {}", credentialRepresentation.getCreatedDate());
		List<CredentialRepresentation> list = new ArrayList<>();
		list.add(credentialRepresentation);
		user.setCredentials(list);
		// methodUtil.getUsersResource().get(user.getId()).update(user);
		methodUtil.getUsersResource().get(user.getId()).resetPassword(credentialRepresentation);

	}

	public GenericMessageResponseDto userEnableAndDisable(UsernameRequestDto requestDto) {
		GenericMessageResponseDto response = new GenericMessageResponseDto();
		String userId = methodUtil.getUserIdByUsername(requestDto.getUsername());
		UserResource userResource = methodUtil.getUsersResource().get(userId);
		UserRepresentation user = userResource.toRepresentation();
		if (user.isEnabled()) {
			user.setEnabled(false);
			userResource.update(user);
			response.setMessage("User Disabled");
		} else {
			user.setEnabled(true);
			userResource.update(user);
			response.setMessage("User Enabled");
		}
		return response;
	}

	public GenericMessageResponseDto verifyEmail(UsernameRequestDto usernameDto) {
		String userId = methodUtil.getUserIdByUsername(usernameDto.getUsername());
		methodUtil.getUsersResource().get(userId).sendVerifyEmail();
		GenericMessageResponseDto response = new GenericMessageResponseDto();
		response.setMessage("verification email sent to user");
		return response;
	}

	public UserRepresentationListResponseDto getAllUsers() {
		UserRepresentationListResponseDto list = new UserRepresentationListResponseDto();
		list.setList(methodUtil.getUsersResource().list());
		return list;
	}

	public ClientRepresentationListResponseDto getClients(String clientId) {
		ClientRepresentationListResponseDto list = new ClientRepresentationListResponseDto();
		list.setClientRepresentation(methodUtil.getClientsResource().findByClientId(clientId));
		return list;
	}

	public ClientRepresentationResponseDto getClientSession(String id) {
		ClientRepresentationResponseDto response = new ClientRepresentationResponseDto();
		response.setClientRepresentation(methodUtil.getClientsResource().get(id).toRepresentation());
		return response;
	}

	public UserSessionListResponseDto getUserSession(String username) {
		UserSessionListResponseDto userSession = new UserSessionListResponseDto();
		String userId = methodUtil.getUserIdByUsername(username);
		userSession.setUserSessionRepresentation(methodUtil.getUsersResource().get(userId).getUserSessions());
		return userSession;
	}

	private String saveUser(BulkUserCreationRequestDto request) {
		log.info("userRegistration:{}", request);
		UserRepresentation user = new UserRepresentation();
		buildUserObject(request, user);
		user.setEnabled(true);
		Response response = methodUtil.getUsersResource().create(user);
		if (response.getStatus() == 201) {
			String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
			user.setId(userId);
//			setPassword(user, false, userId);
			UserRepresentation userRepresentation = methodUtil.getUsersResource().get(userId).toRepresentation();
			String username = userRepresentation.getUsername();
			setPassword(user, false, userId);
			return userId;
		}
		return null;
	}

	public GenericMessageResponseDto saveProfilePhoto(ProfilePictureResponseDto image) {
		GenericMessageResponseDto status = new GenericMessageResponseDto();
		String userId = methodUtil.getLoggedInUserId();
		UserRepresentation user = methodUtil.getUsersResource().get(userId).toRepresentation();
		if (Boolean.TRUE.equals(user.isEnabled())) {
			Map<String, List<String>> attributes = new HashMap<>();
			attributes.put("Photo", Collections.singletonList(image.getImageId()));
			user.setAttributes(attributes);
			methodUtil.getUsersResource().get(userId).update(user);
			status.setMessage("Photo added successfully");
		} else {
			status.setMessage("profile photo cannot be null!!");
		}
		return status;
	}

	public ProfilePictureResponseDto getProfilePhoto() {
		ProfilePictureResponseDto pic = new ProfilePictureResponseDto();
		String userId = methodUtil.getLoggedInUserId();
		UserRepresentation user = methodUtil.getUsersResource().get(userId).toRepresentation();
		String picture = user.getAttributes().get("Photo").get(0);
		pic.setImageId(picture);
		return pic;
	}

	public ProfilePictureResponseDto getProfilePhotoByUsername(UsernameRequestDto request) {
		ProfilePictureResponseDto pic = new ProfilePictureResponseDto();
		String userId = methodUtil.getUserIdByUsername(request.getUsername());
		UserRepresentation user = methodUtil.getUsersResource().get(userId).toRepresentation();
		String picture = user.getAttributes().get("Photo").get(0);
		pic.setImageId(picture);
		return pic;
	}

	public UserMobileResponseDto getMobileNumber(UsernameRequestDto usernameDto) {
		UserMobileResponseDto usermobile = new UserMobileResponseDto();
		String userId = methodUtil.getUserIdByUsername(usernameDto.getUsername());
		UserRepresentation user = methodUtil.getUsersResource().get(userId).toRepresentation();
		String mobile = user.getAttributes().get("Mobile").get(0);
		usermobile.setMobile(mobile);
		return usermobile;
	}

	private StatusCodeResponseDto createBulkUser(BulkUserCreationRequestDto request) {// throws ForbiddenException,
																						// ConflictException,
																						// InternalServerErrorException
																						// {
		StatusCodeResponseDto status = new StatusCodeResponseDto();
		log.info("userRegistration:{}", request);
		UserRepresentation user = new UserRepresentation();
		buildUserObject(request, user);
		Response response = methodUtil.getUsersResource().create(user);
		if (response.getStatus() == 201) {
			String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
			user.setId(userId);
			UserRepresentation userRepresentation = methodUtil.getUsersResource().get(userId).toRepresentation();
//			String username=userRepresentation.getUsername();
			setPassword(user, false);
			emailService.sendResetPasswordEmail(user.getEmail(), pwd);
			status.setCode(response.getStatus());
			status.setMessage("User Created Successfully..");
			return status;
		} else if (response.getStatus() == 403) {
			status.setCode(response.getStatus());
			status.setMessage("Permission Denied!!..");
			return status;
		} else if (response.getStatus() == 409) {
			status.setCode(response.getStatus());
			status.setMessage("User Already Exist..");
			return status;
		} else if (response.getStatus() == 500) {
			status.setCode(response.getStatus());
			status.setMessage("Could not find any group with this name!!");
			return status;
		}
		return status;
	}

	public GenericMessageResponseDto addUsers(List<BulkUserCreationRequestDto> userList) throws IOException {
		List<BulkUserCreationResponseDto> result = new ArrayList<>();
		for (BulkUserCreationRequestDto request : userList) {
			BulkUserCreationResponseDto res = new BulkUserCreationResponseDto();
			StatusCodeResponseDto status = new StatusCodeResponseDto();
			status = createBulkUser(request);
			if (status.getCode() == 201) {
				res.setUsername(request.getUsername().toLowerCase());
				res.setFirstName(request.getFirstName());
				res.setLastName(request.getLastName());
				res.setEmail(request.getEmail());
				res.setMobile(request.getMobile());
				res.setGroupName(request.getGroupName());
				res.setStatus("User Created Successfully");
				result.add(res);
			} else if (status.getCode() == 409) {
				res.setUsername(request.getUsername().toLowerCase());
				res.setFirstName(request.getFirstName());
				res.setLastName(request.getLastName());
				res.setEmail(request.getEmail());
				res.setMobile(request.getMobile());
				res.setGroupName(request.getGroupName());
				res.setStatus("User already exist!!");
				result.add(res);
			} else if (status.getCode() == 500) {
				res.setUsername(request.getUsername().toLowerCase());
				res.setFirstName(request.getFirstName());
				res.setLastName(request.getLastName());
				res.setEmail(request.getEmail());
				res.setMobile(request.getMobile());
				res.setGroupName(request.getGroupName());
				res.setStatus("Could not find any group with this name!!");
				result.add(res);
			}
		}
		XSSFWorkbook excel = generateExcel(result);
		GenericMessageResponseDto data = new GenericMessageResponseDto();
		data.setMessage(convertToBase64(excel));
		return data;
	}

	public GenericMessageResponseDto uploadFile(MultipartFile file) throws IOException {
		Workbook workbook = new XSSFWorkbook(file.getInputStream());
		Sheet sheet = workbook.getSheetAt(0);
		Supplier<Stream<Row>> rowStreamSupplier = uploadUtil.getRowStreamSupplier(sheet);
		Row headerRow = rowStreamSupplier.get().findFirst().get();
		List<String> headerCells = StreamSupport.stream(headerRow.spliterator(), false).map(Cell::getStringCellValue)
				.collect(Collectors.toList());
		int colCount = headerCells.size();
		List<Map<String, String>> data = rowStreamSupplier.get().skip(1).map(row -> {
			List<String> cellList = StreamSupport.stream(row.spliterator(), false).map(Cell::getStringCellValue)
					.collect(Collectors.toList());
			Map<String, String> cellMap = IntStream.range(0, colCount).boxed()
					.collect(Collectors.toMap(index -> headerCells.get(index), index -> cellList.get(index)));
			return cellMap;
		}).collect(Collectors.toList());
		List<BulkUserCreationRequestDto> json = convertList(data);
		log.info("json() {}", json);
		return addUsers(json);
	}

	private List<BulkUserCreationRequestDto> convertList(List<Map<String, String>> list) {
		List<BulkUserCreationRequestDto> result = new ArrayList<>();
		for (Map<String, String> map : list) {
			result.add(BulkUserCreationRequestDto.fromMap(map));
		}
		return result;
	}

	public static List<Map<String, String>> convertListToMapList(List<BulkUserCreationResponseDto> list) {
		return list.stream().map(a -> {
			Map<String, String> map = new HashMap<>();
			map.put("username", a.getUsername());
			map.put("firstName", a.getFirstName());
			map.put("lastName", a.getLastName());
			map.put("email", a.getEmail());
			map.put("groupName", a.getGroupName());
			map.put("mobile", a.getMobile());
//            map.put("statusCode", String.valueOf(a.getStatusCode())); 
			map.put("status", a.getStatus());
			return map;
		}).collect(Collectors.toList());
	}

	public XSSFWorkbook generateExcel(List<BulkUserCreationResponseDto> bulkUserCreationResponseDto) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("UserFile");
		log.info("response:{}", bulkUserCreationResponseDto);
		Row headerRow = sheet.createRow(0);
		List<String> columns = Arrays.asList(responseColumns.split(","));
		for (int i = 0; i < columns.size(); i++) {
			headerRow.createCell(i).setCellValue(columns.get(i));
		}
		int rowNum = 1;
		for (BulkUserCreationResponseDto response : bulkUserCreationResponseDto) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(response.getUsername());
			row.createCell(1).setCellValue(response.getFirstName());
			row.createCell(2).setCellValue(response.getLastName());
			row.createCell(3).setCellValue(response.getEmail());
			row.createCell(4).setCellValue(response.getGroupName());
			row.createCell(5).setCellValue(response.getMobile());
//            row.createCell(6).setCellValue(response.getStatusCode());
			row.createCell(7).setCellValue(response.getStatus());
		}
		return workbook;
	}

	private String convertToBase64(XSSFWorkbook workbook) throws IOException {
		workbook.createSheet("Sheet 1");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		byte[] bytes = bos.toByteArray();
		String base64String = Base64.getEncoder().encodeToString(bytes);
		bos.close();
		workbook.close();
		return base64String;
	}

	public GenericMessageResponseDto getFile(@RequestBody BulkUserRequestDto requestDto) throws IOException {
		byte[] decodedBytes = Base64.getDecoder().decode(requestDto.getBase64String());
		MultipartFile multipartFile = new MockMultipartFile("file", requestDto.getFilename(),
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", decodedBytes);
		GenericMessageResponseDto response = uploadFile(multipartFile);
		return response;
	}

	public int getTotalUserCount() {
		return repo.getTotalUserCount(realm);
	}

	public int getCMUsersCount() {
		return repo.getCMUsersCountsInGroup(realm);
	}

	public int getCEUsersCount() {
		return repo.getCEUsersCountsInGroup(realm);
	}

	public List<String> getCEUsersList() {
		return repo.getCEUsersInGroup(realm);
	}

	public List<String> getCMUsersList() {
		return repo.getCMUsersInGroup(realm);
	}

	public void setRandomPassword(String randomPassword) {
		this.randomPassword = randomPassword;
	}

	public String getRandomPassword() {
		return randomPassword;
	}

	private PortalWorkMetricsDto getUserWorkMetrics(String username) {
		PortalWorkMetricsDto workmetrics = new PortalWorkMetricsDto();
		workmetrics.setEmployeeId(userReportingHierarchyRepository.getEmployeeIdByUsername(username));
		workmetrics.setWorkEmailAddress(userReportingHierarchyRepository.getEmailIdByUsername(username));
		workmetrics.setDateOfJoining(null);
		workmetrics.setArea(null);
		workmetrics.setAtmAssigned(userReportingHierarchyRepository.getTotalAtmAssigned(username));
		workmetrics.setChannelExecutivesAssigned(userReportingHierarchyRepository.getTotalCEAssigned(username));
		return workmetrics;
	}

	private PortalPersonalDetailsDto getPersonalDetails(String username) {
		PortalPersonalDetailsDto personalDetails = new PortalPersonalDetailsDto();
		personalDetails.setFullName(userReportingHierarchyRepository.getfullnamedByUsername(username));
		personalDetails.setDateOfBirth(null);
		personalDetails.setPhoneNumber(userReportingHierarchyRepository.getUserMobiledByUsername(username));
		personalDetails.setPersonalEmailAddress(null);
		Optional<Image> image = imageRepository.findByUsername(username);
		if (image.isPresent()) {
			personalDetails.setProfileImageId(image.get().getFileName());
		}
		personalDetails.setPermanentAddress(null);

		personalDetails.setCurrentAddress(userReportingHierarchyRepository.getHomeAddressdByUsername(username));
		return personalDetails;

	}

	public OrganizationHierarchyListDto getReportingHierarchy() {
		log.info("fetching user details");
		String username = utilmethod.getLoggedInUserName();
		OrganizationHierarchyListDto organizationHierarchyList = new OrganizationHierarchyListDto();
		List<OrganizationHierarchy> hierarchyList = new ArrayList<>();
		UserReportingHierarchy reportinghierarchy = userReportingHierarchyRepository.getUserReportingHead(username);
		OrganizationHierarchy cm = hierarchyRepository.getHierarchyByUsername(reportinghierarchy.getCmUserId());
		OrganizationHierarchy scm = hierarchyRepository.getHierarchyByUsername(reportinghierarchy.getScmUserId());
		OrganizationHierarchy rcm = hierarchyRepository.getHierarchyByUsername(reportinghierarchy.getRcmUserId());
		hierarchyList.add(cm);
		hierarchyList.add(scm);
		hierarchyList.add(rcm);
		organizationHierarchyList.setOrganizationHierarchy(hierarchyList);
		organizationHierarchyList.setWorkDetails(getUserWorkMetrics(username));
		organizationHierarchyList.setPersonalDetails(getPersonalDetails(username));
		return organizationHierarchyList;
	}
	
	

}
