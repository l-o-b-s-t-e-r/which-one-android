package com.android.project.activities.wall;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.project.R;
import com.android.project.activities.detail.RecordDetailActivity;
import com.android.project.activities.userpage.UserPageActivity;
import com.android.project.model.Record;

import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class WallFragment extends Fragment implements WallPresenter.View {

    public static final String RECORD_ID = "RECORD_ID";
    private static final String TAG = WallFragment.class.getSimpleName();

    @BindView(R.id.main_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private String mTargetUsername;
    private WallPresenter.ActionListener mActionListener;
    private WallRecyclerViewAdapter mRecyclerViewAdapter;
    private SharedPreferences mSharedPreferences;

    public static Fragment newInstance(TabLayout tabLayout) {
        WallFragment fragment = new WallFragment();
        tabLayout.addTab(tabLayout.newTab());

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mActionListener = new WallPresenterImpl(getTargetUsername(), this);
        mRecyclerViewAdapter = new WallRecyclerViewAdapter(mActionListener, getTargetUsername());


        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wall_fragment, container, false);
        ButterKnife.bind(this, view);
        showSwipeLayoutProgress();
        swipeLayout.setOnRefreshListener(getRefreshListener());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mRecyclerViewAdapter);

        mActionListener.loadLastRecords();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshVotedRecords();
    }

    @Override
    public void updateRecord(Integer position, Record record) {
        mRecyclerViewAdapter.updateRecord(position, record);
    }

    public void updateWall() {
        mActionListener.loadLastRecords();
    }

    @Override
    public void showRecords(List<Record> records) {
        mRecyclerViewAdapter.updateData(records);
    }

    @Override
    public void showRecordDetail(Long recordId) {
        Intent intent = new Intent(getContext(), RecordDetailActivity.class);
        intent.putExtra(RecordDetailActivity.RECORD_ID, recordId);
        startActivity(intent);
    }

    @Override
    public void showUserPage(String userName) {
        Intent intent = new Intent(getContext(), UserPageActivity.class);
        intent.putExtra(getString(R.string.user_name_opened_page), userName);
        startActivity(intent);
    }

    @Override
    public void clearWall() {
        mRecyclerViewAdapter.cleanData();
    }

    @Override
    public void onStop() {
        super.onStop();
        mActionListener.onStop();
        mRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        swipeLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
    }

    public void showSwipeLayoutProgress() {
        swipeLayout.setRefreshing(true);
    }

    private String getTargetUsername() {
        if (mTargetUsername == null) {
            mTargetUsername = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getString(R.string.user_name), "");
        }

        return mTargetUsername;
    }

    private void refreshVotedRecords() {
        mRecyclerViewAdapter.refreshRecords(mSharedPreferences.getStringSet(RECORD_ID, new HashSet<>()));
        mSharedPreferences.edit()
                .remove(RECORD_ID)
                .apply();
    }

    private SwipeRefreshLayout.OnRefreshListener getRefreshListener() {
        return () -> {
            mActionListener.onStop();
            clearWall();
            updateWall();
        };
    }
}
