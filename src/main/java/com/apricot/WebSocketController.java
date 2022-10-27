package com.apricot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketController {

	@GetMapping("/api/webSocket/getSocketServerUrl")
	public String getSocketServerUrl(@RequestParam String host) {
		String wsUrl="ws://"+host+"/api/webSocket";
		return wsUrl;
	}
	
}
