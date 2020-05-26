package net.skhu.model;

import lombok.Data;

@Data
public class TodoModel {
	int weeklyId;
	String detail;
	int progState;
	int order;
}
