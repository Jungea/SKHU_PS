package net.skhu.model;

import lombok.Data;

@Data
public class ProfileModel {
	String intro; // 한줄소개
	String departmentName;// 학과
	String grade;// 학년
	String github;// 깃허브 닉네임
	String language;// 언어

}
