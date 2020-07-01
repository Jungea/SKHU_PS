package net.skhu.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.skhu.domain.Comment;
import net.skhu.domain.Contest;
import net.skhu.domain.Detail;
import net.skhu.domain.File;
import net.skhu.domain.Post;
import net.skhu.domain.Project;
import net.skhu.domain.ProjectJoin;
import net.skhu.domain.Score;
import net.skhu.domain.Subject;
import net.skhu.domain.Timeline;
import net.skhu.domain.Todo;
import net.skhu.domain.User;
import net.skhu.domain.Weekly;
import net.skhu.model.ContestProjectModel;
import net.skhu.model.EditProjectModel;
import net.skhu.model.FindPassModel;
import net.skhu.model.ListModel;
import net.skhu.model.MakeProjectModel;
import net.skhu.model.ManagerSettingModel;
import net.skhu.model.ModifyNoticePostModel;
import net.skhu.model.MyPinProjectModel;
import net.skhu.model.MyProjectListModel;
import net.skhu.model.NoticeBoardSubmitModel;
import net.skhu.model.ProfileModel;
import net.skhu.model.ProjectBoardModel;
import net.skhu.model.ProjectScoreModel;
import net.skhu.model.SignUpModel;
import net.skhu.model.TodoModel;
import net.skhu.model.UserLoginModel;
import net.skhu.model.WeekGoalModel;
import net.skhu.model.WriteNoticeModel;
import net.skhu.repository.CommentRepository;
import net.skhu.repository.FileRepository;
import net.skhu.repository.PostLikeRepository;
import net.skhu.repository.PostRepository;
import net.skhu.repository.ProjectJoinRepository;
import net.skhu.repository.ProjectRepository;
import net.skhu.repository.ScoreRepository;
import net.skhu.repository.SubjectRepository;
import net.skhu.repository.UserRepository;
import net.skhu.service.CommentService;
import net.skhu.service.ContestService;
import net.skhu.service.DetailService;
import net.skhu.service.FileService;
import net.skhu.service.PostLikeService;
import net.skhu.service.PostService;
import net.skhu.service.ProjectJoinService;
import net.skhu.service.ProjectService;
import net.skhu.service.ProjectStarService;
import net.skhu.service.ScoreService;
import net.skhu.service.SubjectService;
import net.skhu.service.UserService;

@RestController
@RequestMapping("api")
public class APIController {
	@Autowired
	UserService userService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ProjectService projectService;
	@Autowired
	ProjectJoinRepository projectJoinRepository;
	@Autowired
	ProjectJoinService projectJoinService;
	@Autowired
	DetailService detailService;
	@Autowired
	SubjectRepository subjectRepository;
	@Autowired
	SubjectService subjectService;
	@Autowired
	ProjectRepository projectRepository;
	@Autowired
	ProjectStarService projectStarService;
	@Autowired
	PostService postService;
	@Autowired
	PostRepository postRepository;
	@Autowired
	CommentRepository commentRepository;
	@Autowired
	CommentService commentService;
	@Autowired
	FileService fileService;
	@Autowired
	FileRepository fileRepository;
	@Autowired
	PostLikeService postLikeService;
	@Autowired
	PostLikeRepository postLikeRepository;
	@Autowired
	ScoreRepository scoreRepository;
	@Autowired
	ContestService contestService;
	@Autowired
	ScoreService scoreService;

	public int getLoginUserId(HttpServletRequest request) {
		HttpSession session = request.getSession();
		int userId = (int) session.getAttribute("userId");
		return userId;
	}

	@RequestMapping(value = "studentSignUp", method = RequestMethod.POST)
	public String studentSignUp(@RequestBody SignUpModel studentSignUpModel) {
		System.out.println("student:" + studentSignUpModel);
		// UserNum이 중복인지 확인하는 변수
		User redundantUserNum = userRepository.findByUserNum(Integer.parseInt(studentSignUpModel.getUserNum()));
		// Email이 중복인지 확인하는 변수
		User redundantEmail = userRepository.findByEmail(studentSignUpModel.getEmail());

		if (redundantUserNum != null && redundantEmail == null) { // userNum만 중복일떄
			return "userNum 중복입니다.";
		} else if (redundantUserNum == null && redundantEmail != null) { // email만 중복일떄
			return "email 중복입니다.";
		} else if (redundantUserNum != null && redundantEmail != null) { // userNum과 email 둘다 중복일 떄
			return "userNum과 email 둘 다 중복입니다.";
		} else {
			userService.SignUp(studentSignUpModel, false);
			return "success";
		}
	}

	@RequestMapping(value = "professorSignUp", method = RequestMethod.POST)
	public String professorSignUp(@RequestBody SignUpModel professorSignUpModel) {
		// UserNum이 중복인지 확인하는 변수
		User redundantUserNum = userRepository.findByUserNum(Integer.parseInt(professorSignUpModel.getUserNum()));
		// Email이 중복인지 확인하는 변수
		User redundantEmail = userRepository.findByEmail(professorSignUpModel.getEmail());

		if (redundantUserNum != null && redundantEmail == null) { // userNum만 중복일떄
			return "userNum 중복입니다.";
		} else if (redundantUserNum == null && redundantEmail != null) { // email만 중복일떄
			return "email 중복입니다.";
		} else if (redundantUserNum != null && redundantEmail != null) { // userNum과 email 둘다 중복일 떄
			return "userNum과 email 둘 다 중복입니다.";
		} else {
			userService.SignUp(professorSignUpModel, true);
			return "success";
		}
	}

