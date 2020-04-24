package net.skhu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.skhu.domain.User;
import net.skhu.etc.SendEmail;
import net.skhu.repository.CommonRepository;

@RestController
@RequestMapping("api")
public class APIController {
	@Autowired
	CommonRepository commonRepository;
	
//	@RequestMapping("studentSignUp")
//	public void commons() {
//		SendEmail es = new SendEmail();
//		es.sendEmail("gil1122@naver.com","111"); // 받는 사람 이메일
//	}
	@RequestMapping(value="studentSignUp",method = RequestMethod.POST)
	public void studentSignUp(@RequestBody User user) {
		System.out.println(user.getUserNum());
//		SendEmail es = new SendEmail();
//		es.sendEmail("gil1122@naver.com","111"); // 받는 사람 이메일
	}

}
