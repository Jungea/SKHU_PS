package net.skhu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.domain.ProjectJoin;

public interface ProjectJoinRepository extends JpaRepository<ProjectJoin, Integer> {
	List<ProjectJoin> findAllByUser_UserId(int userId);
	
	//FIXME 중복
	ProjectJoin findByProject_ProjectIdAndUser_UserId(int projectId, int userId);
	
	// 해당 프로젝트의 팀장을 제외한 팀원들
	List<ProjectJoin> findByProject_ProjectIdAndTypeNotAndState(int projectId, int type, int state);

	//프로젝트에서 유저를 초대한 현황
	List<ProjectJoin> findByProject_ProjectIdAndTypeAndStateNot(int projectId, int type, int state);
	
	//유저가 초대받은 현황
	List<ProjectJoin> findByUser_UserIdAndTypeAndState(int userId, int type, int state);
	
	void deleteByUser_UserIdAndProject_ProjectId(int userId, int projectId);
	
	List<ProjectJoin> findByProject_ProjectId(int projectId);
	
	//FIXME 중복
	ProjectJoin findByUser_userIdAndProject_projectId(int userId, int projectId); 
	
	//마이페이지에서 유저가 신청한 목록
	List<ProjectJoin> findByUser_UserIdAndTypeAndStateNot(int userId, int type, int state);

	//프로젝트관리에 유저한테 신청받은 목록
	List<ProjectJoin> findByProject_ProjectIdAndTypeAndState(int projectId, int type, int state);
	
	//프로젝트 참여자 전원(팀장포함) 정렬 팀장부터
	List<ProjectJoin> findByProject_ProjectIdAndStateOrderByTypeAscUser_UserIdAsc(int projectId, int state);
	
	//과목의 프로젝트 참가자들
	List<ProjectJoin> findByProject_Subject_SubjectIdOrderByUser_userNum(int subjectId);

}
