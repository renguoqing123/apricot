package com.apricot.util;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

public class AvailablePortUtil {
	
	public static Map<String,Integer> portMap = Maps.newHashMap();
	public static int max = 65535;
	public static int min = 1024;
	
	public static int getAvailablePort(){
        Random random = new Random();
        int port = random.nextInt(max)%(max-min+1) + min;
        boolean using = isLoclePortUsing(port);
        if (using){
            return getAvailablePort();
        }else {
            return port;
        }
    }
	
	public static boolean isLoclePortUsing(int port){
		Collection<Integer> ports = portMap.values();
		if(ports.contains(port)) {return true;}
		return false;
	}
	
	public static void put(String ip, int port) {
		portMap.put(ip, port);
	}
	
	public static Integer get(String ip) {
		return portMap.get(ip);
	}
}
