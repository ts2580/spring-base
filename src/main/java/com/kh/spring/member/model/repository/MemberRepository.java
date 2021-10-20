package com.kh.spring.member.model.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.kh.spring.member.model.dto.Member;
import com.kh.spring.member.validator.JoinForm;

@Mapper
public interface MemberRepository {
	
	@Insert("insert into member(user_id, password, email, tell) values(#{userId}, #{password}, #{email}, #{tell})")
	void insertMember(JoinForm form);
	
	@Select("select * from member where user_id = #{userId} and password = #{password}")
	Member authrnticationUser(Member member);
	
	@Select("select * from member where user_id = #{userId}")
	Member selectMemberByUserId(String userId);


	
}
