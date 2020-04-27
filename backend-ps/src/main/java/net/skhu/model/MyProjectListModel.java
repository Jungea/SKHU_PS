package net.skhu.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MyProjectListModel {
	int projectId;
	String projectName;
	int memNum;
	boolean progState;
	boolean pin;
}
