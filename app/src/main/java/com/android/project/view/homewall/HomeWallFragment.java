package com.android.project.view.homewall;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.project.R;
import com.android.project.WhichOneApp;
import com.android.project.model.Record;
import com.android.project.model.User;
import com.android.project.view.detail.RecordDetailActivity;
import com.android.project.view.homewall.di.HomeWallModule;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeWallFragment extends Fragment implements HomeWallPresenter.View {

    private static final String TAG = HomeWallFragment.class.getSimpleName();

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.home_wall_recycler)
    RecyclerView recyclerView;

    @Inject
    User user;
    @Inject
    HomeWallPresenter.ActionListener presenter;
    @Inject
    HomeWallRecyclerViewAdapter recyclerViewAdapter;

    private String mRequestedUsername;
    private boolean refreshing = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WhichOneApp.getUserComponent()
                .plus(new HomeWallModule(this))
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_wall_fragment, container, false);

        ButterKnife.bind(this, view);
        showSwipeLayoutProgress();
        swipeLayout.setOnRefreshListener(getOnRefreshListener());

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle == null) {
            mRequestedUsername = user.getUsername();
        } else {
            mRequestedUsername = bundle.getString(getString(R.string.user_name_opened_page));
            presenter.loadLastRecords(mRequestedUsername, user.getUsername());
        }

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(recyclerViewAdapter);

        return view;
    }

    @Override
    public void updateRecords(List<Record> records) {
        recyclerViewAdapter.updateData(records);
    }

    @Override
    public void openRecordDetail(Long recordId) {
        Intent intent = new Intent(getContext(), RecordDetailActivity.class);
        intent.putExtra(RecordDetailActivity.RECORD_ID, recordId);
        startActivity(intent);
    }

    @Override
    public void clearHomeWall() {
        recyclerViewAdapter.cleanData();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.stop();
    }

    @Override
    public void showProgress() {
        if (!refreshing) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        if (refreshing) {
            refreshing = false;
        }

        swipeLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
    }


    public void loadRecords() {
        presenter.loadLastRecords(mRequestedUsername, user.getUsername());
    }

    @Override
    public void onError(Throwable throwable) {
        Toast.makeText(getContext(), getString(R.string.general_error), Toast.LENGTH_SHORT).show();
        throwable.printStackTrace();
    }

    public void showSwipeLayoutProgress() {
        swipeLayout.setRefreshing(true);
    }

    private SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return () -> {
                refreshing = true;
                clearHomeWall();
            presenter.loadLastRecords(mRequestedUsername, user.getUsername());
        };
    }
}
