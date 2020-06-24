package net.skhu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

	List<Comment> findByPost_PostId(int postId);
	
	// 내가 쓴 댓글들
	List<Comment> findByUser_UserIdOrderByWriteTimeDesc(int userId);

}
