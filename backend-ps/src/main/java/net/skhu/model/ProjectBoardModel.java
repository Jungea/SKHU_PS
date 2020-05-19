package net.skhu.model;

import java.util.Set;

import lombok.Data;
import net.skhu.domain.Project;

@Data
public class ProjectBoardModel {
	Project project;
	boolean star;
	Set<Integer> allMemGrade; // 중복 없애려고 사용
	String subjectName; // 과목 이름
	String createName; // 프로젝트 생성 유저 이름
}
