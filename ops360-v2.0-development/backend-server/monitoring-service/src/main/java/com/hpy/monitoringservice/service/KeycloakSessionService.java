package com.hpy.monitoringservice.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.UserSessionRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.client.RestTemplate;

import com.hpy.monitoringservice.dto.ActiveCEUsersDetails;
import com.hpy.monitoringservice.dto.ActiveCMUsersDetails;
import com.hpy.monitoringservice.dto.CEUsersDetailsDto;
import com.hpy.monitoringservice.dto.CountDto;
import com.hpy.monitoringservice.dto.GenericMessageDto;
import com.hpy.monitoringservice.dto.HistoryDto;
import com.hpy.monitoringservice.dto.RepresentationDto;
import com.hpy.monitoringservice.dto.SevenDaysResponseData;
import com.hpy.monitoringservice.dto.TimeRequestDto;
import com.hpy.monitoringservice.dto.UserDataDto;
import com.hpy.monitoringservice.dto.UsersDto;
import com.hpy.monitoringservice.entity.UserSessionsEntity;
import com.hpy.monitoringservice.repository.SessionRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@EnableScheduling
public class KeycloakSessionService {

	@Value("${keycloak.realm}")
	private String realm;

	@Value("${keycloak.adminClientId}")
	private String clientId;

	@Value("${user-range.from}")
	private int start;

	@Value("${user-range.to}")
	private int batchSize;

	@Value("${keycloak.allUserCount}")
	private String totalUserCountURL;

	@Value("${keycloak.totalCECount}")
	private String totalCECountURL;

	@Value("${keycloak.totalCMCount}")
	private String totalCMCountURL;

	@Value("${keycloak.allactivecmlist}")
	private String allactivecmlist;

	@Value("${keycloak.allactivecelist}")
	private String allactivecelist;

	@Value("${ce.colours}")
	private String ceColours;
	
	@Value("${ce.max-counts-colours}")
	private String ceMaxCountColours;
	
	@Value("${cm.colours}")
	private String cmColours;
	
	@Value("${cm.max-counts-colours}")
	private String cmMaxCountColours;
	
	@Autowired
	private Keycloak keycloak;

	@Autowired
	private SessionRepo repo;

	@Autowired
	private RestTemplate restTemplate;

	public List<UserSessionsEntity> getTotalSessionList() {
		List<UserSessionsEntity> response = new ArrayList<>();
		try {
			return repo.findAll();
		} catch (Exception e) {
			log.info(e.getMessage());
			response = null;
		}
		return response;
	}

	public RepresentationDto totalSessionCount() {
		RepresentationDto response = new RepresentationDto();
		try {
			response.setCount(getTotalSessionList().size());
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setCount(0);
		}
		return response;
	}

	public Set<UserSessionRepresentation> currentActiveUsersSet() {
		Set<UserSessionRepresentation> uniqueUserSessions = new HashSet<>();
		int first = 0;
		int maxResults = 100;
		boolean moreResults = true;
		while (moreResults) {
			moreResults = false;
			for (ClientRepresentation client : keycloak.realm(realm).clients().findAll()) {
				List<UserSessionRepresentation> userSessions = keycloak.realm(realm).clients().get(client.getId())
						.getUserSessions(first, maxResults);
				uniqueUserSessions.addAll(userSessions);
				if (userSessions.size() == maxResults) {
					moreResults = true;
				}
			}
			first += maxResults;
		}
		return uniqueUserSessions;
	}

