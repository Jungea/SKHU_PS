package net.skhu.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(exclude = {})
@EqualsAndHashCode(exclude = {})
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Timeline {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int timelineId;
	
	String content;
	LocalDateTime time;
	String url;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userId")
	User user;
}
