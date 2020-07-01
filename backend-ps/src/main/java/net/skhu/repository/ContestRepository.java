package net.skhu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.skhu.domain.Contest;

public interface ContestRepository extends JpaRepository<Contest, Integer> {

	@Query("SELECT distinct c.year FROM Contest c ORDER BY c.year DESC")
	Object[] findDistictYearDESC();
	List<Contest> findAllByOrderByYearDesc();

}