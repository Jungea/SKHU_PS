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
@ToString(exclude = { "departmentUsers", "posts" })
@EqualsAndHashCode(exclude = { "departmentUsers", "posts" })
@Entity
public class Detail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int detId;

	int cmnDetId;
	String detName;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cmnId")
	Common common;

	@JsonIgnore
	@OneToMany(mappedBy = "detDepartment", fetch = FetchType.LAZY)
	List<User> departmentUsers;
	
	@JsonIgnore
	@OneToMany(mappedBy = "detail", fetch = FetchType.LAZY)
	List<Post> posts;

}
