package com.apricot.message.message;

import com.apricot.message.user.UserDM;
/**
 * 用户更新个人信息消息
 *
 */
public class UpdateInfoMessage extends AbstractMessage {
	private static final long serialVersionUID = -2394284959496106020L;
	private UserDM onlineUser;
	public UserDM getOnlineUser() {
		return onlineUser;
	}
	public void setOnlineUser(UserDM onlineUser) {
		this.onlineUser = onlineUser;
	}
}
