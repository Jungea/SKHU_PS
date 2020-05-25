package net.skhu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.domain.Weekly;

public interface WeeklyRepository extends JpaRepository<Weekly, Integer> {
	Weekly findByProject_ProjectIdAndWeek(int projectId, int week);
}
