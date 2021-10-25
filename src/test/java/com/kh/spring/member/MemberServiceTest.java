package com.kh.spring.member;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/**/*-context.xml"})
public class MemberServiceTest {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Test
	public void passwordEncoderTest() {
		String password = "123456qwer!!!";
				
		String encodedPassword = passwordEncoder.encode(password);
		logger.debug(encodedPassword);
		encodedPassword = passwordEncoder.encode(password);
		logger.debug(encodedPassword);
		// 다시 돌리면 다른값나옴. 같은 key라고 항상 같은 value가 나오지 않음. 
		
		logger.debug("결과 : " + passwordEncoder.matches(password, encodedPassword));
	}
	
	
}
