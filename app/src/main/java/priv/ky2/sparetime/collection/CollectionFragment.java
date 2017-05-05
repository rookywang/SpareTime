package priv.ky2.sparetime.collection;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import priv.ky2.sparetime.R;
import priv.ky2.sparetime.adapter.CollectionAdapter;
import priv.ky2.sparetime.adapter.OnRecyclerViewOnClickListener;
import priv.ky2.sparetime.bean.BeanType;
import priv.ky2.sparetime.bean.DoubanMomentNews;
import priv.ky2.sparetime.bean.GuokeSelectionNews;
import priv.ky2.sparetime.bean.ZhihuDailyNews;
import priv.ky2.sparetime.search.SearchActivity;


public class CollectionFragment extends Fragment implements CollectionContract.View {


    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private CollectionAdapter adapter;
    private CollectionContract.Presenter presenter;

    public CollectionFragment() {}

    public static CollectionFragment newInstance() {
        return new CollectionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        initViews(view);

        setHasOptionsMenu(true);

        presenter.loadResults(false);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadResults(true);
            }
        });

        return view;
    }

    @Override
    public void setPresenter(CollectionContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bookmarks, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            startActivity(new Intent(getActivity(), SearchActivity.class));
            Logger.d("搜索SearchActivity");
        } else if (id == R.id.action_feel_lucky) {
            presenter.feelLucky();
        }
        return true;
    }

    @Override
    public void showResults(ArrayList<ZhihuDailyNews.Question> zhihuList,
                            ArrayList<GuokeSelectionNews.result> guokrList,
                            ArrayList<DoubanMomentNews.posts> doubanList,
                            ArrayList<Integer> types) {

        if (adapter == null) {

            adapter = new CollectionAdapter(getActivity(), zhihuList, guokrList, doubanList, types);
            adapter.setItemListener(new OnRecyclerViewOnClickListener() {
                @Override
                public void OnItemClick(View v, int position) {
                    int type = recyclerView.findViewHolderForLayoutPosition(position).getItemViewType();
                    if (type == CollectionAdapter.TYPE_ZHIHU_NORMAL) {
                        presenter.startReading(BeanType.TYPE_ZHIHU, position);
                    } else if (type == CollectionAdapter.TYPE_GUOKR_NORMAL) {
                        presenter.startReading(BeanType.TYPE_GUOKE, position);
                    } else if (type == CollectionAdapter.TYPE_DOUBAN_NORMAL) {
                        presenter.startReading(BeanType.TYPE_DOUBAN, position);
                    }
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void notifyDataChanged() {
        presenter.loadResults(true);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void stopLoading() {
        refreshLayout.setRefreshing(false);
    }
}
