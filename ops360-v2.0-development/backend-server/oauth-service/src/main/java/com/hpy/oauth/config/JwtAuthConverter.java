package com.hpy.oauth.config;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

	private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

	@Value("${jwt.auth.converter.principle-attribute:}")
	private String principleAttribute;

	@Value("${jwt.auth.converter.resource-id:}")
	private String resourceId;

	@Override
	public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
		Collection<GrantedAuthority> authorities = Stream
				.concat(jwtGrantedAuthoritiesConverter.convert(jwt).stream(), extractResourceRoles(jwt).stream())
				.collect(Collectors.toSet());
		return new JwtAuthenticationToken(jwt, authorities, getPrincipleClaimName(jwt));
	}

	private String getPrincipleClaimName(Jwt jwt) {
		String claimName = JwtClaimNames.SUB;
		if (principleAttribute != null && !principleAttribute.trim().isEmpty()) {
			claimName = principleAttribute;
		}
		return jwt.getClaimAsString(claimName);
	}

	private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
		// Extract resource_access claim
		Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
		if (resourceAccess == null || resourceAccess.isEmpty()) {
			log.debug("No resource_access claim found in JWT");
			return Collections.emptySet();
		}

 		Map<String, Object> resource = (Map<String, Object>) resourceAccess.get(resourceId);
		if (resource == null || resource.isEmpty()) {
			log.debug("No roles found for resourceId: {}", resourceId);
			return Collections.emptySet();
		}

		// Extract roles
		Object rolesObj = resource.get("roles");
		if (!(rolesObj instanceof Collection<?>)) {
			log.debug("Roles claim is not a collection for resourceId: {}", resourceId);
			return Collections.emptySet();
		}

		Collection<?> roles = (Collection<?>) rolesObj;
		return roles.stream().filter(String.class::isInstance).map(String.class::cast)
				.map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toSet());
	}
}