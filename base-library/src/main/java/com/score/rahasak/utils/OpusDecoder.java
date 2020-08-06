package com.score.rahasak.utils;

public class OpusDecoder {
    private long address;

    private native int nativeDecodeBytes(byte[] bArr, byte[] bArr2, int i);

    private native int nativeDecodeShorts(byte[] bArr, short[] sArr, int i);

    private native int nativeInitDecoder(int i, int i2);

    private native boolean nativeReleaseDecoder();

    static {
        System.loadLibrary("senz");
    }

    public void init(int sampleRate, int channels) {
        OpusError.throwIfError(nativeInitDecoder(sampleRate, channels));
    }

    public int decode(byte[] encodedBuffer, short[] buffer, int frames) {
        return OpusError.throwIfError(nativeDecodeShorts(encodedBuffer, buffer, frames));
    }

    public int decode(byte[] encodedBuffer, byte[] buffer, int frames) {
        return OpusError.throwIfError(nativeDecodeBytes(encodedBuffer, buffer, frames));
    }

    public void close() {
        nativeReleaseDecoder();
    }
}
