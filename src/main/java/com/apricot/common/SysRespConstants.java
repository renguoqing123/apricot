package com.apricot.common;


public enum SysRespConstants {

	SUCCESS(ResponseResult.DEFAULT_SUCCESS_RESP_CODE, ResponseResult.DEFAULT_SUCCESS_RESP_MEMO),
	DEFAULT_SYS_ERROR_RESP_CODE("99999","系统异常"),
	MEMBER_NO_LOGIN("11111","未登录"),
	MEMBER_NOT_EXISTS_ERROR("00001","账号不存在"),
	MEMBER_PASSWORD_NOTNULL_ERROR("00002","密码不能为空"),
	MEMBER_PASSWORD_ERROR("00003","密码不正确"),
	MEMBER_VERIFYCODE_INVALID_ERROR("00004","验证码已失效,请点击刷新"),
	MEMBER_VERIFYCODE_NOTNULL_ERROR("00005","验证码不能为空"),
	MEMBER_VERIFYCODE_ERROR("00006","验证码不正确"),
	MEMBER_ISEXISTS_ERROR("00007","账号已存在"),
	MEMBER_ONCE_PASSWORD_ERROR("00008","两次密码输入不一致"),
	MEMBER_ISNULL_ERROR("00009","账号不能为空"),
	TXT_SAVE_ERROR("00010","保存失败"),
	TXT_CONTEXT_ISNULL_ERROR("00011","内容为空");
	
	
	private String code;
	private String message;
	
	private SysRespConstants(String code,String message) {
		this.code = code;
		this.message = message;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