	// 이메일 인증
	@RequestMapping(value = "authKeyChange/{authKey}", method = RequestMethod.POST)
	public void authKeyChange(@PathVariable("authKey") String authKey) {
		userService.emailCheckChange(authKey);
	}

	// 비밀번호 변경 링크 보내기
	@RequestMapping(value = "findPass", method = RequestMethod.POST)
	public String findPass(@RequestBody FindPassModel findPassModel) {
		User user = userRepository.findByEmail(findPassModel.getEmail());
		if (user == null) // 해당되는 email이 없을때
			return "email이 존재하지 않습니다.";
		else {
			userService.sendPwMail(findPassModel.getEmail());
			return "success";
		}
	}

	@RequestMapping(value = "changePw/{authKey}", method = RequestMethod.POST)
	public void changePw(@RequestBody FindPassModel findPassModel, @PathVariable("authKey") String authKey) {
		userService.changePw(findPassModel, authKey);
	}

	// 로그인
	@RequestMapping(value = "user/login", method = RequestMethod.POST)
	public User login(@RequestBody UserLoginModel userLoginModel, HttpServletRequest request) {
		System.out.println(userLoginModel);
		User u = userService.login(Integer.parseInt(userLoginModel.getUserNum()), userLoginModel.getPassword());
		if (u != null) {
			HttpSession session = request.getSession();
			session.setAttribute("userId", u.getUserId());
		}
		return u;
	}

	@RequestMapping(value = "user/logout", method = RequestMethod.GET)
	public void logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("userId");
	}

	//
	@RequestMapping(value = "user", method = RequestMethod.GET)
	public User user(HttpServletRequest request) {
		return userService.findById(getLoginUserId(request));
	}

	@RequestMapping(value = "user/projects", method = RequestMethod.GET)
	public List<MyProjectListModel> userProjects(HttpServletRequest request) {
		return projectService.projectList(getLoginUserId(request));
	}

	// 마이페이지 프로필이 수정되고 저장되었을 때
	@RequestMapping(value = "user/profile", method = RequestMethod.POST)
	public void profile(@RequestBody ProfileModel profileModel, HttpServletRequest request) {
		System.out.println(profileModel);
		userService.update(getLoginUserId(request), profileModel);
	}

	@RequestMapping(value = "user/inviteList", method = RequestMethod.GET)
	public List<ProjectJoin> userInviteList(HttpServletRequest request) {
		return userService.inviteList(getLoginUserId(request));
	}

	// 0427 윤영
	@RequestMapping(value = "alluser", method = RequestMethod.GET)
	public List<User> allUser() {
		return userService.findAll();
	}

	// 프로젝트 생성
	@RequestMapping(value = "makeProject", method = RequestMethod.POST)
	public String makeProject(@RequestBody MakeProjectModel makeProjectModel, HttpServletRequest request) {
		System.out.println("tag:" + makeProjectModel.getTag());
		return projectService.makeProject(makeProjectModel, getLoginUserId(request));
	}

	// 프로젝트 개요가 수정되고 저장되었을 때
	@RequestMapping(value = "project/{projectId}/edit", method = RequestMethod.POST)
	public void editProject(@RequestBody EditProjectModel editProjectModel,
			@PathVariable("projectId") String projectId, HttpServletRequest request) {
		projectService.update(Integer.parseInt(projectId), editProjectModel, getLoginUserId(request));
	}

	// 핀 바꾸기
	@RequestMapping(value = "changePin", method = RequestMethod.POST)
	public List<MyProjectListModel> changePin(@RequestBody MyProjectListModel myProjectListModel,
			HttpServletRequest request) {
		System.out.println("changePin");
		return projectJoinService.changePin(myProjectListModel, getLoginUserId(request));
	}

	@RequestMapping(value = "pinList", method = RequestMethod.GET)
	public List<MyPinProjectModel> pinList(HttpServletRequest request) {
		System.out.println("pinList");
		return projectJoinService.pinProjectList(getLoginUserId(request));
	}

	// 학과 셀렉션을 위한 메소드
	@RequestMapping(value = "departments", method = RequestMethod.GET)
	public List<Detail> departments() {
		return detailService.getDepartments();
	}

	// 프로젝트에서 멤버 초대(팀장만)
	@RequestMapping(value = "invite/{projectId}/{userNum}", method = RequestMethod.GET)
	public String invite(@PathVariable("projectId") String projectId, @PathVariable("userNum") String userNum,
			HttpServletRequest request) {

		int intUserNum = Integer.parseInt(userNum);
		int intProjectId = Integer.parseInt(projectId);

		User user = userService.findByUserNum(intUserNum);

		//FIXME search selection 사용
		if (user == null)
			return "잘못된 학번을 입력하였습니다.";

		if (user.getUserType() == true)
			return "초대하려는 사람이 교수입니다.";

		if (user.isEmailCheck() == false)
			return "메일 인증이 안된 유저입니다.";

		User capUser = userService.findById(getLoginUserId(request));
		if (intUserNum == capUser.getUserNum())
			return "자기 자신의 번호입니다.";

		// stream으로 바꾸기
		List<ProjectJoin> member = projectService.member(intProjectId);
		for (int i = 0; i < member.size(); i++)
			if (member.get(i).getUser().getUserNum() == intUserNum)
				return "팀원의 번호입니다.";

		boolean result = projectService.inviteMember(intProjectId, intUserNum);
		if (result == true)
			return userNum + "님에게 초대 메세지를 보냈습니다.";
		else
			return "프로젝트에 참여신청한 유저입니다.";
	}

	@RequestMapping(value = "project/{projectId}/member", method = RequestMethod.GET)
	public List<ProjectJoin> projectMember(@PathVariable("projectId") String projectId) {
		return projectService.member(Integer.parseInt(projectId));
	}

	@RequestMapping(value = "project/{projectId}/inviteList", method = RequestMethod.GET)
	public List<ProjectJoin> projectInviteList(@PathVariable("projectId") String projectId) {
		return projectService.inviteList(Integer.parseInt(projectId));
	}

	@RequestMapping(value = "project/{projectId}")
	public Project project(@PathVariable("projectId") String projectId) {
		return projectService.findById(Integer.parseInt(projectId));
	}

	@RequestMapping(value = "/project/projectName/{projectId}")
	public Project projectName2(@PathVariable("projectId") String projectId) {
		System.out.println("projectName2");
		return projectService.findByProjectId(Integer.parseInt(projectId));
	}

	// 0430 추가
	@RequestMapping(value = "/turnjoinstate/{joinId}/{state}", method = RequestMethod.POST)
	public void turnState(@PathVariable("joinId") int joinId, @PathVariable("state") int state, HttpServletRequest request) {
		projectJoinService.turnState(joinId, state, getLoginUserId(request));
	}

