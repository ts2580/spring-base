package com.kh.spring.member.model.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class Member {
	// lombok 라이브러리에서 쓰는 annotation임. 게터세터투스트링매개변수있는생성자 자동으로 잡아줌
	
	private String userId;
	private String password;
	private String email;
	private String grade;
	private String tell;
	private Date rentableDate;
	private Date regDate;
	private int isLeave;

}
