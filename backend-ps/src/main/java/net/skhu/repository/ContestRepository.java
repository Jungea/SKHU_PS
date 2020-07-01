package net.skhu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.skhu.domain.Contest;

public interface ContestRepository extends JpaRepository<Contest, Integer> {

	@Query("SELECT distinct c.year FROM Contest c")
	Object[] findDistictYear();

}