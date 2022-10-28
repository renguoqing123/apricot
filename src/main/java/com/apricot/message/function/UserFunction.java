package com.apricot.message.function;

import java.io.File;

import com.apricot.message.message.ChatMessage;
import com.apricot.message.user.UserDM;
/**
 * 系统用户业务模型
 *
 */
public interface UserFunction {
	/**
	 * 登记
	 * @param name
	 */
	void login(UserDM dm);
	/**
	 * 退出
	 */
	void logout(UserDM dm);
	/**
	 * 更新用户信息
	 * @param name
	 */
	void updateUserInfo(String name);
	/**
	 * 查看用户信息
	 * @return
	 */
	UserDM checkUser(String ip);
	/**
	 * 私聊
	 * @param user
	 * @param message
	 */
	ChatMessage chatWithOne(UserDM user,String message);
	/**
	 * 群聊
	 * @param message
	 */
	ChatMessage chatWithAll(String message);
	/**
	 * 传入文件
	 * @param user
	 * @param file
	 */
	void transferFile(UserDM user,File file);
	/**
	 * 返回用户自己的信息
	 * @return
	 */
	UserDM getMyselfInfo();
}
