package com.kh.spring.member.model.repository;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {
	
	@Autowired
	private SqlSessionTemplate session;
	
	public String selectPasswordByUserId(String userId) {
		return session.selectOne("com.kh.spring.mybatis.mybatisMapper.selectPasswordByUserId", userId);
		
	}

}
