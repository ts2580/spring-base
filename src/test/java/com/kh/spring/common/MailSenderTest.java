package com.kh.spring.common;

import java.util.Date;
import java.util.Map;

import javax.mail.Message;
import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/**/*-context.xml"})
public class MailSenderTest {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	JavaMailSenderImpl mailSender;
	
	@Autowired
	RestTemplate http;
	
	@Autowired
	ObjectMapper mapper;
	
	@Test
	public void sendEmail() throws Exception{
		MimeMessage msg =  mailSender.createMimeMessage();
		msg.setFrom("trstyq@gamil.com");
		msg.setRecipients(Message.RecipientType.TO, "trstyq@gamil.com");
		msg.setSubject("메일테스트");
		msg.setSentDate(new Date());
		msg.setText("<h1>Email Test</h1>", "UTF-8", "HTML");
		mailSender.send(msg);
	}
	
	@Test
	public void restTemplateTest() throws JsonMappingException, JsonProcessingException, RestClientException {
		RequestEntity<Void> request = RequestEntity
				.get("https://dapi.kakao.com//v3/search/book?query=마도정병")
				.header("Authorization", "KakaoAK 060604da6e42267573842a35f2c94da5")
				.build();
	
	JsonNode root = mapper.readTree(http.exchange(request, String.class).getBody());
	
	for (JsonNode jsonNode : root.findValues("url")) {
		logger.debug(jsonNode.asText());
	}
	
	// 양파 까듯이 깔 필요없고 필요한 속성으로 곧장 뺴옴
	
	}
	
	@Test
	public void restTemplatePostTest() throws Exception {
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("source", "en");
		body.add("target", "ko");
		body.add("text", "I love chocolet");
		
		RequestEntity<MultiValueMap<String, String>> request = 
				RequestEntity.post("https://openapi.naver.com/v1/papago/n2mt")
				.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
				.header("X-Naver-Client-Id", "BDZJpEv6LvBtiOAPS8PY")
				.header("X-Naver-Client-Secret", "PnEq6ry2SY")
				.body(body);
		
		JsonNode root = mapper.readTree(http.exchange(request, String.class).getBody());
		logger.debug(root.findValue("translatedText").asText());
		
		// 양파 까듯이 깔 필요없고 필요한 속성으로 곧장 뺴옴2
	}
	
	
}
