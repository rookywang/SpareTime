package priv.ky2.sparetime.firstpage.guoke;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import priv.ky2.sparetime.R;
import priv.ky2.sparetime.adapter.GuokeNewsAdapter;
import priv.ky2.sparetime.adapter.OnRecyclerViewOnClickListener;
import priv.ky2.sparetime.bean.GuokeSelectionNews;


public class GuokeSelectionFragment extends Fragment implements GuokeSelectionContract.View {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private GuokeNewsAdapter adapter;
    private GuokeSelectionContract.Presenter presenter;

    public GuokeSelectionFragment(){

    }

    public static GuokeSelectionFragment newInstance() {
        return new GuokeSelectionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list,container,false);

        initViews(view);

        presenter.start();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refresh();
            }
        });

        return view;
    }

    @Override
    public void setPresenter(GuokeSelectionContract.Presenter presenter) {
        if (presenter != null){
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        //设置下拉刷新的按钮的颜色
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);

    }

    @Override
    public void showError() {
        Snackbar.make(recyclerView, R.string.loaded_failed,Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.refresh();
                    }
                })
                .show();
    }

    @Override
    public void showResults(ArrayList<GuokeSelectionNews.result> list) {
        if (adapter == null) {
            adapter = new GuokeNewsAdapter(getContext(), list);
            adapter.setItemClickListener(new OnRecyclerViewOnClickListener() {
                @Override
                public void OnItemClick(View v, int position) {
                    presenter.startReading(position);
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

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
