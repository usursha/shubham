package com.hpy.uam.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.hpy.uam.config.TemplateConfig;
import com.hpy.uam.dto.EmailDetailsRequestDto;
import com.hpy.uam.dto.RegistrationEmailDTO;
import com.hpy.uam.util.MethodUtil;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private static final Logger log = LoggerFactory.getLogger(EmailService.class);
	private final JavaMailSender javaMailSender;
	private final MethodUtil methodUtil;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Autowired
	private TemplateConfig templateConfig;

	@Autowired
	private Keycloak keycloak;

	@Autowired
	@org.springframework.context.annotation.Lazy
	UserRegistrationService registrationService;

	@Value("${keycloak.realm}")
	private String realm;

	@Value("${keycloak.adminClientId}")
	private String adminClientId;

	@Value("${spring.mail.username}")
	private String sender;

	@Value("${spring.mail.username}")
	private String fromEmail;

	public String sendSimpleMail(EmailDetailsRequestDto details) {
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

	public String findUserByEmail(String email) {
		String user = methodUtil.getUsersResource().searchByEmail(email, true).get(0).getFirstName();
		return user;
	}

	public String findUserNameByEmail(String email) {
		String user = methodUtil.getUsersResource().searchByEmail(email, true).get(0).getUsername();
		return user;
	}

//	public void sendResetPasswordEmail(String email, String newPassword) {
//		String firstname=findUserByEmail(email);
//		String username=findUserNameByEmail(email);
////		Setting username as password
//		newPassword=username;
//		String subject = "Password Reset Successfully";
//		 String body = "Hi "+ firstname +", \n\n\nYour username is: " +username+ "\n\nYour one time password is: " + newPassword
//				  + "\n\nKindly change your password after login for security reasons"+"\n\n\n\nRegards," +"\nHitachi Payments Service";
//		EmailDetails emailDetails = new EmailDetails(email, body, subject, null);
//		sendSimpleMail(emailDetails);
//	}

	public void sendResetPasswordEmail(String email, String newPassword) {
		String firstname = findUserByEmail(email);
		String username = findUserNameByEmail(email);
		String subject = "Password Reset Successfully";
		String body = "Hi " + firstname + ", \n\n\nYour username is: " + username + "\n\nYour one time password is: "
				+ newPassword + "\n\nKindly change your password after login for security reasons" + "\n\n\n\nRegards,"
				+ "\nHitachi Payments Service";
		EmailDetailsRequestDto emailDetails = new EmailDetailsRequestDto(email, body, subject, null);
		sendSimpleMail(emailDetails);

	}

	public String sendMailToUser(RegistrationEmailDTO registrationEmailDTO) {

		String to = registrationEmailDTO.getRecipient();
		String subject = registrationEmailDTO.getSubject();
		Map<String, Object> contextVars = registrationEmailDTO.getContextVars();

		String username = (String) contextVars.get("userName");
		String userId = methodUtil.getUserIdByUsername(username);

		UserResource userResource = keycloak.realm(realm).users().get(userId);

		String tempPassword = registrationService.getRandomPassword();
		log.info("Password sent in email templet::{}", tempPassword);
		contextVars.put("tempPassword", tempPassword);

		LocalDateTime expiryTime = LocalDateTime.now().plusHours(24);
		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedTime = expiryTime.format(formatter);
		contextVars.put("expiryTime", formattedTime);
		String templateName = "UserCreation.html";

		try {
			Context context = new Context();
			context.setVariables(contextVars != null ? contextVars : new HashMap<>());
			String content = templateEngine.process(templateName, context);

			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);
			helper.setFrom(fromEmail);
			javaMailSender.send(message);

			log.info("Email sent successfully to {}", to);
			return "Email sent successfully to " + to;
		} catch (Exception e) {
			log.error("Failed to send email to {}: {}", to, e.getMessage(), e);
			return "Failed to send email: " + e.getMessage();
		}
	}

}
