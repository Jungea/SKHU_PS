package net.skhu.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.skhu.domain.Post;
import net.skhu.repository.PostRepository;

@Service
public class PostService {
	@Autowired
	PostRepository postRepository;
	
	public List<Post> noticeBoard(int page,int subjectId) {
		List<Post> posts=postRepository.findBySubject_subjectId(subjectId);
		Collections.reverse(posts);
		if(posts.size()<page*6) {
			posts=posts.subList((page-1)*6,posts.size());
		} else {
			posts=posts.subList((page-1)*6,page*6);
		}
		return posts;
		
	}
}
