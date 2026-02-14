package com.hpy.oauth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.hpy.oauth.dto.EmailDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private static final Logger log = LoggerFactory.getLogger(EmailService.class);
	private final JavaMailSender javaMailSender;
//	private final ForgotPasswordService forgotPasswordService;
	
	@Value("${spring.mail.username}")
	private String sender;

	public String sendSimpleMail(EmailDetails details) {
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			// Setting up necessary details
			mailMessage.setFrom(sender);
			mailMessage.setTo(details.getRecipient());
			mailMessage.setText(details.getMsgBody());
			mailMessage.setSubject(details.getSubject());

			// Sending the mail
			javaMailSender.send(mailMessage);
			log.info("Mail Send Successfully!!!");
			return "Mail Sent Successfully...";
		} // Catch block to handle the exceptions
		catch (Exception e) {
			log.error("Error while sending mail", e);
			return "Error while Sending Mail";
		}

	}

	
	

}
