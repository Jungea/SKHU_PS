package net.skhu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.skhu.domain.PostLike;
import net.skhu.model.ListModel;

public interface PostLikeRepository extends JpaRepository<PostLike, Integer> {

	PostLike findByPost_PostIdAndUser_UserId(int postId, int userId);

	// 내가 좋아요한 프로젝트들
	@Query("SELECT new net.skhu.model.ListModel(pl.post.postId, pl.post.detail.detId, pl.user.name, pl.post.title, pl.post.writeTime, pl.post.comments.size) FROM PostLike pl WHERE pl.user.userId = ?1 ORDER BY pl.likeId DESC")
	List<ListModel> findPostsAndCommentByUserId(int userId);
	
}
