package net.skhu.etc;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail { // 이메일 보내기 위해 만든 클래스
	final String username = new PersonalInfo().getEmail(); // 이메일을 보낼 계정
	final String password = new PersonalInfo().getPassword(); // 이메일을 보낼 계정의 비밀번호
	Properties prop;

	public SendEmail() {
		prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "465");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.socketFactory.port", "465");
		prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	}
	public void sendEmail(String toEmail,String auth_key) {
		Session session = Session.getInstance(prop,
				new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});


		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(new PersonalInfo().getEmail()));
			message.setRecipients(
					Message.RecipientType.TO,
					InternetAddress.parse(toEmail) // 받는 사람 email
					);
			message.setSubject("SKHU-PS 이메일 인증");
			message.setText(new StringBuffer().append("아래의 링크를 눌러 회원가입 인증을 완료하세요.\n")
					.append("http://localhost:8081/authAlert/")
					.append(auth_key)
					.toString());
			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public void sendPwChange(String toEmail,String auth_key) {
		Session session = Session.getInstance(prop,
				new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});


		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(
					Message.RecipientType.TO,
					InternetAddress.parse(toEmail) // 받는 사람 email
					);
			message.setSubject("SKHU-PS 비밀번호 변경");
			message.setText(new StringBuffer().append("아래의 링크를 눌러 비밀번호를 바꾸세요.\n")
					.append("http://localhost:8081/changePw/")
					.append(auth_key)
					.toString());
			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}

