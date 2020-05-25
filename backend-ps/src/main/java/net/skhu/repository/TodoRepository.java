package net.skhu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.domain.Todo;

public interface TodoRepository extends JpaRepository<Todo, Integer> {
	List<Todo> findByWeekly_WeeklyId(int weeklyId);
}
