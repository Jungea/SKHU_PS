package net.skhu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.skhu.domain.Project;
import net.skhu.domain.ProjectJoin;
import net.skhu.domain.Subject;
import net.skhu.domain.User;
import net.skhu.model.MakeProjectModel;
import net.skhu.model.MyProjectListModel;
import net.skhu.model.SidebarModel;
import net.skhu.repository.ProjectJoinRepository;
import net.skhu.repository.ProjectRepository;
import net.skhu.repository.SubjectRepository;
import net.skhu.repository.UserRepository;

@Service
public class ProjectService {

	@Autowired
	ProjectRepository projectRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	SubjectRepository subjectRepository;
	@Autowired
	ProjectJoinRepository projectJoinRepository;
	
	// 프로젝트 생성
	@Transactional
	public String makeProject(MakeProjectModel makeProjectModel) {
		Project project=new Project(
				makeProjectModel.getProjectName(),
				makeProjectModel.getTheme(),
				makeProjectModel.getContent(),
				makeProjectModel.getTag(),
				makeProjectModel.isRcrtState()
		);
		Optional<User> u = userRepository.findById(1);
		User user=u.get();
		project.setUser(user);
		// 과목 authKey를 입력했다면
		if(makeProjectModel.getAuthKey().length()!=0) {
			Subject subject=subjectRepository.findByAuthKey(makeProjectModel.getAuthKey());
			if(subject==null) { // 과목 authKey가 일치한게 없다면
				return "authKey를 잘못 입력했습니다";
			} else {
				project.setSubject(subject);
				projectRepository.save(project);
				return "authKey에 해당하는 subject가 있습니다.success";
			}
		}
		projectRepository.save(project);
		return "success";
		
	}
	public List<MyProjectListModel> projectList(int userId) {
		List<ProjectJoin> projectJoins=projectJoinRepository.findAllByUser_UserId(userId);
		List<MyProjectListModel> myProjectsList = new ArrayList<>();
		for(int i=0;i<projectJoins.size();i++) {
			if(projectJoins.get(i).getState()==1) { // 초대가 승인인 상태일 때
				MyProjectListModel myProject=new MyProjectListModel();
				myProject.setProjectId(projectJoins.get(i).getProject().getProjectId());
				myProject.setProjectName(projectJoins.get(i).getProject().getProjectName());
				myProject.setMemNum(projectJoins.get(i).getProject().getMemNum());
				myProject.setProgState(projectJoins.get(i).getProject().isProgState());
				myProject.setPin(projectJoins.get(i).isPin());
				myProjectsList.add(myProject);
			}
		}
		return myProjectsList;
	}
	

}
