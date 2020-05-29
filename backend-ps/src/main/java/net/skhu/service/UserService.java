package net.skhu.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.skhu.domain.Detail;
import net.skhu.domain.ProjectJoin;
import net.skhu.domain.Timeline;
import net.skhu.domain.User;
import net.skhu.etc.SendEmail;
import net.skhu.etc.TempAuth_key;
import net.skhu.model.FindPassModel;
import net.skhu.model.ProfileModel;
import net.skhu.model.SignUpModel;
import net.skhu.repository.DetailRepository;
import net.skhu.repository.ProjectJoinRepository;
import net.skhu.repository.TimelineRepository;
import net.skhu.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	DetailRepository detailRepository;
	@Autowired
	ProjectJoinRepository projectJoinRepository;
	@Autowired
	TimelineRepository timelineRepository;

	public User login(int userNum, String password) {
		return userRepository.findByUserNumAndPassword(userNum, password);
	}

	@Transactional
	public void SignUp(SignUpModel studentSignUpModel, boolean userType) {
//		 userType에서 학생은 false, 교수는 true
		String auth_key = new TempAuth_key().getKey(44, false); // 이메일 인증 키 설정
		Detail detDeptId;
		User user;
		if(studentSignUpModel.getDetDeptId() != null) {
			detDeptId = detailRepository.findById(Integer.parseInt(studentSignUpModel.getDetDeptId())).get();
			user = new User(Integer.parseInt(studentSignUpModel.getUserNum()), studentSignUpModel.getName(), studentSignUpModel.getGrade(),
					studentSignUpModel.getEmail(), studentSignUpModel.getPassword(), LocalDateTime.now(), userType, false,
					auth_key, detDeptId);
		}
		else {
		user = new User(Integer.parseInt(studentSignUpModel.getUserNum()), studentSignUpModel.getName(),null,
				studentSignUpModel.getEmail(), studentSignUpModel.getPassword(), LocalDateTime.now(), userType, false,
				auth_key, null);
		System.out.println(user.getGrade());
		}
		SendEmail es = new SendEmail();
		es.sendEmail(studentSignUpModel.getEmail(), auth_key);
		userRepository.save(user);
	}

	@Transactional
	public void emailCheckChange(String authKey) {
		User user = userRepository.findByAuthKey(authKey);
		user.setEmailCheck(true);
		userRepository.save(user);
	}

	// 비밀번호 변경 메일 링크 보내기
	public void sendPwMail(String toEmail) {
		User user = userRepository.findByEmail(toEmail);
		SendEmail es = new SendEmail();
		es.sendPwChange(toEmail, user.getAuthKey());
	}

	// 비밀번호 바꾸기
	@Transactional
	public void changePw(FindPassModel findPassModel, String authKey) {
		System.out.println("auth: " + authKey);
		System.out.println("password: " + findPassModel.getPassword());
		User user = userRepository.findByAuthKey(authKey);
		user.setPassword(findPassModel.getPassword());
		userRepository.save(user);
	}

	public User findById(int userId) {
		return userRepository.findById(userId).get();
	}

	// 프로필 수정후 저장
	@Transactional
	public void update(int userId, ProfileModel profileModel) {
		User user = findById(userId);
		user.setIntro(profileModel.getIntro());
		user.setGrade(profileModel.getGrade());
		user.setGithub(profileModel.getGithub());
		user.setLanguage(profileModel.getLanguage());
		user.setDetDepartment(detailRepository.findById(profileModel.getDepartment()).get());

		userRepository.save(user);
	}
	
	public List<ProjectJoin> inviteList(int userId) {
		return projectJoinRepository.findByUser_UserIdAndTypeAndState(userId, 1, 0);
	}

	// 0427 윤영 추가
	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User findByUserNum(int userNum) {
		return userRepository.findByUserNum(userNum);
	}
	
	// 교수 프로필 수정후 저장
	@Transactional
	public void updateProfessor(int userId, ProfileModel profileModel) {
		User user = findById(userId);
		// user.setIntro(profileModel.getIntro());
		user.setGithub(profileModel.getGithub());
		userRepository.save(user);
	}
	
	public List<ProjectJoin> applicationList(int userId) {
		return projectJoinRepository.findByUser_UserIdAndTypeAndStateNot(userId, 2, 1);
	}

	public List<Timeline> timeline(int userId) {
		userRepository.updateUserTimelineTime(userId, LocalDateTime.now());
		return timelineRepository.findByUser_UserIdOrderByTimeDesc(userId);
	}
}
