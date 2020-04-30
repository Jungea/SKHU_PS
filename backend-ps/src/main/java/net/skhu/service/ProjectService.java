package net.skhu.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.skhu.domain.Project;
import net.skhu.domain.ProjectJoin;
import net.skhu.domain.Subject;
import net.skhu.domain.User;
import net.skhu.model.EditProjectModel;
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
            makeProjectModel.getContent(), makeProjectModel.getTag(), makeProjectModel.isRcrtState());
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
         System.out.println("업데이트작동미ㅑㅓㄷ리ㅑ너리ㅑㅓ디ㅑ러니런디ㅏ러ㅏㅣㄴ더라ㅣㄴ더라ㅣㅓㅏㅣㄷㄴ러ㅏㅣㄴ더라ㅣ더ㅣ");
         
         Project project = findById(projectId);
         project.setProjectName(editProjectModel.getProjectName());
         project.setMemNum(editProjectModel.getMemNum());
         project.setTheme(editProjectModel.getTheme());
         project.setContent(editProjectModel.getContent());
         project.setTag(editProjectModel.getTag());
         project.setGithub(editProjectModel.getGithub());
         project.setRcrtState(editProjectModel.isRcrtState());
       
         System.out.println(project);
         projectRepository.save(project);
      }
      

   //멤버
   public List<ProjectJoin> member(int projectId) {
	   return projectJoinRepository.findByProject_ProjectIdAndTypeNotAndState(projectId, 0, 1);
   }
   
   //초대한 후 대기중인 목록
   public List<ProjectJoin> inviteList(int projectId) {
	   return projectJoinRepository.findByProject_ProjectIdAndTypeAndStateNot(projectId, 1, 1);
   }

	// 프로젝트 생성
	@Transactional
	public String makeProject(MakeProjectModel makeProjectModel,int userId) {
		Project project = new Project(makeProjectModel.getProjectName(), makeProjectModel.getTheme(),
				makeProjectModel.getContent(), makeProjectModel.getTag(), makeProjectModel.isRcrtState());
		Optional<User> u = userRepository.findById(userId);
		User user = u.get();
		project.setUser(user);
		
		ProjectJoin projectJoin=new ProjectJoin(LocalDateTime.now(),user,project);
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
	   public boolean inviteMember(int projectId, int userNum) {
		      ProjectJoin join = projectJoinRepository.findByProject_ProjectIdAndUser_UserNumAndStateNot(projectId, userNum,
		            1);
		      if (join != null) { // 이미 초대나 신청 행위를 함.
		         if (join.getType() == 2 && join.getState() == 0) // userNum이 프로젝트를 신청 후 대기 중일 때
		            return false;
		      } else
		         join = new ProjectJoin();

		      join.setUser(userRepository.findByUserNum(userNum));
		      join.setProject(projectRepository.findById(projectId).get());
		      join.setJoinTime(LocalDateTime.now());
		      join.setColor("#000000");
		      join.setState(0); // 대기
		      join.setType(1); // 초대;

		      projectJoinRepository.save(join);
		      return true;
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

//	public List<AllProjectsListModel> allProjectsList(int userId) {
//		List<Project> allProjects=projectRepository.findAll(); // 모든 프로젝트
//		List<ProjectJoin> myProjects=projectJoinRepository.findAllByUser_UserId(userId); // 내가 참여하고있는 프로젝트
//	
//		List<AllProjectsListModel> allProjectsListModel = new ArrayList<>();
//		
//		for(int i=0;i<allProjects.size();i++) {
//			if(myProjects.get(i).getState()==1) { // 초대가 승인인 상태일 때
//				AllProjectsListModel myProject=new AllProjectsListModel();
//				
//				myProject.setProjectId(myProjects.get(i).getProject().getProjectId());
//				myProject.setProjectName(myProjects.get(i).getProject().getProjectName());
//				myProject.setPin(myProjects.get(i).isPin());
//				allProjectsListModel.add(myProject);
//			}
//		}
//		return myProjectsList;
//	}

	public Project findById(int userId) {
		Optional<Project> p=projectRepository.findById(userId);
		return p.get();
		
	}
	public Project findByProjectId(int projectId) {
		return projectRepository.findByProjectId(projectId);
	}

}
