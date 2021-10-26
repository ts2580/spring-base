package com.kh.spring.common.util.file;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;


import org.springframework.web.multipart.MultipartFile;

import com.kh.spring.common.code.Config;
import com.kh.spring.common.code.ErrorCode;
import com.kh.spring.common.exception.HandleableException;

public class FileUtil {
	
	public FileDTO fileUpload(MultipartFile file) {
		FileDTO fileDto = null;
		try {
			String uploadPath = createUploadPath(createSubPath());
			fileDto = createFileDTO(file);
			File dest = new File(uploadPath+fileDto.getOriginFileName());
			file.transferTo(dest);
		} catch (IllegalStateException | IOException e) {
			throw new HandleableException(ErrorCode.FAILED_FILE_UPLOAD, e);
		}
		
		return fileDto;
	}
	
	private String createSubPath() {

		// 2. 파일 업로드 날자 기준으로 저장될 파일 경로 생성(날자)
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int date = cal.get(Calendar.DAY_OF_MONTH);

		String subPath = year + "\\" + month + "\\" + date + "\\";

		return subPath;
	}
	
	private String createUploadPath(String subPath) {

		// 2. 파일 업로드 날자 기준으로 저장될 파일 경로 생성
		String uploadPath = Config.UPLOAD_PATH.DESC + subPath;

		File dir = new File(uploadPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		return uploadPath;
	}
	
	private FileDTO createFileDTO(MultipartFile filePart) {
		String originFileName = filePart.getOriginalFilename(); // 4. File_INFO 테이블에 저장할 FileDTO 생성	
		String renameFileName = UUID.randomUUID().toString(); // 1. 서버에 저장될 유니크한 파일이름 생성
		
		if (originFileName.contains(".")) {
			renameFileName = renameFileName + originFileName.substring(originFileName.lastIndexOf("."));
		}
		
		String savePath = createSubPath();

		FileDTO fileDTO = new FileDTO();
		fileDTO.setOriginFileName(originFileName);
		fileDTO.setRenameFileName(renameFileName);
		fileDTO.setSavePath(savePath);

		return fileDTO;
	}
	
	
}
