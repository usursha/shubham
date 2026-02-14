//package com.hpy.uam.service;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertion.*;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.BeforeEach;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.junit.jupiter.api.extension.ExtendWith;
//import javax.ws.rs.core.Response;
//
//import java.util.List;
//
//ExtendWith(MockitoExtension.class)
//public class UserRegistrationServiceTest {
//	
//	@Mock
//	private UserResource userResource;
//	
//	@InjectMock
//	private UserRegistrationService userRegistrationService;
//	private UserRegistration userRegistration;
//	
//	@BeforeEach
//	public void setup() {
//		userRegistration = new UserRegistration("testUsername", "testEmail@example.com", "testFirstName",
//				"testLastName", "testPassword");
//
//	}
//
//	@Test
//	public void testCreateUser() {
//		Response mockedResponse = mock(Response.class);
//		when(userResource.create(any(UserRepresentation.class))).thenReturn(mockedResponse);
//
//		when(mockedResponse.getStatus()).thenReturn(201);
//
//		UserRegistration result = userRegistration.createUser(UserRegistrationRecord);
//		verify(userResource).create(argThat(user -> user.isEnabled() && "testUsername".equals(getUsername())
//				&& "testEmail@example.com".equals(getEmail()) && "testFirstName".equals(getFirstName())
//				&& "testLastName".equals(getLastName()) && !user.isEmailVerified() && user.getCredentials.size() == 1
//				&& "testPassword".equals(user.getcredentials().get(0).getValue())
//				&& !user.getcredential().get(0).isTemporary()
//				&& CredentialRepresentation.PASSWORD.equals(user.getCredentials().get(0).getType())));
//	}
//	
//}
