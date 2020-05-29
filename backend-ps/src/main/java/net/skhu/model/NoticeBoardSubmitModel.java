package net.skhu.model;

import java.util.List;

import lombok.Data;
import net.skhu.domain.File;
import net.skhu.domain.Project;
import net.skhu.domain.User;

@Data
public class NoticeBoardSubmitModel {
	Project project;
	List<File> files;
}
