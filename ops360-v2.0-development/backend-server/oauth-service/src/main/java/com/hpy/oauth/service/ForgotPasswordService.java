package com.hpy.oauth.service;

import java.util.ArrayList;
import java.util.List;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hpy.oauth.dto.EmailDetails;
import com.hpy.oauth.dto.ForgotPasswordDto;
import com.hpy.oauth.util.MethodUtil;
import com.hpy.oauth.util.PasswordUtils;

@Service
public class ForgotPasswordService {
	
	String pwd="";
	
	@Autowired
	private EmailService emailService;
	@Autowired
	private PasswordUtils passwordUtils;
	@Autowired
	private MethodUtil methodUtil;
	
	public String findUserByEmail(String email) {
		String user=methodUtil.getUsersResource().searchByEmail(email, true).get(0).getFirstName();
		return user;
		
	}
	
	
	public void setPassword(UserRepresentation user, boolean isTemporary, String password) {
		CredentialRepresentation credentials=new CredentialRepresentation();
    	pwd = passwordUtils.generatedRandomPassword();
    	credentials.setValue(pwd);
    	credentials.setTemporary(isTemporary);
    	credentials.setType(CredentialRepresentation.PASSWORD); 
		List<CredentialRepresentation> list = new ArrayList<>();
		list.add(credentials);
		user.setCredentials(list);
		methodUtil.getUsersResource().get(user.getId()).update(user);

	}
	
	public void sendResetPasswordEmail(String email, String newPassword) {
		String firstname=findUserByEmail(email);
		String subject = "Reset Password!!!";
		String body = "Hi "+firstname+" , \n\nyour one time password is: " + newPassword
				+ "\nKindly change your password after logging in for security reasons."
				+ "\nThank you for choosing our services.";

		EmailDetails emailDetails = new EmailDetails(email, body, subject, null);
		emailService.sendSimpleMail(emailDetails);

	}

	public String resetPasswordEmail(ForgotPasswordDto forgotPasswordDto) {//throws MailSendException{
		UserRepresentation user = methodUtil.getUsersResource().search(forgotPasswordDto.getUserName()).get(0);
		String email= user.getEmail();
		List<String> userMobiles = user.getAttributes().get("Mobile");
	        if (userMobiles.contains(forgotPasswordDto.getUserMobileNumber())) {
	        	CredentialRepresentation credentials=new CredentialRepresentation();
	        	pwd = passwordUtils.generatedRandomPassword();
	        	credentials.setValue(pwd);
	        	credentials.setTemporary(true);
	        	credentials.setType(CredentialRepresentation.PASSWORD); 
	    		List<CredentialRepresentation> list = new ArrayList<>();
	    		list.add(credentials);
	    		user.setCredentials(list);
	    		methodUtil.getUsersResource().get(user.getId()).update(user);

			sendResetPasswordEmail(email, pwd);
			return "reset password email sent successfully!!!";
        } else {
            return "Mobile number is invalid";
        }
	}
}
