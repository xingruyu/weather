package com.xingruyu.weather.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.umeng.analytics.MobclickAgent;

import java.util.List;
import java.util.Stack;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 * Created by WDX on 2016/10/16.
 */

public class AppManagerUtils {

    private Stack<Activity> activityStack;
    private static AppManagerUtils instance;

    private AppManagerUtils() {
    }

    /**
     * 单一实例
     */
    public static AppManagerUtils getAppManager() {
        if (instance == null) {
            instance = new AppManagerUtils();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    @SuppressWarnings("deprecation")       //编译时不显示使用了不赞成使用的类或方法时的警告
    public void appExit(Context context) {
        MobclickAgent.onKillProcess(context);   //友盟统计
        try {
            finishAllActivity();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取应用程序版本信息
     * @param mContext
     * @return 当前应用的版本name、code
     */
    public String[] getVersionInfo(Context mContext) {
        try {
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            String[] versionInfo = new String[2];
            if (packageInfo != null){
                versionInfo[0] = packageInfo.versionName;
                versionInfo[1] = packageInfo.versionCode + "";
                return versionInfo;
            }
            return null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 当前app是前台还是后台运行
     * @param context
     * @return true为后台
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    //后台
                    return true;
                }else{
                    //前台
                    return false;
                }
            }
        }
        return false;
    }

}
