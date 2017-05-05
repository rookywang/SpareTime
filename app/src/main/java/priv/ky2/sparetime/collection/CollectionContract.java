package priv.ky2.sparetime.collection;

import java.util.ArrayList;

import priv.ky2.sparetime.base.BasePresenter;
import priv.ky2.sparetime.base.BaseView;
import priv.ky2.sparetime.bean.BeanType;
import priv.ky2.sparetime.bean.DoubanMomentNews;
import priv.ky2.sparetime.bean.GuokeSelectionNews;
import priv.ky2.sparetime.bean.ZhihuDailyNews;

/**
 * Created by wangkaiyan on 2017/4/19.
 */

public interface CollectionContract {

    interface View extends BaseView<Presenter> {

        void showResults(ArrayList<ZhihuDailyNews.Question> zhihuList,
                         ArrayList<GuokeSelectionNews.result> guokrList,
                         ArrayList<DoubanMomentNews.posts> doubanList,
                         ArrayList<Integer> types);

        void notifyDataChanged();

        void showLoading();

        void stopLoading();

    }

    interface Presenter extends BasePresenter {

        void loadResults(boolean refresh);

        void startReading(BeanType type, int position);

        void checkForFreshData();

        void feelLucky();

    }
}
