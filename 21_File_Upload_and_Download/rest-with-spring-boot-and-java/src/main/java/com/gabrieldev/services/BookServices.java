package com.gabrieldev.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import com.gabrieldev.controllers.BookController;
import com.gabrieldev.data.vo.v1.BookVO;
import com.gabrieldev.exceptions.RequiredObjectIsNullException;
import com.gabrieldev.exceptions.ResourceNotFoundException;
import com.gabrieldev.mapper.GabrielMapper;
import com.gabrieldev.model.Book;
import com.gabrieldev.repositories.BookRepository;


@Service
public class BookServices {
	
	private Logger logger = Logger.getLogger(BookServices.class.getName());
	
	@Autowired
	BookRepository repository;
	
	@Autowired
	PagedResourcesAssembler<BookVO> assembler;

	public PagedModel<EntityModel<BookVO>> findAll(Pageable pageable) {

		logger.info("Finding all book!");
		
		var booksPage = repository.findAll(pageable);
		var booksVosPage = booksPage.map(p -> GabrielMapper.parseObject(p, BookVO.class));
		booksVosPage.map(
				p -> p.add(linkTo(methodOn(BookController.class).findById(p.getKey())).withSelfRel()));
		
		Link link = linkTo(methodOn(BookController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
		return assembler.toModel(booksVosPage, link );
	}

	public BookVO findById(Long id) {
		
		logger.info("Finding one book!");
		var entity = repository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		var vo = GabrielMapper.parseObject(entity, BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
		return vo;
	}
	
	public BookVO create(BookVO book) {

		if (book == null) throw new RequiredObjectIsNullException();
		logger.info("Creating one book!");
		var entity = GabrielMapper.parseObject(book, Book.class);
		var vo =  GabrielMapper.parseObject(repository.save(entity), BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	public BookVO update(BookVO book) {

		if (book == null) throw new RequiredObjectIsNullException();
		logger.info("Updating one book!");
		
		var entity = repository.findById(book.getKey())
			.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		entity.setAuthor(book.getAuthor());
		entity.setLaunchDate(book.getLaunchDate());
		entity.setPrice(book.getPrice());
		entity.setTitle(book.getTitle());
		var vo =  GabrielMapper.parseObject(repository.save(entity), BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	public void delete(Long id) {
		
		logger.info("Deleting one book!");
		
		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		repository.delete(entity);
	}
}
