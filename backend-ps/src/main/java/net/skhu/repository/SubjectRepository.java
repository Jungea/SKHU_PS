package net.skhu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.domain.Project;
import net.skhu.domain.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Integer>{

	Subject findByAuthKey(String authKey);
	List<Subject> findSubjectByUser_userId(int userId);
	Subject findBySubjectId(int subjectId);
	
}
