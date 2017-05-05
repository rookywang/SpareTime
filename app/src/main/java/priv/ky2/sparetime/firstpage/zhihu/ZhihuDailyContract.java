package priv.ky2.sparetime.firstpage.zhihu;

import java.util.ArrayList;

import priv.ky2.sparetime.base.BasePresenter;
import priv.ky2.sparetime.base.BaseView;
import priv.ky2.sparetime.bean.ZhihuDailyNews;

/**
 * Created by wangkaiyan on 2017/4/18.
 */

public interface ZhihuDailyContract {

    interface View extends BaseView<Presenter> {

        // 显示加载或者显示错误
        void showError();

        // 显示正在加载
        void showLoading();

        // 停止显示正在加载
        void stopLoading();

        // 成功获取到数据后，在界面中显示
        void showResults(ArrayList<ZhihuDailyNews.Question> list);

    }

    interface Presenter extends BasePresenter {

        // 请求数据
        void loadPosts(long date, boolean clearing);

        // 刷新数居
        void refresh();

        // 加载更多文章
        void loadMore(long date);

        // 显示文章详情
        void startReading(int position);

        // 随便看看
        void feelLucky();

    }
}
