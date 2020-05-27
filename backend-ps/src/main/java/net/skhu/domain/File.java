package net.skhu.domain;

import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(exclude = { "" })
@EqualsAndHashCode(exclude = { "" })
@Entity
public class File {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    int fileId;
	
	String name;
	int size;
	LocalDateTime submitTime;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "postId")
	Post post;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "projectId")
	Project project;
	
    @Basic(fetch=FetchType.LAZY)
    @Lob
    byte[] data;

}
