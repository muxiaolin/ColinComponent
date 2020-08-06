package com.mgtj.airadio.base.net.exception;

import java.io.IOException;

/**
 * @author PengLin
 * @desc
 */
public class ParserException extends IOException {

    public ParserException() {
        super();
    }

    public ParserException(Throwable exception) {
        super(exception);
    }

    public ParserException(String detailMessage) {
        super(detailMessage);
    }


    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
