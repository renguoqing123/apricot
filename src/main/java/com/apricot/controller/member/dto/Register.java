package com.apricot.controller.member.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class Register implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6756071189242707476L;
	private String userName;
	private String nickName;
	private String password;
	private String oncePassword;
	
}
