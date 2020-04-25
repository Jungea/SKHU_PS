package net.skhu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.skhu.domain.User;
import net.skhu.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	public User login(int userNum, String password) {
		return userRepository.findByUserNumAndPasswordAndEmailCheckTrue(userNum, password);
	}
	
	public User findById(int userId) {
		return userRepository.findById(userId).get();
	}

}
