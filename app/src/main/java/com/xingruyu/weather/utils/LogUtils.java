package com.xingruyu.weather.utils;

import android.util.Log;

/**
 * 日子工具类
 * 根据传入的mContext等获取类名
 * Created by WDX on 2016/10/21.
 */

public class LogUtils {

    public static boolean isDebug = true;     //在application中设置是否开启打印log

    public static void i(String tag, String msg, boolean isOpen) {
        if (isDebug && isOpen)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg, boolean isOpen) {
        if (isDebug && isOpen)
            Log.i(tag, msg);
    }

    public static void e(String tag, String msg, boolean isOpen) {
        if (isDebug && isOpen)
            Log.i(tag, msg);
    }

    public static void v(String tag, String msg, boolean isOpen) {
        if (isDebug && isOpen)
            Log.i(tag, msg);
    }
}
