package com.hpy.uam.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.rest.dto.IResponseDto;
import com.hpy.uam.dto.RoleRepresentationResponseDto;
import com.hpy.uam.service.RoleService;
import com.hpy.uam.util.RestUtilsImpl;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/role-service")
@AllArgsConstructor
@CrossOrigin("${app.cross-origin.allow}")
public class RoleController {
	private RoleService roleService;
	private RestUtilsImpl restutil;

	@GetMapping("/list")
//	@PreAuthorize("hasRole('admin_client_role')")
	public ResponseEntity<IResponseDto> getListofRole() {
		RoleRepresentationResponseDto response = roleService.list();
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));

	}
	
	@GetMapping("/listOfClientRoles")
	public ResponseEntity<IResponseDto> getListofRole(String clientId) {
		RoleRepresentationResponseDto response = roleService.getClientRoles(clientId);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));

	}
	
}
