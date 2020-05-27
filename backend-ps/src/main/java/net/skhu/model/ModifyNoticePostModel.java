package net.skhu.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ModifyNoticePostModel {
	int postId;
	String title;
	String content;
	LocalDate deadlineTime;
	LocalDate extentionTime;
}
