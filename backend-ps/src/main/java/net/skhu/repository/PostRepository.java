package net.skhu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.skhu.domain.Post;
import net.skhu.model.ListModel;
import net.skhu.domain.Subject;
import net.skhu.model.MyProjectListModel;


public interface PostRepository extends JpaRepository<Post, Integer>{

	List<Post> findBySubject_subjectId(int subjectId);

	List<Post> findByProject_projectId(int projectId);
	
	//내가 작성한 게시글들
	@Query("SELECT new net.skhu.model.ListModel(p.postId, p.detail.detId, p.title, p.writeTime, p.comments.size) FROM Post p WHERE p.user.userId = ?1 ORDER BY p.writeTime DESC")
	List<ListModel> findPostAndCommentByUserId(int userId);

	List<Post> findByDetail_detId(int i);

}
