package net.skhu.domain;

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
@ToString(exclude = { "projects" })
@EqualsAndHashCode(exclude = { "projects" })
@Entity
public class Subject {
	   @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	   int subjectId;
	   
	   int year;
	   int semester;
	   String title;
	   String authKey;
	   boolean pin;
	   
	   @ManyToOne(fetch = FetchType.EAGER)
	   @JoinColumn(name = "userId")
	   User user;
	   
	   @JsonIgnore
	   @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
	   List<Project> projects;

	   
}
