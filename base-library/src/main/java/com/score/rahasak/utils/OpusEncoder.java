package com.score.rahasak.utils;


import androidx.annotation.IntRange;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class OpusEncoder {
    public static final int OPUS_APPLICATION_AUDIO = 2049;
    public static final int OPUS_APPLICATION_RESTRICTED_LOWDELAY = 2051;
    public static final int OPUS_APPLICATION_VOIP = 2048;
    public static final int OPUS_AUTO = -1;
    public static final int OPUS_BITRATE_MAX = -1;
    public static final int OPUS_COMPLEXITY_MAX = 10;
    private long address;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ApplicationType {
    }

    private native int nativeEncodeBytes(byte[] bArr, int i, byte[] bArr2);

    private native int nativeEncodeShorts(short[] sArr, int i, byte[] bArr);

    private native int nativeInitEncoder(int i, int i2, int i3);

    private native boolean nativeReleaseEncoder();

    private native int nativeSetBitrate(int i);

    private native int nativeSetComplexity(@IntRange(from = 0, to = 10) int i);

    static {
        System.loadLibrary("senz");
    }

    public void init(int sampleRate, int channels, int application) {
        OpusError.throwIfError(nativeInitEncoder(sampleRate, channels, application));
    }

    public void setBitrate(int bitrate) {
        OpusError.throwIfError(nativeSetBitrate(bitrate));
    }

    public void setComplexity(int complexity) {
        OpusError.throwIfError(nativeSetComplexity(complexity));
    }

    public int encode(short[] buffer, int frames, byte[] out) {
        return OpusError.throwIfError(nativeEncodeShorts(buffer, frames, out));
    }

    public int encode(byte[] buffer, int frames, byte[] out) {
        return OpusError.throwIfError(nativeEncodeBytes(buffer, frames, out));
    }

    public void close() {
        nativeReleaseEncoder();
    }
}
