package com.apricot;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ServerEndpoint("/api/webSocket")
public class WebSocket implements Serializable{
	
	private static final long serialVersionUID = -2524528632848964738L;

	//静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
	
	//concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<WebSocket>();
    //采用redis缓存
//    private static RedisMap<String,WebSocketController> redisWebSocketMap = new RedisMap<String,WebSocketController>(SysCacheUtil.WEB_SOCKET_KEY);
    
    
	//与某个客户端的连接会话，需要通过它来给客户端发送数据(可悲，Session不支持序列化)
    private Session session;
    
    /**
     * 建立连接
     * @param session
     */
    @OnOpen
//    @ApiOperation(value = "建立连接")
	public void onOpen(Session session) {
		this.session = session;
		String sessionId = session.getId();
		log.info("sessionId:{}",sessionId);
		webSocketSet.add(this);
//		redisWebSocketMap.put(userName, this);
		addOnlineCount();
		log.info("有新连接加入！当前在线人数为" + getOnlineCount());
	}
	
	/**
     * 连接关闭调用的方法
     */
    @OnClose
//    @ApiOperation(value = "建立关闭")
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
//        redisWebSocketMap.remove(userName);
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }
	
	/**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
//    @ApiOperation(value = "收到客户端消息后调用的方法")
	public void onMessage(String message, Session session) {
		this.session = session;
		log.info("来自客户端的消息:" + message);
		// 单发
		try {
			sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
	/**
     * 群发自定义消息
     * */
	@OnMessage
//    @ApiOperation(value = "群发自定义消息")
    public static void sendInfo(String message) throws IOException {
    	log.info("群发自定义消息:" + message);
    	for (WebSocket item : webSocketSet) {
            try {
            	item.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }	
        
    }
    
    /**
  　　 * 发生错误时触发的方法
   　　*/
   @OnError
   public void onError(Session session,Throwable error){
      log.info(session.getId()+"连接发生错误"+error.getMessage());
      error.printStackTrace();
   }
	
	public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
	
	public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocket.onlineCount--;
    }
}
