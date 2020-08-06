package com.mgtj.airadio.base.net.exception;

/**
 * @author PengLin
 * @desc
 */
public class BusinessException extends Exception {

    public static final int AuthError = 2;   //用户验证异常
    public static final int NoDataError = 3;   //无数据返回异常
    public static final int BusinessError = 4;   //业务异常
    public static final int FormatError = 5;   //返回格式异常

    private int type = BusinessError;

    public BusinessException() {
        super();
    }


    public BusinessException(Throwable exception, int type) {
        super(exception);
        this.type = type;
    }

    public BusinessException(String detailMessage, int type) {
        super(detailMessage);
        this.type = type;
    }


    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public int getType() {
        return type;
    }
}
