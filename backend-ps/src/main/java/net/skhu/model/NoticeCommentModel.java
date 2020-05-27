package net.skhu.model;

import lombok.Data;
import net.skhu.domain.Comment;
import net.skhu.domain.User;

@Data
public class NoticeCommentModel {
	Comment commnet;
	User user;
	
}
