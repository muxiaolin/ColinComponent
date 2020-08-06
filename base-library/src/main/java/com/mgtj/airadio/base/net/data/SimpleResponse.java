package com.mgtj.airadio.base.net.data;

import java.io.Serializable;

/**
 * @author PengLin
 * @desc
 */

public class SimpleResponse implements Serializable {
    public String code;
    public String message;

    public DataResponse toDataResponse() {
        DataResponse response = new DataResponse();
        response.code = code;
        response.message = message;
        return response;
    }
}
