package net.skhu.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.skhu.domain.Comment;
import net.skhu.domain.Post;
import net.skhu.domain.Project;
import net.skhu.domain.ProjectJoin;
import net.skhu.domain.Timeline;
import net.skhu.repository.CommentRepository;
import net.skhu.repository.PostRepository;
import net.skhu.repository.TimelineRepository;
import net.skhu.repository.UserRepository;

@Service
public class CommentService {
	@Autowired
	CommentRepository commentRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	PostRepository postRepository;
	@Autowired
	ProjectService projectService;
	@Autowired
	TimelineRepository timelineRepository;
	
	
	@Transactional
	public void noticeAddComment(String content,int postId,int userId) {
		Comment c=new Comment();
		c.setContent(content);
		c.setWriteTime(LocalDateTime.now());
		c.setChoice(0);
		c.setPost(postRepository.findById(postId).get());
		c.setUser(userRepository.findById(userId).get());
		commentRepository.save(c);
		
		//TIMELINE 댓글 작성 [작성자에게]
		Post p = c.getPost();
		if(userId != p.getUser().getUserId()) { //댓글 작성자가 게시글 작성자가 아닌 경우
			if(p.getSubject() != null) {  //공지게시판
				String text = p.getSubject().getTitle()+"의 공지 게시글에 새로운 댓글이 추가되었습니다.";
				String url = "/subject/"+p.getSubject().getSubjectId()+"/noticeBoard/"+postId;
				
				timelineRepository.save(new Timeline(0, text, LocalDateTime.now(), url, p.getUser()));
				
			} else if(p.getProject() != null){  //자유게시판
				String text = p.getProject().getProjectName()+"의 자유 게시글에 새로운 댓글이 추가되었습니다.";
				String url = "/project/"+p.getProject().getProjectId()+"/freeBoard/"+postId;
				
				timelineRepository.save(new Timeline(0, text, LocalDateTime.now(), url, p.getUser()));
			}
		}
	}
	@Transactional
	public void noticedeleteComment(int commentId) {
		commentRepository.delete(commentRepository.findById(commentId).get());
	}
	@Transactional
	// FIXME 채택 해제 불가, 채택은 한 개만
	public int commentCheck(int commentId,int postId) {
		Comment c=commentRepository.findById(commentId).get();
		List<Comment> comments=commentRepository.findByPost_PostId(postId);
		if(c.getChoice()==1) { // 현재 채택된 댓글을 다시 선택한 것일때 
			c.setChoice(0);
			commentRepository.save(c);
			return 1; // ok
		}
		int num=0;
		for(Comment comment:comments) {
			if(comment.getChoice()==0) {
				num++;
			}
		}
		if(num==comments.size()) { // 채택된 댓글이 없을때
			c.setChoice(1);
			commentRepository.save(c);
			
			//TIMELINE 댓글 채택 [댓글 작성자에게]
			Post p = c.getPost();
			if(c.getUser().getUserId() != p.getUser().getUserId()) { //댓글 작성자가 게시글 작성자가 아닌 경우
				String subContent = c.getContent().length() > 9? c.getContent().substring(0, 10)+"..." : c.getContent();
				String text = "내 댓글이 채택되었습니다. (" + subContent + ")";
				String url = "/project/"+p.getProject().getProjectId()+"/freeBoard/"+postId;
				
				timelineRepository.save(new Timeline(0, text, LocalDateTime.now(), url, c.getUser()));
			}
			
			return 1; // ok
		} 
		return 0; // not ok!

	}
}
