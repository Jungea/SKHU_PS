package net.skhu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.domain.ProjectJoin;

public interface ProjectJoinRepository extends JpaRepository<ProjectJoin, Integer>{

	List<ProjectJoin> findAllByUser_UserId(int userId);
	ProjectJoin findByProject_ProjectIdAndUser_UserId(int projectId, int userId);
}
