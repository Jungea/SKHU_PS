package net.skhu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.domain.Detail;

public interface DetailRepository extends JpaRepository<Detail, Integer> {
	// cmnName이 일치하는 디테일 가져오기
	List<Detail> findByCommon_CmnName(String cmnName);
}
