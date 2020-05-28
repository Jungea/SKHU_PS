package net.skhu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.domain.Timeline;

public interface TimelineRepository extends JpaRepository<Timeline, Integer>{

	//유저의 타임라인
	List<Timeline> findByUser_UserId(int userId);

}
