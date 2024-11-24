package com.gabrieldev.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabrieldev.model.Person;


public interface PersonRepository extends JpaRepository<Person, Long>{

}
