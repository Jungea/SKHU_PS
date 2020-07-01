package net.skhu.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Contest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int contestId;

	int year;
	String prize;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "detId")
	Detail detail;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "projectId")
	Project project;
}
