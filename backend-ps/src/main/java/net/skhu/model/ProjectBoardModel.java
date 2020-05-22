package net.skhu.model;

import java.util.Set;

import lombok.Data;
import net.skhu.domain.Project;

@Data
public class ProjectBoardModel {
	Project project;
	boolean star;
	Set<Integer> allMemGrade; // 모든 참여자의 학년(중복 없애기 위해 set사용)
	String subjectName; // 과목 이름
	String createName; // 프로젝트 생성 유저 이름
	int state; // 프로젝트에 참가하고 있는지(1), 승인대기인지(0), 신청할 수 있는 상태인지(그외)
}
