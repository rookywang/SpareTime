package priv.ky2.sparetime.systemsettings;

import android.support.v7.preference.Preference;

import priv.ky2.sparetime.base.BasePresenter;
import priv.ky2.sparetime.base.BaseView;

/**
 * Created by wangkaiyan on 2017/5/1.
 */

public interface SystemSettingsContract {

    interface View extends BaseView<Presenter> {

        //清除缓存后提示
        void showCleanGlideCacheDone();

    }

    interface Presenter extends BasePresenter {

        //设置文章有无图片
        void setNoPictureMode(Preference preference);

        //使用
        void setInAppBrowser(Preference preference);

        void cleanGlideCache();

        void setTimeOfSavingArticles(Preference preference, Object newValue);

        String getTimeSummary();

    }
}
