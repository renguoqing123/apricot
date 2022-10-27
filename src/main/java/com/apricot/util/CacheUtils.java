package com.apricot.util;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import com.apricot.model.Member;

public class CacheUtils {
	
	public static String cachekey ="verifyCode";
	
	public static String createVerifyCode(String name,Integer num, Integer time){
		String verifyCode = VerifyCodeUtil.generateTextCode(VerifyCodeUtil.TYPE_NUM_ONLY, num, null);
		GuavaCacheUtil cache = getCache(name);
		cache.put(name, verifyCode);
		return verifyCode;
	}
	
	public static String createVerifyCode(){
		String verifyCode = VerifyCodeUtil.generateTextCode(VerifyCodeUtil.TYPE_NUM_ONLY, 4, null);
		GuavaCacheUtil cache = getCache(cachekey);
		cache.put(cachekey, verifyCode);
		return verifyCode;
	}

	public static String getVerifyCode(String name){
		GuavaCacheUtil cache = getCache(name);
		String verifyCodeInSession = (cache == null) ? "":GuavaCacheUtil.get(name).toString();
		return verifyCodeInSession;
	}
	
	public static String getVerifyCode(){
		Object verifyObj = GuavaCacheUtil.get(cachekey);
		String verifyCodeInSession = (verifyObj == null) ? "":verifyObj.toString();
		return verifyCodeInSession;
	}

	public static void deleteVerifyCode(){
		GuavaCacheUtil.del(cachekey);
	}
	
	public static GuavaCacheUtil getCache(String key){
		return GuavaCacheUtil.builder().build().create(key,5,TimeUnit.SECONDS);
	}
	
	public static Member memberCache(HttpServletRequest request){
		String sessionid = request.getSession().getId();
		return (Member)GuavaCacheUtil.get(sessionid);
	}
}