	@Scheduled(cron = "${daily.scheduled.cron}")
	public GenericMessageDto storeActiveSessions() {
		GenericMessageDto response = new GenericMessageDto();
		UserSessionsEntity ceUser = new UserSessionsEntity();
		UserSessionsEntity cmUser = new UserSessionsEntity();
		UserSessionsEntity hdUser = new UserSessionsEntity();
		try {
			Set<UserSessionRepresentation> allUserSessions = currentActiveUsersSet();
			int i = 0;

			for (UserSessionRepresentation session : allUserSessions) {
				log.info("ith value is {} and user is {}", i, session.getUsername());
				if (repo.getCeUserList().contains(session.getUsername())) {
					ceUser.setId(session.getId());
					ceUser.setUserId(session.getUserId());
					ceUser.setUsername(session.getUsername());
					ceUser.setSessionStart(convertMilliSectoDateTime(session.getStart()));
					ceUser.setSessionEnd(convertMilliSectoDateTime(session.getLastAccess()));
					ceUser.setIpAddress(session.getIpAddress());
					ceUser.setUserType("MOBILE-APP");
					repo.save(ceUser);
				} else if (repo.getCmUserList().contains(session.getUsername())) {
					cmUser.setId(session.getId());
					cmUser.setUserId(session.getUserId());
					cmUser.setUsername(session.getUsername());
					cmUser.setSessionStart(convertMilliSectoDateTime(session.getStart()));
					cmUser.setSessionEnd(convertMilliSectoDateTime(session.getLastAccess()));
					cmUser.setIpAddress(session.getIpAddress());
					cmUser.setUserType("PORTAL");
					repo.save(cmUser);
				} else if (repo.getHdUserList().contains(session.getUsername())) {
					hdUser.setId(session.getId());
					hdUser.setUserId(session.getUserId());
					hdUser.setUsername(session.getUsername());
					hdUser.setSessionStart(convertMilliSectoDateTime(session.getStart()));
					hdUser.setSessionEnd(convertMilliSectoDateTime(session.getLastAccess()));
					hdUser.setIpAddress(session.getIpAddress());
					hdUser.setUserType("PORTAL-HANDLER-USER");
					repo.save(hdUser);
				}else {
					throw new NullPointerException();
				}
				i++;
			}
			response.setMessage("data saved successfully");
		}catch(NullPointerException npe) {
			log.info("user not registered!!!");
			response.setMessage("User not registered!!");
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setMessage("error during saving or fetching data!!");
		}
		return response;
	}

	private String convertMilliSectoDateTime(long milliseconds) {
		LocalDateTime dateTime = Instant.ofEpochMilli(milliseconds).atZone(ZoneId.systemDefault()).toLocalDateTime();
        String formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		return formattedDateTime;
	}

	public Set<UserSessionsEntity> getUserLoggedInLast7DaysList(TimeRequestDto request) {
		Set<UserSessionsEntity> data = new HashSet<>();
		try {
			for (int i = 0; i < 7; i++) {
				LocalDateTime currentDay = LocalDateTime.now().minusDays(i);
				LocalDateTime lastDay = currentDay.minusDays(1);
				String formattedlastDay = convertToFormattedDate(lastDay);
				String formattedCurrentDay = convertToFormattedDate(currentDay);
				Set<UserSessionsEntity> data2 = repo.getUserDetails(formattedlastDay, formattedCurrentDay, request.getTime());
				data.addAll(data2);
			}
			return data;

		} catch (InternalServerError ie) {
			log.info(ie.getMessage());
			return null;
		}
	}
	
	public SevenDaysResponseData getSevenDaysUserData(TimeRequestDto request) {
		int ceCount=0;
		int cmCount=0;
		UsersDto ceUserSet=new UsersDto();
		UsersDto cmUserSet=new UsersDto();
		Set<UsersDto> ceUser=new HashSet<>();
		Set<UsersDto> cmUser=new HashSet<>();
		HistoryDto ceHistory=new HistoryDto();
		HistoryDto cmHistory=new HistoryDto();
		Set<HistoryDto> ceHistorySet=new HashSet<>();
		Set<HistoryDto> cmHistorySet=new HashSet<>();
		SevenDaysResponseData data=new SevenDaysResponseData();
		Set<UserSessionsEntity> response=getUserLoggedInLast7DaysList(request);
		for(UserSessionsEntity entity : response) {
			if(repo.getCeUserList().contains(entity.getUsername())) {
				ceUserSet.setName(entity.getUsername());
				ceUser.add(ceUserSet);
				ceCount++;
				ceHistory.setCount(ceCount);
				ceHistory.setDate(entity.getSessionStart());
				ceHistory.setUsers(ceUser);
				ceHistorySet.add(ceHistory);
				data.setColor(ceColours);
				data.setMaxCountColor(ceMaxCountColours);
				data.setDesignation("CE");
				data.setHistory(ceHistorySet);
			}else if(repo.getCmUserList().contains(entity.getUsername())) {
				cmUserSet.setName(entity.getUsername());
				cmUser.add(cmUserSet);
				cmCount++;
				cmHistory.setCount(cmCount);
				cmHistory.setDate(entity.getSessionStart());
				cmHistory.setUsers(cmUser);
				cmHistorySet.add(cmHistory);
				data.setColor(cmColours);
				data.setMaxCountColor(cmMaxCountColours);
				data.setDesignation("CM");
				data.setHistory(cmHistorySet);
//			}else {
//				data=null;
			}
		}
		return data;
	}
	
	
	private HistoryDto setHistory(String Date, int count, Set<UsersDto> set) {
		HistoryDto dto=new HistoryDto();
		dto.setCount(count);
		dto.setDate(Date);
		dto.setUsers(set);
		return dto;
	}


