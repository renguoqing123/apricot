package com.apricot.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @version 0.0.1
 */
@Data
public class BaseRespMsg implements Serializable {

    private static final long serialVersionUID = 801850187875714324L;

    public static final String DEFAULT_SUCCESS_RESP_CODE = "00000";
    public static final String DEFAULT_SUCCESS_RESP_MEMO = "成功";
    public static final String EMPTY = "";

    public String respCode = DEFAULT_SUCCESS_RESP_CODE;//返回码

    public String memo = DEFAULT_SUCCESS_RESP_MEMO;//返回结果
    

    public static final String ERROR = "error";//返回结果

}
