package android.jlu.com.municipalmanage.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharePreference封装
 */

public class PreferenceUtils {

    //保存在手机里的文件名
    public static final String PREF_NAME = "config";

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        //获得SharedPreferences对象
        SharedPreferences sp = context.
                getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    public static void setBoolean(Context context, String key, boolean value) {

        SharedPreferences sp = context.
                getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static String getString(Context context, String key, String defaultValue) {

        SharedPreferences sp = context.
                getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);

    }

    public static void setString(Context context, String key, String value) {

        SharedPreferences sp = context.
                getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }


}
