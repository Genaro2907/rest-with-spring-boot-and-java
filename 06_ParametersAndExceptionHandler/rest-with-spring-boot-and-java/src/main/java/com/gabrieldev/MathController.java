package com.gabrieldev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MathController {
	
	@Autowired
	private MathService mathService;
	
	// private static final AtomicLong counter = new AtomicLong();

	@GetMapping(value = "/sum/{numberOne}/{numberTwo}")
	public Double sum(@PathVariable String numberOne, @PathVariable String numberTwo) throws Exception {
		return mathService.sumService(numberOne, numberTwo);
	}

	@GetMapping(value = "/subtraction/{numberOne}/{numberTwo}")
	public Double subtraction(@PathVariable String numberOne, @PathVariable String numberTwo) throws Exception {
		return mathService.subtraction(numberOne, numberTwo);

	}

	@GetMapping(value = "/multiplication/{numberOne}/{numberTwo}")
	public Double multiplication(@PathVariable String numberOne, @PathVariable String numberTwo){
		return mathService.multiplicationService(numberOne, numberTwo);

	}

	@GetMapping(value = "/division/{numberOne}/{numberTwo}")
	public Double division(@PathVariable String numberOne, @PathVariable String numberTwo) throws Exception {
		return mathService.divisionService(numberOne, numberTwo);

	}
	@GetMapping(value = "/mean/{numberOne}/{numberTwo}")
	public Double mean(@PathVariable String numberOne, @PathVariable String numberTwo) throws Exception {
		return mathService.meanService(numberOne, numberTwo);
	}

	@GetMapping(value = "/squareRoot/{number}")
	public Double squareRoot(@PathVariable String number){

		return mathService.squareRootService(number);

	}

}
