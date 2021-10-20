package com.kh.spring.member.validator;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.kh.spring.member.model.dto.Member;
import com.kh.spring.member.model.repository.MemberRepository;

@Component // 쨌건 애도 스프링에 Bean으로 등록되어야 하니까
public class JoinFormValidator implements Validator{
	// 스프링으로 Validator만들기
	
	private final MemberRepository memberRepository;
	// @Autowird 많이 써봤으니 이제 생성자로 주입받을꺼임. 따로 이유가 있대. 왜냐...유행임...?!
	
	public JoinFormValidator(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		// Validator로 넘어오는 수많은 요청중에 어떤 타입을 잡을지 여기서 검증
		// 매개변수로 넘어오는 clazz의 타입이 JoinForm일때만 검증
		return JoinForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		JoinForm form = (JoinForm)target;
		
		// 요청 파라미터가 JoinForm에 바인딩
		// 그게 컨트롤러로 가기 전에 여기 validator를 탐
		// 즉, 검증 시점을 설정 
		
		// 1. 아이디 존재 유무
		if (memberRepository.selectMemberByUserId(form.getUserId()) != null) {
			errors.rejectValue("userId", "error-userId", "이미 존재하는 아이디입니다");
		}
		
		// 2. 비밀번호가 8글자 이상 영수특문 혼합
		Boolean vaild = Pattern.matches("(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Zㄱ-힣0-9])(.{8,})", form.getPassword());
		if (!vaild) {
			errors.rejectValue("password", "error-password", "비밀번호는 영수문자특문 혼합 8글자입니다");
		}
		// 3. 휴대폰 존재 유무
		vaild =  Pattern.matches("^\\d{9,11}$", form.getTell());
		if (!vaild) {
			errors.rejectValue("tell", "error-tell", "전화번호는 9~11자리의 숫자입니다");
		}
	}

}
