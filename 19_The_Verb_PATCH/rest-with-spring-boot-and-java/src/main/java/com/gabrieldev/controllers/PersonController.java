package com.gabrieldev.controllers;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabrieldev.data.vo.v1.PersonVO;
import com.gabrieldev.services.PersonServices;
import com.gabrieldev.util.MediaType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

//@CrossOrigin
@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoints for Managing Pleope")
public class PersonController {
	
	@Autowired
	private PersonServices service;
	
	@GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML} )
	@Operation(summary = "Finds all People", description = "Finds all People", tags = {"People"}, responses = {
			@ApiResponse(description = "Success", responseCode = "200", 
					content = {
							@Content(
									mediaType = "application/json",
									array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))
									)}),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
	})
	public List<PersonVO> findAll() {
		return service.findAll();
	}
	
	
	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.APPLICATION_YML})
	@Operation(summary = "Finds a Person", description = "Finds a Person", tags = {"People"}, responses = {
			@ApiResponse(description = "Success", responseCode = "200", 
					content = @Content(schema = @Schema(implementation = PersonVO.class))
			),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
	})
	public PersonVO findById(@PathVariable Long id) {
		return service.findById(id);
	}

	@CrossOrigin(origins = {"http://localhost:8080", "https://gabriel.com.br"})
	@PostMapping(consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.APPLICATION_YML}, produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.APPLICATION_YML})
	@Operation(summary = "Adds a new Person", description = "Adds a new Person by passing in a JSON, XML or YML representation of the person!", tags = {"People"}, responses = {
			@ApiResponse(description = "Success", responseCode = "200", 
					content = @Content(schema = @Schema(implementation = PersonVO.class))
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
	})
	public PersonVO create(@RequestBody PersonVO person) {
		return service.create(person);
	}
	@PutMapping(consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.APPLICATION_YML}, produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.APPLICATION_YML})
	@Operation(summary = "Updates a  Person", description = "Updates a Person by passing in a JSON, XML or YML representation of the person!", tags = {"People"}, responses = {
			@ApiResponse(description = "Updated", responseCode = "200", 
					content = @Content(schema = @Schema(implementation = PersonVO.class))
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
	})
	public PersonVO update(@RequestBody PersonVO person) {
		return service.update(person);
	}
	
	@PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.APPLICATION_YML})
	@Operation(summary = "Disable a specific Person by your ID", description = "Disable a specific Person by your ID", tags = {"People"}, responses = {
			@ApiResponse(description = "Success", responseCode = "200", 
					content = @Content(schema = @Schema(implementation = PersonVO.class))
			),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
	})
	public PersonVO disablePerson(@PathVariable (value = "id")Long id) {
		return service.disablePerson(id);
	}
	
	
	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Deletes a  Person", description = "Deletes a Person by passing in a JSON, XML or YML representation of the person!", tags = {"People"}, responses = {
			@ApiResponse(description = "No content", responseCode = "204", content = @Content
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
	})
	public ResponseEntity<?> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
