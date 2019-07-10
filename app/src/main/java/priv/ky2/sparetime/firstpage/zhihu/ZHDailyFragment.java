package priv.ky2.sparetime.firstpage.zhihu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;

import java.util.Calendar;

import priv.ky2.sparetime.R;
import priv.ky2.sparetime.adapter.MainPagerAdapter;
import priv.ky2.sparetime.adapter.OnRecyclerViewOnClickListener;
import priv.ky2.sparetime.adapter.ZHDailyAdapter;
import priv.ky2.sparetime.bean.ZhihuDailyNews;

/**
 * @author wangkaiyan
 * @date 2017/4/18.
 */
public class ZHDailyFragment extends Fragment implements ZHDailyContract.View {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefresh;
    private FloatingActionButton fab;
    private TabLayout mTabLayout;
    private ZHDailyAdapter mZHDailyAdapter;

    private int mYear = Calendar.getInstance().get(Calendar.YEAR);
    private int mMonth = Calendar.getInstance().get(Calendar.MONTH);
    private int mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    private ZHDailyContract.Presenter mPresenter;

    public ZHDailyFragment() {
    }

    public static ZHDailyFragment newInstance() {
        return new ZHDailyFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        initViews(view);
        mPresenter.start();
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mPresenter.refresh();
            }
        });
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 获取最后一个完全显示的item position
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();
                    // 判断是否滚动到底部并且是向下滑动
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        Calendar c = Calendar.getInstance();
                        c.set(mYear, mMonth, --mDay);
                        mPresenter.loadMore(c.getTimeInMillis());
                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isSlidingToLast = dy > 0;
                // 隐藏或者显示fab
                if (dy > 0) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });

        // 直接将豆瓣精选的fab点击事件放在知乎的部分
        // 因为fab是属于activity的view
        // 按通常的做法，在每个fragment中去设置监听事件会导致先前设置的listener失效
        // 尝试将监听放置到main pager adapter中，这样做会引起fragment中recycler view和fab的监听冲突
        // fab并不能获取到点击事件
        // 根据tab layout的位置选择显示不同的dialog
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTabLayout.getSelectedTabPosition() == 0) {
                    Calendar now = Calendar.getInstance();
                    now.set(mYear, mMonth, mDay);
                    DatePickerDialog dialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;
                            Calendar temp = Calendar.getInstance();
                            temp.clear();
                            temp.set(year, monthOfYear, dayOfMonth);
                            mPresenter.loadPosts(temp.getTimeInMillis(), true);
                        }
                    }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                    dialog.setMaxDate(Calendar.getInstance());
                    Calendar minDate = Calendar.getInstance();
                    // 2013.5.20是知乎日报api首次上线
                    minDate.set(2013, 5, 20);
                    dialog.setMinDate(minDate);
                    dialog.vibrate(false);
                    dialog.show(getActivity().getFragmentManager(), "DatePickerDialog");
                } else if (mTabLayout.getSelectedTabPosition() == 2) {
                    ViewPager p = getActivity().findViewById(R.id.view_pager);
                    MainPagerAdapter ad = (MainPagerAdapter) p.getAdapter();
                    ad.getDBFragment().showPickDialog();
                }
            }
        });

        return view;
    }

    @Override
    public void showError() {
        Snackbar.make(mRecyclerView, R.string.loaded_failed, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.refresh();
                    }
                })
                .show();
    }

    @Override
    public void showLoading() {
        mRefresh.post(new Runnable() {
            @Override
            public void run() {
                mRefresh.setRefreshing(true);
            }
        });
    }

    @Override
    public void stopLoading() {
        mRefresh.post(new Runnable() {
            @Override
            public void run() {
                mRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void showResults(ArrayList<ZhihuDailyNews.Question> list) {

        if (mZHDailyAdapter == null) {
            mZHDailyAdapter = new ZHDailyAdapter(getContext(), list);
            mZHDailyAdapter.setItemClickListener(new OnRecyclerViewOnClickListener() {
                @Override
                public void OnItemClick(View v, int position) {
                    mPresenter.startReading(position);
                }
            });
            mRecyclerView.setAdapter(mZHDailyAdapter);
        } else {
            mZHDailyAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setPresenter(ZHDailyContract.Presenter presenter) {
        if (presenter != null) {
            mPresenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRefresh = view.findViewById(R.id.refreshLayout);
        //设置下拉刷新的按钮的颜色
        mRefresh.setColorSchemeResources(R.color.colorPrimary);
        fab = getActivity().findViewById(R.id.fab);
        fab.setRippleColor(getResources().getColor(R.color.colorPrimaryDark));
        mTabLayout = getActivity().findViewById(R.id.tab_layout);
    }
}
