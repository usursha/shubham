package com.hpy.ops360.dashboard.config;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;
// You'll need to use your application's specific way to get the token (e.g., Spring Security context, ThreadLocal, etc.)
import jakarta.servlet.http.HttpServletRequest;

public class TokenRelayRequestInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {

		try {
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			if (attributes != null) {
				HttpServletRequest request = attributes.getRequest();
				String authorizationHeader = request.getHeader("Authorization");

				if (authorizationHeader != null) {
					template.header("Authorization", authorizationHeader);
				}
			}
		} catch (Exception e) {
			System.err.println("Could not relay token: " + e.getMessage());
		}
	}
}
