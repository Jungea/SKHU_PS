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
@ToString(exclude = { "projects", "projectJoins","projectStars", "todos","posts","comments", "timelines" })
@EqualsAndHashCode(exclude = { "projects", "projectJoins","projectStars", "todos","posts","comments", "timelines" })
@Entity
public class User {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   int userId;

	int userNum;
	String name;
	Integer grade;
	String email;
	String password;
	LocalDateTime timelineTime;
	Boolean userType;
	String github;
	String intro;
	String language;
	boolean emailCheck;
	String authKey;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "detDeptId")
   Detail detDepartment;

	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	List<Project> projects;

	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	List<ProjectJoin> projectJoins;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	List<ProjectStar> projectStars;

	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	List<Todo> todos;

	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	List<Post> posts;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	List<Comment> comments;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	List<Timeline> timelines;
	
   public User() {
   }

   public User(int userNum, String name, Integer grade, String email, String password, LocalDateTime timelineTime, boolean userType,
         boolean emailCheck, String authKey, Detail detDepartment) {
      this.userNum = userNum;
      this.name = name;
      this.grade = grade;
      this.email = email;
      this.password = password;
      this.timelineTime = timelineTime;
      this.userType = userType;
      this.emailCheck = emailCheck;
      this.authKey = authKey;
      this.detDepartment = detDepartment;
   }

}