package com.apricot.controller.member;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apricot.common.ResponseResult;
import com.apricot.common.SysRespConstants;
import com.apricot.controller.member.dto.Login;
import com.apricot.controller.member.dto.Register;
import com.apricot.dao.MemberDao;
import com.apricot.model.Member;
import com.apricot.util.CacheUtils;
import com.apricot.util.GuavaCacheUtil;
import com.apricot.util.VerifyCodeUtil;

@RestController
@RequestMapping("/api/member")
public class MemberController {

	@Autowired
	private MemberDao memberDao;
	
	@PostMapping(value="/login")
	public ResponseResult<Void> mlogin(@RequestBody Login login,HttpServletRequest request, HttpServletResponse response) {
		String userName = login.getUserName();
		if(StringUtils.isEmpty(userName)) {
			return new ResponseResult<>(SysRespConstants.MEMBER_ISNULL_ERROR);
		}
		Member member = getMember(login.getUserName());
		if(StringUtils.isEmpty(member)) {
			return new ResponseResult<>(SysRespConstants.MEMBER_NOT_EXISTS_ERROR);
		}
		if(StringUtils.isEmpty(login.getPassword())) {
			return new ResponseResult<>(SysRespConstants.MEMBER_PASSWORD_NOTNULL_ERROR);
		}
		if(!login.getPassword().equals(member.getPassword())) {
			return new ResponseResult<>(SysRespConstants.MEMBER_PASSWORD_ERROR);
		}
		HttpSession session = request.getSession();
		String oldVerifyCode = (String) session.getAttribute(CacheUtils.cachekey);
		if(StringUtils.isEmpty(login.getVcode())) {
			return new ResponseResult<>(SysRespConstants.MEMBER_VERIFYCODE_NOTNULL_ERROR);
		}
		if(StringUtils.isEmpty(oldVerifyCode)) {
			return new ResponseResult<>(SysRespConstants.MEMBER_VERIFYCODE_INVALID_ERROR);
		}
		if(!StringUtils.isEmpty(login.getVcode()) && !oldVerifyCode.equals(login.getVcode())) {
			session.removeAttribute(CacheUtils.cachekey);
			return new ResponseResult<>(SysRespConstants.MEMBER_VERIFYCODE_ERROR);
		}
		String sessionid = request.getSession().getId();
		GuavaCacheUtil memberCache = GuavaCacheUtil.builder().build().create(sessionid, 24, TimeUnit.HOURS);
		memberCache.put(sessionid, member);
		member.setLastLoginTime(new Date());
		memberDao.save(member);
		session.removeAttribute(CacheUtils.cachekey);
		return new ResponseResult<>(SysRespConstants.SUCCESS);
	}
	
	@PostMapping("/register")
	public ResponseResult<Void> mRegister(@RequestBody Register register) {
		String userName = register.getUserName();
		if(StringUtils.isEmpty(userName)) {
			return new ResponseResult<>(SysRespConstants.MEMBER_ISNULL_ERROR);
		}
		Member member = getMember(register.getUserName());
		if(!StringUtils.isEmpty(member)) {
			return new ResponseResult<>(SysRespConstants.MEMBER_ISEXISTS_ERROR);
		}
		if(!register.getPassword().equals(register.getOncePassword())) {
			return new ResponseResult<>(SysRespConstants.MEMBER_ONCE_PASSWORD_ERROR);
		}
		Member m = new Member();
		m.setUserName(register.getUserName());
		m.setNickName(register.getNickName());
		m.setPassword(register.getPassword());
		m.setCreateDate(new Date());
		m.setCreateUser(register.getUserName());
		memberDao.save(m);
		return new ResponseResult<>(SysRespConstants.SUCCESS);
	}
	
	@PostMapping("/updatePwd")
	public ResponseResult<Void> updatePwd(@RequestBody Register register) {
		Member member = getMember(register.getUserName());
		if(StringUtils.isEmpty(member)) {
			return new ResponseResult<>(SysRespConstants.MEMBER_NOT_EXISTS_ERROR);
		}
		if(!register.getPassword().equals(register.getOncePassword())) {
			return new ResponseResult<>(SysRespConstants.MEMBER_ONCE_PASSWORD_ERROR);
		}
		member.setUserName(register.getUserName());
		if(!StringUtils.isEmpty(register.getNickName())) {
			member.setNickName(register.getNickName());
		}
		member.setPassword(register.getPassword());
		member.setUpdateDate(new Date());
		member.setUpdateUser(member.getUserName());
		memberDao.save(member);
		return new ResponseResult<>(SysRespConstants.SUCCESS);
	}
	
	@PostMapping("/loginExit")
	public ResponseResult<Void> mloginExit(HttpServletRequest request) {
		HttpSession session = request.getSession();
		//删除session
		GuavaCacheUtil.del(session.getId());
		session.invalidate();
		return new ResponseResult<>();
	}
	
	@GetMapping("/loginInfo")
	public ResponseResult<Member> getLognInfo(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object obj = GuavaCacheUtil.get(session.getId());
		if(StringUtils.isEmpty(obj)) {
			return new ResponseResult<>();
		}
		return new ResponseResult<>((Member)obj);
	}
	
	@GetMapping("/getVerifyCodeImage")
    public void getVerifyCodeImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 设置页面不缓存
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        String verifyCode = CacheUtils.createVerifyCode();
        
        System.out.println("验证码：" + verifyCode);
        // 将验证码放到HttpSession里面
        request.getSession().setAttribute(CacheUtils.cachekey, verifyCode);
        // 设置输出的内容的类型为JPEG图像
        response.setContentType("image/jpeg");
        BufferedImage bufferedImage = VerifyCodeUtil.generateImageCode(verifyCode, 90, 30, 3, true, Color.WHITE,
                Color.BLACK, null);
        // 写给浏览器
        ImageIO.write(bufferedImage, "JPEG", response.getOutputStream());
    }

	
	private Member getMember(String userName) {
		Member m = new Member();
		m.setUserName(userName);
		Example<Member> example = Example.of(m);
		Optional<Member> mm = memberDao.findOne(example);
		if(mm.isPresent()) {
			Member member = mm.get();
			return member;
		}
		return null;
	}
	
}
