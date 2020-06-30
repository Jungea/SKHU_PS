package net.skhu.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.skhu.domain.Score;
import net.skhu.model.ProjectScoreModel;
import net.skhu.repository.ScoreRepository;

@Service
public class ScoreService {
	@Autowired
	ScoreRepository scoreRepository;
	
	@Transactional
	public void setProjectScore(ProjectScoreModel projectScoreModel) {
		Score score=new Score();
		
	}
}
