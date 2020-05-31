package net.skhu.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class WriteNoticeModel {
	int subjectId;
	int projectId;
	String title;
	String content;
	LocalDate deadlineTime;
	LocalDate extensionTime;
}
