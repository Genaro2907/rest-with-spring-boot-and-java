package com.gabrieldev.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {
	
	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("RESTful API with Java and SpringBoot 3")
						.version("v1")
						.description("Curso de SpringBoot")
						.termsOfService("https://github.com/Genaro2907")
						.license(new License().name("Apache 2.0")
								.url("https://github.com/Genaro2907")));
	}
}
