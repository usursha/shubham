package com.hpy.ops360.dashboard.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.dashboard.dto.OrganizationHierarchyListDto;
import com.hpy.ops360.dashboard.dto.PortalPersonalDetailsDto;
import com.hpy.ops360.dashboard.dto.PortalUsersLatLongDto;
import com.hpy.ops360.dashboard.dto.PortalWorkMetricsDto;
import com.hpy.ops360.dashboard.dto.ProfilePictureDto;
import com.hpy.ops360.dashboard.entity.OrganizationHierarchy;
import com.hpy.ops360.dashboard.entity.PortalUsersLatLongDetails;
import com.hpy.ops360.dashboard.entity.UserReportingHierarchy;
import com.hpy.ops360.dashboard.repository.OrganizationHierarchyRepository;
import com.hpy.ops360.dashboard.repository.PortalUsersLatLongRespository;
import com.hpy.ops360.dashboard.repository.ProfilePictureFeignClientRepository;
import com.hpy.ops360.dashboard.repository.UserReportingHierarchyRepository;
import com.hpy.ops360.dashboard.util.Helper;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserProfileService {
	
	@Autowired
	private PortalUsersLatLongRespository portalUsersLatLongRespository; 

	@Autowired
	private OrganizationHierarchyRepository hierarchyRepository;

	@Autowired
	private UserReportingHierarchyRepository userReportingHierarchyRepository;
	
	@Autowired
	ProfilePictureFeignClientRepository profilePictureFeignClientRepository;
	
	@Autowired
	private Helper utilmethod;

	public OrganizationHierarchyListDto getReportingHierarchy() {
		String username=utilmethod.getLoggedInUser();
		OrganizationHierarchyListDto organizationHierarchyList=new OrganizationHierarchyListDto();
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

	private PortalWorkMetricsDto getUserWorkMetrics(String username) {
		PortalWorkMetricsDto workmetrics=new PortalWorkMetricsDto();
		workmetrics.setEmployeeId(userReportingHierarchyRepository.getEmployeeIdByUsername(username));
		workmetrics.setWorkEmailAddress(userReportingHierarchyRepository.getEmailIdByUsername(username));
		workmetrics.setDateOfJoining(null);
		workmetrics.setArea(null);
		workmetrics.setAtmAssigned(userReportingHierarchyRepository.getTotalAtmAssigned(username));
		workmetrics.setChannelExecutivesAssigned(userReportingHierarchyRepository.getTotalCEAssigned(username));
		return workmetrics;
	}
	
	private PortalPersonalDetailsDto getPersonalDetails(String username) {
		PortalPersonalDetailsDto personalDetails=new PortalPersonalDetailsDto();
		personalDetails.setFullName(userReportingHierarchyRepository.getfullnamedByUsername(username));
		personalDetails.setDateOfBirth(null);
		personalDetails.setPhoneNumber(userReportingHierarchyRepository.getUserMobiledByUsername(username));
		personalDetails.setPersonalEmailAddress(null);
		personalDetails.setPermanentAddress(null);
		personalDetails.setCurrentAddress(userReportingHierarchyRepository.getHomeAddressdByUsername(username));
		return personalDetails;
		
	}
	
	public String updateLatLong(HttpServletRequest request,PortalUsersLatLongDto portalUsersLatLongDto){ 
		String token=getAuthorizationHeader(request);
		PortalUsersLatLongDetails entity=new PortalUsersLatLongDetails();
		ProfilePictureDto profilePictureDto=new ProfilePictureDto();
		entity.setUsername(portalUsersLatLongDto.getUsername());
		entity.setBaseLocation(userReportingHierarchyRepository.getHomeAddressdByUsername(portalUsersLatLongDto.getUsername()));
		entity.setBaseLocationLatitude(portalUsersLatLongDto.getLatitude());
		entity.setBaseLocationLongitude(portalUsersLatLongDto.getLongitude());
		profilePictureDto.setFileName(portalUsersLatLongDto.getFileName());
		profilePictureDto.setMediaType(portalUsersLatLongDto.getMediaType());
		profilePictureDto.setParentType(portalUsersLatLongDto.getParentType());
		profilePictureDto.setBase64String(portalUsersLatLongDto.getBase64String());
		profilePictureFeignClientRepository.addProfilePhoto(token, profilePictureDto);
		//		PortalUsersLatLongDetails data=portalUsersLatLongRespository.getById(portalUsersLatLongDto.getUsername());
//		String extingBaseLocation=data.getBaseLocation();
//		if(userReportingHierarchyRepository.getHomeAddressdByUsername(portalUsersLatLongDto.getUsername()).equals(extingBaseLocation) || 
//		(userReportingHierarchyRepository.getHomeAddressdByUsername(portalUsersLatLongDto.getUsername()).equals(null)));
		portalUsersLatLongRespository.save(entity);		
		return "Lat-Long Updated Successfully";
	}
	
	private String getAuthorizationHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && !authHeader.isEmpty()) {
            return authHeader;
        }
        return null;
    }

}
