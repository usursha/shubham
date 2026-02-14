package com.hpy.uam.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hpy.rest.dto.IResponseDto;
import com.hpy.uam.dto.AggUserRepresentationRequestDto;
import com.hpy.uam.dto.BooleanResponseDto;
import com.hpy.uam.dto.BulkUserCreationRequestDto;
import com.hpy.uam.dto.BulkUserRequestDto;
import com.hpy.uam.dto.ClientRepresentationListResponseDto;
import com.hpy.uam.dto.ClientRepresentationResponseDto;
import com.hpy.uam.dto.GenericMessageResponseDto;
import com.hpy.uam.dto.OrganizationHierarchyListDto;
import com.hpy.uam.dto.ProfilePictureRequestDto;
import com.hpy.uam.dto.ProfilePictureResponseDto;
import com.hpy.uam.dto.SetPasswordRequestDto;
import com.hpy.uam.dto.StatusCodeResponseDto;
import com.hpy.uam.dto.UserMobileResponseDto;
import com.hpy.uam.dto.UserRepresentationListResponseDto;
import com.hpy.uam.dto.UserSessionListResponseDto;
import com.hpy.uam.dto.UsernameRequestDto;
import com.hpy.uam.entity.Image;
import com.hpy.uam.repo.ImageRepository;
import com.hpy.uam.service.ImageService;
import com.hpy.uam.service.UserRegistrationService;
import com.hpy.uam.util.RestUtilsImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user-service")
@CrossOrigin("${app.cross-origin.allow}")
public class UserController {

	@Autowired
	private UserRegistrationService userRegistrationService;

	@Autowired
	private RestUtilsImpl restutil;

	@Autowired
	private ImageService imageService;

	@Autowired
	private ImageRepository imageRepository;

	@Value("${basePath}")
	private String basePath;

	@PostMapping("/create")
	public ResponseEntity<IResponseDto> addUser(@Valid @RequestBody BulkUserCreationRequestDto user) {
		StatusCodeResponseDto status = userRegistrationService.createUser(user);
		return ResponseEntity.ok(restutil.wrapResponse(status, "success"));
	}

