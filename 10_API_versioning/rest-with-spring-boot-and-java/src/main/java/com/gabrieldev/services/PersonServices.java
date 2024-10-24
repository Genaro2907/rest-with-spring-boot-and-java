package com.gabrieldev.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabrieldev.data.vo.v1.PersonVO;
import com.gabrieldev.data.vo.v2.PersonVOV2;
import com.gabrieldev.exceptions.ResourceNotFoundException;
import com.gabrieldev.mapper.GabrielMapper;
import com.gabrieldev.mapper.custom.PersonMapper;
import com.gabrieldev.model.Person;
import com.gabrieldev.repositories.PersonRepository;


@Service
public class PersonServices {

	
	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	PersonRepository repository;
	
	@Autowired
	PersonMapper mapper;
	
	
	public List<PersonVO> findAll() {
		logger.info("Finding all People!");
		return GabrielMapper.parseListObjects(repository.findAll(), PersonVO.class) ;
	}
	
	public PersonVO findById(Long id) {
		logger.info("Finding one person!");
		var entity =  repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		return GabrielMapper.parseObject(entity, PersonVO.class);
	}
	
	public PersonVO create(PersonVO person) {
		logger.info("Created one person!");
		var entity = GabrielMapper.parseObject(person, Person.class);
		var vo = GabrielMapper.parseObject(repository.save(entity), PersonVO.class);
		return vo;
		
	}
	
	public PersonVOV2 createV2(PersonVOV2 person) {
		logger.info("Created one person with V2!");
		var entity = mapper.convertVoToEntity(person);
		var vo = mapper.convertEntityToVo(repository.save(entity));
		return vo;
		
	}
	
	public PersonVO update(PersonVO person) {
		logger.info("Updating one person!");
		var entity = repository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		var vo = GabrielMapper.parseObject(repository.save(entity), PersonVO.class);
		return vo;  
	}
	public void delete(Long id) {
		logger.info("Deleting one person!");
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		repository.delete(entity);
		 
	}
}
