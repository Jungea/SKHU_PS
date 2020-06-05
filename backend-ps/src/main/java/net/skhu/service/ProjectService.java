package net.skhu.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.skhu.domain.Post;
import net.skhu.domain.Project;
import net.skhu.domain.ProjectJoin;
import net.skhu.domain.ProjectStar;
import net.skhu.domain.Subject;
import net.skhu.domain.Timeline;
import net.skhu.domain.Todo;
import net.skhu.domain.User;
import net.skhu.domain.Weekly;
import net.skhu.model.EditProjectModel;
import net.skhu.model.MakeProjectModel;
import net.skhu.model.ManagerSettingModel;
import net.skhu.model.MyProjectListModel;
import net.skhu.model.ProjectBoardModel;
import net.skhu.model.SidebarModel;
import net.skhu.model.TodoModel;
import net.skhu.model.WeekGoalModel;
import net.skhu.repository.ProjectJoinRepository;
import net.skhu.repository.ProjectRepository;
import net.skhu.repository.ProjectStarRepository;
import net.skhu.repository.SubjectRepository;
import net.skhu.repository.TimelineRepository;
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
	@Autowired
	TimelineRepository timelineRepository;

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
	@Transactional
	public void update(int projectId, EditProjectModel editProjectModel, int loginUserId) {
		Project project = findById(projectId);
		project.setProjectName(editProjectModel.getProjectName());
		project.setTheme(editProjectModel.getTheme());
		project.setContent(editProjectModel.getContent());
		project.setTag(editProjectModel.getTag());
		project.setGithub(editProjectModel.getGithub());
		project.setProgState(editProjectModel.isProgState());
		project.setRcrtState(editProjectModel.isRcrtState());

		projectRepository.save(project);
		
		//TIMELINE 프로젝트 정보 수정 [팀원 전체가 받음]
		User loginUser = userRepository.findById(loginUserId).get();
		for(ProjectJoin join : allMember(projectId)) {
			if(join.getUser().getUserId() != loginUserId) {
				String content = loginUser.getName()+"님이 "+project.getProjectName()+"의 정보를 수정하였습니다.";
				String url = "/project/"+projectId+"/summary";
				
				timelineRepository.save(new Timeline(0, content, LocalDateTime.now(), url, join.getUser()));
			}
		}
		
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
	@Transactional
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
		
		//TIMELINE 유저가 프로젝트에 초대 신청을 받음.
		Project project = projectRepository.findById(projectId).get();
		String content = project.getProjectName()+"("+project.getUser().getName()+")에서 초대 신청을 받았습니다.";
		String url = "/profile";
		timelineRepository.save(new Timeline(0, content, LocalDateTime.now(), url, user));
		
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
	@Transactional
	public void createWeekGoal(WeekGoalModel weekGoalModel, int loginUserId) {
		Weekly weekly = new Weekly();
		weekly.setStartTime(weekGoalModel.getStartTime());
		weekly.setDetail(weekGoalModel.getDetail());
		weekly.setProject(projectRepository.findById(weekGoalModel.getProjectId()).get());

		weeklyRepository.save(weekly);
		
		//TIMELINE 주간목표 생성 [팀원 전체가 받음]
		User loginUser = userRepository.findById(loginUserId).get();
		for(ProjectJoin join : allMember(weekly.getProject().getProjectId())) {
			if(join.getUser().getUserId() != loginUserId) {
				String content = loginUser.getName()+"님이 "+weekly.getProject().getProjectName()+"에 새로운 주차별 목표를 생성하였습니다.";
				String url = "/project/"+weekly.getProject().getProjectId()+"/weekly";
				
				timelineRepository.save(new Timeline(0, content, LocalDateTime.now(), url, join.getUser()));
			}
		}
	}

	public List<ProjectBoardModel> projectBoard(int userId,int page) {
		List<Project> project = projectRepository.findAll();
		List<ProjectBoardModel> board = new ArrayList<>();
		if(project.size()<page*6) {
			project=project.subList((page-1)*6,project.size());
		} else {
			project=project.subList((page-1)*6,page*6);
		}
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
				if(pj.getState()==1) {
					allGrade.add(pj.getUser().getGrade());
				}
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
	public List<Todo> todoList(int weeklyId) {
		return todoRepository.findByWeekly_WeeklyId(weeklyId);
	}

	// 새로운 todo 추가
	@Transactional
	public void createTodo(int userId, TodoModel todoModel) {
		Todo todo = new Todo();

		todo.setUser(userRepository.findById(userId).get());
		todo.setWeekly(weeklyRepository.findById(todoModel.getWeeklyId()).get());
		todo.setDetail(todoModel.getDetail());
		todo.setCreateTime(LocalDateTime.now());
		todo.setProgState(todoModel.getProgState());
		todo.setOrder(todoModel.getOrder());

		todoRepository.save(todo);
		
		//TIMELINE 새로운 todo 추가 [팀원 전체가 받음]
		User loginUser = userRepository.findById(userId).get();
		Project p = todo.getWeekly().getProject();
		for(ProjectJoin join : allMember(p.getProjectId())) {
			if(join.getUser().getUserId() != userId) {
				String content = loginUser.getName()+"님이 "+p.getProjectName()+"에 새로운 todo를 생성하였습니다.";
				String url = "/project/"+p.getProjectId()+"/weekly/"+todo.getWeekly().getWeeklyId();
				
				timelineRepository.save(new Timeline(0, content, LocalDateTime.now(), url, join.getUser()));
			}
		}
	}
	public ProjectBoardModel getModal(int projectId,int userId) {
//		List<Project> project = projectRepository.findAll();
//		List<ProjectBoardModel> board = new ArrayList<>();

		Project p=projectRepository.findById(projectId).get();
		ProjectBoardModel model = new ProjectBoardModel();
		model.setProject(p);
		ProjectStar ps = projectStarRepository.findByUser_userIdAndProject_ProjectId(userId, p.getProjectId());
		model.setStar(ps != null ? true : false);
		model.setSubjectName(p.getSubject() != null ? p.getSubject().getTitle() : null);
		model.setCreateName(p.getUser().getName());
		List<ProjectJoin> projectJoin = projectJoinRepository.findByProject_ProjectId(p.getProjectId());
		Set<Integer> allGrade = new HashSet<>();
		for (ProjectJoin pj : projectJoin) {
			if(pj.getState()==1) {
				allGrade.add(pj.getUser().getGrade());
			}
		}
		model.setAllMemGrade(allGrade);
		ProjectJoin pj = projectJoinRepository.findByUser_userIdAndProject_projectId(userId, p.getProjectId());
		if (pj == null || pj.getState()==2) // 거절 상태이거나 신청 이력이 없을때
			model.setState(2); // 프로젝트 신청할수 있는 상태
		else if (pj.getState() == 0)
			model.setState(0); // 승인대기 상태
		else if (pj.getState() == 1)
			model.setState(1); // 참가하고있는 상태
		return model;
	}
	public List<ProjectBoardModel> search(int userId,String type,String text) {
		List<Project> project = projectRepository.findAll();
		List<ProjectBoardModel> board = new ArrayList<>();
		if(type.equals("0")) { // 프로젝트 이름으로 검색할때
			project=projectRepository.findByProjectNameIgnoreCaseContaining(text);
		} else { // 팀장 이름으로 검색할 때
			User u=userRepository.findByNameContaining(text);
			if(u==null) {
				return null;
			}
			project=projectRepository.findByUser_userId(u.getUserId());
		}
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
				if(pj.getState()==1) {
					allGrade.add(pj.getUser().getGrade());
				}
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

	public List<ProjectBoardModel> filter(int userId,String grade,String year,String subject,String tag) {
		List<Project> project = projectRepository.findAll();
		List<ProjectBoardModel> board = new ArrayList<>();
		List<Project> p1=new ArrayList<Project>();
		List<Project> p2=new ArrayList<Project>();
		List<Project> p3=new ArrayList<Project>();
		List<Project> p4=new ArrayList<Project>();
		List<Project> p5=new ArrayList<Project>();
		
		if(subject.length()!=0) {
			System.out.println("subject.length()!=0");
			for(Project pr:project) {
				if(pr.getSubject()!=null) {
					if(pr.getSubject().getSubjectId()==Integer.parseInt(subject)) {
						System.out.println(pr.getProjectName());
						p1.add(pr);
					}
				} 
			}
			if(p1.size()==0) {
				return null;
			}
		} 
		if(tag.length()!=0) {
			System.out.println("tag.length()!=0");
			if(p1.size()==0) {
				p1=project;
			}
			String[] tags=tag.split(",");
			for(Project pr:p1) {
				if(pr.getTag().length()!=0) {
					String[] dbTags=pr.getTag().split(",");
					int i=0;
					for(String s:tags) {
						if(Arrays.asList(dbTags).contains(s)) {
							i++;
						}
					}
					if(i==tags.length) {
						p2.add(pr);
					}
				} 
				
			}
			if(p2.size()==0) {
				return null;
			}
		}
		if(year.length()!=0) {
			System.out.println("year.length()!=0");
			if(p1.size()!=0 && p2.size()==0) { // p1만 값이 있을때
				p2=p1;
			}
//			else if(p1.size()!=0 && p2.size()==0) { // // p2만 값이 있을때
//				
//			} else if(p1.size()!=0 && p2.size()!=0){ // 둘다 값이 있을때
//				
//			} 
			else if(p1.size()==0 && p2.size()==0){ // 둘다 값이 없을때
				p2=project;
			}
			for(Project pr:p2) {
				if(pr.getStartDate().getYear()==Integer.parseInt(year)) {
					p3.add(pr);
				}
			}
			if(p3.size()==0) {
				return null;
			}
		}
		if(grade.length()!=0) {
			System.out.println("grade.length()!=0");
			if(p1.size()==0 && p2.size()==0 && p3.size()==0) {
				p3=project;
			}
			else if(p1.size()!=0 && p2.size()==0 && p3.size()==0) { // p1만 값이 있을때
				p3=p1;
			} else if(p1.size()==0 && p2.size()!=0 && p3.size()==0) {
				p3=p2;
			} 
//			else if(p1.size()==0 && p2.size()==0 && p3.size()!=0) {
//				
//			}
			else if(p1.size()!=0 && p2.size()!=0 && p3.size()==0) {
				p3=p2;
			} 
//			else if(p1.size()!=0 && p2.size()==0 && p3.size()!=0) {
//				
//			} 
//			else if(p1.size()==0 && p2.size()!=0 && p3.size()!=0) {
//				
//			} 
//			else if(p1.size()!=0 && p2.size()!=0 && p3.size()!=0) {
//				
//			}
			
			
			for(Project pr:p3) {
				List<ProjectJoin> pj=projectJoinRepository.findByProject_ProjectId(pr.getProjectId());
				for(ProjectJoin j:pj) {
					User u=userRepository.findById(j.getUser().getUserId()).get();
					if(u.getGrade()==Integer.parseInt(grade) && j.getState()==1) { // 승인상태만
						System.out.println("id:"+u.getUserId());
						if(!p4.contains(pr)) {
							System.out.println("year add");
							p4.add(pr);
						}
						
					}
				}
				
			}
			if(p4.size()==0) {
				System.out.println("year null");
				return null;
			}
		}
		if(p1.size()!=0) {
			p5=p1;
		} 
		if(p2.size()!=0) {
			p5=p2;
		}
		if(p3.size()!=0) {
			p5=p3;
		}
		if(p4.size()!=0) {
			p5=p4;
		}
		for (Project p : p5) {
			ProjectBoardModel model = new ProjectBoardModel();
			model.setProject(p);
			ProjectStar ps = projectStarRepository.findByUser_userIdAndProject_ProjectId(userId, p.getProjectId());
			model.setStar(ps != null ? true : false);
			model.setSubjectName(p.getSubject() != null ? p.getSubject().getTitle() : null);
			model.setCreateName(p.getUser().getName());
			List<ProjectJoin> projectJoin = projectJoinRepository.findByProject_ProjectId(p.getProjectId());
			Set<Integer> allGrade = new HashSet<>();
			for (ProjectJoin pj : projectJoin) {
				if(pj.getState()==1) {
					allGrade.add(pj.getUser().getGrade());
				}
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

	// todo 순서 변경
	@Transactional
	public void moveTodo(int todoId, int progState, int order, int loginUserId) {
		Todo todo = todoRepository.findById(todoId).get();
		int oldState = todo.getProgState();
		todo.setProgState(progState);
		todo.setOrder(order);
		
		todoRepository.save(todo);
		
		//TIMELINE todo 상태 변경 [팀원 전체가 받음]
		if(oldState != progState) {
			String[] state = {"시작 안함", "진행 중", "완료"};
			User loginUser = userRepository.findById(loginUserId).get();
			Project p = todo.getWeekly().getProject();
			for(ProjectJoin join : allMember(p.getProjectId())) {
				if(join.getUser().getUserId() != loginUserId) {
					String content = loginUser.getName()+"님이 "+p.getProjectName()+"의 todo 상태를 "+state[progState]+"으로 변경하였습니다.";
					String url = "/project/"+p.getProjectId()+"/weekly/"+todo.getWeekly().getWeeklyId();
					
					timelineRepository.save(new Timeline(0, content, LocalDateTime.now(), url, join.getUser()));
				}
			}
		}
	}

	// todo 디테일 수정
	@Transactional
	public void editTodo(Todo editTodo, int loginUserId) {
		Todo originTodo = todoRepository.findById(editTodo.getTodoId()).get();
		originTodo.setDetail(editTodo.getDetail());

		todoRepository.save(originTodo);
		
		//TIMELINE todo 수정 [팀원 전체가 받음]
		User loginUser = userRepository.findById(loginUserId).get();
		Project p = originTodo.getWeekly().getProject();
		for(ProjectJoin join : allMember(p.getProjectId())) {
			if(join.getUser().getUserId() != loginUserId) {
				String content = loginUser.getName()+"님이 "+p.getProjectName()+"의 todo를 수정하였습니다.";
				String url = "/project/"+p.getProjectId()+"/weekly/"+originTodo.getWeekly().getWeeklyId();
				
				timelineRepository.save(new Timeline(0, content, LocalDateTime.now(), url, join.getUser()));
			}
		}
	}

	// todo 삭제
	@Transactional
	public void deleteTodo(int todoId, int loginUserId) {
		//TIMELINE todo 삭제 [팀원 전체가 받음]
		User loginUser = userRepository.findById(loginUserId).get();
		Todo todo = todoRepository.findById(todoId).get();
		Project p = todo.getWeekly().getProject();
		for(ProjectJoin join : allMember(p.getProjectId())) {
			if(join.getUser().getUserId() != loginUserId) {
				String content = loginUser.getName()+"님이 "+p.getProjectName()+"의 todo를 삭제하였습니다.";
				String url = "/project/"+p.getProjectId()+"/weekly/"+todo.getWeekly().getWeeklyId();
				
				timelineRepository.save(new Timeline(0, content, LocalDateTime.now(), url, join.getUser()));
			}
		}
		
		todoRepository.deleteById(todoId);
	}

	// 주간 목표 수정
	@Transactional
	public void editWeekly(Weekly editWeekly, int loginUserId) {
		Weekly originWeekly = weeklyRepository.findById(editWeekly.getWeeklyId()).get();
		originWeekly.setDetail(editWeekly.getDetail());
		originWeekly.setStartTime(editWeekly.getStartTime());
		
		weeklyRepository.save(originWeekly);
		
		//TIMELINE 주차별 목표 수정 [팀원 전체가 받음]
		User loginUser = userRepository.findById(loginUserId).get();
		for(ProjectJoin join : allMember(originWeekly.getProject().getProjectId())) {
			if(join.getUser().getUserId() != loginUserId) {
				String content = loginUser.getName()+"님이 "+originWeekly.getProject().getProjectName()+"의 주차별 목표를 수정하였습니다.";
				String url = "/project/"+originWeekly.getProject().getProjectId()+"/weekly";
				
				timelineRepository.save(new Timeline(0, content, LocalDateTime.now(), url, join.getUser()));
			}
		}
	}

	// 주간 목표 삭제
	@Transactional
	public void deleteWeekly(int weeklyId, int loginUserId) {
		//TIMELINE 주차별 목표 삭제 [팀원 전체가 받음]
		User loginUser = userRepository.findById(loginUserId).get();
		Weekly weekly = weeklyRepository.findById(weeklyId).get();
		for(ProjectJoin join : allMember(weekly.getProject().getProjectId())) {
			if(join.getUser().getUserId() != loginUserId) {
				String content = loginUser.getName()+"님이 "+weekly.getProject().getProjectName()+"의 주차별 목표를 삭제하였습니다.";
				String url = "/project/"+weekly.getProject().getProjectId()+"/weekly";
				
				timelineRepository.save(new Timeline(0, content, LocalDateTime.now(), url, join.getUser()));
			}
		}
		
		weeklyRepository.deleteById(weeklyId);
	}

	public void editProjectManagerSetting(int projectId, ManagerSettingModel settingModel) {
		Project project = projectRepository.findById(projectId).get();
		
		project.setProgState(settingModel.isProgState());
		project.setRcrtState(settingModel.isRcrtState());
		
		project.setSubject(subjectRepository.findByAuthKey(settingModel.getSubjectAuthKey()));
		
		projectRepository.save(project);
	}

	public List<Project> subjectProjects(int subjectId) {
		return projectRepository.findBySubject_SubjectId(subjectId);
	}

	// 프로젝트 참가자들(팀장포함)
	// 정렬 팀장 우선, 팀원 학번 순
	public List<ProjectJoin> allMember(int projectId) {
		return projectJoinRepository.findByProject_ProjectIdAndStateOrderByTypeAscUser_UserIdAsc(projectId, 1);
	}

	// 과목의 프로젝트 참가자들
	public List<ProjectJoin> subjectMember(int subjectId, String sort) {
		System.out.println(subjectId+" "+sort);
		if(sort.equals("userNum"))
			return projectJoinRepository.findByProject_Subject_SubjectIdOrderByUser_userNum(subjectId);
		
		else if(sort.equals("project")) {
			List<Project> projects = subjectProjects(subjectId);
			List<ProjectJoin> members = new ArrayList<>();
			for(Project p : projects)
				members.addAll(allMember(p.getProjectId()));
			return members;
					
		} else
			return null;
	}
	
	
}
