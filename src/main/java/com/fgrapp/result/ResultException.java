package com.fgrapp.result;

import lombok.Getter;

/**
 * @author fgr
 * @date 2022-11-01 20:41
 **/
@Getter
public class ResultException extends RuntimeException {

    /**
     * 业务异常信息信息
     */
    ResultStatus resultStatus;

    public ResultException() {
        this(ResultStatus.INTERNAL_SERVER_ERROR);
    }

    public ResultException(ResultStatus resultStatus) {
        super(resultStatus.getMessage());
        this.resultStatus = resultStatus;
    }

    public ResultException(String msg){
        super(msg);
        this.resultStatus = ResultStatus.INTERNAL_SERVER_ERROR;
    }
}

