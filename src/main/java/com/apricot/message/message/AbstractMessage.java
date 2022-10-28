package com.apricot.message.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
/**
 * 抽象消息类
 */
public abstract class AbstractMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6002157620233228324L;
	protected transient InetAddress ip;
	protected transient int port;

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
	/**
	 * 把消息通过序列化转换为字节数组,便于传输
	 * @return
	 * @throws IOException
	 */
	public byte[] getBytes() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
		ObjectOutputStream out = new ObjectOutputStream(baos);
		out.writeObject(this);
		byte[] data= baos.toByteArray();
		out.close();
		return data;
	}
	/**
	 * 把消息转换UDP数据报,便用Socket传输
	 */
	public final DatagramPacket toPacket() {
		try {
			byte[] buf = this.getBytes();
			DatagramPacket p = new DatagramPacket(buf, buf.length);
			p.setPort(this.port);
			p.setAddress(this.ip);
			return p;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
