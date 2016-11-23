package com.xingruyu.weather.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * 单位转换工具类
 * Created by WDX on 2016/10/21.
 */

public class DensityUtils {

    /**
     * dp转px
     * @param context
     * @param dpVal
     * @return
     */
    public static int dpTopx(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     * @param context
     * @param spVal
     * @return
     */
    public static int spTopx(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     * @param context
     * @param pxVal
     * @return
     */
    public static float pxTodp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     * @param context
     * @param pxVal
     * @return
     */
    public static float pxTosp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }
}
