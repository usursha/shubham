package com.hpy.uam.controller;

import java.util.List;

import org.keycloak.representations.idm.GroupRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.rest.dto.IResponseDto;
import com.hpy.uam.dto.GenericMessageResponseDto;
import com.hpy.uam.dto.GroupPresentationRequestDto;
import com.hpy.uam.dto.GroupRegistrationRequestDto;
import com.hpy.uam.dto.GroupRepresentationListResponseDto;
import com.hpy.uam.dto.GroupRepresentationResponseDto;
import com.hpy.uam.dto.StatusCodeResponseDto;
import com.hpy.uam.service.GroupService;
import com.hpy.uam.service.SubGroupService;
import com.hpy.uam.util.RestUtilsImpl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/group-service")
@AllArgsConstructor
@CrossOrigin("${app.cross-origin.allow}")
public class GroupController {

	private GroupService groupService;
	private SubGroupService subGroupService;
	private RestUtilsImpl restutil;

	@PostMapping("/group/create")
	public ResponseEntity<IResponseDto> createGroup(@RequestBody @Valid GroupRegistrationRequestDto groupRegistration) {
		StatusCodeResponseDto response=groupService.createGroup(groupRegistration);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));	
	}


	@PutMapping("/group/addUser/{userId}")
	@Validated
	public ResponseEntity<IResponseDto> addUserToGroup(@PathVariable String userId, @RequestBody @NotEmpty(message="id cannot be blank!!")String id) {
		GenericMessageResponseDto response =groupService.addingUserToGroup(userId, id);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));	
	}


	@GetMapping("/group/get/{id}")
	public ResponseEntity<IResponseDto> getGroupById(@PathVariable String id) {
		GroupRepresentationResponseDto response=groupService.getGroupById(id);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));	
	}


	@GetMapping("/group/list")
//	@PreAuthorize("hasRole('hdfc_client_role')")
	public ResponseEntity<IResponseDto> getListofGroup() {
		GroupRepresentationListResponseDto response=groupService.getAllGroup();    		
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

	@PutMapping("/group/update/{id}")
	public ResponseEntity<IResponseDto> updateGroupById(@PathVariable String id, @RequestBody GroupRepresentation group) {
		GenericMessageResponseDto response=groupService.updateGroup(id, group);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}
		

	@DeleteMapping("/group/delete/{id}")
	public ResponseEntity<IResponseDto> deleteGroupById(@PathVariable String id) {
		GenericMessageResponseDto response=groupService.deleteGroup(id);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}
		
	@PostMapping("/subGroup/create/{groupId}")
	public ResponseEntity<IResponseDto> createSubGroup(@PathVariable String groupId,
			@RequestBody String subgroupName) {
		StatusCodeResponseDto response=subGroupService.createSubgroup(groupId, subgroupName);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));	
	}
	
	
	@GetMapping("/subGroup/list/{groupId}")
	public ResponseEntity<IResponseDto> getAllSubgroups(@PathVariable String groupId) {
		GroupRepresentationListResponseDto response=subGroupService.getAllSubgroup(groupId);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	
	}

	@DeleteMapping("/subGroup/delete/{id}")
	public ResponseEntity<IResponseDto> deleteSubGroupById(@PathVariable String id) {
		GenericMessageResponseDto response=subGroupService.deleteSubGroup(id);	
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

	@PutMapping("/group/assignRole/{groupId}")
	public ResponseEntity<IResponseDto> assignGroupToRoleEntity(@PathVariable String groupId,
			@RequestBody @Valid List<String> roleList) throws Exception {
		GenericMessageResponseDto response=subGroupService.assignRole(groupId, roleList);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}
	
	
	@GetMapping("/group/getDetails/{groupId}")
	public ResponseEntity<IResponseDto> getGroupsAndAccessCode(@PathVariable String groupId) {
		GroupPresentationRequestDto response=groupService.getGroupDetails(groupId);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

	
}
