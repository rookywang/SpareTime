package priv.ky2.sparetime.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import priv.ky2.sparetime.R;
import priv.ky2.sparetime.content.douban.DBMomentFragment;
import priv.ky2.sparetime.content.guoke.GKSelectionFragment;
import priv.ky2.sparetime.content.zhihu.ZHDailyFragment;

/**
 * @author wangkaiyan
 * @date 2017/4/19.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {


    private String[] titles;
    private ZHDailyFragment mZHFragment;
    private GKSelectionFragment mGKFragment;
    private DBMomentFragment mDBFragment;

    public GKSelectionFragment getGKFragment() {
        return mGKFragment;
    }

    public ZHDailyFragment getZHFragment() {
        return mZHFragment;
    }

    public DBMomentFragment getDBFragment() {
        return mDBFragment;
    }

    public MainPagerAdapter(FragmentManager fm,
                            Context context,
                            ZHDailyFragment mZHDailyFragment,
                            GKSelectionFragment mGKSelectionFragment,
                            DBMomentFragment mDBMomentFragment) {
        super(fm);
        titles = new String[]{
                context.getResources().getString(R.string.zhihu_daily),
                context.getResources().getString(R.string.guokr_handpick),
                context.getResources().getString(R.string.douban_moment)
        };
        this.mZHFragment = mZHDailyFragment;
        mGKFragment = mGKSelectionFragment;
        mDBFragment = mDBMomentFragment;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return mGKFragment;
        } else if (position == 2) {
            return mDBFragment;
        }
        return mZHFragment;
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
