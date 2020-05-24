package net.skhu.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.skhu.domain.Project;
import net.skhu.domain.ProjectJoin;
import net.skhu.domain.ProjectStar;
import net.skhu.domain.Subject;
import net.skhu.domain.Todo;
import net.skhu.domain.User;
import net.skhu.domain.Weekly;
import net.skhu.model.EditProjectModel;
import net.skhu.model.MakeProjectModel;
import net.skhu.model.MyProjectListModel;
import net.skhu.model.ProjectBoardModel;
import net.skhu.model.SidebarModel;
import net.skhu.model.TodoModel;
import net.skhu.model.WeekGoalModel;
import net.skhu.repository.ProjectJoinRepository;
import net.skhu.repository.ProjectRepository;
import net.skhu.repository.ProjectStarRepository;
import net.skhu.repository.SubjectRepository;
import net.skhu.repository.TodoRepository;
import net.skhu.repository.UserRepository;
import net.skhu.repository.WeeklyRepository;

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
	@Autowired
	WeeklyRepository weeklyRepository;
	@Autowired
	ProjectStarRepository projectStarRepository;
	@Autowired
	TodoRepository todoRepository;

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
		Project project = new Project(makeProjectModel.getProjectName(), makeProjectModel.getTheme(),
				makeProjectModel.getContent(), makeProjectModel.getTag(), makeProjectModel.getGithub(),
				makeProjectModel.isRcrtState());
		Optional<User> u = userRepository.findById(1);
		User user = u.get();
		project.setUser(user);
		// 과목 authKey를 입력했다면
		if (makeProjectModel.getAuthKey().length() != 0) {
			Subject subject = subjectRepository.findByAuthKey(makeProjectModel.getAuthKey());
			if (subject == null) { // 과목 authKey가 일치한게 없다면
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

	// 프로젝트 수정후 저장
	public void update(int projectId, EditProjectModel editProjectModel) {
		Project project = findById(projectId);
		project.setProjectName(editProjectModel.getProjectName());
		project.setTheme(editProjectModel.getTheme());
		project.setContent(editProjectModel.getContent());
		project.setTag(editProjectModel.getTag());
		project.setGithub(editProjectModel.getGithub());
		project.setProgState(editProjectModel.isProgState());
		project.setRcrtState(editProjectModel.isRcrtState());

		projectRepository.save(project);
	}

	// 멤버
	public List<ProjectJoin> member(int projectId) {
		return projectJoinRepository.findByProject_ProjectIdAndTypeNotAndState(projectId, 0, 1);
	}

	// 초대한 후 대기중인 목록
	public List<ProjectJoin> inviteList(int projectId) {
		return projectJoinRepository.findByProject_ProjectIdAndTypeAndStateNot(projectId, 1, 1);
	}

	// 프로젝트 생성
	@Transactional
	public String makeProject(MakeProjectModel makeProjectModel, int userId) {
		Project project = new Project(makeProjectModel.getProjectName(), makeProjectModel.getTheme(),
				makeProjectModel.getContent(), makeProjectModel.getTag(), makeProjectModel.getGithub(),
				makeProjectModel.isRcrtState());
		Optional<User> u = userRepository.findById(userId);
		User user = u.get();
		project.setUser(user);

		ProjectJoin projectJoin = new ProjectJoin(LocalDateTime.now(), user, project);
		// 과목 authKey를 입력했다면
		if (makeProjectModel.getAuthKey().length() != 0) {
			Subject subject = subjectRepository.findByAuthKey(makeProjectModel.getAuthKey());
			if (subject == null) { // 과목 authKey가 일치한게 없다면
				return "authKey를 잘못 입력했습니다";
			} else {
				project.setSubject(subject);
				projectRepository.save(project);
				projectJoinRepository.save(projectJoin);
				return "authKey에 해당하는 subject가 있습니다.success";
			}
		}

		projectRepository.save(project);
		projectJoinRepository.save(projectJoin);
		return "success";

	}

	// 팀장이 유저를 초대함.
	public boolean inviteMember(int projectId, int userNum) {
		User user = userRepository.findByUserNum(userNum);
		ProjectJoin join = projectJoinRepository.findByProject_ProjectIdAndUser_UserId(projectId, user.getUserId());
		if (join != null) {
			if (join.getType() == 2 && join.getState() == 0) // userNum이 프로젝트를 신청 후 대기 중일 때
				return false;
		} else
			join = new ProjectJoin();

		join.setUser(user);
		join.setProject(projectRepository.findById(projectId).get());
		join.setJoinTime(LocalDateTime.now());
		join.setColor("#000000");
		join.setState(0); // 대기
		join.setType(1); // 초대;

		projectJoinRepository.save(join);
		return true;
	}

	public List<MyProjectListModel> projectList(int userId) {
		List<ProjectJoin> projectJoins = projectJoinRepository.findAllByUser_UserId(userId);
		List<MyProjectListModel> myProjectsList = new ArrayList<>();
		for (int i = 0; i < projectJoins.size(); i++) {
			if (projectJoins.get(i).getState() == 1) { // 초대가 승인인 상태일 때
				MyProjectListModel myProject = new MyProjectListModel();
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

	public Project findById(int userId) {
		Optional<Project> p = projectRepository.findById(userId);
		return p.get();

	}

	public Project findByProjectId(int projectId) {
		return projectRepository.findByProjectId(projectId);
	}

	// 0501 멤버수 증가
	public void increaseMember(int projectId) {
		Project project = projectRepository.findByProjectId(projectId);
		project.setMemNum(project.getMemNum() + 1);
		projectRepository.save(project);
	}

	// 0501 멤버수 감소
	public void decreaseMember(int projectId) {
		Project project = projectRepository.findByProjectId(projectId);
		project.setMemNum(project.getMemNum() - 1);
		projectRepository.save(project);
	}

	// 주감 목표 목록
	public List<Weekly> getWeeklyGoals(int projectId) {
		return projectRepository.findById(projectId).get().getWeeklies();
	}

	// 주간 목표 저장
	public void createWeekGoal(WeekGoalModel weekGoalModel) {
		Weekly weekly = new Weekly();
		weekly.setStartTime(weekGoalModel.getStartTime());
		weekly.setDetail(weekGoalModel.getDetail());
		// FIXME 주차수 생각 필요
//		weekly.setWeek(weekGoalModel.getWeek());
		weekly.setProject(projectRepository.findById(weekGoalModel.getProjectId()).get());

		weeklyRepository.save(weekly);
	}

	public List<ProjectBoardModel> projectBoard(int userId) {
		List<Project> project = projectRepository.findAll();
		List<ProjectBoardModel> board = new ArrayList<>();

		for (Project p : project) {
			ProjectBoardModel model = new ProjectBoardModel();
			model.setProject(p);
			ProjectStar ps = projectStarRepository.findByUser_userIdAndProject_ProjectId(userId, p.getProjectId());
			model.setStar(ps != null ? true : false);
			model.setSubjectName(p.getSubject() != null ? p.getSubject().getTitle() : null);
			model.setCreateName(p.getUser().getName());
			List<ProjectJoin> projectJoin = projectJoinRepository.findByProject_ProjectId(p.getProjectId());
			Set<Integer> allGrade = new HashSet<>();
			for (ProjectJoin pj : projectJoin) {
				allGrade.add(pj.getUser().getGrade());
			}
			model.setAllMemGrade(allGrade);
			ProjectJoin pj = projectJoinRepository.findByUser_userIdAndProject_projectId(userId, p.getProjectId());
			if (pj == null)
				model.setState(2); // 프로젝트 신청할수 있는 상태
			else if (pj.getState() == 0)
				model.setState(0); // 승인대기 상태
			else if (pj.getState() == 1)
				model.setState(1); // 참가하고있는 상태
			board.add(model);
		}
		return board;
	}

	// 프로젝트 관리에 유저한테 신청받은 이력(대기중)
	public List<ProjectJoin> applicationList(int projectId) {
		return projectJoinRepository.findByProject_ProjectIdAndTypeAndState(projectId, 2, 0);
	}

	// todoList 목록
	public List<Todo> todoList(int projectId, int week) {
		Weekly weekly = weeklyRepository.findByProject_ProjectIdAndWeek(projectId, week);
		return todoRepository.findByWeekly_WeeklyId(weekly.getWeeklyId());
	}

	// 새로운 todo 추가
	public void createTodo(int userId, TodoModel todoModel) {
		Todo todo = new Todo();
		
		todo.setUser(userRepository.findById(userId).get());
		Weekly weekly = weeklyRepository.findByProject_ProjectIdAndWeek(todoModel.getProjectId(), todoModel.getWeek());
		todo.setWeekly(weekly);
		todo.setDetail(todoModel.getDetail());
		todo.setCreateTime(LocalDateTime.now());
		todo.setProgState(todoModel.getProgState());
		
		todoRepository.save(todo);
	}
}
