package com.apricot.message;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.apricot.message.function.UserFunctionImpl;
import com.apricot.message.message.AbstractMessage;
import com.apricot.message.message.ChatMessage;
import com.apricot.message.message.OfflineMessage;
import com.apricot.message.message.OnlineMessage;

/**
 * 接收消息线程任务
 *
 */
public class ReceiveThread extends Thread {
	private DatagramPacket receivePacket;
	private UserFunctionImpl model;
	private final DatagramSocket socket;
	private final String localhost;
	
	public ReceiveThread(UserFunctionImpl model) {
		this.model=model;
		this.socket = model.socket;
		localhost=this.model.dm.getIp().getHostAddress();
		byte[] buf = new byte[1024 * 1024 * 10];// 10M
		receivePacket = new DatagramPacket(buf, buf.length);
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			this.receiveOneMessage();
		}
	}


	private void receiveOneMessage() {
		try {
			this.socket.receive(this.receivePacket);
			DatagramPacket p=this.receivePacket;
			InetAddress fromIp=p.getAddress();
			//miss自己的
//			String ip=fromIp.getHostAddress();
//			if(ip.equals(this.localhost)){
//				return;
//			}
			
			ByteArrayInputStream bais=new ByteArrayInputStream(p.getData(),p.getOffset(),p.getLength());
			ObjectInputStream in=new ObjectInputStream(bais);
			AbstractMessage message=(AbstractMessage) in.readObject();
			message.setIp(fromIp);
			message.setPort(p.getPort());
			this.handleMessage(message);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void handleMessage(Object message){
		if(message instanceof OnlineMessage){
			System.out.println("onlineMessage :"+message);
			model.handleOnlineMessage((OnlineMessage)message);
		}else if(message instanceof OfflineMessage){
			System.out.println("offlineMessage :"+message);
			model.handleOfflineMessage((OfflineMessage)message);
		}else if(message instanceof ChatMessage){
			System.out.println("chatMessage :"+message);
			model.handleChatMessage((ChatMessage)message);
		}
	}
	
}
