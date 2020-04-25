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
@ToString(exclude = { "projects", "projectJoins" })
@EqualsAndHashCode(exclude = { "projects", "projectJoins" })
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int userId;

	int userNum;
	String name;
	int grade;
	String email;
	String password;
	LocalDateTime timelineTime;
	Boolean userType;
	String github;
	String intro;
	boolean emailCheck;
	String authKey;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "detDeptId")
	Detail det_dept_id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "detLanguageId")
	Detail det_detLanguage_id;

	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	List<Project> projects;

	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	List<ProjectJoin> projectJoins;
	
	public User(int userNum,String name,String email,String password,LocalDateTime timelineTime,boolean userType,boolean emailCheck,String authKey) {
		this.userNum=userNum;
		this.name=name;
		this.email=email;
		this.password=password;
		this.timelineTime=timelineTime;
		this.userType=userType;
		this.emailCheck=emailCheck;
		this.authKey=authKey;
	}

}
