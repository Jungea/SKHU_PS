package net.skhu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.domain.Post;
import net.skhu.domain.Subject;
import net.skhu.model.MyProjectListModel;

public interface PostRepository extends JpaRepository<Post, Integer>{

	List<Post> findBySubject_subjectId(int subjectId);

	List<Post> findByProject_projectId(int projectId);

	List<Post> findByDetail_detId(int i);

}
