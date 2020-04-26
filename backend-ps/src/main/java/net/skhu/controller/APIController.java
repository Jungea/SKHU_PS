package net.skhu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.skhu.domain.Project;
import net.skhu.domain.User;
import net.skhu.model.ProfileModel;
import net.skhu.model.SidebarModel;
import net.skhu.model.StudentSignUpModel;
import net.skhu.model.UserLoginModel;
import net.skhu.repository.UserRepository;
import net.skhu.service.ProjectService;
import net.skhu.service.UserService;

@RestController
@RequestMapping("api")
public class APIController {
	@Autowired
	UserService userService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ProjectService projectService;

	@RequestMapping(value = "studentSignUp", method = RequestMethod.POST)
	public String studentSignUp(@RequestBody StudentSignUpModel studentSignUpModel) {
		System.out.println("student:" + studentSignUpModel);
		// UserNum이 중복인지 확인하는 변수
		User redundantUserNum = userRepository.findByUserNum(Integer.parseInt(studentSignUpModel.getUserNum()));
		// Email이 중복인지 확인하는 변수
		User redundantEmail = userRepository.findByEmail(studentSignUpModel.getEmail());

		if (redundantUserNum != null && redundantEmail == null) { // userNum만 중복일떄
			return "userNum 중복입니다.";
		} else if (redundantUserNum == null && redundantEmail != null) { // email만 중복일떄
			return "email 중복입니다.";
		} else if (redundantUserNum != null && redundantEmail != null) { // userNum과 email 둘다 중복일 떄
			return "userNum과 email 둘 다 중복입니다.";
		} else {
			userService.StudentSignUp(studentSignUpModel);
			return "success";
		}
	}

	// 이메일 인증
	@RequestMapping(value = "authKeyChange/{authKey}", method = RequestMethod.POST)
	public void authKeyChange(@PathVariable("authKey") String authKey) {
		userService.emailCheckChange(authKey);
	}

	// PathVariable

	// 로그인
	// session, cookie 추가 필요
	@RequestMapping(value = "user/login", method = RequestMethod.POST)
	public User login(@RequestBody UserLoginModel userLoginModel) {
		System.out.println(userLoginModel);
		return userService.login(Integer.parseInt(userLoginModel.getUserNum()), userLoginModel.getPassword());
	}

	// ** 1을 로그인한 유저 값으로 변경(session, cookie)
	@RequestMapping(value = "user", method = RequestMethod.GET)
	public User user() {
		return userService.findById(1);
	}

	@RequestMapping(value = "user/projects", method = RequestMethod.GET)
	public List<Project> userProjects() {
		return projectService.findProjectByUserId(1);
	}

	// 마이페이지 프로필이 수정되고 저장되었을 때
	@RequestMapping(value = "user/profile", method = RequestMethod.POST)
	public void profile(@RequestBody ProfileModel profileModel) {
		System.out.println(profileModel);
//		userService.update(1,profileModel);
	}

	@RequestMapping(value = "user/sidebar", method = RequestMethod.GET)
	public List<SidebarModel> userSidebar() {
		return projectService.userSidebar(1);
	}

}
