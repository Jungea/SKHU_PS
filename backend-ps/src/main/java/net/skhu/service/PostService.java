package net.skhu.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.skhu.domain.Post;
import net.skhu.domain.Project;
import net.skhu.domain.ProjectJoin;
import net.skhu.domain.Timeline;
import net.skhu.domain.User;
import net.skhu.model.ModifyNoticePostModel;
import net.skhu.model.WriteNoticeModel;
import net.skhu.repository.DetailRepository;
import net.skhu.repository.FileRepository;
import net.skhu.repository.PostRepository;
import net.skhu.repository.ProjectRepository;
import net.skhu.repository.SubjectRepository;
import net.skhu.repository.TimelineRepository;
import net.skhu.repository.UserRepository;

@Service
public class PostService {
	@Autowired
	PostRepository postRepository;
	@Autowired
	SubjectRepository subjectRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	DetailRepository detailRepository;
	@Autowired
	FileRepository fileRepository;
	@Autowired
	ProjectRepository projectRepository;
	@Autowired
	ProjectService projectService;
	@Autowired
	TimelineRepository timelineRepository;
	
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
	@Transactional
	public int writeNotice(WriteNoticeModel notice,int userId) {
		Post post=new Post();
		post.setSubject(subjectRepository.findBySubjectId(notice.getSubjectId()));
		post.setTitle(notice.getTitle());
		post.setContent(notice.getContent());
		post.setWriteTime(LocalDateTime.now());
		LocalDate date=LocalDate.of(1000, 01, 01);
		if(notice.getDeadlineTime()==null && notice.getExtensionTime()==null) {
			post.setDeadlineTime(date.atTime(0, 0, 0));
			post.setExtentionTime(date.atTime(0, 0, 0));
		} else if(notice.getDeadlineTime()!=null && notice.getExtensionTime()==null) {
			post.setDeadlineTime(notice.getDeadlineTime().atTime(0, 0, 0));
			post.setExtentionTime(date.atTime(0, 0, 0));
		} else {
			post.setDeadlineTime(notice.getDeadlineTime().atTime(0, 0, 0));
			post.setExtentionTime(notice.getExtensionTime().atTime(0, 0, 0));
		}
		post.setUser(userRepository.findById(userId).get());
		post.setDetail(detailRepository.findById(13).get());
		
		int postId = postRepository.save(post).getPostId();
		
		//TIMELINE 새로운 공지게시글 작성 [과목 선택 프로젝트 모든 참가자가 받음]
		//어떤 과목에 새로운 공지 게시글이 생성되었습니다.
		for(Project project : projectService.subjectProjects(notice.getSubjectId())) {
			for(ProjectJoin join : projectService.allMember(project.getProjectId())) {
				String content = post.getSubject().getTitle()+"에 새로운 공지 게시글이 생성되었습니다. ("+post.getTitle()+")";
				String url = "/project/"+project.getProjectId()+"/noticeBoard/"+postId;
				
				timelineRepository.save(new Timeline(0, content, LocalDateTime.now(), url, join.getUser()));
			}
		}
		
		return postId;
	}
	@Transactional
	public void noticeModifyPost(ModifyNoticePostModel post) {
		Post p=postRepository.findById(post.getPostId()).get();
		p.setTitle(post.getTitle());
		p.setContent(post.getContent());
		p.setDeadlineTime(post.getDeadlineTime().atTime(0,0,0));
		p.setExtentionTime(post.getExtentionTime().atTime(0,0,0));
		postRepository.save(p);
		
		//TIMELINE 새로운 공지게시글 수정 [과목 선택 프로젝트 모든 참가자가 받음]
		for(Project project : projectService.subjectProjects(p.getSubject().getSubjectId())) {
			for(ProjectJoin join : projectService.allMember(project.getProjectId())) {
				String content = p.getSubject().getTitle()+"의 공지 게시글이 수정되었습니다. ("+p.getTitle()+")";
				String url = "/project/"+project.getProjectId()+"/noticeBoard/"+p.getPostId();
				
				timelineRepository.save(new Timeline(0, content, LocalDateTime.now(), url, join.getUser()));
			}
		}
	}
	public List<String> fileSubmitList(int page,int projectId,int subjectId) {
		List<Post> posts=postRepository.findBySubject_subjectId(subjectId);
		Collections.reverse(posts);
		List<String> lists=new ArrayList<>();
		for(int i=0;i<posts.size();i++) {
			if(fileRepository.findByPost_PostIdAndProject_ProjectId(posts.get(i).getPostId(),projectId).size()==0) {
				lists.add("0");
			} else {
				lists.add("1");
			}
		}
		if(posts.size()<page*6) {
			lists=lists.subList((page-1)*6,lists.size());
		} else {
			lists=lists.subList((page-1)*6,page*6);
		}
		return lists;
	}
	public List<Post> freeBoard(int page,int projectId) {
		List<Post> posts=postRepository.findByProject_projectId(projectId);
		Collections.reverse(posts);
		if(posts.size()<page*6) {
			posts=posts.subList((page-1)*6,posts.size());
		} else {
			posts=posts.subList((page-1)*6,page*6);
		}
		return posts;
	}
	@Transactional
	public int writeFree(WriteNoticeModel notice,int userId) {
		Post post=new Post();
		post.setProject(projectRepository.findById(notice.getProjectId()).get());
		post.setTitle(notice.getTitle());
		post.setContent(notice.getContent());
		post.setWriteTime(LocalDateTime.now());
		post.setUser(userRepository.findById(userId).get());
		post.setDetail(detailRepository.findById(14).get());
		return postRepository.save(post).getPostId();
	}
}
