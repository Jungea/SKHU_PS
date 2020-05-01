package net.skhu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.domain.ProjectJoin;

public interface ProjectJoinRepository extends JpaRepository<ProjectJoin, Integer> {
	List<ProjectJoin> findAllByUser_UserId(int userId);
	ProjectJoin findByProject_ProjectIdAndUser_UserId(int projectId, int userId);
	// 해당 프로젝트의 팀장을 제외한 팀원들
	List<ProjectJoin> findByProject_ProjectIdAndTypeNotAndState(int projectId, int type, int state);
	
	//프로젝트에서 유저 초대를 한번 더
	ProjectJoin findByProject_ProjectIdAndUser_UserNumAndStateNot(int projectId, int userNum, int state);
	
	//프로젝트에서 유저를 초대한 현황
	List<ProjectJoin> findByProject_ProjectIdAndTypeAndStateNot(int projectId, int type, int state);
	
	//유저가 초대받은 현황
	List<ProjectJoin> findByUser_UserIdAndTypeAndState(int userId, int type, int state);
	
	void deleteByUser_UserIdAndProject_ProjectId(int userId, int projectId);

}
