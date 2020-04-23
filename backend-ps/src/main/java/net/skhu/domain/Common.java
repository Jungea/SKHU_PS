package net.skhu.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(exclude = { "details" })
@EqualsAndHashCode(exclude = { "details" })
@Entity
public class Common {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int cmnId;

	String cmnName;

	@JsonIgnore
	@OneToMany(mappedBy = "common", fetch = FetchType.LAZY)
	List<Detail> details;

}
