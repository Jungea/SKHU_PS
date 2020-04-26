package net.skhu.model;

import lombok.Data;

@Data
public class SignUpModel {
	String userNum;
	String password;
	String name;
	String email;
	String authKey;
}
