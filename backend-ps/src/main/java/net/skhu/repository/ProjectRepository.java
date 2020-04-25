package net.skhu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.skhu.domain.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

	// userId 유저의 프로젝트 목록
	@Query("SELECT p FROM Project p JOIN p.projectJoins pj WHERE pj.user.userId = ?1 AND pj.state = 1")
	List<Project> findProjectByUserId(int userId);
}
