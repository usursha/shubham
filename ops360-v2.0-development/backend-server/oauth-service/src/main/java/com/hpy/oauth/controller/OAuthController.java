package com.hpy.oauth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.oauth.dto.AccessTokenDto;
import com.hpy.oauth.dto.AccessTokenRequestedDto;
import com.hpy.oauth.dto.CELoginRequestDto;
import com.hpy.oauth.dto.ChangePasswordDto;
import com.hpy.oauth.dto.GenericResponseDto;
import com.hpy.oauth.dto.LoginRequestDto;
import com.hpy.oauth.dto.LoginResponse2Dto;
import com.hpy.oauth.dto.LoginResponseDto;
import com.hpy.oauth.dto.LogoutResponseDto;
import com.hpy.oauth.dto.MessageDto;
import com.hpy.oauth.dto.PortalChangePasswordDto;
import com.hpy.oauth.dto.ResponseMessageDto;
import com.hpy.oauth.dto.UserDetailsDto;
import com.hpy.oauth.service.LoginService;
import com.hpy.oauth.service.TokenValidation;
import com.hpy.oauth.util.MethodUtil;
import com.hpy.rest.dto.ResponseDto;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/oauth")
@CrossOrigin("${app.cross-origin.allow}")
public class OAuthController {

	private LoginService loginService;
	private MethodUtil methodUtil;
	private TokenValidation tokenValidation;

	public OAuthController(LoginService loginService, MethodUtil methodUtil, TokenValidation tokenValidation) {
		this.loginService = loginService;
		this.methodUtil = methodUtil;
		this.tokenValidation=tokenValidation;
	}
	
	@PostMapping("/cm_login")
	public ResponseEntity<LoginResponseDto > login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
		return ResponseEntity.ok(loginService.loginUser(loginRequestDto));
	}
	
	@PostMapping("/ce_login")
	public ResponseEntity<Mono<LoginResponse2Dto>> loginUser(@Valid @RequestBody CELoginRequestDto loginRequestDto) {
		return ResponseEntity.ok(loginService.loginWithoutToken(loginRequestDto));
	}

	@PostMapping("/refreshAccessToken")
	public ResponseEntity<Mono<AccessTokenDto>> refreshTokenDto(@Valid @RequestBody AccessTokenRequestedDto accessTokenRequestedDto) {
		return ResponseEntity.ok(loginService.accessTokenRequestedDto(accessTokenRequestedDto));
	}	

	@PutMapping("/update-password")
	public ResponseEntity<ResponseDto<ResponseMessageDto>> updatePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) throws Exception {
		ResponseDto<ResponseMessageDto> response=new ResponseDto<>() {};
		try {
			response.setData(loginService.changePasswordSplChar(changePasswordDto));
			response.setMessage("success");
			response.setResponseCode(HttpStatus.OK.value());
		}catch(Exception e) {
			response.setData(null);
			response.setMessage(e.getMessage());
			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return ResponseEntity.ok(response);	
	}
	
	@PutMapping("/update-password-with-otp")
	public ResponseEntity<ResponseDto<ResponseMessageDto>> updatePasswordWithOTP(@Valid @RequestBody PortalChangePasswordDto changePasswordDto) throws Exception {
		ResponseDto<ResponseMessageDto> response=new ResponseDto<>() {};
		try {
			response.setData(loginService.changePasswordSplCharWithOTP(changePasswordDto));
			response.setMessage("success");
			response.setResponseCode(HttpStatus.OK.value());
		}catch(Exception e) {
			response.setData(null);
			response.setMessage(e.getMessage());
			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return ResponseEntity.ok(response);	
	}
	
	@PostMapping("/logout")
	public LogoutResponseDto userLogout(@Valid @RequestBody AccessTokenRequestedDto accessTokenRequestedDto) {
		return loginService.logout(accessTokenRequestedDto);
	}
	
	@GetMapping("/userDetails")
	public UserDetailsDto getRepresentation() {
		return loginService.getUserDetails();
	}
	
	@PostMapping("/validateToken")
	public GenericResponseDto tokenValidation(@RequestBody String token) throws JWTVerificationException, Exception {
		return tokenValidation.validateAccessToken(token);
	}

	@GetMapping("/user-session-count/{username}")
	public ResponseEntity<Long> userSessionCounts(@PathVariable String username) {
		return ResponseEntity.ok(loginService.getSessionCount(username));
		

	}
	
}
