package com.gabrieldev.integrationtests.controller.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.gabrieldev.configs.TestConfigs;
import com.gabrieldev.integrationtests.testcontainers.AbstractIntegrationTest;
import com.gabrieldev.integrationtests.vo.AccountCredentialsVO;
import com.gabrieldev.integrationtests.vo.TokenVO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class AuthControllerJsonTest extends AbstractIntegrationTest{
	
	private static TokenVO tokenVO;
	

	@Test
	@Order(1)
	public void testSignin() throws JsonMappingException, JsonProcessingException {
		
		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");
		tokenVO = given()
				.basePath("/auth/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.body(user)
					.when()
				.post()
				.then()
					.statusCode(200)
				.extract()
					.body()
						.as(TokenVO.class);
		
		assertNotNull(tokenVO.getAccessToken());
		assertNotNull(tokenVO.getRefreshToken());
		
	}
	@Test
	@Order(2)
	public void testRefresh() throws JsonMappingException, JsonProcessingException {
		
		var newtokenVO = given()
				.basePath("/auth/refresh")
				.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.pathParam("username", tokenVO.getUsername())
					.header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
				.when()
				.put("{username}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(TokenVO.class);
		
		assertNotNull(newtokenVO.getAccessToken());
		assertNotNull(newtokenVO.getRefreshToken());
		
	}

}
