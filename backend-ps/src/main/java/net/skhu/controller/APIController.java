package net.skhu.controller;

import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
=======
import org.springframework.web.bind.annotation.PathVariable;
>>>>>>> 5808cc8c5697b00c5fd3a8c575576ce2cdf853a8
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.skhu.domain.User;
import net.skhu.etc.SendEmail;
import net.skhu.domain.User;
import net.skhu.model.UserLoginModel;
import net.skhu.repository.CommonRepository;
import net.skhu.service.UserService;

@RestController
@RequestMapping("api")
public class APIController {
	@Autowired
	CommonRepository commonRepository;
	@Autowired
	UserService userService;
	
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

	@RequestMapping(value = "user/login", method = RequestMethod.POST)
	public User login(@RequestBody UserLoginModel userLoginModel) {
		System.out.println(userLoginModel);
		return userService.login(Integer.parseInt(userLoginModel.getUserNum()), userLoginModel.getPassword());
	}

}
