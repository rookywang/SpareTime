package priv.ky2.sparetime.firstpage.douban;

import java.util.ArrayList;

import priv.ky2.sparetime.base.BasePresenter;
import priv.ky2.sparetime.base.BaseView;
import priv.ky2.sparetime.bean.DoubanMomentNews;

/**
 * Created by wangkaiyan on 2017/4/19.
 */

public interface DoubanMomentContract {

    interface View extends BaseView<Presenter> {

        void startLoading();

        void stopLoading();

        void showLoadingError();

        void showResults(ArrayList<DoubanMomentNews.posts> list);

    }

    interface Presenter extends BasePresenter {

        void startReading(int position);

        void loadPosts(long date, boolean clearing);

        void refresh();

        void loadMore(long date);

        void feelLucky();

    }
}
