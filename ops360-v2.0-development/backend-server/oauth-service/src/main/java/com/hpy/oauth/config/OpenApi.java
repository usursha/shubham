package com.hpy.oauth.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
		security=@SecurityRequirement(name="Bearer")
		)
@SecurityScheme(
		name = "Bearer",
		description= "JWT auth description",
		scheme= "bearer",
		type= SecuritySchemeType.HTTP,
		bearerFormat= "JWT",
		in= SecuritySchemeIn.HEADER
		)
public class OpenApi {

}