	private List<UserRepresentation> userList() {
		List<UserRepresentation> response = new ArrayList<>();
		try {
			response = keycloak.realm(realm).users().list();
		} catch (Exception e) {
			log.info(e.getMessage());
			response = null;
		}
		return response;
	}

	private RepresentationDto totalUserCount() {
		RepresentationDto response = new RepresentationDto();
		try {
			response.setCount(userList().size());
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setCount(0);
		}
		return response;
	}

	private List<UserSessionsEntity> userCEList() {
		List<UserSessionsEntity> response = new ArrayList<>();
		try {
			response = repo.findAll().stream().filter(user -> user.getUserType().equals("MOBILE-APP")).toList();
		} catch (Exception e) {
			log.info(e.getMessage());
			response = null;
		}
		return response;
	}

	private List<UserSessionsEntity> userCMList() {
		List<UserSessionsEntity> response = new ArrayList<>();
		try {
			response = repo.findAll().stream().filter(user -> user.getUserType().equals("PORTAL")).toList();
		} catch (Exception e) {
			log.info(e.getMessage());
			response = null;
		}
		return response;
	}

	public CountDto allCount(String token) {
		CountDto counts = new CountDto();
		HttpHeaders headers = new HttpHeaders();
		String url = String.format(totalUserCountURL);
		String ceCountUrl = String.format(totalCECountURL);
		String cmCountUrl = String.format(totalCMCountURL);
		headers.set("Authorization", token); // Set the JWT token in Authorization header
		// Create an HttpEntity with the headers
		HttpEntity<String> entity = new HttpEntity<>(headers);
		try {
			ResponseEntity<Integer> alluser = restTemplate.exchange(url, HttpMethod.GET, entity, Integer.class);
			log.info("url /total-user-count: run successfully");
			ResponseEntity<Integer> ceCount = restTemplate.exchange(ceCountUrl, HttpMethod.GET, entity, Integer.class);
			log.info("url /totalCECount: run successfully");
			ResponseEntity<Integer> cmCount = restTemplate.exchange(cmCountUrl, HttpMethod.GET, entity, Integer.class);
			log.info("url /totalCMCount: run successfully");
			counts.setCeLoggedInCount(activeCEUserCount(token));
			log.info("url /allactivecelist: run successfully");
			counts.setCmLoggedInCount(activeCMUserCount(token));
			log.info("url /allactivecelist: run successfully");
			counts.setTotalCeCount(ceCount.getBody().intValue());
			counts.setTotalCmCount(cmCount.getBody().intValue());
			counts.setTotalUserCount(alluser.getBody().intValue());
		} catch (Exception e) {
			log.info(e.getMessage());
			counts.setCeLoggedInCount(0);
			counts.setCmLoggedInCount(0);
			counts.setTotalCeCount(0);
			counts.setTotalCmCount(0);
			counts.setTotalUserCount(0);
		}
		return counts;
	}

	public int activeCEUserCount(String token) {
		int ceCount = 0;
		HttpHeaders headers = new HttpHeaders();
		String activeCEListUrl = String.format(allactivecelist);
		headers.set("Authorization", token);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		ResponseEntity<List<String>> allCEuser = restTemplate.exchange(activeCEListUrl, HttpMethod.GET, entity,
				new ParameterizedTypeReference<List<String>>() {
				});
		Set<UserSessionRepresentation> users = currentActiveUsersSet();
		for (UserSessionRepresentation userSet : users) {
			if (allCEuser.getBody().contains(userSet.getUsername())) {
				ceCount++;
			}
		}
		return ceCount;
	}

