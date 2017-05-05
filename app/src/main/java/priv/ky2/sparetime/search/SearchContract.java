package priv.ky2.sparetime.search;

import java.util.ArrayList;

import priv.ky2.sparetime.base.BasePresenter;
import priv.ky2.sparetime.base.BaseView;
import priv.ky2.sparetime.bean.BeanType;
import priv.ky2.sparetime.bean.DoubanMomentNews;
import priv.ky2.sparetime.bean.GuokeSelectionNews;
import priv.ky2.sparetime.bean.ZhihuDailyNews;

/**
 * Created by wangkaiyan on 2017/5/1.
 */

public interface SearchContract {

    interface View extends BaseView<Presenter> {

        void showResults(ArrayList<ZhihuDailyNews.Question> zhihuList,
                         ArrayList<GuokeSelectionNews.result> guokrList,
                         ArrayList<DoubanMomentNews.posts> doubanList,
                         ArrayList<Integer> types);

        void showLoading();

        void stopLoading();

    }

    interface Presenter extends BasePresenter {

        void loadResults(String queryWords);

        void startReading(BeanType type, int position);

    }
}
