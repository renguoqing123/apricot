package com.apricot.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="t_member")
public class Member {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(name="user_name")
	private String userName;
	@Column(name="pass_word")
	private String password;
	@Column(name="nick_name")
	private String nickName;
	@Column(name="last_login_time", columnDefinition = "DATETIME")
	private Date lastLoginTime;
	@Column(name="create_date", columnDefinition = "DATETIME")
	private Date createDate;
	@Column(name="create_user")
	private String createUser;
	@Column(name="update_date", columnDefinition = "DATETIME")
	private Date updateDate;
	@Column(name="update_user")
	private String updateUser;
	
}
