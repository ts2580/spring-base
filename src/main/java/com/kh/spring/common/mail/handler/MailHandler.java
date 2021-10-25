package com.kh.spring.common.mail.handler;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MailHandler {
	
	@PostMapping("mail")
	public String writeMailTemplate(@RequestParam Map<String, String> template) {
		
		// Map은 게터 세터가 없으니 requestParam임
		return "mail-template/" +  template.get("mailTemplate");
	}
}
