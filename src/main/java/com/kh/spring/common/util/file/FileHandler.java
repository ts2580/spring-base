package com.kh.spring.common.util.file;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FileHandler {
	
	@GetMapping("/download")
	public ResponseEntity<UrlResource> downloadFile(FileDTO file) throws UnsupportedEncodingException, URISyntaxException, MalformedURLException{
		
		UrlResource resource = new UrlResource(new URI(file.getLink()));
		
		ResponseEntity<UrlResource> response = 
				ResponseEntity.ok()
				.header("Content-Disposition", "attachment; filename="+URLEncoder.encode(file.getOriginFileName(), "UTF-8"))
				.body(resource);
		
		return response;
	}
	
	@GetMapping("/download2")
	public ResponseEntity<FileSystemResource> downloadFile2(FileDTO file) throws UnsupportedEncodingException, URISyntaxException, MalformedURLException{
		
		FileSystemResource resource = new FileSystemResource(FileSystems.getDefault().getPath(file.getDownloadPath(), file.getRenameFileName()));
		
		ResponseEntity<FileSystemResource> response = 
				ResponseEntity.ok()
				.header("Content-Disposition", "attachment; filename="+URLEncoder.encode(file.getOriginFileName(), "UTF-8"))
				.body(resource);
		
		return response;
	}
	
	
	
	
}
