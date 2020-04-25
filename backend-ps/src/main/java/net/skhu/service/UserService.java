package net.skhu.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.skhu.domain.User;
import net.skhu.etc.SendEmail;
import net.skhu.etc.TempAuth_key;
import net.skhu.model.ProfileModel;
import net.skhu.model.StudentSignUpModel;
import net.skhu.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	public User login(int userNum, String password) {
		return userRepository.findByUserNumAndPasswordAndEmailCheckTrue(userNum, password);
	}

	@Transactional
	public void StudentSignUp(StudentSignUpModel studentSignUpModel) {
		String auth_key = new TempAuth_key().getKey(44, false); // 이메일 인증 키 설정
		User user = new User(Integer.parseInt(studentSignUpModel.getUserNum()), studentSignUpModel.getName(),
				studentSignUpModel.getEmail(), studentSignUpModel.getPassword(), LocalDateTime.now(), false, // 학생
				false, auth_key);
		SendEmail es = new SendEmail();
		es.sendEmail(studentSignUpModel.getEmail(), auth_key);
		 userRepository.save(user);
	 }
	 
	 @Transactional
	 public void emailCheckChange(String authKey) {
		 User user=userRepository.findByAuthKey(authKey);
		user.setEmailCheck(true);
		userRepository.save(user);
	 }

	public User findById(int userId) {
		return userRepository.findById(userId).get();
	}

	// 학과 추가해야 한다.
	public void update(int userId, ProfileModel profileModel) {
		User user = findById(userId);
		user.setIntro(profileModel.getIntro());
		user.setGrade(Integer.parseInt(profileModel.getGrade()));
		user.setGithub(profileModel.getGithub());
		user.setLanguage(profileModel.getLanguage());

		userRepository.save(user);
	}

}
