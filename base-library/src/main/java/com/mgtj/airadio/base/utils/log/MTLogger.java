package com.mgtj.airadio.base.utils.log;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MTLogger {

//    private final static boolean DEBUG = true;

    public static void init(boolean debug) {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(2)         // (Optional) How many method line to show. Default 2
                .methodOffset(1)        // (Optional) Hides internal method calls up to offset. Default 5
//                .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("MTLog")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return debug;
            }
        });
    }

    public static void v(@NonNull String message, Object... args) {
        Logger.v(message, args);
    }

    public static void i(@NonNull String message, Object... args) {
        Logger.i(message, args);
    }

    public static void d(@NonNull String message, Object... args) {
        Logger.d(message, args);
    }

    public static void w(@NonNull String message, Object... args) {
        Logger.w(message, args);
    }

    public static void e(@NonNull String message, @Nullable Object... args) {
        Logger.e(message, args);
    }

    public static void e(@Nullable Throwable throwable, @NonNull String message, @Nullable Object... args) {
        Logger.e(throwable, message, args);
    }

    public static void verbose(String tag, @NonNull String message, Object... args) {
        Logger.t(tag).v(message, args);
    }


    public static void debug(String tag, @NonNull String message, Object... args) {
        Logger.t(tag).d(message, args);
    }


    public static void info(String tag, @NonNull String message, Object... args) {
        Logger.t(tag).i(message, args);
    }


    public static void warn(String tag, @NonNull String message, Object... args) {
        Logger.t(tag).w(message, args);
    }

    public static void error(String tag, @NonNull String message, @Nullable Object... args) {
        Logger.t(tag).e(message, args);
    }

    public static void error(String tag, @Nullable Throwable throwable, @NonNull String message, @Nullable Object... args) {
        Logger.t(tag).e(throwable, message, args);
    }

    public static void json(String json) {
        Logger.json(json);
    }

    public static void json(String tag, String json) {
        Logger.t(tag).json(json);
    }

    public static void json(String tag, String msg, String json) {
        if (json == null || json.isEmpty()) {
            debug(tag, msg + "\n" + "Empty/Null json content");
            return;
        }
        try {
            json = json.trim();
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String message = jsonObject.toString(2);
                debug(tag, msg + "\n" + message);
                return;
            }
            if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String message = jsonArray.toString(2);
                debug(tag, msg + "\n" + message);
                return;
            }
            error(tag, msg + "\n" + "Invalid Json");
        } catch (JSONException e) {
            error(tag, msg + "\n" + "Invalid Json");
        }

    }

}
