package com.kh.spring.common.interceptor;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import com.kh.spring.common.code.ErrorCode;
import com.kh.spring.common.code.MemberGrade;
import com.kh.spring.common.exception.HandleableException;
import com.kh.spring.member.model.dto.Member;

public class Authinterceptor implements HandlerInterceptor{
	
	@Override
	public boolean preHandle(HttpServletRequest httpRequest, HttpServletResponse httpRespopnse, Object handler) throws IOException, ServletException {
		
		String[] uriArr = httpRequest.getRequestURI().split("/");

		// /member/join => [, member, join]

		if (uriArr.length != 0) {

			switch (uriArr[1]) {
			case "member":
				memberAuthorize(httpRequest, httpRespopnse, uriArr);
				break;
			case "admin":
				adminAuthorize(httpRequest, httpRespopnse, uriArr);
				break;
			case "board":
				boardAuthorize(httpRequest, httpRespopnse, uriArr);
				break;
			case "upload":
				boardAuthorize(httpRequest, httpRespopnse, uriArr);
				break;
			}
		}

		return true;
	};
	
	

	private void boardAuthorize(HttpServletRequest httpRequest, HttpServletResponse httpRespopnse, String[] uriArr) throws IOException, ServletException {
		
		HttpSession session = httpRequest.getSession();
		Member member = (Member)session.getAttribute("authentication");
		
		switch (uriArr[2]) {
		case "board-form":
			if(member == null) {
				throw new HandleableException(ErrorCode.UNAUTHORIZED_PAGE_ERROR);
			}
			break;
		case "upload":
			if(member == null) {
				throw new HandleableException(ErrorCode.UNAUTHORIZED_PAGE_ERROR);
			}
			break;
		default:
			break;
		}
		
	}

	private void adminAuthorize(HttpServletRequest httpRequest, HttpServletResponse httpRespopnse, String[] uriArr) throws IOException, ServletException {
		
		HttpSession session = httpRequest.getSession();
		Member member = (Member)session.getAttribute("authentication");
		
		// 비회원, 사용자 회원인지 판단
		if (member == null || MemberGrade.valueOf(member.getGrade()).ROLE.equals("user")) {
			throw new HandleableException(ErrorCode.UNAUTHORIZED_PAGE_ERROR);
		}
		
		// 슈퍼관리자니? 모든 어드민 페이지에 접근 가능
		if (MemberGrade.valueOf(member.getGrade()).DESC.equals("super")) {
			return;
		}
		
		switch (uriArr[2]) {
		case "member":
			if(!MemberGrade.valueOf(member.getGrade()).DESC.equals("member")) {
				throw new HandleableException(ErrorCode.UNAUTHORIZED_PAGE_ERROR);
			}
			break;
		case "board":
			if(!MemberGrade.valueOf(member.getGrade()).DESC.equals("board")) {
				throw new HandleableException(ErrorCode.UNAUTHORIZED_PAGE_ERROR);
			}
			break;
		default:
			break;
		}
		
	}

	private void memberAuthorize(HttpServletRequest httpRequest, HttpServletResponse httpRespopnse, String[] uriArr) throws IOException, ServletException {
		
		HttpSession session = httpRequest.getSession();
		
		switch (uriArr[2]) {
		case "mypage":
			
			if (session.getAttribute("authentication") == null) {
				throw new HandleableException(ErrorCode.UNAUTHORIZED_PAGE_ERROR);
			}
			break;
		default:break;
		}

	}

	public void init(FilterConfig fConfig) throws ServletException {
	}
}
