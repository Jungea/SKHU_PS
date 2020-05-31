package net.skhu.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.skhu.domain.Comment;
import net.skhu.repository.CommentRepository;
import net.skhu.repository.PostRepository;
import net.skhu.repository.UserRepository;

@Service
public class CommentService {
	@Autowired
	CommentRepository commentRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	PostRepository postRepository;
	
	@Transactional
	public void noticeAddComment(String content,int postId,int userId) {
		Comment c=new Comment();
		c.setContent(content);
		c.setWriteTime(LocalDateTime.now());
		c.setChoice(0);
		c.setPost(postRepository.findById(postId).get());
		c.setUser(userRepository.findById(userId).get());
		commentRepository.save(c);
		
	}
	@Transactional
	public void noticedeleteComment(int commentId) {
		commentRepository.delete(commentRepository.findById(commentId).get());
	}
	@Transactional
	public void noticedeletePost(int postId) {
		postRepository.delete(postRepository.findById(postId).get());
	}
	@Transactional
	public void commentCheck(int commentId) {
		Comment c=commentRepository.findById(commentId).get();
		if(c.getChoice()==0) 
			c.setChoice(1);
		else
			c.setChoice(0);
		commentRepository.save(c);
	}
}