	public int activeCMUserCount(String token) {
		int cmCount = 0;
		HttpHeaders headers = new HttpHeaders();
		String activeCMListUrl = String.format(allactivecmlist);
		headers.set("Authorization", token);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<List<String>> allCMuser = restTemplate.exchange(activeCMListUrl, HttpMethod.GET, entity,
				new ParameterizedTypeReference<List<String>>() {
				});
		Set<UserSessionRepresentation> users = currentActiveUsersSet();
		for (UserSessionRepresentation userSet : users) {
			if (allCMuser.getBody().contains(userSet.getUsername())) {
				cmCount++;
			}
		}
		return cmCount;
	}
	
	public ActiveCMUsersDetails activeCMUserDetails(String token) {
		ActiveCMUsersDetails activeUsersData=new ActiveCMUsersDetails();
		HttpHeaders headers = new HttpHeaders();
		String activeCMListUrl = String.format(allactivecmlist);
		headers.set("Authorization", token);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<List<String>> allCMuser = restTemplate.exchange(activeCMListUrl, HttpMethod.GET, entity,
				new ParameterizedTypeReference<List<String>>() {
				});
		Set<UserSessionRepresentation> users = currentActiveUsersSet();
		for (UserSessionRepresentation userSet : users) {
			int count=0;
			if (allCMuser.getBody().contains(userSet.getUsername())) {
				Set<UserSessionRepresentation> set=new HashSet<>();
				count++;
				activeUsersData.setCmCount(count);
				set.add(userSet);
				activeUsersData.setActiveCMUsersSet(set);
			
			}
		}
		return activeUsersData;
	}

	
	public ActiveCEUsersDetails activeCEUserDetails(String token) {
		ActiveCEUsersDetails activeUsersData=new ActiveCEUsersDetails();
		HttpHeaders headers = new HttpHeaders();
		String activeCEListUrl = String.format(allactivecelist);
		headers.set("Authorization", token);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<List<String>> allCMuser = restTemplate.exchange(activeCEListUrl, HttpMethod.GET, entity,
				new ParameterizedTypeReference<List<String>>() {
				});
		Set<UserSessionRepresentation> users = currentActiveUsersSet();
		for (UserSessionRepresentation userSet : users) {
			int count=0;
			if (allCMuser.getBody().contains(userSet.getUsername())) {
				Set<UserSessionRepresentation> set=new HashSet<>();
				count++;
				activeUsersData.setCeCount(count);
				set.add(userSet);
				activeUsersData.setActiveCEUsersSet(set);
			}
		}
		return activeUsersData;
	}
	
	public UserDataDto getUserData(String token) {
		UserDataDto userData = new UserDataDto();
		List<CEUsersDetailsDto> ceUserData = new ArrayList<>();
		CEUsersDetailsDto usersData = new CEUsersDetailsDto();
		CEUsersDetailsDto usersData2=new CEUsersDetailsDto();
		Map<String, List<CEUsersDetailsDto>> map = new HashMap<>();
		try {
			CountDto allUsersCount = allCount(token);
			int totalCeCount = activeCEUserCount(token);
			int totalCmCount = activeCMUserCount(token);
			usersData.setDesignation("CE");
			usersData.setLoggedIn(totalCeCount);
			usersData.setTotal(allUsersCount.getTotalCeCount());
			usersData2.setDesignation("CM");
			usersData2.setLoggedIn(totalCmCount);
			usersData2.setTotal(allUsersCount.getTotalCmCount());
			ceUserData.add(usersData);
			ceUserData.add(usersData2);
			map.put("userdata", ceUserData);
			userData.setTotalUsers(allUsersCount.getTotalUserCount());
			userData.setUserData(ceUserData);
			return userData;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getUserList(){
		return repo.getCeUserList();
	}
	
	public List<SevenDaysResponseData> getSevenDaysData() {
		List<SevenDaysResponseData> response=new ArrayList<>();
		return response;
	}
	

    // Convert LocalDateTime to formatted date string
    public String convertToFormattedDate(LocalDateTime dateTime) {
        try {
            // Convert LocalDateTime to Instant (requires ZoneId)
            Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();

            // Create a formatter for the desired output format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                                          .withZone(ZoneId.systemDefault());

            // Format the Instant and return
            return formatter.format(instant);
        } catch (Exception e) {
            return "Invalid date-time format";
        }
    }
}
