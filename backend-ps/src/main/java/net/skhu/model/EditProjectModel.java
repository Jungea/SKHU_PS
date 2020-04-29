package net.skhu.model;

import lombok.Data;

@Data
public class EditProjectModel {
	String projectName;
	String theme;
	String content;
	String tag;
	boolean rcrtState;
}