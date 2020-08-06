package com.mgtj.airadio.base;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;
import com.mgtj.airadio.base.net.OkGoConfig;
import com.mgtj.airadio.base.utils.log.MTLogger;

/**
 * author : 彭林
 * date   : 2020/7/23
 * desc   :
 */
public final class LibInitApp {

    private static final String TAG = LibInitApp.class.getSimpleName();

    private static LibInitApp instance;
    private static Application sApp;
    private static boolean sDebug = false;

    private LibInitApp() {
    }


    public static LibInitApp getInstance() {
        if (instance == null) {
            instance = new LibInitApp();
        }
        return instance;
    }


    public static void init(final Application app, boolean debug) {
        if (app == null) {
            throw new NullPointerException("reflect failed.");
        }
        sDebug = debug;
        sApp = app;
        Utils.init(sApp);
        SPStaticUtils.setDefaultSPUtils(SPUtils.getInstance("mgtj-appRref"));
        LogUtils.getConfig().setGlobalTag("mgtj-tag");
        LogUtils.getConfig().setLogSwitch(debug);
        LogUtils.getConfig().setLogHeadSwitch(debug);
        LogUtils.getConfig().setLog2FileSwitch(debug);
        MTLogger.init(debug);
        //OKGo配置
        OkGoConfig config = new OkGoConfig.Builder()
                .retryCount(2)
                .build();
        config.okGoConfig();
    }


    public static boolean isDebug() {
        return sDebug;
    }

    @NonNull
    public static Context getContext() {
        if (sApp == null) {
            throw new NullPointerException("reflect failed.");
        }
        return sApp.getApplicationContext();
    }

    @NonNull
    public static Application getApplication() {
        if (sApp == null) {
            throw new NullPointerException("reflect failed.");
        }
        return sApp;
    }

}
