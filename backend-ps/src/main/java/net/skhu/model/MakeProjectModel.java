package net.skhu.model;

import lombok.Data;

@Data
public class MakeProjectModel {
	String projectName;
	String theme;
	String content;
	String tag;
	String github;
	boolean rcrtState;
	String authKey;
}
