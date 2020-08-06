package com.mgtj.airadio.base.net.data;

import java.io.Serializable;

/**
 * @author PengLin
 * @desc
 */

public class DataResponse<T> implements Serializable {
    public String code;
    public String message;
    public T data;

    @Override
    public String toString() {
        return "DataResponse{" +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
