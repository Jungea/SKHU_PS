package net.skhu.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import net.skhu.domain.Post;
import net.skhu.domain.Project;
@Data
public class FileModel {
	MultipartFile[] multipartFiles;
	int postId;
	String projectId; // 교수일때는 null, 학생일때는 해당 프로젝트 번호
}
