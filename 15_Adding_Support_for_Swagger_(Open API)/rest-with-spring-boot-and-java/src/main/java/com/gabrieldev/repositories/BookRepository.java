package com.gabrieldev.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabrieldev.model.Book;


public interface BookRepository extends JpaRepository<Book, Long>{

}
