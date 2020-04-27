package net.skhu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.skhu.domain.ProjectJoin;
import net.skhu.model.MyProjectListModel;
import net.skhu.repository.ProjectJoinRepository;

@Service
public class ProjectJoinService {
	@Autowired
	ProjectJoinRepository projectJoinRepository;
	@Autowired
	ProjectService projectService;
	
	@Transactional
	public List<MyProjectListModel> changePin(MyProjectListModel myProjectListModel,int userId) {
		ProjectJoin projectJoin=projectJoinRepository.findByProject_ProjectIdAndUser_UserId(myProjectListModel.getProjectId(),userId);
		projectJoin.setPin(!projectJoin.isPin());
		projectJoinRepository.save(projectJoin);
		return projectService.projectList(userId);
	}
}
