package net.skhu.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
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
public class Todo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int todoId;

	String detail;
	int progState;
	LocalDateTime createTime;
	@Column(name = "`order`")
	int order;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userId")
	User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "weeklyId")
	Weekly weekly;

}
