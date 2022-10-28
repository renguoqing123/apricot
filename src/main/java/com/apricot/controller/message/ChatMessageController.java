package com.apricot.controller.message;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apricot.common.ResponseResult;
import com.apricot.controller.BaseController;
import com.apricot.message.function.UserFunction;
import com.apricot.message.function.UserFunctionImpl;
import com.apricot.message.user.UserDM;
import com.apricot.util.AvailablePortUtil;
import com.apricot.util.CacheUtils;

@RestController
@RequestMapping("/api/chat")
public class ChatMessageController extends BaseController{
	
	private UserFunctionImpl func;
	
	//创建连接
	@GetMapping("/newConnect")
	public ResponseResult<Object> newConnect(HttpServletRequest request) {
		try {
			String ip = getIpAddr();
			UserDM dm = CacheUtils.userDMCache(request);
			dm.setIp(InetAddress.getByName(ip));
			int availablePort = AvailablePortUtil.getAvailablePort();
			AvailablePortUtil.put(ip, availablePort);
			dm.setPort(availablePort);
			func = new UserFunctionImpl(dm);
			func.start();
			return new ResponseResult<>(dm.getNickName());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseResult<>();
	}

	//用户上线
	@GetMapping("/online")
	public void online(HttpServletRequest request) {
		try {
			String ip = getIpAddr();
			UserDM dm = CacheUtils.userDMCache(request);
			dm.setIp(InetAddress.getByName(ip));
			dm.setPort(AvailablePortUtil.get(ip));
			dm.setNickName(dm.getNickName());
			func.login(dm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//用户下线
	@GetMapping("/offline")
	public void offline(HttpServletRequest request) {
		try {
			String ip = getIpAddr();
			UserDM dm = CacheUtils.userDMCache(request);
			dm.setIp(InetAddress.getByName(ip));
			dm.setPort(AvailablePortUtil.get(ip));
			dm.setNickName(dm.getNickName());
			func.logout(dm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//加入连接
	public void addConnect() {}
	
	//监听消息
	public void htmlListener() {}
	
	//发送消息
	@GetMapping("/sendClientMsg")
	public void sendClientMsg(@RequestParam String context, HttpServletRequest request) {
		try {
			String ip = getIpAddr();
			UserDM dm = CacheUtils.userDMCache(request);
			dm.setIp(InetAddress.getByName(ip));
			dm.setPort(AvailablePortUtil.get(ip));
			dm.setNickName(dm.getNickName());
			func.chatWithOne(dm, context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//发送群消息
	public void sendClientAllMsg(String context,HttpServletRequest request) {
		try {
			UserDM dm = CacheUtils.userDMCache(request);
			UserFunction model = new UserFunctionImpl(dm);
			model.chatWithAll(context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
