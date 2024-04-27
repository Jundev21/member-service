package com.commerce.memberservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@OpenAPIDefinition(
	servers = {
		@Server(url = "http://localhost:8080", description = "Local Server")
	},
	info = @Info(
		title = "Member Service",
		description = "Member service API",
		contact = @Contact(name = "김준래", email = "jundev21@gmail.com", url = "https://github.com/Jundev21/member-service")
	)
)
@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI openApi() {
		String authorizationHeader = "Authorization";

		SecurityScheme jwtScheme = new SecurityScheme()
			.name(authorizationHeader)
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT");
		Components components = new Components()

			.addSecuritySchemes(authorizationHeader, jwtScheme);

		SecurityRequirement securityRequirement = new SecurityRequirement()
			.addList(authorizationHeader);

		return new OpenAPI()
			.addSecurityItem(securityRequirement)
			.components(components);

	}
}
