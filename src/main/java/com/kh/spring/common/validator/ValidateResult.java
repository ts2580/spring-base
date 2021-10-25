package com.kh.spring.common.validator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

public class ValidateResult {
	
	private Map<String, Object> error; 
	
	public ValidateResult() {
		error = new HashMap<String, Object>();
	}
	
	public void addError(Errors errors) {
		
		for (FieldError fieldError : errors.getFieldErrors()) {
			error.put(fieldError.getField(), fieldError.getDefaultMessage());
		}
		// Validator에 에러들이 발생하면 그 값이 error Map에 담기고 value에 DefaultMessage 담김.
		// JSTL로 값 뺴올꺼니까 key값 필요
	}

	public Map<String, Object> getError() {
		return error;
	}
	
	
	
}
