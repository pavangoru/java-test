package com.sharp.sharp.exception;

import java.util.HashMap;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler{

	
	public ResponseEntity<Object> handleValidationExceptions(
	  MethodArgumentNotValidException ex) {
	    HashMap<String, Object> errors = new HashMap<>();
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        errors.put(fieldName, errorMessage);
	    });
	    return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
	}
}
