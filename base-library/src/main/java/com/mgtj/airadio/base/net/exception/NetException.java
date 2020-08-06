package com.mgtj.airadio.base.net.exception;

import java.io.IOException;

/**
 * @author PengLin
 * @desc
 */
public class NetException extends IOException {

    public static final int NetworkError = 1;   //网络异常
    public static final int NetworkNOconnect = 1;   //网络未连接

    private int type = NetworkError;

    public NetException() {
        super();
    }

    public NetException(Throwable exception, int type) {
        super(exception);
        this.type = type;
    }

    public NetException(String detailMessage, int type) {
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
