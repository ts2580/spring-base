package com.kh.spring.board.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.kh.spring.board.model.dto.Board;
import com.kh.spring.board.model.repository.BoardReposirory;
import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.service.BoardServiceImpl;
import com.kh.spring.common.util.file.FileUtil;
import com.kh.spring.member.model.dto.Member;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("board")
public class BoardController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private final BoardService boardService;
	
	@GetMapping("board-form")
	public void boardForm(){}
	
	@PostMapping("upload")
	public String uploadBoard(Board board, @RequestParam List<MultipartFile> files, @SessionAttribute("authentication") Member member) {
		// 스프링에서 파일 올릴떈 List로 받는다. 타입은 MultipartFile. 얼마나 편한지는 한번 써보자. 여기에 파일을 챱챱챱 넣어줌
		
		logger.debug("파일 사이즈 : ", files.size());
		logger.debug("파일 0인덱스 : ", files.get(0));
		logger.debug("f.isEmpty : ", files.get(0).isEmpty());
		
		board.setUserId(member.getUserId());
		boardService.insertBoard(files, board);
		
		  
		return "redirect:/";
	}
	
	@GetMapping("board-detail")
	public void boardDetail(Model model, String bdIdx){
		
		Map<String, Object> commandMap = boardService.selectBoardById(bdIdx);
		model.addAllAttributes(commandMap);
		
		
	}

}
