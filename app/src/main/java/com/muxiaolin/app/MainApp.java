package com.muxiaolin.app;


import androidx.multidex.MultiDexApplication;


/**
 * author : 彭林
 * date   : 2020/8/6
 * desc   :
 */
public class MainApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
//        LibInitApp.init(this, BuildConfig.DEBUG);
    }
}
