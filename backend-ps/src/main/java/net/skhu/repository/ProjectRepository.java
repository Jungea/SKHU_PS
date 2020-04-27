package net.skhu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.skhu.domain.Project;
import net.skhu.domain.ProjectJoin;
import net.skhu.model.SidebarModel;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

	
	// sidebar
	@Query("SELECT new net.skhu.model.SidebarModel(p.projectId, p.projectName) FROM Project p JOIN p.projectJoins pj WHERE pj.user.userId = ?1 AND pj.state = 1 AND pj.pin = 1")
	List<SidebarModel> findProjectIdAndProjectNameByUserId(int userId);

}
