package com.xingruyu.weather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * 网络连接类
 * Created by WDX on 2016/10/24.
 */

public class NetUtils {

    /**
     * 判断网络是否连接
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null){
            return false;
        }
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * GET方式获取数据
     * @param httpUrl
     * @return
     */
    public static String request(String httpUrl) {
        String result = null;
        try {
            URL url = new URL(httpUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            result = inputStreamToString(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * InputStream转String
     * @param inputStream
     * @return
     */
    public static String inputStreamToString(InputStream inputStream){
        StringBuffer sbf = new StringBuffer();
        BufferedReader reader = null;
        String result = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
