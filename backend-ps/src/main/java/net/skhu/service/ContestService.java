package net.skhu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.skhu.domain.Detail;
import net.skhu.repository.ContestRepository;
import net.skhu.repository.DetailRepository;

@Service
public class ContestService {
	@Autowired
	ContestRepository contestRepository;
	@Autowired
	DetailRepository detailRepository;
	
	public List<Detail> contestNames() {
		return detailRepository.findByCommon_CmnName("경진대회");
	}
	
	public Object[] contestYears() {
		return contestRepository.findDistictYear();
	}
	
}
