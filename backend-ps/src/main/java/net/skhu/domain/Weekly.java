package net.skhu.domain;

import java.time.LocalDate;
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
@ToString(exclude = { "todos" })
@EqualsAndHashCode(exclude = { "todos" })
@Entity
public class Weekly {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int weeklyId;

	LocalDate startTime;
	String detail;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "projectId")
	Project project;

	@JsonIgnore
	@OneToMany(mappedBy = "weekly", fetch = FetchType.LAZY)
	List<Todo> todos;
}
