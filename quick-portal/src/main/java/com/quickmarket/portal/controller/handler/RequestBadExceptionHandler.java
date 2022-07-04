package com.quickmarket.portal.controller.handler;

import com.quickmarket.common.api.CommonResult;
import com.quickmarket.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class RequestBadExceptionHandler {

    /**
     * 兜底异常捕捉
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public CommonResult<String> ExepitonHandler(Exception e){
        if(e instanceof BusinessException){
            BusinessException businessException = (BusinessException)e;
        }
        return CommonResult.failed("请求错误:->"+e.getMessage());
    }

    //可自定义异常，执行捕捉

}
