package net.skhu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.domain.File;

public interface FileRepository extends JpaRepository<File, Integer> {

	List<File> findByPost_PostId(int postId);

	List<File> findByPost_PostIdAndProject_ProjectId(int postId, int projectId);

}
