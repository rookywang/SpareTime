package priv.ky2.sparetime.base;

import android.view.View;

/**
 * Created by wangkaiyan on 2017/4/18.
 */

public interface BaseView<T> {

    //为View设置Presenter
    void setPresenter(T presenter);

    //初始化界面控件
    void initViews(View view);
}
