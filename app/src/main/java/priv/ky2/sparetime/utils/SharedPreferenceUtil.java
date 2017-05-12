package priv.ky2.sparetime.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import priv.ky2.sparetime.application.SpareTimeApplication;

public class SharedPreferenceUtil {

    private static String PREDERENCE_NAME = "SharedPreferenceUtil";


    public static void init() {
        PREDERENCE_NAME = SpareTimeApplication.getApplication().getPackageName();
    }

    private static Context getContext() {
        return SpareTimeApplication.getApplication();
    }

    public static void setBoolean(String key, Boolean value) {
        SharedPreferences sp = getContext().getSharedPreferences(PREDERENCE_NAME, 0);

        if (value != sp.getBoolean(key, false)) {
            Editor editor = sp.edit();
            editor.putBoolean(key, value);
            editor.commit();
        }
    }

    public static boolean getBoolean(String key, Boolean defaultValue) {
        SharedPreferences sp = getContext().getSharedPreferences(PREDERENCE_NAME, 0);
        return sp.getBoolean(key, defaultValue);
    }

    public static void setString(String key, String value) {
        SharedPreferences sp = getContext().getSharedPreferences(PREDERENCE_NAME, 0);

        if (!sp.getString(key, "").equals(value)) {
            Editor editor = sp.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    public static String getString(String key, String defaultValue) {
        SharedPreferences sp = getContext().getSharedPreferences(PREDERENCE_NAME, 0);
        return sp.getString(key, defaultValue);
    }

    public static void setLong(String key, Long value) {
        SharedPreferences sp = getContext().getSharedPreferences(PREDERENCE_NAME, 0);

        if (value != sp.getLong(key, 0xFF)) {
            Editor editor = sp.edit();
            editor.putLong(key, value);
            editor.commit();
        }
    }

    public static long getLong(String key, Long defaultValue) {
        SharedPreferences sp = getContext().getSharedPreferences(PREDERENCE_NAME, 0);
        return sp.getLong(key, defaultValue);
    }

    public static void setInt(String key, int value) {
        SharedPreferences sp = getContext().getSharedPreferences(PREDERENCE_NAME, 0);
        if (value != sp.getInt(key, 0xFF)) {
            Editor editor = sp.edit();
            editor.putInt(key, value);
            editor.commit();
        }
    }

    public static int getInt(String key, int defaultValue) {
        SharedPreferences sp = getContext().getSharedPreferences(PREDERENCE_NAME, 0);
        return sp.getInt(key, defaultValue);
    }

    public static void setOnSharedPreferenceChangeListener(
            OnSharedPreferenceChangeListener listener) {
        SharedPreferences sp = getContext().getSharedPreferences(PREDERENCE_NAME, 0);
        sp.registerOnSharedPreferenceChangeListener(listener);
    }

    public static void clearOnSharedPreferenceChangeListener(
            OnSharedPreferenceChangeListener listener) {
        SharedPreferences sp = getContext().getSharedPreferences(PREDERENCE_NAME, 0);
        sp.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
