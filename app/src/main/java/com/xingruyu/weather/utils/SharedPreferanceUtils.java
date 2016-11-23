package com.xingruyu.weather.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.xingruyu.weather.MyApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 本地键值对保存工具类
 * 对外公布出put，get，remove，clear等等方法；里面所有的commit操作使用了SharedPreferencesCompat.apply进行了替代，
 * 因为commit方法是同步的，apply时异步进行写入，毕竟是IO操作，尽可能异步；apply相当于commit来说是new API，为了更好的兼容，做了适配；
 * Created by WDX on 2016/10/21.
 */

public class SharedPreferanceUtils {

    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "share_data";

    /**
     * 保存数据
     * @param key
     * @param object
     */
    public static void put(String key, Object object) {
        SharedPreferences sp = MyApplication.mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 获取保存数据
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(String key, Object defaultObject) {
        SharedPreferences sp = MyApplication.mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    /**
     * 移除某个key值已经对应的值
     * @param key
     */
    public static void remove(String key) {
        SharedPreferences sp = MyApplication.mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     */
    public static void clear() {
        SharedPreferences sp = MyApplication.mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     * @param key
     * @return
     */
    public static boolean contains( String key) {
        SharedPreferences sp = MyApplication.mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     * @return
     */
    public static Map<String, ?> getAll() {
        SharedPreferences sp = MyApplication.mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         * @return
         */
        @SuppressWarnings({ "unchecked", "rawtypes" })
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }
            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }
}
