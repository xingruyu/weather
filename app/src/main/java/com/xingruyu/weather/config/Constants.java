package com.xingruyu.weather.config;

/**
 * 全局常量类
 * Created by WDX on 2016/10/21.
 */

public class Constants {

    //定位时间间隔,10分钟定位一次
    public static final int LOCATION_SPAN = 1000 * 60 * 10;
    //天气信息接口地址
//    public static final String WEATHER_URL = "https://api.heweather.com/x3/weather";
    public static final String WEATHER_URL = "https://free-api.heweather.com/v5/weather";
    //天气key
    public static final String WEATHER_KEY = "3f187b92d785401a94e3e3e655d101d5";
    //更新主界面视图的广播
    public static final String UPDATE_MAIN_VIEW_ACTION = "android.intent.action.update_main_view";
    //定位状态的广播
    public static final String LOCATION_STATE = "android.intent.action.location_state";


    //默认城市
    public static final String DefaultCity = "DEFAULTCITYCODE";
    //定位城市
    public static final String LocationCity = "LOCATIONCITYCODE";
    //第一次使用
    public static final String firstUse = "FIRSTUSE";
}
