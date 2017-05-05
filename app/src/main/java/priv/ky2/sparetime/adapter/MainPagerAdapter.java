package priv.ky2.sparetime.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import priv.ky2.sparetime.R;
import priv.ky2.sparetime.firstpage.douban.DoubanMomentFragment;
import priv.ky2.sparetime.firstpage.guoke.GuokeSelectionFragment;
import priv.ky2.sparetime.firstpage.zhihu.ZhihuDailyFragment;

/**
 * Created by wangkaiyan on 2017/4/19.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {


    private String[] titles;
    private final Context context;

    private ZhihuDailyFragment mZhihuDailyFragment;
    private GuokeSelectionFragment mGuokeSelectionFragment;
    private DoubanMomentFragment mDoubanFragment;

    public GuokeSelectionFragment getGuokrFragment() {
        return mGuokeSelectionFragment;
    }

    public ZhihuDailyFragment getZhihuFragment() {
        return mZhihuDailyFragment;
    }

    public DoubanMomentFragment getDoubanFragment() {
        return mDoubanFragment;
    }


    public MainPagerAdapter(FragmentManager fm,
                            Context context,
                            ZhihuDailyFragment zhihuDailyFragment,
                            GuokeSelectionFragment guokeSelectionFragment,
                            DoubanMomentFragment doubanMomentFragment) {
        super(fm);

        this.context = context;
        titles = new String[]{
                context.getResources().getString(R.string.zhihu_daily),
                context.getResources().getString(R.string.guokr_handpick),
                context.getResources().getString(R.string.douban_moment)
        };
        mZhihuDailyFragment = zhihuDailyFragment;
        mGuokeSelectionFragment = guokeSelectionFragment;
        mDoubanFragment = doubanMomentFragment;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return mGuokeSelectionFragment;
        } else if (position == 2) {
            return mDoubanFragment;
        }
        return mZhihuDailyFragment;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
