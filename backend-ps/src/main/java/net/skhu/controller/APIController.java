package net.skhu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.skhu.domain.Common;
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

	@RequestMapping("studentSignUp")
	public List<Common> commons() {
		return commonRepository.findAll();
	}

	@RequestMapping(value = "user/login", method = RequestMethod.POST)
	public User login(@RequestBody UserLoginModel userLoginModel) {
		System.out.println(userLoginModel);
		return userService.login(Integer.parseInt(userLoginModel.getUserNum()), userLoginModel.getPassword());
	}

	// ** 1을 로그인한 유저 값으로 변경(session, cache) 
	@RequestMapping(value = "user", method = RequestMethod.GET)
	public User user() {
		return userService.findById(1);
	}

	//PathVariable
}
