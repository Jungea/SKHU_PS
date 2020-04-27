package net.skhu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.skhu.domain.Project;
import net.skhu.domain.Subject;
import net.skhu.domain.User;
import net.skhu.model.MakeProjectModel;
import net.skhu.model.SidebarModel;
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
	
	// userId 유저의 프로젝트 목록
	public List<Project> findProjectByUserId(int userId) {
		return projectRepository.findProjectByUserId(userId);
	}

	public List<SidebarModel> userSidebar(int userId) {
		return projectRepository.findProjectIdAndProjectNameByUserId(userId);
	}
	
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
	

}
