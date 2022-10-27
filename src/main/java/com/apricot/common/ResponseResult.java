package com.apricot.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 统一返回格式
 * @author rengq
 * @version v0.0.1
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ResponseResult<T> extends BaseRespMsg {

    private static final long serialVersionUID = 1L;

    private T data;
    

    public ResponseResult() {
    }
    
    public ResponseResult(SysRespConstants defaultSysErrorRespCode) {
    	this.respCode = defaultSysErrorRespCode.getCode();
        this.memo = defaultSysErrorRespCode.getMessage();
    }

    public ResponseResult(SysRespConstants defaultSysErrorRespCode, String message) {
    	this.respCode = defaultSysErrorRespCode.getCode();
        this.memo = message;
    }

    public ResponseResult(String respCode, String memo) {
        this.respCode = respCode;
        this.memo = memo;
    }

    public ResponseResult(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return DEFAULT_SUCCESS_RESP_CODE.equals(getRespCode());
    }

}

