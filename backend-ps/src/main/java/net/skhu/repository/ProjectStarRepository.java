package net.skhu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.domain.ProjectStar;

public interface ProjectStarRepository extends JpaRepository<ProjectStar, Integer> {
	ProjectStar findByUser_userIdAndProject_ProjectId(int userId, int projectId);

}
