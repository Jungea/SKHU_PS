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
import net.skhu.model.FindPassModel;
import net.skhu.model.ProfileModel;
import net.skhu.model.SidebarModel;
import net.skhu.model.SignUpModel;
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
	public String studentSignUp(@RequestBody SignUpModel studentSignUpModel) {
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
			userService.SignUp(studentSignUpModel,false);
			return "success";
		}
	}

	@RequestMapping(value = "professorSignUp", method = RequestMethod.POST)
	public String professorSignUp(@RequestBody SignUpModel professorSignUpModel) {
		System.out.println("student:" + professorSignUpModel);
		// UserNum이 중복인지 확인하는 변수
		User redundantUserNum = userRepository.findByUserNum(Integer.parseInt(professorSignUpModel.getUserNum()));
		// Email이 중복인지 확인하는 변수
		User redundantEmail = userRepository.findByEmail(professorSignUpModel.getEmail());

		if (redundantUserNum != null && redundantEmail == null) { // userNum만 중복일떄
			return "userNum 중복입니다.";
		} else if (redundantUserNum == null && redundantEmail != null) { // email만 중복일떄
			return "email 중복입니다.";
		} else if (redundantUserNum != null && redundantEmail != null) { // userNum과 email 둘다 중복일 떄
			return "userNum과 email 둘 다 중복입니다.";
		} else {
			userService.SignUp(professorSignUpModel,true);
			return "success";
		}
	}
	
	// 이메일 인증
	@RequestMapping(value = "authKeyChange/{authKey}", method = RequestMethod.POST)
	public void authKeyChange(@PathVariable("authKey") String authKey) {
		userService.emailCheckChange(authKey);
	}
	// 비밀번호 변경 링크 보내기
	@RequestMapping(value = "findPass", method = RequestMethod.POST)
	public String findPass(@RequestBody FindPassModel findPassModel) {
		User user=userRepository.findByEmail(findPassModel.getEmail());
		if(user==null)  // 해당되는 email이 없을때
			return "email이 존재하지 않습니다.";
		else {
			userService.sendPwMail(findPassModel.getEmail());
			return "success";
		}
	}
	
	@RequestMapping(value = "changePw/{authKey}", method = RequestMethod.POST)
	public void changePw(@RequestBody FindPassModel findPassModel,@PathVariable("authKey") String authKey) {
		userService.changePw(findPassModel, authKey);
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
	
	//0427 윤영
	@RequestMapping(value = "alluser", method = RequestMethod.GET)
	public List<User> allUser() {
		return userService.findAll();
	}

}
