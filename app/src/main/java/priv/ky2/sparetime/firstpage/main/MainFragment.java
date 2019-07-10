package priv.ky2.sparetime.firstpage.main;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

import priv.ky2.sparetime.R;
import priv.ky2.sparetime.adapter.MainPagerAdapter;
import priv.ky2.sparetime.firstpage.douban.DoubanMomentFragment;
import priv.ky2.sparetime.firstpage.douban.DoubanMomentPresenter;
import priv.ky2.sparetime.firstpage.guoke.GuokeSelectionFragment;
import priv.ky2.sparetime.firstpage.guoke.GuokeSelectionPresenter;
import priv.ky2.sparetime.firstpage.zhihu.ZHDailyFragment;
import priv.ky2.sparetime.firstpage.zhihu.ZHDailyPresenter;


public class MainFragment extends Fragment {

    private Context mContext;
    private MainPagerAdapter mMainPagerAdapter;

    private TabLayout mTabLayout;

    private ZHDailyFragment mZHDailyFragment;
    private GuokeSelectionFragment mGuokeSelectionFragment;
    private DoubanMomentFragment mDoubanMomentFragment;

    private ZHDailyPresenter mZHDailyPresenter;
    private GuokeSelectionPresenter mGuokeSelectionPresenter;
    private DoubanMomentPresenter mDoubanMomentPresenter;

    public MainFragment() {

    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();

        if (savedInstanceState != null) {
            FragmentManager fragmentManager = getChildFragmentManager();
            mZHDailyFragment = (ZHDailyFragment) fragmentManager.getFragment(savedInstanceState, "zhihu");
            mGuokeSelectionFragment = (GuokeSelectionFragment) fragmentManager.getFragment(savedInstanceState, "guoke");
            mDoubanMomentFragment = (DoubanMomentFragment) fragmentManager.getFragment(savedInstanceState, "douban");
        } else {
            mZHDailyFragment = ZHDailyFragment.newInstance();
            mGuokeSelectionFragment = GuokeSelectionFragment.newInstance();
            mDoubanMomentFragment = DoubanMomentFragment.newInstance();
        }

        mZHDailyPresenter = new ZHDailyPresenter(mContext, mZHDailyFragment);
        mGuokeSelectionPresenter = new GuokeSelectionPresenter(mContext, mGuokeSelectionFragment);
        mDoubanMomentPresenter = new DoubanMomentPresenter(mContext, mDoubanMomentFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        initViews(view);

        setHasOptionsMenu(true);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //由于果壳精选没有选择日期，故隐藏
                FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
                if (tab.getPosition() == 1) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    private void initViews(View view) {
        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(3);

        mMainPagerAdapter = new MainPagerAdapter(
                getChildFragmentManager(),
                mContext,
                mZHDailyFragment,
                mGuokeSelectionFragment,
                mDoubanMomentFragment);

        viewPager.setAdapter(mMainPagerAdapter);
        mTabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_feel_lucky);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_feel_lucky :
                feelLucky();
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentManager manager = getChildFragmentManager();
        manager.putFragment(outState, "zhihu", mZHDailyFragment);
        manager.putFragment(outState, "guoke", mGuokeSelectionFragment);
        manager.putFragment(outState, "douban", mDoubanMomentFragment);
    }

    public void feelLucky() {
        Random random = new Random();
        int type = random.nextInt(3);
        switch (type) {
            case 0:
                mZHDailyPresenter.feelLucky();
                break;
            case 1:
                mGuokeSelectionPresenter.feelLucky();
                break;
            default:
                mDoubanMomentPresenter.feelLucky();
                break;
        }
    }
}
