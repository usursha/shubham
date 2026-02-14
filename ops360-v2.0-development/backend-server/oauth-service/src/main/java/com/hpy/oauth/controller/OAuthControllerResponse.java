package com.hpy.oauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.hpy.generic.impl.GenericDto;
import com.hpy.oauth.dto.AccessTokenDto;
import com.hpy.oauth.dto.AccessTokenRequestedDto;
import com.hpy.oauth.dto.CELoginRequestDto;
import com.hpy.oauth.dto.ChangePasswordDto;
import com.hpy.oauth.dto.GenericResponseDto;
import com.hpy.oauth.dto.LoginRequestDto;
import com.hpy.oauth.dto.LoginResponse2Dto;
import com.hpy.oauth.dto.LoginResponseDto;
import com.hpy.oauth.dto.LogoutResponseDto;
import com.hpy.oauth.dto.PortalChangePasswordDto;
import com.hpy.oauth.dto.ResponseMessageDto;
import com.hpy.oauth.dto.UserDetailsDto;
import com.hpy.oauth.service.LoginService;
import com.hpy.oauth.service.TokenValidation;
import com.hpy.oauth.util.MethodUtil;
import com.hpy.oauth.util.ResponseMessageUtil;
import com.hpy.oauth.util.RestUtilImpl;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.dto.ResponseDto;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/v1/oauth")
@CrossOrigin("${app.cross-origin.allow}")
public class OAuthControllerResponse {

	private LoginService loginService;
	private TokenValidation tokenValidation;
	private RestUtilImpl restutil;

	public OAuthControllerResponse(LoginService loginService, TokenValidation tokenValidation, RestUtilImpl restutil) {
		this.loginService = loginService;
		this.tokenValidation = tokenValidation;
		this.restutil = restutil;
	}

	@PostMapping("/ce-login")
	public ResponseEntity<Mono<Object>> loginUser(@Valid @RequestBody CELoginRequestDto loginRequestDto) {

 
		log.info("Inside loginUser method: {} ",loginRequestDto);
		return ResponseEntity.ok(loginService.loginWithoutToken(loginRequestDto).map(response -> {
			ResponseDto<LoginResponse2Dto> formattedResponse = new ResponseDto<>();
			formattedResponse.setResponseCode(HttpStatus.OK.value());
			formattedResponse.setMessage(ResponseMessageUtil.getMessageForStatusCode(HttpStatus.OK.value()));
			formattedResponse.setData(response);
			return Mono.just(formattedResponse);
		}));
	}
 

	@PostMapping("/cm-login")
	public ResponseEntity<ResponseDto<GenericDto>> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
		LoginResponseDto response = loginService.loginUser(loginRequestDto);
		int statusCodeValue = HttpStatus.OK.value();
		String statusMessage = ResponseMessageUtil.getMessageForStatusCode(statusCodeValue);
		ResponseDto<GenericDto> formattedResponse = new ResponseDto<>();
		formattedResponse.setResponseCode(statusCodeValue);
		formattedResponse.setMessage(statusMessage);
		formattedResponse.setData(response);
		return ResponseEntity.ok(formattedResponse);
	}

	@PostMapping("/refresh-access-token")
	public ResponseEntity<Mono<Object>> refreshTokenDto(
			@Valid @RequestBody AccessTokenRequestedDto accessTokenRequestedDto) {
		return ResponseEntity.ok(loginService.accessTokenRequestedDto(accessTokenRequestedDto).map(response -> {
			ResponseDto<AccessTokenDto> formattedResponse = new ResponseDto<>();
			formattedResponse.setResponseCode(HttpStatus.OK.value());
			formattedResponse.setMessage(ResponseMessageUtil.getMessageForStatusCode(HttpStatus.OK.value()));
			formattedResponse.setData(response);
			return Mono.just(formattedResponse);
		}));
	}

	@PutMapping("/update-password")
	public ResponseEntity<ResponseDto<ResponseMessageDto>> updatePassword(
			@Valid @RequestBody ChangePasswordDto changePasswordDto) throws Exception {
		ResponseDto<ResponseMessageDto> response = new ResponseDto<>() {
		};
		try {
			response.setData(loginService.changePasswordSplChar(changePasswordDto));
			response.setMessage("success");
			response.setResponseCode(HttpStatus.OK.value());
		} catch (Exception e) {
			response.setData(null);
			response.setMessage(e.getMessage());
			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return ResponseEntity.ok(response);
	}

	@PutMapping("/update-password-with-otp")
	public ResponseEntity<ResponseDto<ResponseMessageDto>> updatePasswordWithOTP(
			@Valid @RequestBody PortalChangePasswordDto changePasswordDto) throws Exception {
		ResponseDto<ResponseMessageDto> response = new ResponseDto<>() {
		};
		try {
			response.setData(loginService.changePasswordSplCharWithOTP(changePasswordDto));
			response.setMessage("success");
			response.setResponseCode(HttpStatus.OK.value());
		} catch (Exception e) {
			response.setData(null);
			response.setMessage(e.getMessage());
			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/logout")
	public ResponseEntity<ResponseDto<LogoutResponseDto>> userLogout(
			@RequestBody AccessTokenRequestedDto accessTokenRequestedDto) {

		ResponseDto<LogoutResponseDto> response = new ResponseDto<>() {
		};
		try {
			response.setData(loginService.logout(accessTokenRequestedDto));
			response.setMessage("success");
			response.setResponseCode(HttpStatus.OK.value());
		} catch (Exception e) {
			response.setData(null);
			response.setMessage(e.getMessage());
			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping("/user-details")
	public ResponseEntity<ResponseDto<UserDetailsDto>> getRepresentation() {
		ResponseDto<UserDetailsDto> response = new ResponseDto<>() {
		};
		try {
			response.setData(loginService.getUserDetails());
			response.setMessage("success");
			response.setResponseCode(HttpStatus.OK.value());
		} catch (Exception e) {
			response.setData(null);
			response.setMessage(e.getMessage());
			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return ResponseEntity.ok(response);
	}
//	
//	@PostMapping("/validateToken")
//	public GenericResponseDto tokenValidation(@RequestBody String token) throws JWTVerificationException, Exception {
//		return tokenValidation.validateAccessToken(token);
//	}

	@PostMapping("/validateToken")
	public ResponseEntity<IResponseDto> tokenValidation(@RequestBody String token)
			throws JWTVerificationException, Exception {
		GenericResponseDto response = tokenValidation.validateAccessToken(token);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

	@GetMapping("/user-session-count/{username}")
	public ResponseEntity<Long> userSessionCounts(@PathVariable String username) {
		return ResponseEntity.ok(loginService.getSessionCount(username));

	}
}
