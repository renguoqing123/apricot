package com.apricot.message.function;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.apricot.message.ReceiveThread;
import com.apricot.message.SendThread;
import com.apricot.message.config.SystemConfig;
import com.apricot.message.message.ChatMessage;
import com.apricot.message.message.InputStreamMessage;
import com.apricot.message.message.OfflineMessage;
import com.apricot.message.message.OnlineMessage;
import com.apricot.message.message.UpdateInfoMessage;
import com.apricot.message.user.UserDM;
import com.apricot.util.GsonUtil;

import lombok.extern.slf4j.Slf4j;
/**
 * 用户业务模型具体实现和消息处理接口具体实现
 *
 */
@Slf4j
public class UserFunctionImpl implements UserFunction, MessageHandler {
	/**当前用户信息*/
	public final UserDM dm;
	/**UDPsocket*/
	public final DatagramSocket socket;
	/**发送消息线程*/
	private SendThread sendThread;
	/**接收消息线程*/
	private ReceiveThread receiveThread;
	/**在线用户Map集合*/
	public final HashMap<String, UserDM> onlineUsers;

	public UserFunctionImpl(UserDM dm) throws Exception {
		this.dm = dm;
		stop();
		this.socket = new DatagramSocket(dm.getPort());
		this.sendThread = new SendThread(socket);
		this.receiveThread = new ReceiveThread(this);
		this.onlineUsers = new LinkedHashMap<>(128);
	}
	
	public void stop() {
		if(null == socket) {
			return;
		}
		this.socket.disconnect();
	}
	
	/**
	 * 开启发送和接收线程
	 */
	public void start() {
		log.info("----------开启发送和接收线程:{}----------",GsonUtil.gson.toJson(dm));
		this.sendThread.start();
		this.receiveThread.start();
	}
	
	/**
	 * 使用UDP点对点方式发送上线消息回执
	 * @param use 上线用户
	 */
	private void sendOnlineTo(UserDM use) {
		OnlineMessage m = new OnlineMessage();
		m.setIp(use.getIp());
		m.setPort(use.getPort());
		m.setOnlineUser(this.dm);
		this.sendThread.sendMessage(m.toPacket());
	}
	/**
	 * 用户上线
	 */
	@Override
	public void login(UserDM dm) {
		log.info("----------用户上线:{}----------",GsonUtil.gson.toJson(dm));
		OnlineMessage m = new OnlineMessage();
		m.setIp(dm.getIp());
		m.setPort(dm.getPort());
		this.dm.setNickName(dm.getNickName());
		m.setOnlineUser(this.dm);
		this.sendThread.sendMessage(m.toPacket());
	}
	/**
	 * 用户下线
	 */
	@Override
	public void logout(UserDM dm) {
		OfflineMessage m = new OfflineMessage();
		m.setIp(dm.getIp());
		m.setPort(dm.getPort());
		m.setOnlineUser(this.dm);
		this.sendThread.sendMessage(m.toPacket());
	}
	/**
	 * 更新个人信息
	 */
	@Override
	public void updateUserInfo(String name) {
		this.dm.setNickName(name);
		UpdateInfoMessage m = new UpdateInfoMessage();
		m.setOnlineUser(this.dm);
		m.setIp(SystemConfig.BROADCAST_IP);
		m.setPort(SystemConfig.PORT);
		this.sendThread.sendMessage(m.toPacket());
	}
	/**
	 * 查询在线用户个人信息
	 */
	@Override
	public UserDM checkUser(String ip) {
		return this.onlineUsers.get(ip);
	}
	/**
	 * 私聊实现
	 */
	@Override
	public ChatMessage chatWithOne(UserDM user, String message) {
		log.info("----------私聊:{}----------",GsonUtil.gson.toJson(user));
		ChatMessage m = new ChatMessage();
		m.setIp(user.getIp());
		m.setMessage(message);
		m.setPort(user.getPort());
		m.setType(1);
		m.setUser(dm);
		this.sendThread.sendMessage(m.toPacket());
		return m;
	}
	/**
	 * 群聊实现
	 */
	@Override
	public ChatMessage chatWithAll(String message) {
		ChatMessage m = new ChatMessage();
		m.setIp(SystemConfig.BROADCAST_IP);
		m.setMessage(message);
		m.setPort(SystemConfig.PORT);
		m.setUser(this.dm);
		this.sendThread.sendMessage(m.toPacket());
		return m;
	}
	/**
	 * 发送文件给指定在线用户
	 */
	@Override
	public void transferFile(UserDM user, File file) {
		try {
			FileInputStream in = new FileInputStream(file);
			byte[] buf = new byte[in.available()];
			String fileName = file.getName();
			in.read(buf);
			in.close();
			InputStreamMessage m = new InputStreamMessage();
			m.setFileName(fileName);
			m.setForm(this.dm);
			m.setIp(user.getIp());
			m.setPort(user.getPort());
			m.setData(buf);
			this.sendThread.sendMessage(m.toPacket());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 * 接收用户上线信息处理
	 */
	@Override
	public void handleOnlineMessage(OnlineMessage m) {
		UserDM dm = m.getOnlineUser();
		String ip = dm.getIp().getHostAddress();
		if (!this.onlineUsers.containsKey(ip)) {
			this.onlineUsers.put(dm.getIp().getHostAddress(), dm);
			// 通知视图
			log.info("用户上线：{}", GsonUtil.gson.toJson(dm));
		}
		// 发送回执
//		this.sendOnlineTo(dm);
	}
	/**
	 * 接收用户更新个人信息处理
	 */
	@Override
	public void handleUpdateInfoMessage(UpdateInfoMessage m) {
		UserDM dm = m.getOnlineUser();
		String ip = dm.getIp().getHostAddress();
		this.onlineUsers.put(ip, dm);
//		listener.userChangeInfo(dm);

	}
	/**
	 * 接收用户下线消息处理
	 */
	@Override
	public void handleOfflineMessage(OfflineMessage m) {
		UserDM dm = m.getOnlineUser();
		String ip = dm.getIp().getHostAddress();
		this.onlineUsers.remove(ip);
		// 通知
		log.info("用户下线：{}", GsonUtil.gson.toJson(dm));
	}
	/**
	 * 接收用户聊天信息处理
	 */
	@Override
	public void handleChatMessage(ChatMessage m) {
		// 通知
		System.out.println("接收用户聊天:"+GsonUtil.gson.toJson(m));
	}
	/**
	 * 接收用户文件消息处理
	 */
	@Override
	public void handleInputStreamMessage(InputStreamMessage m) {
		byte[] data = m.getData();
		try {
			FileOutputStream out = new FileOutputStream(m.getFileName());
			out.write(data);
			out.close();
			// 通知
			System.out.println(m);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 * 返回当前用户信息
	 */
	@Override
	public UserDM getMyselfInfo() {
		return this.dm;
	}
}
