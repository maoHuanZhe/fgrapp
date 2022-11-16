package com.fgrapp.result;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * @author fgr
 * @date 2022-11-01 20:36
 **/
@ToString
@Getter
public enum ResultStatus {

    /**
     *
     */
    SUCCESS(HttpStatus.OK, 200, "OK"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 400, "Bad Request"),
    NOT_LOGIN(HttpStatus.UNAUTHORIZED, 401, "Not Login"),
    AUTH_ERROR(HttpStatus.UNAUTHORIZED, 402, "AUTH ERROR"),
    PARAMS_ERROR(HttpStatus.UNAUTHORIZED, 403, "PARAMS ERROR"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "Internal Server Error"),;

    /**
     * 返回的HTTP状态码,  符合http请求
     */
    private final HttpStatus httpStatus;
    /**
     * 业务异常码
     */
    private final Integer code;
    /**
     * 业务异常信息描述
     */
    private final String message;

    ResultStatus(HttpStatus httpStatus, Integer code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}

