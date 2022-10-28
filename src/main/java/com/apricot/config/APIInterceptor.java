package com.apricot.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.apricot.util.GsonUtil;
import com.apricot.util.GuavaCacheUtil;

/**
 * api拦截器
 * @author rengq
 *
 */
//@Component
public class APIInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		String sessionid = session.getId();
		System.out.println("session:"+GsonUtil.gson.toJson(session));
		Object cacheMember = GuavaCacheUtil.get(sessionid);
		if(null == cacheMember) {
//			ResponseResult<Void> rr = new ResponseResult<>();
//			response.setContentType("application/json;charset=UTF-8");
//			rr.setRespCode(SysRespConstants.MEMBER_NO_LOGIN.getCode());
//			rr.setMemo(SysRespConstants.MEMBER_NO_LOGIN.getMessage());
//			response.getWriter().print(GsonUtil.gson.toJson(rr));
//			response.getWriter().flush();
			response.sendRedirect("/login.html");
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}