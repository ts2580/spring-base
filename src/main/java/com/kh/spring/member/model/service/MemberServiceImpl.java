package com.kh.spring.member.model.service;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.kh.spring.common.code.Config;
import com.kh.spring.common.mail.MailSender;
import com.kh.spring.member.model.dto.Member;
import com.kh.spring.member.model.repository.MemberRepository;
import com.kh.spring.member.validator.JoinForm;

import lombok.Data;




@Data
@Service
public class MemberServiceImpl implements MemberService {
	
	private final MemberRepository memberRepository;
	private final MailSender mailsender;
	private final RestTemplate http;
	private final PasswordEncoder passwordEncoder;
	

	public void insertMember(JoinForm form) {
		form.setPassword(passwordEncoder.encode(form.getPassword()));
		memberRepository.insertMember(form);
	}


	public Member authrnticationUser(Member member) {
		Member storedMember = memberRepository.selectMemberByUserId(member.getUserId());
		
		if (storedMember != null && passwordEncoder.matches(member.getPassword(), storedMember.getPassword())) {
			// 사용자가 입력한 아이디와 DB에 인코딩되서 저장된 비번 비교
			return storedMember;
		}
		
		return null;

	}


	public Member selectMemberByUserId(String userId) {
		return memberRepository.selectMemberByUserId(userId);
	}


	public void authenticateByEmail(JoinForm form, String token) {
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("mailTemplate", "join-auth-mail");
		body.add("userId", form.getUserId());
		body.add("persistToken", token);
		
		// RestTemplate의 기본 Content-type : application/json
		RequestEntity<MultiValueMap<String, String>> request = RequestEntity.post(Config.DOMAIN.DESC + "/mail").accept(MediaType.APPLICATION_FORM_URLENCODED).body(body);
		
		String htmlTxt = http.exchange(request, String.class).getBody();
		mailsender.send(form.getEmail(), "회원가입에 감사드립니다.", htmlTxt);
		
	}

}
