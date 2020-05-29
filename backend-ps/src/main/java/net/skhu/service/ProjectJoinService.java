package net.skhu.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.skhu.domain.Project;
import net.skhu.domain.ProjectJoin;
import net.skhu.domain.Timeline;
import net.skhu.domain.User;
import net.skhu.model.MyPinProjectModel;
import net.skhu.model.MyProjectListModel;
import net.skhu.repository.ProjectJoinRepository;
import net.skhu.repository.ProjectRepository;
import net.skhu.repository.TimelineRepository;
import net.skhu.repository.UserRepository;

@Service
public class ProjectJoinService {
	@Autowired
	ProjectJoinRepository projectJoinRepository;
	@Autowired
	ProjectService projectService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ProjectRepository projectRepository;
	@Autowired
	TimelineRepository timelineRepository;

	@Transactional
	public List<MyProjectListModel> changePin(MyProjectListModel myProjectListModel, int userId) {
		ProjectJoin projectJoin = projectJoinRepository
				.findByProject_ProjectIdAndUser_UserId(myProjectListModel.getProjectId(), userId);
		projectJoin.setPin(!projectJoin.isPin());
		projectJoinRepository.save(projectJoin);
		return projectService.projectList(userId);
	}

	public List<MyPinProjectModel> pinProjectList(int userId) {
		List<ProjectJoin> projectJoins = projectJoinRepository.findAllByUser_UserId(userId);
		List<MyPinProjectModel> myPinProjectsList = new ArrayList<>();
		for (int i = 0; i < projectJoins.size(); i++) {
			// 초대가 승인인 상태이고 핀이 true일때
			if (projectJoins.get(i).getState() == 1 && projectJoins.get(i).isPin() == true) {
				MyPinProjectModel myPinProject = new MyPinProjectModel();
				myPinProject.setProjectId(projectJoins.get(i).getProject().getProjectId());
				myPinProject.setProjectName(projectJoins.get(i).getProject().getProjectName());
				myPinProjectsList.add(myPinProject);
			}
		}
		return myPinProjectsList;
	}

	// 프로젝트 멤버 초대, 신청에서 수락, 거절을 하면 실행
	@Transactional
	public void turnState(int joinId, int state, int loginUserId) {
		ProjectJoin projectJoin = projectJoinRepository.findById(joinId).get();
		projectJoin.setState(state);
		projectJoinRepository.save(projectJoin);
		
		//TIMELINE
		String[] stateString = {"수락", "거절"};
		String content;
		String url;
		User user;
		//TIMELINE 유저가 초대 신청을 수락, 거절함 [팀장이 받음]
		if(projectJoin.getType() == 1) { //초대 신청
			content = projectJoin.getUser().getName()+"님이 초대 신청을 "+stateString[projectJoin.getState()-1]+"하였습니다.";
			url = "/project/"+projectJoin.getProject().getProjectId()+"/manage";
			user = projectJoin.getProject().getUser();

		//TIMELINE 팀장이 참가 신청을 수락, 거절함 [유저가 받음]
		} else { //참가 신청
			content = projectJoin.getProject().getProjectName()+"에서 참가 신청을 "+stateString[projectJoin.getState()-1]+"하였습니다.";
			url = "/profile";
			user = projectJoin.getUser();
		}
		timelineRepository.save(new Timeline(0, content, LocalDateTime.now(), url, user));
		
		//TIMELINE 수락해서 팀원이 됨.[팀원 전체가 받음]
		if(projectJoin.getState() == 1) {
			for(ProjectJoin join : projectService.allMember(projectJoin.getProject().getProjectId())) {
				if(join.getUser().getUserId() != loginUserId) {
					content = projectJoin.getUser().getName()+"님이 "+projectJoin.getProject().getProjectName()+"에 팀원이 되었습니다.";
					url = "/project/"+projectJoin.getProject().getProjectId()+"/summary";
					user = join.getUser();
					
					timelineRepository.save(new Timeline(0, content, LocalDateTime.now(), url, user));
				}
			}
		}
		

		if (state == 1) { // 수락 시
			// FIXME projectService를 통할 필요 있음?
			projectService.increaseMember(projectJoin.getProject().getProjectId());
		}
	}

	// 0501 초대 기록 대기 삭제
	@Transactional
	public void deleteWaiting(int joinId) {
		ProjectJoin projectJoin = projectJoinRepository.findById(joinId).get();
		
		//TIMELINE
		//유저님이 프로젝트 참가 신청을 취소하였습니다.
		String content;
		String url;
		User user;
		//TIMELINE 프로젝트가 유저 초대 신청을 취소함 [유저가 받음]
		if(projectJoin.getType() == 1) { //초대 신청
			content = projectJoin.getProject().getProjectName()+"에서 초대 신청을 취소하였습니다.";
			url = "/profile";
			user = projectJoin.getUser();

		//TIMELINE 유저가 참가 신청을 취소함 [팀장이 받음]
		} else { //참가 신청
			content = projectJoin.getUser().getName()+"님이 "+projectJoin.getProject().getProjectName()+" 참가 신청을 취소하였습니다.";
			url = "/project/"+projectJoin.getProject().getProjectId()+"/manage";
			user = projectJoin.getProject().getUser();
		}
		timelineRepository.save(new Timeline(0, content, LocalDateTime.now(), url, user));
		
		projectJoinRepository.deleteById(joinId);
	}

	// 0501 멤버 삭제
	@Transactional
	public void exileMember(int memId, int projectId) {
		//TIMELINE 프로젝트에서 추방당함 [유저가 받음]
		Project project = projectRepository.findById(projectId).get();
		User user = userRepository.findById(memId).get();
		String content = project.getProjectName()+"에서 추방되었습니다.";
		String url = "/home";
		timelineRepository.save(new Timeline(0, content, LocalDateTime.now(), url, user));
		
		
		projectJoinRepository.deleteByUser_UserIdAndProject_ProjectId(memId, projectId);
		// FIXME projectService를 통할 필요 있음?
		projectService.decreaseMember(projectId);
	}

	// 유저가 프로젝트 참여 신청 함.
	@Transactional
	public boolean joinProject(int projectId, int userId) {
		ProjectJoin projectJoin = new ProjectJoin();
		projectJoin.setUser(userRepository.findById(userId).get());
		projectJoin.setProject(projectRepository.findById(projectId).get());
		projectJoin.setJoinTime(LocalDateTime.now());
		projectJoin.setColor("#000000");
		projectJoin.setState(0);
		projectJoin.setType(2);

		projectJoinRepository.save(projectJoin);
		
		//TIMELINE 프로젝트에 유저가 참가 신청 함 [팀장 받음].
		String content = projectJoin.getUser().getName()+"님에게 "+projectJoin.getProject().getProjectName()+" 참가 신청을 받았습니다.";
		String url = "/project/"+projectJoin.getProject().getProjectId()+"/manage";
		timelineRepository.save(new Timeline(0, content, LocalDateTime.now(), url, projectJoin.getProject().getUser()));
		
		return true;
	}

}
