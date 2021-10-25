package com.kh.spring.common.mail;

import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.kh.spring.common.code.Config;
import com.kh.spring.common.code.ErrorCode;
import com.kh.spring.common.exception.HandleableException;

@Component
public class MailSender {

	JavaMailSender mailSender;
	
	public MailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public void send(String to, String subject, String htmlText) {
		
		MimeMessage msg =  mailSender.createMimeMessage();
	
		try {
			msg.setFrom(Config.COMPANY_EMAIL.DESC);
			msg.setRecipients(Message.RecipientType.TO, to);
			msg.setSubject(subject);
			msg.setSentDate(new Date());
			msg.setText(htmlText, "UTF-8", "HTML");
			mailSender.send(msg);
		} catch (MessagingException e) {
			throw new HandleableException(ErrorCode.MAIL_SEND_FAIL_EXCEPTION);
		}
		
		
	}
	
	
	
	
	
	
}
