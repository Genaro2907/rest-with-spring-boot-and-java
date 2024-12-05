package com.gabrieldev.integrationtests.controller.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrieldev.configs.TestConfigs;
import com.gabrieldev.data.vo.v1.security.TokenVO;
import com.gabrieldev.integrationtests.testcontainers.AbstractIntegrationTest;
import com.gabrieldev.integrationtests.vo.AccountCredentialsVO;
import com.gabrieldev.integrationtests.vo.BookVO;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
 @TestMethodOrder(OrderAnnotation.class)
public class BookControllerJsonTest extends AbstractIntegrationTest{

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	
	private static BookVO book;
	
	@BeforeAll
	public static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		book = new BookVO();
		
	}
	
	
	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		
		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");
		var accessToken = given()
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
						.as(TokenVO.class)
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
		
		var content = given().spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(book)
				.when()
				.post()
			.then()
				.statusCode(200)
			.extract()
				.body()
					.asString();
		book  = objectMapper.readValue(content, BookVO.class);;
		
		assertNotNull(book);
		assertNotNull(book.getId());
		assertNotNull(book.getAuthor());
		assertNotNull(book.getPrice());
		assertNotNull(book.getTitle());
		assertTrue(book.getId() > 0);
		
		assertEquals("Docker Deep Dive", book.getTitle());
        assertEquals("Nigel Poulton", book.getAuthor());
        assertEquals(55.99, book.getPrice());
    
	}
	
	
	@Test
	@Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		book.setTitle("Docker Deep Dive - Updated");
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(book)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();
		BookVO bookUpdated = objectMapper.readValue(content, BookVO.class);
        
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
		
		var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .pathParam("id", book.getId())
                    .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                            .asString();
        
        BookVO foundBook = objectMapper.readValue(content, BookVO.class);
        
        assertNotNull(foundBook.getId());
        assertNotNull(foundBook.getTitle());
        assertNotNull(foundBook.getAuthor());
        assertNotNull(foundBook.getPrice());
        assertEquals(foundBook.getId(), book.getId());
        assertEquals("Docker Deep Dive - Updated", foundBook.getTitle());
        assertEquals("Nigel Poulton", foundBook.getAuthor());
        assertEquals(55.99, foundBook.getPrice());
	}
	@Test
	@Order(4)
	public void testDelete() throws JsonMappingException, JsonProcessingException {
		
		 given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
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
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .queryParams("page", 0 , "limit", 5, "direction", "asc")
                    .when()
                    .get()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                .asString();
		
		List<BookVO> books = objectMapper.readValue(content, new TypeReference<List<BookVO>>() {});

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
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(book)
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
