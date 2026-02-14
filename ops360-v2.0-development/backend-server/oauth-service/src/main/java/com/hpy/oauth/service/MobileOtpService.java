//package com.hpy.oauth.service;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.util.Random;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j
//public class MobileOtpService {
//
//	@Value("${gupshup.url}")
//	private String gupshupUrl;
//
//	@Value("${gupshup.user.id}")
//	private String userId;
//
//	@Value("${gupshup.password}")
//	private String password;
//
//	@Value("${gupshup.otp.template}")
//	private String gupshupOtpTemplate;
//
//	@Value("${gupshup.otp.validity}")
//	private String gupshupOtpValidity;
//
//	public String sendOTP(String miduserId, String mobileNumber) {
//
//		log.info("mid:{} mobileNumber:{} ", mid, mobileNumber);
//
//		String otp = generateOTP();
//
////		saveOTP(mid, otp, mobileNumber);
//
//		String otpMessage = String.format(gupshupOtpTemplate, otp, mid, gupshupOtpValidity);
//
//		sendOTPWithGupshup(mobileNumber, otp, otpMessage);
//
//		log.info("static userMobile: {} otpMessage: {}", mobileNumber, otpMessage);
//
//		log.info("mid:{} otp:{} ", mid, otp);
//		log.info("otpMessage:{} ", otpMessage);
//		return otpMessage;
//	}
//
//	public String sendOTPWithGupshup(String mobileNumber, String otp, String otpMessage) {
//		try {
//			String encodedMessage = URLEncoder.encode(otpMessage, "UTF-8");
//			String urlString = gupshupUrl + "?method=SendMessage" + "&send_to=" + mobileNumber + "&msg="
//					+ encodedMessage + "&msg_type=TEXT" + "&userid=" + userId + "&auth_scheme=plain" + "&password="
//					+ password + "&v=1.1" + "&format=text";
//			log.info("urlString: {}", urlString);
//
//			URL url = new URL(urlString);
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//			connection.setRequestMethod("POST");
//			connection.setRequestProperty("Content-Type", "application/json");
//			connection.setDoOutput(true);
//
//			OutputStream os = connection.getOutputStream();
//			os.flush();
//			os.close();
//
//			int responseCode = connection.getResponseCode();
//			if (responseCode == HttpURLConnection.HTTP_OK) {
//				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//				String inputLine;
//				StringBuilder response = new StringBuilder();
//				while ((inputLine = in.readLine()) != null) {
//					response.append(inputLine);
//				}
//				in.close();
//
//				String responseBody = response.toString();
//				log.info("responseBody: {}", responseBody);
//
//				String[] parts = responseBody.split("\\|");
//				String resp = parts[0].trim();
//				String mobile = parts[1].trim();
//				String otpId = parts[2].trim();
//
//				log.info("resp: {}\n mobile: {}\n otpId: {}\n", resp, mobile, otpId);
//
//				if (responseBody.contains("success")) {
//					return "OTP sent successfully on mobileNumber: " + mobileNumber;
//				} else {
//					return "Failed to send OTP on mobileNumber: " + mobileNumber + " Response: " + responseBody;
//				}
//			} else {
//				return "Failed to send OTP. Status Code: " + responseCode;
//			}
//		} catch (Exception e) {
//			return "Failed to send OTP. Exception: " + e;
//		}
//	}
//
//	public String generateOTP() {
//		Random random = new Random();
//		int otpLength = 6;
//		StringBuilder otp = new StringBuilder();
//
//		for (int i = 0; i < otpLength; i++) {
//			otp.append(random.nextInt(10));
//		}
//
//		return otp.toString();
//	}
//
//}
