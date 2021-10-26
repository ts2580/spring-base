package com.kh.spring.board.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.spring.board.model.dto.Board;
import com.kh.spring.board.model.repository.BoardReposirory;
import com.kh.spring.common.code.ErrorCode;
import com.kh.spring.common.exception.HandleableException;
import com.kh.spring.common.util.file.FileDTO;
import com.kh.spring.common.util.file.FileUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
	
	private final BoardReposirory boardReposirory;
	
	public void insertBoard(List<MultipartFile> multipart, Board board) {
		boardReposirory.insertBoard(board);
		
		FileUtil util = new FileUtil();
		
		for (MultipartFile multipartFile : multipart) {
			if (!multipartFile.isEmpty()) {
				boardReposirory.insertFileInfo(util.fileUpload(multipartFile));
			}
		}
		
		
	}

	public Map<String, Object> selectBoardById(String bdIdx) {

		Board board = boardReposirory.selectBoardByIdx(bdIdx);
		List<FileDTO> files = boardReposirory.selectFilesByBdIdx(bdIdx);
		
		return Map.of("board",board,"files",files);
	}
}
