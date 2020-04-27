package net.skhu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.domain.Project;
import net.skhu.domain.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Integer>{

	Subject findByAuthKey(String authKey);
	
	
}
