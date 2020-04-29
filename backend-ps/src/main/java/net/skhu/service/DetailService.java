package net.skhu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.skhu.domain.Detail;
import net.skhu.repository.DetailRepository;

@Service
public class DetailService {
	@Autowired
	DetailRepository detailRepository;
	
	public List<Detail> getDepartments() {
		return detailRepository.findByCommon_CmnName("학과");
	}
}
