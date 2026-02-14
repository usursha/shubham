//import org.keycloak.RSATokenVerifier;
//import org.keycloak.TokenVerifier;
//import org.keycloak.adapters.KeycloakDeployment;
//import org.keycloak.adapters.KeycloakDeploymentBuilder;
//import org.keycloak.adapters.rotation.AdapterTokenVerifier;
//import org.keycloak.common.VerificationException;
//import org.keycloak.representations.AccessToken;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.hpy.keycloak.dto.LoginRequestDto;
//import com.hpy.keycloak.service.LoginService;
//
//import reactor.core.publisher.Mono;
//
//public class JwtValidators {
//	
//	@Autowired
//	private LoginService loginService;
//
//
//        
//        
//        
//		public String getToken(LoginRequestDto loginRequestDto) {
//			String keycloakRealmUrl = "http://localhost:8080/realms/HPY/protocol/openid-connect/token";
//			String accessTokenString = loginService.login(loginRequestDto).block();
//			KeycloakDeployment deployment = KeycloakDeploymentBuilder.build(keycloakRealmUrl);
//			try {
//				AccessToken token = RSATokenVerifier.verifyToken(accessTokenString, deployment.getRealmKey(),
//						deployment.getRealmInfoUrl());
//				System.out.println("Token is valid!");
//				System.out.println("User ID: " + token.getSubject());
//				System.out.println("Username: " + token.getPreferredUsername());
//				// Add more claims extraction as needed
//			} catch (VerificationException e) {
//				System.err.println("Token verification failed: " + e.getMessage());
//			}
//    }
//}



