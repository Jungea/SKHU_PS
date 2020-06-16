package net.skhu.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 게시판 같은 내용, 작성일, 작성자, 코멘트 수 만 필요한 목록을 담는 모델 
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListModel {
	int id;
	int detId;
	String userName;
	String content;
	LocalDateTime writeTime;
	int commentNum;

	public ListModel(int id, int detId, String content, LocalDateTime writeTime, int commentNum) {
		this.id = id;
		this.detId = detId;
		this.content = content;
		this.writeTime = writeTime;
		this.commentNum = commentNum;
	}
}
