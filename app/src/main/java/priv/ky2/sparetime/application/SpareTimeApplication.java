package priv.ky2.sparetime.application;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import priv.ky2.sparetime.utils.SharedPreferenceUtil;

/**
 * Created by wangkaiyan on 2017/4/18.
 */

public class SpareTimeApplication extends Application {

    public static SpareTimeApplication sSpareTimeApplication;

    @Override
    public void onCreate() {
        super.onCreate();

        sSpareTimeApplication = this;

        SharedPreferenceUtil.init();

        // 0 代表日间模式，1 代表夜间模式
        if (getSharedPreferences("user_settings",MODE_PRIVATE).getInt("theme", 0) == 0) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    public static SpareTimeApplication getApplication(){
        return sSpareTimeApplication;
    }
}
