package com.mgtj.airadio.base.utils.statusbar;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;


/**
 * 沉浸式、透明系统栏工具类
 *
 * @author PengLin
 * @project MyStock
 * @desc
 * @date 2017/3/3
 */

public class StatusBarHelper extends StatusBarUtil {


    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        System.out.println("statusBarHeight = " + statusBarHeight);
        return statusBarHeight;
    }

    /**
     * 获取标题栏高度
     *
     * @param context context
     * @return
     */
    public static int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
            // 输出结果：actionBarHeight = 168
            System.out.println("actionBarHeight = " + actionBarHeight);
            return actionBarHeight;
        }
        return 0;
    }

    /**
     * 获取导航栏高度
     *
     * @param context context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            int naviBarHeight = resources.getDimensionPixelSize(resourceId);
            System.out.println("naviBarHeight = " + naviBarHeight);
        }
        return 0;
    }


//    /**
//     * 计算状态栏颜色
//     *
//     * @param color color值
//     * @param alpha alpha值
//     * @return 最终的状态栏颜色
//     */
//    private static int calculateStatusColor(@ColorInt int color, int alpha) {
//        if (alpha == 0) {
//            return color;
//        }
//        float a = 1 - alpha / 255f;
//        int red = color >> 16 & 0xff;
//        int green = color >> 8 & 0xff;
//        int blue = color & 0xff;
//        red = (int) (red * a + 0.5);
//        green = (int) (green * a + 0.5);
//        blue = (int) (blue * a + 0.5);
//        return 0xff << 24 | red << 16 | green << 8 | blue;
//    }

    public static void fullscreen(Activity context) {
        int version = Build.VERSION.SDK_INT;
        Window window = context.getWindow();
        if (version >= Build.VERSION_CODES.KITKAT) {
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        } else {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        }
    }


}
