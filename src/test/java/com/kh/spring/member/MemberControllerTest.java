package com.kh.spring.member;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.spring.member.model.dto.Member;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;

import javax.servlet.http.Cookie;


@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/**/*-context.xml"})
public class MemberControllerTest {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// MockMVC : http 요청 밒 응답 상황 테스트를 위한 객체
	
	@Autowired
	WebApplicationContext wac;
	MockMvc mockMvc;
	
	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	@Test
	public void joinTest() throws Exception {
	// view 페이지로 타고들어가니 리포지토리나 mapper 안써도 댐.
		mockMvc.perform(post("/member/join").param("userId", "SungJin")
				.param("password", "1234")
				.param("tell", "0000111122")
				.param("email", "aa@bb.cc"))
		.andExpect(status().isOk())
		.andDo(print());
	}
	

	@Test
	public void joinWithJson() throws Exception {
		
		Member member = new Member();
		member.setUserId("testJson");
		member.setPassword("1234");
		member.setEmail("aa@cc.bb");
		member.setTell("01011112222");
		
		ObjectMapper mapper = new ObjectMapper();
		String memberJson = mapper.writeValueAsString(member);
		logger.debug(memberJson);
		// 직렬화
		logger.debug(mapper.readValue(memberJson, Map.class).toString());
		// 역직렬화
		
		
		mockMvc.perform(post("/member/join-json")
				.contentType(MediaType.APPLICATION_JSON)
				.content(memberJson))
		.andExpect(status().isOk())
		.andDo(print());
		// MemberController의 joinWithJson메서드를 통해 json을 자바객체로 변환
		
	}
	
	
	@Test
	public void loginImpl() throws Exception{

		mockMvc.perform(post("/member/login")
				.param("userId", "a")
				.param("password", "a"))
		.andExpect(status().is3xxRedirection()) // 300번 뜨면 성공. login 들어가면 리다이렉트(300) 때리니까.
		.andDo(print());
	}
	
	@Test
	public void mypage() throws Exception{
		
		Member member = new Member();
		member.setUserId("testId");
		member.setPassword("testPW");
		member.setEmail("testEmail");
		member.setTell("testTell");
		
		mockMvc.perform(get("/member/mypage")
				.cookie(new Cookie("JSESSIONID", "123456798")) // JSESSIONID는 짜피 난수생성이니까 암거나 일단 넣어줘
				.sessionAttr("authentication", member))
		.andExpect(status().isOk())
		.andDo(print());
		// 로그인 기능 확인 전에 쿠키와 세션 세팅
	}
	
	@Test
	public void idCheck() throws Exception{
		mockMvc.perform(get("/member/id-check?userId=a"))
		.andExpect(status().isOk())
		.andDo(print());
	// Body = disable. 굳
	// 컨트롤러에서 return을 Member로 잡으면 body에 Member 정보가 json으로 담겨져서 날라옴. 굳
	// 나중에 만일을 위해(한글을 쓸때? charset=ISO-8859-1을 UTF-8로)
	}
	
	
	
}
