package net.skhu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.skhu.domain.Project;
import net.skhu.domain.ProjectStar;
import net.skhu.domain.User;
import net.skhu.repository.ProjectRepository;
import net.skhu.repository.ProjectStarRepository;
import net.skhu.repository.UserRepository;

@Service
public class ProjectStarService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	ProjectRepository projectRepository;
	@Autowired
	ProjectStarRepository projectStarRepository;
	
	@Transactional
	public void changeStar(int projectId, int userId) {
		User user=userRepository.findById(userId).get();
		Project project=projectRepository.findById(projectId).get();
		ProjectStar ps=projectStarRepository.findByUser_userIdAndProject_ProjectId(user.getUserId(),project.getProjectId());
		if(ps!=null) { // 이미 존재한다면
			projectStarRepository.delete(ps);
		} else {
			ProjectStar projectStar=new ProjectStar();
			projectStar.setProject(project);
			projectStar.setUser(user);
			projectStarRepository.save(projectStar);
		}
	}
}
