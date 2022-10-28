package com.apricot.message.message;

import com.apricot.message.user.UserDM;
/**
 * 发送/接收文件消息
 *
 */
public class InputStreamMessage extends AbstractMessage {
	private static final long serialVersionUID = -519481211356113990L;
	/**文件数据*/
	private byte[] data;
	/**文件名称*/
	private String fileName;
	/**消息来源*/
	private UserDM form;

	public byte[] getData() {
		return data;
	}
	public String getFileName() {
		return this.fileName;
	}

	public UserDM getForm() {
		return form;
	}
	public void setData(byte[] data) {
		this.data = data;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setForm(UserDM form) {
		this.form = form;
	}


}
