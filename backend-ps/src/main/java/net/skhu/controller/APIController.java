package net.skhu.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.skhu.domain.Detail;
import net.skhu.domain.Project;
import net.skhu.domain.ProjectJoin;
import net.skhu.domain.User;
import net.skhu.model.FindPassModel;
import net.skhu.model.MakeProjectModel;
import net.skhu.model.ProfileModel;
import net.skhu.model.SidebarModel;
import net.skhu.model.SignUpModel;
import net.skhu.model.UserLoginModel;
import net.skhu.repository.UserRepository;
import net.skhu.service.DetailService;
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
	@Autowired
	DetailService detailService;

	public int getLoginUserId(HttpServletRequest request) {
		HttpSession session = request.getSession();
		int userId = (int) session.getAttribute("userId");
		return userId;
	}

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
			userService.SignUp(studentSignUpModel, false);
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
			userService.SignUp(professorSignUpModel, true);
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
		User user = userRepository.findByEmail(findPassModel.getEmail());
		if (user == null) // 해당되는 email이 없을때
			return "email이 존재하지 않습니다.";
		else {
			userService.sendPwMail(findPassModel.getEmail());
			return "success";
		}
	}

	@RequestMapping(value = "changePw/{authKey}", method = RequestMethod.POST)
	public void changePw(@RequestBody FindPassModel findPassModel, @PathVariable("authKey") String authKey) {
		userService.changePw(findPassModel, authKey);
	}

	// 로그인
	@RequestMapping(value = "user/login", method = RequestMethod.POST)
	public User login(@RequestBody UserLoginModel userLoginModel, HttpServletRequest request) {
		System.out.println(userLoginModel);
		User u = userService.login(Integer.parseInt(userLoginModel.getUserNum()), userLoginModel.getPassword());
		if (u != null) {
			HttpSession session = request.getSession();
			session.setAttribute("userId", u.getUserId());
		}
		return u;
	}

	//
	@RequestMapping(value = "user", method = RequestMethod.GET)
	public User user(HttpServletRequest request) {
		return userService.findById(getLoginUserId(request));
	}

	@RequestMapping(value = "user/projects", method = RequestMethod.GET)
	public List<Project> userProjects(HttpServletRequest request) {
		return projectService.findProjectByUserId(getLoginUserId(request));
	}

	// 마이페이지 프로필이 수정되고 저장되었을 때
	@RequestMapping(value = "user/profile", method = RequestMethod.POST)
	public void profile(@RequestBody ProfileModel profileModel, HttpServletRequest request) {
		System.out.println(profileModel);
		userService.update(getLoginUserId(request), profileModel);
	}

	@RequestMapping(value = "user/sidebar", method = RequestMethod.GET)
	public List<SidebarModel> userSidebar(HttpServletRequest request) {
		return projectService.userSidebar(getLoginUserId(request));
	}

	// 0427 윤영
	@RequestMapping(value = "alluser", method = RequestMethod.GET)
	public List<User> allUser() {
		return userService.findAll();
	}

	// 프로젝트 생성
	@RequestMapping(value = "makeProject", method = RequestMethod.POST)
	public String makeProject(@RequestBody MakeProjectModel makeProjectModel) {
		System.out.println("tag:" + makeProjectModel.getTag());
		return projectService.makeProject(makeProjectModel);

	}

	// 학과 셀렉션을 위한 메소드
	@RequestMapping(value = "departments", method = RequestMethod.GET)
	public List<Detail> departments() {
		return detailService.getDepartments();
	}

	// 프로젝트에서 멤버 초대(팀장만)
	@RequestMapping(value = "invite/{projectId}/{userNum}", method = RequestMethod.GET)
	public String invite(@PathVariable("projectId") String projectId, @PathVariable("userNum") String userNum,
			HttpServletRequest request) {

		int intUserNum = Integer.parseInt(userNum);
		int intProjectId = Integer.parseInt(projectId);

		if (userService.findByUserNum(intUserNum) == null)
			return "잘못된 번호를 입력하였습니다.";

		User capUser = userService.findById(getLoginUserId(request));
		if (intUserNum == capUser.getUserNum())
			return "자기 자신의 번호입니다.";

		// stream으로 바꾸기
		List<ProjectJoin> member = projectService.member(intProjectId);
		for (int i = 0; i < member.size(); i++)
			if (member.get(i).getUser().getUserNum() == intUserNum)
				return "팀원의 번호입니다.";

		Boolean result = projectService.inviteMember(intProjectId, intUserNum);
		if (result == true)
			return userNum + "님에게 초대 메세지를 보냈습니다.";
		else
			return "프로젝트에 참여신청한 유저입니다.";
	}

	@RequestMapping(value = "a", method = RequestMethod.GET)
	public List<ProjectJoin> invite() {
		return projectService.member(5);
	}

}
