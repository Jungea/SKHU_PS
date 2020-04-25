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
@ToString(exclude = { "languageProjects", "subjectProjects", "departmentUsers", "languageUsers" })
@EqualsAndHashCode(exclude = { "languageProjects", "subjectProjects", "departmentUsers", "languageUsers" })
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
   @OneToMany(mappedBy = "detLanguage", fetch = FetchType.LAZY)
   List<Project> languageProjects;

   @JsonIgnore
   @OneToMany(mappedBy = "detSubject", fetch = FetchType.LAZY)
   List<Project> subjectProjects;

   @JsonIgnore
   @OneToMany(mappedBy = "det_dept_id", fetch = FetchType.LAZY)
   List<User> departmentUsers;


}