package net.skhu.model;

import lombok.Data;
import net.skhu.domain.Detail;
import net.skhu.domain.Project;

/*
 * 경진대회 목록에서 프로젝트들
 */

@Data
public class ContestProjectModel {
	int contestId;
	Detail detail;
	Project project;
	int year;
	String prize;
	boolean star;
}
