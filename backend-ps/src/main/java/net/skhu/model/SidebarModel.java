package net.skhu.model;

import lombok.Data;

@Data
public class SidebarModel {
	int value;
	String text;
	
	public SidebarModel() {}

	public SidebarModel(int projectId, String projectName) {
		super();
		this.value = projectId;
		this.text = projectName;
	}
}
