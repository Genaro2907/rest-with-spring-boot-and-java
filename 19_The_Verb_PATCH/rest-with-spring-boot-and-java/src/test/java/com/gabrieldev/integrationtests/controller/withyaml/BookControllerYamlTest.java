package com.gabrieldev.integrationtests.controller.withyaml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.gabrieldev.configs.TestConfigs;
import com.gabrieldev.data.vo.v1.security.TokenVO;
import com.gabrieldev.integrationtests.controller.withyaml.mapper.YMLMapper;
import com.gabrieldev.integrationtests.testcontainers.AbstractIntegrationTest;
import com.gabrieldev.integrationtests.vo.AccountCredentialsVO;
import com.gabrieldev.integrationtests.vo.BookVO;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
 @TestMethodOrder(OrderAnnotation.class)
public class BookControllerYamlTest extends AbstractIntegrationTest{

	private static RequestSpecification specification;
	private static YMLMapper objectMapper;
	
	private static BookVO book;
	
	@BeforeAll
	public static void setup() {
		objectMapper = new YMLMapper();
		
		book = new BookVO();
		
	}
	
	
	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		
		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");
		var accessToken = given()
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.basePath("/auth/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_YML)
					.accept(TestConfigs.CONTENT_TYPE_YML)
					.body(user, objectMapper)
					.when()
				.post()
				.then()
					.statusCode(200)
				.extract()
					.body()
						.as(TokenVO.class, objectMapper)
						.getAccessToken();
		
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/api/book/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
	}
	
	
	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		mockBook();
		
		var persistedBook = given().spec(specification)
			.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
			.contentType(TestConfigs.CONTENT_TYPE_YML)
			.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(book, objectMapper)
				.when()
				.post()
			.then()
				.statusCode(200)
			.extract()
				.body()
					.as(BookVO.class, objectMapper);
		book = persistedBook;
		
		assertNotNull(persistedBook);
		assertNotNull(persistedBook.getId());
		assertNotNull(persistedBook.getAuthor());
		assertNotNull(persistedBook.getPrice());
		assertNotNull(persistedBook.getTitle());
		assertTrue(persistedBook.getId() > 0);
		
		assertEquals("Docker Deep Dive", persistedBook.getTitle());
        assertEquals("Nigel Poulton", persistedBook.getAuthor());
        assertEquals(55.99, persistedBook.getPrice());
	}
	
	
	@Test
	@Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		book.setTitle("Docker Deep Dive - Updated");

		
		var bookUpdated = given().spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(book, objectMapper)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(BookVO.class, objectMapper);
		book = bookUpdated;
		
		assertNotNull(bookUpdated.getId());
        assertNotNull(bookUpdated.getTitle());
        assertNotNull(bookUpdated.getAuthor());
        assertNotNull(bookUpdated.getPrice());
        assertEquals(bookUpdated.getId(), book.getId());
        assertEquals("Docker Deep Dive - Updated", bookUpdated.getTitle());
        assertEquals("Nigel Poulton", bookUpdated.getAuthor());
        assertEquals(55.99, bookUpdated.getPrice());
	}
	

	@Test
	@Order(3)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		mockBook();
		
		var persistedBook = given().spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
					.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_GABRIEL)
				.pathParam("id", book.getId())
				.when()
				.get("{id}")
			.then()
				.statusCode(200)
			.extract()
			.body()
			.as(BookVO.class, objectMapper);
		
		book = persistedBook;
		
		assertNotNull(persistedBook.getId());
        assertNotNull(persistedBook.getTitle());
        assertNotNull(persistedBook.getAuthor());
        assertNotNull(persistedBook.getPrice());
        assertEquals(persistedBook.getId(), book.getId());
        assertEquals("Docker Deep Dive - Updated", persistedBook.getTitle());
        assertEquals("Nigel Poulton", persistedBook.getAuthor());
        assertEquals(55.99, persistedBook.getPrice());
	}
	@Test
	@Order(4)
	public void testDelete() throws JsonMappingException, JsonProcessingException {
		
		 given().spec(specification)
		 		.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.pathParam("id", book.getId())
				.when()
				.delete("{id}")
				.then()
				.statusCode(204);
	}
	@Test
	@Order(5)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
			.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
			.contentType(TestConfigs.CONTENT_TYPE_YML)
			.accept(TestConfigs.CONTENT_TYPE_YML)
				.when()
				.get()
			.then()
				.statusCode(200)
			.extract()
			.body()
			.as(BookVO[].class, objectMapper);
		
		List<BookVO> books = Arrays.asList(content);

		BookVO foundBookOne = books.get(0);
		book = foundBookOne;
		
		assertNotNull(foundBookOne.getId());
        assertNotNull(foundBookOne.getTitle());
        assertNotNull(foundBookOne.getAuthor());
        assertNotNull(foundBookOne.getPrice());
        assertTrue(foundBookOne.getId() > 0);
        assertEquals("Working effectively with legacy code", foundBookOne.getTitle());
        assertEquals("Michael C. Feathers", foundBookOne.getAuthor());
        assertEquals(49.00, foundBookOne.getPrice());
		
		BookVO foundBookTwo = books.get(1);
		
		
		assertNotNull(foundBookTwo.getId());
		assertNotNull(foundBookTwo.getAuthor());
		assertNotNull(foundBookTwo.getPrice());
		assertNotNull(foundBookTwo.getTitle());
		assertTrue(foundBookTwo.getId() > 0);
		
		assertEquals("Ralph Johnson, Erich Gamma, John Vlissides e Richard Helm",foundBookTwo.getAuthor());
		assertEquals(45.00,foundBookTwo.getPrice());
		assertEquals("Design Patterns",foundBookTwo.getTitle());
	}
	@Test
	@Order(6)
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {
		
		RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
				.setBasePath("/api/book/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		
		given().spec(specificationWithoutToken)
			.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
			.contentType(TestConfigs.CONTENT_TYPE_YML)
			.accept(TestConfigs.CONTENT_TYPE_YML)
				.when()
				.get()
			.then()
				.statusCode(403);
			
	}
	
	
	private void mockBook() {
		book.setTitle("Docker Deep Dive");
        book.setAuthor("Nigel Poulton");
        book.setPrice(Double.valueOf(55.99));
        book.setLaunchDate(new Date());
		
	}

}
