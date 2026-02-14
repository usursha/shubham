package com.hpy.swagger_app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.stereotype.Component;

@Component
@OpenAPIDefinition(
//		servers= {
//				@Server(
//				description="user-access-management",
//				url="http://localhost:9104/swagger-ui/index.html"
//				),
//				@Server(
//				description="oauth",
//				url="http://localhost:9103/swagger-ui/index.html"
//				)
//		},
		security=@SecurityRequirement(name="Auth")
		)
@SecurityScheme(
		name = "Auth",
		description= "JWT auth description",
		scheme= "bearer",
		type= SecuritySchemeType.HTTP,
		bearerFormat= "JWT",
		in= SecuritySchemeIn.HEADER
		)
public class OpenApiConfig {

}
