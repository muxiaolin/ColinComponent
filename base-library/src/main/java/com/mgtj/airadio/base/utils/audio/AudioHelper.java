package com.mgtj.airadio.base.utils.audio;

import android.util.Log;

import com.mgtj.airadio.base.LibInitApp;


/**
 * author : 彭林
 * date   : 2020/6/23
 * desc   :
 */
public class AudioHelper {
    private final String TAG = "AudioHelper";
    private static AudioHelper mInterface;

    public static AudioHelper getInstance() {
        if (mInterface == null) {
            mInterface = new AudioHelper();
        }
        return mInterface;
    }

    public void setCallVolume(int volumePercent) {
        int maxValue = AudioUtil.getCallMaxVolume(LibInitApp.getContext());
        int value = (int) Math.ceil((volumePercent) * maxValue * 0.01);
        value = Math.max(value, 0);
        value = Math.min(value, 100);
        AudioUtil.setCallVolume(LibInitApp.getContext(), value);
    }

    /**
     * 以0-100为范围，获取当前的音量值
     *
     * @return 获取当前的音量值
     */
    public int get100CallVolume() {
        int maxValue = AudioUtil.getCallMaxVolume(LibInitApp.getContext());
        int currValue = AudioUtil.getCallVolume(LibInitApp.getContext());
        return 100 * currValue / maxValue;
    }

    public void setMediaVolume(int volumePercent) {
        int maxValue = AudioUtil.getMediaMaxVolume(LibInitApp.getContext());
        int value = (int) Math.ceil((volumePercent) * maxValue * 0.01);
        value = Math.max(value, 0);
        value = Math.min(value, 100);
        AudioUtil.setMediaVolume(LibInitApp.getContext(), value);
    }

    /**
     * 以0-100为范围，获取当前的音量值
     *
     * @return 获取当前的音量值
     */
    public int get100MediaVolume() {
        int maxValue = AudioUtil.getMediaMaxVolume(LibInitApp.getContext());
        int currValue = AudioUtil.getMediaVolume(LibInitApp.getContext());
        return 100 * currValue / maxValue;
    }

    public void setMediaVolumeToMaximum() {
        AudioUtil.setMediaVolumeToMaximum(LibInitApp.getContext());
    }

    public void setSystemVolumeToMaximum() {
        AudioUtil.setSystemVolumeToMaximum(LibInitApp.getContext());
    }


    public void setSpeakerStatus(boolean z) {
        //
        if (AudioUtil.isWiredHeadsetOn(LibInitApp.getContext())) {
            AudioUtil.setSpeakerStatus(LibInitApp.getContext(), false);
            return;
        }
        AudioUtil.setSpeakerStatus(LibInitApp.getContext(), z);
        if (z) {
            Log.i(TAG, "setSpeakerStatus: 打开扬声器!");
        } else {
            Log.i(TAG, "setSpeakerStatus: 关闭扬声器!");
        }
    }


}
