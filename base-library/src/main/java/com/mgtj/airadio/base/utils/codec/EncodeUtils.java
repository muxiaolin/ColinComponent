package com.mgtj.airadio.base.utils.codec;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * @author PengLin
 * @project hroa-android-v3
 * @desc
 * @date 2018/5/21
 */
public final class EncodeUtils {
    public static final Charset ASCII = Charset.forName("US-ASCII");

    private EncodeUtils() {
    }

    public static String getString(byte[] data, int offset, int length, String charset) {
        try {
            return new String(data, offset, length, charset);
        } catch (UnsupportedEncodingException var5) {
            return new String(data, offset, length);
        }
    }

    public static String getString(byte[] data, String charset) {
        return getString(data, 0, data.length, charset);
    }

    public static byte[] getBytes(String data, String charset) {
        try {
            return data.getBytes(charset);
        } catch (UnsupportedEncodingException var3) {
            return data.getBytes();
        }
    }

    public static byte[] getAsciiBytes(String data) {
        try {
            return data.getBytes(ASCII.name());
        } catch (UnsupportedEncodingException var2) {
            throw new Error("ASCII not supported");
        }
    }

    public static String getAsciiString(byte[] data, int offset, int length) {
        try {
            return new String(data, offset, length, ASCII.name());
        } catch (UnsupportedEncodingException var4) {
            throw new Error("ASCII not supported");
        }
    }

    public static String getAsciiString(byte[] data) {
        return getAsciiString(data, 0, data.length);
    }

    public static String URLEncoder(String encodeContent) {
        String content = encodeContent;
        if (!TextUtils.isEmpty(encodeContent)) {
            try {
                content = URLEncoder.encode(encodeContent, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

}