//	// 0501 추가
//	@RequestMapping(value = "/increaseMember/{projectId}", method = RequestMethod.POST)
//	public void increaseMember(@PathVariable("projectId") int projectId) {
//		System.out.println("멤버수가증가합니다");
//		projectService.increaseMember(projectId);
//	}

	// 0501 초대 취소
	@RequestMapping(value = "/deletejoin/{joinId}", method = RequestMethod.POST)
	public void deleteWaiting(@PathVariable("joinId") int joinId) {
		System.out.println("기록을 삭제합니다");
		projectJoinService.deleteWaiting(joinId);
	}

	// 0501 멤버삭제
	@RequestMapping(value = "/exile/{memId}/{projectId}", method = RequestMethod.POST)
	public void exileMember(@PathVariable("projectId") int projectId, @PathVariable("memId") int memId) {
		System.out.println("멤버를 추방합니다");
		projectJoinService.exileMember(memId, projectId);
	}

	// 0501 과목정보
	@RequestMapping(value = "/subjects", method = RequestMethod.POST)
	public List<Subject> findAllSubject() {
		return subjectRepository.findAll();
	}

	// @RequestMapping(value = "allProjects", method = RequestMethod.GET)
	// public List<AllProjectsListModel> allProjects(HttpServletRequest request) {
	// return projectService.allProjectsList(getLoginUserId(request));
	// }

	// 교수의 자기 과목 가져오기
	@RequestMapping(value = "/subjects", method = RequestMethod.GET)
	public List<Subject> getSubjects(HttpServletRequest request) {
		HttpSession session = request.getSession();
		int userId = (int) session.getAttribute("userId");
		return subjectRepository.findSubjectByUser_userId(userId);
	}

	// 교수의 과목 생성시 인증 번호 검사
	@RequestMapping(value = "checkAuthKey", method = RequestMethod.POST)
	public boolean checkAuthKey(@RequestBody Subject subject) {

		System.out.println("authKey:" + subject.getAuthKey());
		return subjectService.checkAuthKey(subject);
	}

	// 교수의 과목 만들기
	@RequestMapping(value = "makeSubject", method = RequestMethod.POST)
	public void makeSubject(@RequestBody Subject subject, HttpServletRequest request) {
		HttpSession session = request.getSession();
		int userId = (int) session.getAttribute("userId");
		subjectService.makeSubject(subject, userId);
	}

	// 교수의 과목 핀 여부 바꾸기
	@RequestMapping(value = "changeSubjectPin", method = RequestMethod.POST)
	public void changeSubjectPin(@RequestBody Subject subject) {
		System.out.println("subjectId:" + subject.getSubjectId());
		subjectService.changeSubjectPin(subject);
	}

	@RequestMapping(value = "/subject/subjectName/{subjectId}")
	public Subject subjecttName(@PathVariable("subjectId") String subjectId) {
		System.out.println("subjectName");
		return subjectRepository.findBySubjectId(Integer.parseInt(subjectId));
	}
	// 교수 마이페이지 프로필이 수정되고 저장되었을 때
	@RequestMapping(value = "user/professorProfile", method = RequestMethod.POST)
	public void professorProfile(@RequestBody ProfileModel profileModel, HttpServletRequest request) {
		userService.updateProfessor(getLoginUserId(request), profileModel);
	}

	// 프로젝트의 주간 목표 목록.
	@RequestMapping(value = "/project/{projectId}/weeklyGoal", method = RequestMethod.GET)
	public List<Weekly> getWeeklyGoals(@PathVariable("projectId") String projectId, HttpServletRequest request) {
		return projectService.getWeeklyGoals(Integer.parseInt(projectId));
	}

	// 주간 목표 생성
	@RequestMapping(value = "createGoal", method = RequestMethod.POST)
	public void createWeekGoal(@RequestBody WeekGoalModel weekGoalModel, HttpServletRequest request) {
		System.out.println(weekGoalModel);
		projectService.createWeekGoal(weekGoalModel, getLoginUserId(request));

	}

	// 전체 프로젝트 개수 반환
	@RequestMapping(value = "all/projectsNum", method = RequestMethod.GET)
	public int allProjects(HttpServletRequest request) {
		return projectRepository.findAll().size();
		//return projectService.projectBoard(getLoginUserId(request));
	}
	// project_star 변경
	@RequestMapping(value = "changeStar", method = RequestMethod.POST)
	public void changeStar(@RequestBody Project project,HttpServletRequest request) {
		projectStarService.changeStar(project.getProjectId(), getLoginUserId(request));
	}
	
	// 참가 신청하기
	@RequestMapping(value = "joinProject", method = RequestMethod.POST)
	public boolean joinProject(@RequestBody Project project,HttpServletRequest request) {
		System.out.println("joinProject");
		return projectJoinService.joinProject(project.getProjectId(), getLoginUserId(request));
	}
	
	// 마이페이지 신청한 이력들
	@RequestMapping(value = "user/applicationList", method = RequestMethod.GET)
	public List<ProjectJoin> userApplicationList(HttpServletRequest request) {
		return userService.applicationList(getLoginUserId(request));
		
	}
	
	//프로젝트 관리 페이지에 신청받은 이력들
	@RequestMapping(value = "project/{projectId}/applicationList", method = RequestMethod.GET)
	public List<ProjectJoin> projectApplicationList(@PathVariable("projectId") String projectId) {
		return projectService.applicationList(Integer.parseInt(projectId));
	}
	
	// 해당 페이지에 해당하는 프로젝트 데이터 전달
	@RequestMapping(value = "projectBoard", method = RequestMethod.GET)
	public List<ProjectBoardModel> projectBoard(@RequestParam("page") int page,HttpServletRequest request) {
		System.out.println("page:"+page);
		return projectService.projectBoard(getLoginUserId(request),page);
	}
	//프로젝트 해당 주차의 ToDoList 목록
	@RequestMapping(value = "project/weekly/{weeklyId}/todos", method = RequestMethod.GET)
	public List<Todo> projectWeekTodoList(@PathVariable("weeklyId") String weeklyId) {
		return projectService.todoList(Integer.parseInt(weeklyId));
	}
	
	// todo생성
	@RequestMapping(value = "createTodo", method = RequestMethod.POST)
	public void createTodo(@RequestBody TodoModel todoModel, HttpServletRequest request) {
		projectService.createTodo(getLoginUserId(request), todoModel);

	}
	// 프로젝트 게시판에서 해당 모달 정보 가져오기
	@RequestMapping(value = "projectBoard/modal/{projectId}", method = RequestMethod.GET)
	public ProjectBoardModel modal(@PathVariable("projectId") int projectId,HttpServletRequest request) {
		System.out.println("modal:"+projectId);
		return projectService.getModal(projectId, getLoginUserId(request));
	}
	// 프로젝트 게시판에서 검색 결과 
	@RequestMapping(value = "projectBoard/search", method = RequestMethod.GET)
	public List<ProjectBoardModel> projectBoard(@RequestParam("type") String type,@RequestParam("text") String text,HttpServletRequest request) {
		//System.out.println("page:"+page);
		System.out.println("text:"+text);
		return projectService.search(getLoginUserId(request),type,text);
	}
	// 0501 과목정보
	@RequestMapping(value = "/projectBoard/subjects", method = RequestMethod.GET)
	public List<Subject> allSubject() {
		return subjectRepository.findAll();
	}
	
	// 프로젝트 게시판에서 필터 결과 
	@RequestMapping(value = "projectBoard/filter", method = RequestMethod.GET)
	public List<ProjectBoardModel> projectBoard(@RequestParam("grade") String grade,@RequestParam("year") String year,@RequestParam("subject") String subject,@RequestParam("tag") String tag,HttpServletRequest request) {
		System.out.println(subject.length()==0);
		return projectService.filter(getLoginUserId(request),grade,year,subject,tag);
	}

	// todo 위치 변경
	@RequestMapping(value = "moveTodo/{todoId}/{progState}/{order}", method = RequestMethod.GET)
	public void moveTodo(@PathVariable("todoId") int todoId, @PathVariable("progState") int progState, @PathVariable("order") int order, HttpServletRequest request) {
		projectService.moveTodo(todoId, progState, order, getLoginUserId(request));
	}
	
	// todo 디테일 수정
	@RequestMapping(value = "editTodo", method = RequestMethod.POST)
	public void editTodo(@RequestBody Todo todo, HttpServletRequest request) {
		projectService.editTodo(todo, getLoginUserId(request));
	}
	
	// todo 삭제
	@RequestMapping(value = "deleteTodo/{todoId}", method = RequestMethod.GET)
	public void deleteTodo(@PathVariable("todoId") int todoId, HttpServletRequest request) {
		projectService.deleteTodo(todoId, getLoginUserId(request));
	}
	
	// 주간 목표 수정
	@RequestMapping(value = "editWeekly", method = RequestMethod.POST)
	public void editWeekly(@RequestBody Weekly weekly, HttpServletRequest request) {
		System.out.println(weekly);
		projectService.editWeekly(weekly, getLoginUserId(request));
	}
	
	// 주간 목표 삭제
	@RequestMapping(value = "deleteWeekly/{weeklyId}", method = RequestMethod.GET)
	public void deleteWeekly(@PathVariable("weeklyId") int weeklyId, HttpServletRequest request) {
		projectService.deleteWeekly(weeklyId, getLoginUserId(request));
	}
	// 공지사항 게시판에서 페이징  
	@RequestMapping(value = "noticeBoard", method = RequestMethod.GET)
	public List<Post> noticeBoard(@RequestParam("page") int page,@RequestParam("subjectId") int subjectId) {
//		System.out.println(subject.length()==0);
		return postService.noticeBoard(page,subjectId);
	}
	// 공지사항 게시판에서 전체 개수
	@RequestMapping(value = "noticeListNum", method = RequestMethod.GET)
	public int noticeListNum(@RequestParam("subjectId") int subjectId) {
//		System.out.println(subject.length()==0);
		return postRepository.findBySubject_subjectId(subjectId).size();
	}
	// 해당 게시글 내용
	@RequestMapping(value = "noticeBoard/post/{postId}", method = RequestMethod.GET)
	public Post post(@PathVariable("postId") int postId) {
//		System.out.println(subject.length()==0);
		return postRepository.findById(postId).get();
	}
	// 게시글 쓰기
	@RequestMapping(value = "writeNotice", method = RequestMethod.POST)
	public int  writeNotice(@RequestBody WriteNoticeModel notice, HttpServletRequest request) {
		return postService.writeNotice(notice,getLoginUserId(request));
	}

	// 공지사항 해당 게시풀 댓글
	@RequestMapping(value = "noticeBoard/comment/{postId}", method = RequestMethod.GET)
	public List<Comment> noticeComment(@PathVariable("postId") int postId) {
		List<Comment> lists=commentRepository.findByPost_PostId(postId);
		Collections.reverse(lists);
		return lists;
	}
	// 공지사항 해당 게시풀 댓글 작성
	@RequestMapping(value = "noticeBoard/addComment/{postId}", method = RequestMethod.POST)
	public void noticeAddComment(@PathVariable("postId") int postId, @RequestBody Comment comment,HttpServletRequest request) {
		commentService.noticeAddComment(comment.getContent(),postId,getLoginUserId(request));
	}
	// 공지사항 해당 게시풀 댓글 삭제
	@RequestMapping(value = "noticeBoard/deleteComment/{commentId}", method = RequestMethod.POST)
	public void noticedeleteComment(@PathVariable("commentId") int commentId) {
		commentService.noticedeleteComment(commentId);
	}
	// 게시글 삭제(공지,자유)
	@RequestMapping(value = "board/deletePost", method = RequestMethod.POST)
	public void deletePost(@RequestBody Post post) {
		postService.deletePost(post.getPostId());
	}
	// 게시글 수정(공지,자유)
	@RequestMapping(value = "board/modifyPost", method = RequestMethod.POST)
	public void modifyPost(@RequestBody ModifyNoticePostModel post) {
		postService.modifyPost(post);
	}
	
	//프로젝트 관리-설정 수정
	@RequestMapping(value = "project/{projectId}/projectManager/setting", method = RequestMethod.PUT)
	public void editProjectManagerSetting(@PathVariable("projectId") int projectId, @RequestBody ManagerSettingModel settingModel) {
		projectService.editProjectManagerSetting(projectId, settingModel);

	}
	
	/*밑에부터 파일 업로드*/
	// 공지사항 게시글의 교수가 올린 파일 리스트
	@RequestMapping(value="file1/list/{postId}",method= RequestMethod.GET)
    public List<File> list(@PathVariable("postId") int postId) {
        return fileService.findAll(postId);
    }
	// 공지 생성시 파일 업로드
    @RequestMapping(value="/file1/upload/{postId}", method=RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public void upload(@PathVariable("postId") int postId,@RequestParam("file")  MultipartFile[] multipartFiles) throws IOException {
        System.out.println("fileupload");
        System.out.println("크기:"+multipartFiles.length);
//        System.out.println("model:"+model.getTitle());
    	for(MultipartFile multipartFile : multipartFiles) {
            if (multipartFile.getSize() <= 0) continue;
//            if(fileModel.getProjectId()==null) { // 교수가 올릴때
//            	fileService.save(multipartFile,fileModel.getPostId(),null);
//            } else { // 학생이 올릴때
//            	fileService.save(multipartFile,fileModel.getPostId(),null);
//            }
            fileService.save(multipartFile,postId,null);

        }
    }

    @RequestMapping("file1/delete/{fileIds}")
    public void delete(@PathVariable String fileIds) throws Exception {
//        System.out.println(fileIds);
    	fileService.delete(fileIds);
    }

    @RequestMapping(value="/file1/download/{fileId}", method=RequestMethod.GET)
    public void download(@PathVariable("fileId") int id, HttpServletResponse response) throws Exception {
//        int fileId=Integer.parseInt(id);
        System.out.println("download:"+id);
        File uploadedfile = fileService.getUploadedFile(id);
        if (uploadedfile == null) return;
         String fileName = URLEncoder.encode(uploadedfile.getName(),"UTF-8");
         response.setContentType("application/octet-stream");
         response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ";");
         try (BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream())) {
             output.write(uploadedfile.getData());
         }
    }
    
    //과목의 프로젝트들
 	@RequestMapping(value = "subject/{subjectId}/projects", method = RequestMethod.GET)
 	public List<Project> subjectProjects(@PathVariable("subjectId") int subjectId) {
 		return projectService.subjectProjects(subjectId);
 	}
    
 	//타임라인 목록
 	@RequestMapping(value = "user/{userId}/timeline", method = RequestMethod.GET)
 	public List<Timeline> userTimeline(@PathVariable("userId") int userId) {
 		return userService.timeline(userId);
 	}
 	

 	//프로젝트 참여자 전부
 	@RequestMapping(value = "project/{projectId}/allMember", method = RequestMethod.GET)
	public List<ProjectJoin> projectAllMember(@PathVariable("projectId") String projectId) {
		return projectService.allMember(Integer.parseInt(projectId));
	}
 	
 	//과목의 프로젝트 참가자들
 	// /api/subject/{subjectId}/member?sort=project  프로젝트 중심(팀장먼저) 후 팀원 학번
 	// /api/subject/{subjectId}/member?sort=userNum  학번 중심
 	@RequestMapping(value = "subject/{subjectId}/member", method = RequestMethod.GET)
	public List<ProjectJoin> subjectMember(@PathVariable("subjectId") int subjectId, @RequestParam("sort") String sort) {
		return projectService.subjectMember(subjectId, sort);
 	}
 	// 프로젝트에 참가한 유저인지 확인
 	@RequestMapping(value = "user/checkJoinMember/{projectId}", method = RequestMethod.GET)
 	public boolean checkJoinMember(@PathVariable("projectId") int projectId,HttpServletRequest request) {
 		System.out.println(projectJoinRepository.findByUser_userIdAndProject_projectId(getLoginUserId(request), projectId).getState());
 		return projectJoinRepository.findByUser_userIdAndProject_projectId(getLoginUserId(request), projectId).getState()==1?true:false;
 		
 	}
 	// 프로젝트별 제출한 파일
	@RequestMapping(value="file1/list/{postId}/{projectId}",method= RequestMethod.GET)
    public List<File> list(@PathVariable("postId") int postId,@PathVariable("projectId") int projectId) {
		return fileRepository.findByPost_PostIdAndProject_ProjectId(postId,projectId);
    }
	// 프로젝트가 파일 제출
    @RequestMapping(value="/file1/upload/{postId}/{projectId}", method=RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public void upload(@PathVariable("postId") int postId,@PathVariable("projectId") int projectId,@RequestParam("file")  MultipartFile[] multipartFiles) throws IOException {
        System.out.println("fileupload");
        System.out.println("크기:"+multipartFiles[0].getOriginalFilename());
//        System.out.println("model:"+model.getTitle());
    	for(MultipartFile multipartFile : multipartFiles) {
            if (multipartFile.getSize() <= 0) continue;
//            if(fileModel.getProjectId()==null) { // 교수가 올릴때
//            	fileService.save(multipartFile,fileModel.getPostId(),null);
//            } else { // 학생이 올릴때
//            	fileService.save(multipartFile,fileModel.getPostId(),null);
//            }
            fileService.save(multipartFile,postId, Integer.toString(projectId));

        }
    }
	// 공지사항 게시판에서 제출 여부 리턴 
	@RequestMapping(value = "/noticeBoard/fileSubmitList", method = RequestMethod.GET)
	public List<String> fileSubmitList(@RequestParam("page") int page,@RequestParam("projectId") int projectId,@RequestParam("subjectId") int subjectId) {
//		return fileRepository.findAll();
		return postService.fileSubmitList(page,projectId,subjectId);
	}
	// 공지사항 게시판에서 제출 여부 리턴 
	@RequestMapping(value = "/noticeBoard/submitFiles/{subjectId}/{postId}", method = RequestMethod.GET)
	public List<NoticeBoardSubmitModel> fileSubmitList(@PathVariable("subjectId") int subjectId,@PathVariable("postId") int postId) {
		System.out.println("NoticeBoardSubmitModel");
		List<NoticeBoardSubmitModel> models=new ArrayList<>();
		for(Project p:projectRepository.findBySubject_SubjectId(subjectId)) {
			NoticeBoardSubmitModel item=new NoticeBoardSubmitModel();
			item.setProject(p);
			List<File> files=fileRepository.findByPost_PostIdAndProject_ProjectId(postId, p.getProjectId());
			item.setFiles(files);
			models.add(item);
		}
		return models;
	}

	//유저의 timelineTime
	@RequestMapping(value = "user/timelineTime", method = RequestMethod.GET)
	public LocalDateTime userTimelineTime(HttpServletRequest request) {
		return user(request).getTimelineTime();
	}
	
	//timeline 모달 닫을 때 유저의 timelineTime 변경
	@RequestMapping(value = "user/timelineTime", method = RequestMethod.PUT)
	public void updateTimelineTime(HttpServletRequest request) {
		userService.updateTimelineTime(getLoginUserId(request));
	}
	
	// 자유  게시판에서 페이징  
	@RequestMapping(value = "freeBoard", method = RequestMethod.GET)
	public List<Post> freeBoard(@RequestParam("page") int page,@RequestParam("projectId") int projectId) {
		return postService.freeBoard(page,projectId);
	}
	// 공지사항 게시판에서 전체 개수
	@RequestMapping(value = "freeListNum", method = RequestMethod.GET)
	public int freeListNum(@RequestParam("projectId") int projectId) {
		return postRepository.findByProject_projectId(projectId).size();
	}
	// 자유게시판 게시글 쓰기
	@RequestMapping(value = "writeFree", method = RequestMethod.POST)
	public int  writeFree(@RequestBody WriteNoticeModel notice, HttpServletRequest request) {
		return postService.writeFree(notice,getLoginUserId(request));
	}
	// 댓글 채택
	@RequestMapping(value = "freeBoard/commentCheck/{commentId}", method = RequestMethod.POST)
	public int commentCheck(@PathVariable("commentId") int commentId,@RequestBody Post post) {
		return commentService.commentCheck(commentId,post.getPostId());
	}
	// 게시글 좋아요
	@RequestMapping(value = "freeBoard/postLike/{postId}", method = RequestMethod.POST)
	public void postLike(@PathVariable("postId") int postId,HttpServletRequest request) {
		System.out.println("postLike");
		postLikeService.postLike(postId,getLoginUserId(request));
	}
	// 게시글 좋아요한 상태 리턴
	@RequestMapping(value = "freeBoard/postLike/{postId}", method = RequestMethod.GET)
	public boolean isPostLiked(@PathVariable("postId") int postId,HttpServletRequest request) {
		System.out.println("postLike");
		return postLikeService.isPostLiked(postId,getLoginUserId(request));
	}
	// 게시판에서 댓글 수 리턴
	@RequestMapping(value = "freeBoard/commentNum", method = RequestMethod.GET)
	public List<Integer> commentNum(@RequestParam("page") int page,@RequestParam("projectId") int projectId) {
		return postService.commentNum(page,projectId);
	}
	// 게시판에서 댓글 수 리턴
	@RequestMapping(value = "noticeBoard/commentNum", method = RequestMethod.GET)
	public List<Integer> commentNum2(@RequestParam("page") int page,@RequestParam("subjectId") int subjectId) {
		return postService.commentNum2(page,subjectId);
	}
	// 프로젝트 삭제
	@RequestMapping(value = "project/delete", method = RequestMethod.POST)
	public void projectDelete(@RequestBody Project project) {
		projectService.delete(project.getProjectId());
	}
	// 프로젝트 수정 권한이 있는지(프로젝트의 조장인지)
	@RequestMapping(value = "project/isCap/{projectId}", method = RequestMethod.GET)
	public boolean isCap(@PathVariable("projectId") int projectId,HttpServletRequest request) {
		return projectService.isCap(projectId,getLoginUserId(request));
	}
	
	//0625 임시 추가
	//프로젝트보드 모델 포맷으로 페이지네이션 없는 전체 프로젝트 반환
	@RequestMapping(value = "allProjects", method = RequestMethod.GET)
	public List<ProjectBoardModel> projectBoard2(HttpServletRequest request) {
		return projectService.projectBoard2(getLoginUserId(request));
	}

	// 내가 작성한 게시글
	@RequestMapping(value = "user/posts", method = RequestMethod.GET)
	public List<ListModel> userPosts(HttpServletRequest request) {
		return userService.userPosts(getLoginUserId(request));
	}
	// 내가 작성한 댓글
	@RequestMapping(value = "user/comments", method = RequestMethod.GET)
	public List<Comment> userComments(HttpServletRequest request) {
		return userService.userComments(getLoginUserId(request));
	}
	// 내가 좋아요한 프로젝트
	@RequestMapping(value = "user/likeProjects", method = RequestMethod.GET)
	public List<ProjectBoardModel> userLikeProjects(@RequestParam("page") int page, HttpServletRequest request) {
		return userService.userLikeProjects(getLoginUserId(request), page);
	}
	// 내가 좋아요한 게시글
	@RequestMapping(value = "user/likePosts", method = RequestMethod.GET)
	public List<ListModel> userLikePosts(HttpServletRequest request) {
		return userService.userLikePosts(getLoginUserId(request));
	}
	// 게시글 이동
	@RequestMapping(value = "user/post/{postId}/url", method = RequestMethod.GET)
	public String userPostUrl(@PathVariable("postId") int postId, HttpServletRequest request) {
		return postService.userPostUrl(postId, getLoginUserId(request));
	}
	// 커뮤니티  게시판에서 페이징  
	@RequestMapping(value = "community", method = RequestMethod.GET)
	public List<Post> community(@RequestParam("page") int page) {
		return postService.community(page);
	}
	// 커뮤니티 게시판에서 전체 개수
	@RequestMapping(value = "communityListNum", method = RequestMethod.GET)
	public int communityListNum() {
		return postRepository.findByDetail_detId(15).size();
	}
	// 커뮤니티게시판 게시글 쓰기
	@RequestMapping(value = "writeCommunity", method = RequestMethod.POST)
	public int  writeCommunity(@RequestBody WriteNoticeModel notice, HttpServletRequest request) {
		return postService.writeCommunity(notice,getLoginUserId(request));
	}
	// 커뮤니티 게시판에서 댓글 수 리턴
	@RequestMapping(value = "community/commentNum", method = RequestMethod.GET)
	public List<Integer> commentNum(@RequestParam("page") int page) {
		return postService.communityCommentNum(page);
	}
	// 커뮤니티 게시판에서 좋아요 수 리턴
	@RequestMapping(value = "community/likeNum", method = RequestMethod.GET)
	public List<Integer> likeNum(@RequestParam("page") int page) {
		return postService.communitylikeNum(page);
	}
	// 좋아요 수 리턴
	@RequestMapping(value = "community/content/likeNum", method = RequestMethod.GET)
	public int contentLikeNum(@RequestParam("postId") int postId) {
		return postLikeRepository.findByPost_PostId(postId).size();
	}
	// 커뮤니티  게시판 검색 페이징  
	@RequestMapping(value = "communitySearch", method = RequestMethod.GET)
	public List<Post> communitySearch(@RequestParam("page") int page,@RequestParam("type") int type,@RequestParam("text") String text) {
		return postService.communitySearch(page,type,text);
	}
	// 커뮤니티 게시판에서 검색 전체 개수
	@RequestMapping(value = "communitySearchListNum", method = RequestMethod.GET)
	public int communitySearchListNum(@RequestParam("type") int type,@RequestParam("text") String text) {
		return postService.communitySearchListNum(type,text);
	}
	// 커뮤니티 게시판에서 검색 댓글 수 리턴
	@RequestMapping(value = "communitySearch/commentNum", method = RequestMethod.GET)
	public List<Integer> communitySearchCommentNum(@RequestParam("page") int page,@RequestParam("type") int type,@RequestParam("text") String text) {
		return postService.communitySearchCommentNum(page,type,text);
	}
	// 커뮤니티 게시판에서 검색 좋아요 수 리턴
	@RequestMapping(value = "communitySearch/likeNum", method = RequestMethod.GET)
	public List<Integer> communitySearchLikeNum(@RequestParam("page") int page,@RequestParam("type") int type,@RequestParam("text") String text) {
		return postService.communitySearchLikeNum(page,type,text);
	}
	// 자유 게시판에서 좋아요 수 리턴
	@RequestMapping(value = "freeBoard/likeNum", method = RequestMethod.GET)
	public List<Integer> freeBoardLikeNum(@RequestParam("page") int page,@RequestParam("projectId") int projectId) {
		return postService.freeBoardLikeNum(page,projectId);
	}
	// 프로젝트별 점수 지정
//	@RequestMapping(value = "project/score", method = RequestMethod.POST)
//	public void  setProjectScore(@RequestBody ProjectScoreModel projectScoreModel) {
//		scoreRepository.setProjectScore(projectScoreModel);
//	}
	
	// 경진대회 이름들 리턴
	@RequestMapping(value = "contestNames", method = RequestMethod.GET)
	public List<Detail> contestNames() {
		return contestService.contestNames();
	}
	// 경진대회 연도들 리턴
	@RequestMapping(value = "contestYears", method = RequestMethod.GET)
	public Object[] contestYears() {
		return contestService.contestYears();
	}
	// 경진대회 수상 프로젝트들 리턴
	@RequestMapping(value = "contestProjects", method = RequestMethod.GET)
	public List<ContestProjectModel> contestProjects(HttpServletRequest request) {
		return contestService.contestProjects(getLoginUserId(request));
	}
	// 경진대회 수상 프로젝트 자세히
	@RequestMapping(value = "contest/modal/{contestId}", method = RequestMethod.GET)
	public Contest contestModal(@PathVariable("contestId") int contestId) {
		System.out.println("modal:"+contestId);
		return contestService.getModal(contestId);
	}
	@RequestMapping(value = "project/score", method = RequestMethod.POST)
	public void  setProjectScore(@RequestBody ProjectScoreModel projectScoreModel) {
		scoreService.setProjectScore(projectScoreModel);
	}
	// 프로젝트별 점수 가져오기
	@RequestMapping(value = "project/getScore", method = RequestMethod.GET)
	public List<String> getProjectScore(@RequestParam("postId") String postId) {
		System.out.println("postId");
		List<Score> scoreList=scoreRepository.findByPost_postId(Integer.parseInt(postId));		
		if(scoreList!=null) {
			List<String> r=new ArrayList<>();
			for(int i=0;i<scoreList.size();i++) {
				r.add(scoreList.get(i).getScore());
			}
			return r;
		}
		return new ArrayList<>();
	}
	// 내 점수 목록 가져오기
	@RequestMapping(value = "project/myScore", method = RequestMethod.GET)
	public List<Score> myScore(HttpServletRequest request) {
		return scoreRepository.findByUser_userId(getLoginUserId(request));
	}
	// 내 점수 하나 가져오기
	@RequestMapping(value = "project/getMyScore", method = RequestMethod.GET)
	public String getMyScore(@RequestParam("postId") String postId,HttpServletRequest request) {
		return scoreRepository.findByUser_userIdAndPost_postId(getLoginUserId(request),Integer.parseInt(postId)).getScore();
	}
	// 공지사항별 모든 점수 가져오기
	@RequestMapping(value = "subject/allScore/{userId}", method = RequestMethod.GET)
	public List<Score> allScore(@PathVariable("userId") int userId) {
		return scoreRepository.findByUser_userId(userId);
	}
	// 공지사항별 모든 점수 가져오기
	@RequestMapping(value = "subject/{subjectId}/setScorePost", method = RequestMethod.GET)
	public List<Post> setScorePost(@PathVariable("subjectId") int subjectId) {
		return postRepository.findBySubject_subjectId(subjectId);
	}
	
}
