package net.skhu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.domain.Score;

public interface ScoreRepository extends JpaRepository<Score, Integer>{

	Score findByUser_userIdAndPost_postId(int userId, int postId);

	List<Score> findByPost_postId(int postId);
	List<Score> findByUser_userId(int userId);

}
