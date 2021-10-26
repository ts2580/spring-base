package com.kh.spring.common.util.file;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FileHandler {
	
	@GetMapping("/download")
	public ResponseEntity<FileSystemResource> downloadFile(FileDTO file){
		// 파일 다운받을려면 응답 헤더를 건드려야함. 
		
		
		return null;
	}

}
