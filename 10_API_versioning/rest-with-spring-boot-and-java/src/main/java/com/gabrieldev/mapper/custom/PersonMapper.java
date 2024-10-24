package com.gabrieldev.mapper.custom;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.gabrieldev.data.vo.v2.PersonVOV2;
import com.gabrieldev.model.Person;

@Service
public class PersonMapper {

	public PersonVOV2 convertEntityToVo (Person person) {
		PersonVOV2 vo = new PersonVOV2();
		vo.setId(person.getId());
		vo.setFirstName(person.getFirstName());
		vo.setLastName(person.getLastName());
		vo.setBirthDay(new Date());
		vo.setAddress(person.getAddress());
		vo.setGender(person.getGender());
		return vo;
		
		
	}
	
	public Person convertVoToEntity (PersonVOV2 person) {
		Person entity = new Person();
		entity.setId(person.getId());
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		// entity.setBirthDay(new Date());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		return entity;
		
		
	}
}
