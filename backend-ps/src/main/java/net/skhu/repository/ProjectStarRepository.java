package net.skhu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.skhu.domain.Project;
import net.skhu.domain.ProjectStar;

public interface ProjectStarRepository extends JpaRepository<ProjectStar, Integer> {
	ProjectStar findByUser_userIdAndProject_ProjectId(int userId, int projectId);

	//내가 좋아요한 프로젝트들
	@Query("SELECT ps.project FROM ProjectStar ps WHERE ps.user.userId = ?1")
	List<Project> findProjectsByUserId(int userId);
}
