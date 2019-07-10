package priv.ky2.sparetime.content.douban;

import java.util.ArrayList;

import priv.ky2.sparetime.base.BasePresenter;
import priv.ky2.sparetime.base.BaseView;
import priv.ky2.sparetime.bean.DoubanMomentNews;

/**
 * @author wangkaiyan
 * @date 2017/4/19.
 */
public interface DBMomentContract {

    interface View extends BaseView<Presenter> {

        /**
         * 开始加载
         */
        void startLoading();

        /**
         * 停止加载
         */
        void stopLoading();

        /**
         * 加载失败
         */
        void showLoadingError();

        /**
         * 获取到数据后在界面上显示更新界面
         */
        void showResults(ArrayList<DoubanMomentNews.posts> list);

    }

    interface Presenter extends BasePresenter {

        /**
         * 显示文章详情
         */
        void startReading(int position);

        /**
         * 请求数据
         */
        void loadPosts(long date, boolean clearing);

        /**
         * 刷新文章数据
         */
        void refresh();

        /**
         * 加载更多文章
         */
        void loadMore(long date);

        /**
         * 随便看看
         */
        void feelLucky();

    }
}
