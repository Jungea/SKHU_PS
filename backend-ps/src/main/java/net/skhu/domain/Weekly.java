package net.skhu.domain;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(exclude = {})
@EqualsAndHashCode(exclude = {})
@Entity
public class Weekly {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int weeklyId;

	LocalDate startTime;
	String detail;
	int week;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "projectId")
	Project project;
}
