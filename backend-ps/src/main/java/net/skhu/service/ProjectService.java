package net.skhu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.skhu.domain.Project;
import net.skhu.repository.ProjectRepository;

@Service
public class ProjectService {

	@Autowired
	ProjectRepository projectRepository;

	// userId 유저의 프로젝트 목록
	public List<Project> findProjectByUserId(int userId) {
		return projectRepository.findProjectByUserId(userId);
	}

}
