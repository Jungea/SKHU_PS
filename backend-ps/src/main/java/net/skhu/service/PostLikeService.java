package net.skhu.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.skhu.domain.PostLike;
import net.skhu.repository.PostLikeRepository;
import net.skhu.repository.PostRepository;
import net.skhu.repository.UserRepository;

@Service
public class PostLikeService {
	@Autowired 
	PostLikeRepository postLikeRepository;
	@Autowired 
	PostRepository postRepository;
	@Autowired 
	UserRepository userRepository;
	
	@Transactional
	public void postLike(int postId,int userId) {
		PostLike p=postLikeRepository.findByPost_PostIdAndUser_UserId(postId,userId);
		if(p==null) { // like가 존재하지않는다면
			PostLike pl=new PostLike();
			System.out.println(postId+" "+userId);
			pl.setPost(postRepository.findById(postId).get());
			pl.setUser(userRepository.findById(userId).get());
			postLikeRepository.save(pl);
		} else { // like가 존재한다면
			postLikeRepository.delete(p);
		}
	}
}
