package com.fgrapp.result;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * @author fgr
 * @date 2022-11-01 20:39
 **/
@Getter
@ToString
public class Result<T> {
    /**
     * 业务错误码
     */
    private final Integer code;
    /**
     * 信息描述
     */
    private final String message;
    /**
     * 返回参数
     */
    private final T data;

    private Result(ResultStatus resultStatus, T data) {
        this.code = resultStatus.getCode();
        this.message = resultStatus.getMessage();
        this.data = data;
    }
    private Result(int code,String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

    /**
     * 业务成功返回业务代码和描述信息
     * @return Result
     */
    public static Result<Void> success() {
        return new Result<Void>(ResultStatus.SUCCESS, null);
    }

    /**
     * 业务成功返回业务代码,描述和返回的参数
     * @param data data
     * @param <T> data
     * @return Result
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(ResultStatus.SUCCESS, data);
    }

    /**
     * 业务成功返回业务代码,描述和返回的参数
     * @param resultStatus resultStatus
     * @param data data
     * @param <T> data
     * @return Result
     */
    public static <T> Result<T> success(ResultStatus resultStatus, T data) {
        if (resultStatus == null) {
            return success(data);
        }
        return new Result<T>(resultStatus, data);
    }

    /**
     * 业务异常返回业务代码和描述信息
     * @param <T> data
     * @return Result
     */
    public static <T> Result<T> failure() {
        return new Result<T>(ResultStatus.INTERNAL_SERVER_ERROR, null);
    }

    /**
     * 业务异常返回业务代码,描述和返回的参数
     * @param resultStatus resultStatus
     * @param <T> data
     * @return Result
     */
    public static <T> Result<T> failure(ResultStatus resultStatus) {
        return failure(resultStatus, null);
    }

    /**
     * 业务异常返回业务代码,描述和返回的参数
     * @param resultStatus resultStatus
     * @param data data
     * @param <T> data
     * @return Result
     */
    public static <T> Result<T> failure(ResultStatus resultStatus, T data) {
        if (resultStatus == null) {
            return new Result<T>(ResultStatus.INTERNAL_SERVER_ERROR, null);
        }
        return new Result<T>(resultStatus, data);
    }
    public static Result<Void> failure(int code, String msg) {
        return new Result<>(code,msg);
    }
    public static Result<Void> failure(String msg) {
        return new Result<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),msg);
    }
}

