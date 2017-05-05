package priv.ky2.sparetime.firstpage.guoke;

import java.util.ArrayList;

import priv.ky2.sparetime.base.BasePresenter;
import priv.ky2.sparetime.base.BaseView;
import priv.ky2.sparetime.bean.GuokeSelectionNews;

/**
 * Created by wangkaiyan on 2017/4/19.
 */

public interface GuokeSelectionContract {

    interface View extends BaseView<Presenter> {

        void showError();

        void showResults(ArrayList<GuokeSelectionNews.result> list);

        void showLoading();

        void stopLoading();

    }

    interface Presenter extends BasePresenter {

        void loadPosts();

        void refresh();

        void startReading(int position);

        void feelLucky();

    }
}
