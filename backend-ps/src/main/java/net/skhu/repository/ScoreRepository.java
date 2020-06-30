package net.skhu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.domain.Score;

public interface ScoreRepository extends JpaRepository<Score, Integer>{

}
