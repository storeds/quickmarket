package com.quickmarket.common.exception;



import com.quickmarket.common.api.IErrorCode;
import lombok.Data;

/**
* @desc: 类的描述:网关异常类
* @createDate: 2022/1/22 10:52
* @version: 1.0
*/
@Data
public class GateWayException extends RuntimeException{

    private long code;

    private String message;

    public GateWayException(IErrorCode iErrorCode) {
        this.code = iErrorCode.getCode();
        this.message = iErrorCode.getMessage();
    }
}
