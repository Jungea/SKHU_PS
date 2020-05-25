package net.skhu.model;

import lombok.Data;

@Data
public class TodoModel {
	int projectId;
	String detail;
	int week;
	int progState;
}
