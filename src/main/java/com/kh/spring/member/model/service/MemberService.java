package com.kh.spring.member.model.service;

import com.kh.spring.member.model.dto.Member;
import com.kh.spring.member.validator.JoinForm;

public interface MemberService {
	
	void insertMember(JoinForm form);


	Member authrnticationUser(Member member);


	Member selectMemberByUserId(String userId);


	void authenticateByEmail(JoinForm form, String token);

}
