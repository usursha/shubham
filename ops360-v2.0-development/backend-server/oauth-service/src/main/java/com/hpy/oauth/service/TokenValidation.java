
package com.hpy.oauth.service;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.hpy.oauth.dto.GenericResponseDto;
import com.hpy.oauth.dto.InvalidTokenExceptionDto;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TokenValidation {

	JwkProvider jwkProvider;

	@Value("${keycloak.realm-uri}")
	private String realmUrl;

	@Value("${keycloak.certs-uri}")
	private String certsUrl;

	@PostConstruct
	public void init() throws MalformedURLException {
		log.info("Initializing TokenValidation with certsUrl: {}", certsUrl);
		try {
			jwkProvider = new JwkProviderBuilder(new URL(certsUrl)).build();
			log.info("JwkProvider initialized successfully for certsUrl: {}", certsUrl);
		} catch (MalformedURLException e) {
			log.error("Failed to initialize JwkProvider with certsUrl: {}", certsUrl, e);
			throw e;
		}
	}

	public GenericResponseDto validateAccessToken(String token)
			throws JwkException, InvocationTargetException, InvalidTokenExceptionDto {
		log.info("Starting validateAccessToken for token with keyId: {}", JWT.decode(token).getKeyId());
		GenericResponseDto response = new GenericResponseDto();

		try {
			DecodedJWT decodedJWT = JWT.decode(token);
			log.info("Token decoded successfully, keyId: {}, subject: {}, issuer: {}", decodedJWT.getKeyId(),
					decodedJWT.getSubject(), decodedJWT.getIssuer());

			Date time = decodedJWT.getExpiresAt();
			Date currentDate = new Date();
			log.info("Checking token expiration: expiresAt={}, currentTime={}", time, currentDate);

			if (time.compareTo(currentDate) <= 0) {
				log.warn("Token expired for subject: {}, expiresAt: {}", decodedJWT.getSubject(), time);
				throw new InvalidTokenExceptionDto();
			}

			log.info("Fetching JWK for keyId: {} from certsUrl: {}", decodedJWT.getKeyId(), certsUrl);
			Jwk jwk = jwkProvider.get(decodedJWT.getKeyId());
			log.info("JWK retrieved successfully for keyId: {}", decodedJWT.getKeyId());

			Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
			log.info("RSA256 algorithm initialized for token verification");

			JWTVerifier verifier = JWT.require(algorithm).withIssuer(realmUrl).withAudience("account").build();
			log.info("JWT verifier built with issuer: {}, audience: account", realmUrl);

			verifier.verify(decodedJWT);
			log.info("Token verified successfully for subject: {}", decodedJWT.getSubject());

			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
					decodedJWT.getSubject(), "***", List.of(new SimpleGrantedAuthority("SIMPLE_AUTHORITY"))));
			log.info("Security context updated with subject: {}", decodedJWT.getSubject());

			response.setStatusCode(2001);
			response.setStatusMessage("Success");
			log.info("Token validation successful for subject: {}", decodedJWT.getSubject());

			return response;
		} catch (JwkException e) {
			log.error("JWK retrieval failed for keyId: {}", JWT.decode(token).getKeyId(), e);
			throw e;
		} catch (JWTVerificationException e) {
			log.error("Token verification failed for token with keyId: {}", JWT.decode(token).getKeyId(), e);
			throw new InvalidTokenExceptionDto();
		} catch (Exception e) {
			log.error("Unexpected error during token validation for token with keyId: {}", JWT.decode(token).getKeyId(),
					e);
			throw new InvocationTargetException(e, "Token validation failed");
		}
	}
}
