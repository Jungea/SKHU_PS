package net.skhu.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class WeekGoalModel {
	int projectId;
	LocalDate startTime;
	String detail;
}
