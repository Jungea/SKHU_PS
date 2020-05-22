package net.skhu.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.skhu.domain.ProjectJoin;
import net.skhu.model.MyPinProjectModel;
import net.skhu.model.MyProjectListModel;
import net.skhu.repository.ProjectJoinRepository;
import net.skhu.repository.ProjectRepository;
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
	public void turnState(int joinId, int state) {
		ProjectJoin projectJoin = projectJoinRepository.findById(joinId).get();
		projectJoin.setState(state);
		projectJoinRepository.save(projectJoin);

		if (state == 1) { // 수락 시
			// FIXME projectService를 통할 필요 있음?
			projectService.increaseMember(projectJoin.getProject().getProjectId());
		}
	}

	// 0501 초대 기록 대기 삭제
	public void deleteWaiting(int joinId) {
		projectJoinRepository.deleteById(joinId);
	}

	// 0501 멤버 삭제
	@Transactional
	public void exileMember(int memId, int projectId) {
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
		return true;
	}

}
