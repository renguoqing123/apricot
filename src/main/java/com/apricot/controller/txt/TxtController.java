package com.apricot.controller.txt;

import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Example;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apricot.common.BaseRespMsg;
import com.apricot.common.ResponseResult;
import com.apricot.common.SysRespConstants;
import com.apricot.controller.txt.dto.TxtContext;
import com.apricot.dao.TxtDao;
import com.apricot.model.Txt;
import com.apricot.util.CacheUtils;

@RestController
@RequestMapping(("/api/txt"))
public class TxtController {

	@Resource
	private TxtDao txtDao;
	
	@PostMapping("/save")
	public ResponseResult<String> save(@RequestBody TxtContext context, HttpServletRequest request) {
		Integer memberId = CacheUtils.memberCache(request).getId();
		if(StringUtils.isEmpty(memberId)) {
			return new ResponseResult<>(SysRespConstants.TXT_SAVE_ERROR);
		}
		String text = context.getContext();
		if(StringUtils.isEmpty(text)) {
			return new ResponseResult<>(SysRespConstants.TXT_CONTEXT_ISNULL_ERROR);
		}
		Txt t = new Txt();
		Txt txt = getTxt(request);
		if(!StringUtils.isEmpty(txt)) {
			t.setId(txt.getId());
		}
		t.setMemberId(memberId);
		t.setContext(context.getContext());
		txtDao.save(t);
		return new ResponseResult<>(BaseRespMsg.EMPTY);
	}
	
	@GetMapping("/getContext")
	public ResponseResult<TxtContext> getContext(HttpServletRequest request) {
		TxtContext context = new TxtContext();
		Txt txt = getTxt(request);
		if(!StringUtils.isEmpty(txt)) {
			context.setId(txt.getId());
			context.setContext(txt.getContext());
		}
		return new ResponseResult<>(context);
	}

	private Txt getTxt(HttpServletRequest request) {
		Txt t = new Txt();
		t.setMemberId(CacheUtils.memberCache(request).getId());
		Example<Txt> example = Example.of(t);
		Optional<Txt> optxt = txtDao.findOne(example);
		if(optxt.isPresent()) {
			return optxt.get();
		}
		return null;
	}
}
