package net.skhu.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.skhu.domain.Score;
import net.skhu.model.ProjectScoreModel;
import net.skhu.repository.PostRepository;
import net.skhu.repository.ScoreRepository;

@Service
public class ScoreService {
	@Autowired
	ScoreRepository scoreRepository;
	@Autowired
	PostRepository postRepository;
	
	@Transactional
	public void setProjectScore(ProjectScoreModel projectScoreModel) {
		for(int i=0;i<projectScoreModel.getMemberList().size();i++) {
			Score score=scoreRepository.findByUser_userIdAndPost_postId(
					projectScoreModel.getMemberList().get(i).getUser().getUserId(),
					projectScoreModel.getPostId());
			System.out.println(postRepository.findById(projectScoreModel.getPostId()).get().toString());
			if(score==null) {
				score=new Score();
				score.setPost(postRepository.findById(projectScoreModel.getPostId()).get());
				score.setUser(projectScoreModel.getMemberList().get(i).getUser());
			}
			String s=projectScoreModel.getScoreList().get(i);
			System.out.println(projectScoreModel.getMemberList().toString());
			if(s==null|| s.length()==0) {
				score.setScore("0");
			} else {
				score.setScore(s);
			}
			scoreRepository.save(score);
		}
		
	}
}