	@GetMapping("/get/{username}")
	public ResponseEntity<IResponseDto> getUserById(@PathVariable String username) {
//		ResponseDto<AggUserRepresentationRequestDto> response = new ResponseDto<>() ;
		AggUserRepresentationRequestDto response = userRegistrationService.getUserWithRolesAndGroups(username);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

	@PutMapping("/update/{username}")
	public ResponseEntity<IResponseDto> updateUserByName(@PathVariable String username,
			@Valid @RequestBody BulkUserCreationRequestDto request) {
		GenericMessageResponseDto response = userRegistrationService.updateUser(username, request);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));

	}

	@DeleteMapping("/delete")
	public ResponseEntity<IResponseDto> deleteUser(@Valid @RequestBody UsernameRequestDto requestDto) {
		GenericMessageResponseDto response = userRegistrationService.deleteUser(requestDto);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

	@PostMapping("/activateAndDeactivateUser")
	public ResponseEntity<IResponseDto> activateAndDeactivateUser(@Valid @RequestBody UsernameRequestDto usernameDto) {
		GenericMessageResponseDto response = userRegistrationService.userEnableAndDisable(usernameDto);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

	@PostMapping("/verify-usermail")
	public ResponseEntity<IResponseDto> verUserEMail(@Valid @RequestBody UsernameRequestDto usernameDto) {
		GenericMessageResponseDto response = userRegistrationService.verifyEmail(usernameDto);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

	// testing
//	@PostMapping("/verify-mail")
//	public String verifyMail(@Valid @RequestBody UsernameRequestDto usernameDto) {
//		return userRegistrationService.verifyEmail(usernameDto);
//	}

	@GetMapping("/list")
	public ResponseEntity<IResponseDto> getListOfUsers() {
		UserRepresentationListResponseDto response = userRegistrationService.getAllUsers();
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

	@GetMapping("/client-session/{id}")
	public ResponseEntity<IResponseDto> getSessions(@PathVariable String id) {
		ClientRepresentationResponseDto response = userRegistrationService.getClientSession(id);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

	@GetMapping("/getClient/{clientId}")
	public ResponseEntity<IResponseDto> getSession(@PathVariable String clientId) {
		ClientRepresentationListResponseDto response = userRegistrationService.getClients(clientId);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

	@GetMapping("/user-session/{username}")
	public ResponseEntity<IResponseDto> userSession(@PathVariable String username) {
		UserSessionListResponseDto response = userRegistrationService.getUserSession(username);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

	@GetMapping("/getUserWithRolesAndGroup/{username}")
	public ResponseEntity<IResponseDto> getList(@PathVariable String username) {
		AggUserRepresentationRequestDto response = userRegistrationService.getUserWithRolesAndGroups(username);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

	@GetMapping("/status/{username}")
	public ResponseEntity<IResponseDto> isUserAvailable(@PathVariable @Valid String username) {
		BooleanResponseDto response = userRegistrationService.getUserStatus(username);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

	// no need to modify response as it's being called from asset service
//	@PostMapping("/addProfilePhoto")
//	public ResponseEntity<StatusMessageResponseDto> addProfilePhoto(@RequestBody @Valid ProfilePictureRequestDto image) {
//		return ResponseEntity.ok(userRegistrationService.saveProfilePhoto(image));
//	}

	@PostMapping("/addProfilePhoto")
	public ResponseEntity<IResponseDto> addProfilePhoto(@RequestBody @Valid ProfilePictureResponseDto image) {
		GenericMessageResponseDto response = userRegistrationService.saveProfilePhoto(image);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

	// no need to modify response as it's being called from asset service
//	@GetMapping("/profilePicture")
//	public ResponseEntity<ProfilePictureRequestDto> addProfilePhoto() {
//		return ResponseEntity.ok(userRegistrationService.getProfilePhoto());
//	}

	@GetMapping("/profilePicture")
	public ResponseEntity<IResponseDto> getProfilePicture() {
		ProfilePictureResponseDto response = userRegistrationService.getProfilePhoto();
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

	@PostMapping("/user-profilePicture")
	public ResponseEntity<IResponseDto> getUserProfilePicture(@RequestBody UsernameRequestDto request) {
		ProfilePictureResponseDto response = userRegistrationService.getProfilePhotoByUsername(request);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

	@PostMapping("/userMobile")
	public ResponseEntity<IResponseDto> getMobile(@RequestBody UsernameRequestDto usernameDto) {
		UserMobileResponseDto response = userRegistrationService.getMobileNumber(usernameDto);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

	@PostMapping("/set-password")
	public ResponseEntity<IResponseDto> status(@Valid @RequestBody SetPasswordRequestDto setPasswordDto) {
		GenericMessageResponseDto response = userRegistrationService.setPasswordByAdmin(setPasswordDto);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

//	 taking excel directly
	@PostMapping("/bulk-upload-using-excel")
	public ResponseEntity<IResponseDto> convertExcelToJson(@RequestParam("file") @Valid MultipartFile file)
			throws IOException {
		GenericMessageResponseDto response = userRegistrationService.uploadFile(file);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

//
	@PostMapping("/bulk-upload")
	public ResponseEntity<IResponseDto> convertExcelToJson(@Valid @RequestBody BulkUserRequestDto requestDto)
			throws IOException {
		GenericMessageResponseDto response = userRegistrationService.getFile(requestDto);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

	@GetMapping("/total-user-count")
	public ResponseEntity<Integer> getUserPassword() {
		return ResponseEntity.ok(userRegistrationService.getTotalUserCount());
	}

	@GetMapping("/total-ce-user-count")
	public ResponseEntity<Integer> getTotalCE() {
		return ResponseEntity.ok(userRegistrationService.getCEUsersCount());
	}

	@GetMapping("/total-cm-user-count")
	public ResponseEntity<Integer> getTotalCM() {
		return ResponseEntity.ok(userRegistrationService.getCMUsersCount());
	}

	@GetMapping("/total-cm-users")
	public ResponseEntity<List> getAllCMList() {
		return ResponseEntity.ok(userRegistrationService.getCMUsersList());
	}

	@GetMapping("/total-ce-users")
	public ResponseEntity<List> getAllCEList() {
		return ResponseEntity.ok(userRegistrationService.getCEUsersList());
	}

	@PostMapping("/upload-image")
	public ResponseEntity<IResponseDto> uploadProfileImage(
			@RequestBody ProfilePictureRequestDto profilePictureRequestDto) throws IOException {
		String uploadImage = imageService.uploadOrUpdateImage(profilePictureRequestDto);
		return ResponseEntity.ok(restutil.wrapResponse(uploadImage, "success"));
	}

	@GetMapping("/view/{filename:.+}")
	public ResponseEntity<byte[]> viewImage(@PathVariable String filename) {
		Path filePath = Paths.get(basePath).resolve(filename).normalize();

		try {
			byte[] imageBytes = Files.readAllBytes(filePath);

			Optional<Image> imageOpt = imageRepository.findByFileName(filename);
			String contentType = imageOpt.map(Image::getMediaType).orElseGet(() -> {
				try {
					return Files.probeContentType(filePath);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return filename;
			});

			if (contentType == null) {
				contentType = "application/octet-stream";
			}

			return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(imageBytes);
		} catch (IOException e) {
			log.error("File not found or failed to read: {}", filename, e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/get")
	public ResponseEntity<IResponseDto> getReportingHierarchy() {
		OrganizationHierarchyListDto response = userRegistrationService.getReportingHierarchy();
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

	@PostMapping("/delete/{imageId}")
	public ResponseEntity<IResponseDto> deleteImage(@PathVariable String imageId) {
		String deleteImage = imageService.deleteImage(imageId);
		return ResponseEntity.ok(restutil.wrapResponse(deleteImage, "success"));
	}
//	file changes
}
