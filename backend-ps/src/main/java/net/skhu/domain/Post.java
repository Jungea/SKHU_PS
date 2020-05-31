package net.skhu.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(exclude = { "comments","files","postLikes" })
@EqualsAndHashCode(exclude = { "comments","files","postLikes" })
@Entity
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int postId;

	String title;
	String content;
	LocalDateTime writeTime;
	LocalDateTime deadlineTime;
	LocalDateTime extentionTime;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userId")
	User user;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subjectId")
	Subject subject;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "projectId")
	Project project;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "detId")
	Detail detail;

	@JsonIgnore
	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
	List<Comment> comments;
	
	@JsonIgnore
	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
	List<File> files;
	
	@JsonIgnore
	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
	List<PostLike> postLikes;
}
