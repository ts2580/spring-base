package com.kh.spring.board.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.kh.spring.board.model.dto.Board;

public interface BoardService {
	
	 void insertBoard(List<MultipartFile> multiparts, Board board);
	 // 인터페이스로 프록시객체를 만들기 위해

	Map<String, Object> selectBoardById(String bdIdx);
}
