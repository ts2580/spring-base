package com.kh.spring.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Component
@ControllerAdvice(basePackages = "com.kh.spring")
public class ExceptionAdvice {
	// AOP 용어
	// Advice : 공통관심사를 모듈화한 객체. spring03 패키지의 applicationContext 참조
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	// 예외 발생시 응답 상태코드를 500으로 지정. 안하면 200(문제업음)으로 날라가서 꼬임
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(HandleableException.class)
	public String handleableExceptionProcess(HandleableException e, Model model) {
		model.addAttribute("msg", e.error.MESSAGE);
		model.addAttribute("url", e.error.URL);
		return "error/result";
		
	}
	
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(DataAccessException.class)
	public String dataAccessExceptionProcess(DataAccessException e, Model model) {
		
		logger.error(e.getMessage());
		model.addAttribute("msg", "데이터베이스 접근도중 예외가 발생하였습니다");
		model.addAttribute("url", "/");
		return "error/result";
		
	}
}
