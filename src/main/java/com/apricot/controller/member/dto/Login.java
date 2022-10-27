package com.apricot.controller.member.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class Login implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3820411128338422217L;
	private String userName;
	private String password;
	private String vcode;
	
}
