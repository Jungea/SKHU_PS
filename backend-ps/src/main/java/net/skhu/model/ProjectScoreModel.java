package net.skhu.model;

import java.util.List;

import lombok.Data;
import net.skhu.domain.ProjectJoin;

@Data
public class ProjectScoreModel {
	List<ProjectJoin> memberList;
	List<String> scoreList;
	int postId;
}
