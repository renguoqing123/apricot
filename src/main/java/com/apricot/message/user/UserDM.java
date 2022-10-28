package com.apricot.message.user;

import java.io.Serializable;
import java.net.InetAddress;

import com.apricot.model.Member;
/**
 * 用户数据模型,代表用户个人信息,继承Member类
 */
public class UserDM extends Member implements Serializable{
	private static final long serialVersionUID = -7798990187931132951L;
	/**用户所在ip*/
	private  InetAddress ip;
	/**用户UDP对应端口*/
	private int port;
	
	public InetAddress getIp() {
		return ip;
	}
	public void setIp(InetAddress ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public static void main(String[] args) throws Exception{
		InetAddress local=InetAddress.getLocalHost();
		System.out.println(InetAddress.getLocalHost().getHostAddress());
		System.out.println(local.equals(InetAddress.getByName("192.168.1.106")));
	}
}
