package net.skhu.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.skhu.domain.Subject;
import net.skhu.domain.User;
import net.skhu.repository.SubjectRepository;
import net.skhu.repository.UserRepository;

@Service
public class SubjectService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	SubjectRepository subjectRepository;
	
	@Transactional
	public void makeSubject(Subject subject,int userId) {
		Optional<User> u=userRepository.findById(userId);
		User user=u.get();
		subject.setUser(user);
		subjectRepository.save(subject);
	}
	
	public boolean checkAuthKey(Subject subject) {
		Subject s=subjectRepository.findByAuthKey(subject.getAuthKey());
		if(s==null) 
			return true;
		else
			return false;
	}
	@Transactional
	public void changeSubjectPin(Subject subject) {
		Optional<Subject> sub=subjectRepository.findById(subject.getSubjectId());
		Subject s=sub.get();
		s.setPin(!s.isPin());
		subjectRepository.save(s);
	}
	
}
