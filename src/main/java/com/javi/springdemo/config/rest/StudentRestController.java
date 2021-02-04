package com.javi.springdemo.config.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javi.springdemo.entity.Student;

@RestController
@RequestMapping("/api")
public class StudentRestController {
	
	private List<Student> theStudents;
	
	//define the @PostConstruct to load the student data only once
	@PostConstruct
	public void loadData() {
		
		theStudents = new ArrayList<>();
		
		theStudents.add(new Student("Pepe", "Perez"));
		theStudents.add(new Student("Paco", "Rodriguez"));
		theStudents.add(new Student("Maria", "Garcia"));
		
	}
	
	//define endpoint for "/students" - return list of students
	
		@GetMapping("/students")
		public List<Student> getStudents() {
			return theStudents;
		}
	
		//access the endpoint with /api/students
		
	//define endpoint for "/students/{studentId}" - return student at index
		
		@GetMapping("/students/{studentId}")
		public Student getStudent(@PathVariable int studentId) {
			//index into the list
			
			//check the studentId against list size
			
			if ((studentId >= theStudents.size()) || (studentId < 0)) {
				throw new StudentNotFoundException("Student id not found - " + studentId);
			}
			
			return theStudents.get(studentId);
			
		}
		
		//Add an exception handler using @ExceptionHandler
		
		@ExceptionHandler
		public ResponseEntity<StudentErrorResponse> handleException(StudentNotFoundException exc) {
			
			//create a StudentErrorRepsonse
			StudentErrorResponse error = new StudentErrorResponse();
			
			error.setStatus(HttpStatus.NOT_FOUND.value());
			error.setMessage(exc.getMessage());
			error.setTimeStamp(System.currentTimeMillis());
			
			//return ResponseEntity
			
			return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
			
		}
		
		//add another exception handler... to catch any exception (catch all)
		@ExceptionHandler
		public ResponseEntity<StudentErrorResponse> handleException(Exception exc) {
			
			//create a StudentErrorRepsonse
			StudentErrorResponse error = new StudentErrorResponse();
			
			error.setStatus(HttpStatus.BAD_REQUEST.value());
			error.setMessage(exc.getMessage());
			error.setTimeStamp(System.currentTimeMillis());
			
			//return ResponseEntity
			
			return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
		

}
