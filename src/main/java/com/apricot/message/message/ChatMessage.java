package com.apricot.message.message;

import com.apricot.model.Member;

/**
 * 聊天信息
 */
public class ChatMessage extends AbstractMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4702481486132933173L;
	/**消息内容*/
	private String message;
	/**消息来源*/
	private Member user;
	/***
	 * 0=群聊
	 * 1=私聊
	 */
	private int type=0;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Member getUser() {
		return user;
	}

	public void setUser(Member user) {
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
