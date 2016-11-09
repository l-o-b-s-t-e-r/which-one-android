package com.android.project.view.wall;


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
import android.widget.Toast;

import com.android.project.R;
import com.android.project.WhichOneApp;
import com.android.project.model.Record;
import com.android.project.model.User;
import com.android.project.view.detail.RecordDetailActivity;
import com.android.project.view.userpage.UserPageActivity;
import com.android.project.view.wall.di.WallModule;

import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

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

    @Inject
    User user;
    @Inject
    WallPresenter.ActionListener presenter;
    @Inject
    WallRecyclerViewAdapter recyclerViewAdapter;

    public static Fragment newInstance(TabLayout tabLayout) {
        WallFragment fragment = new WallFragment();
        tabLayout.addTab(tabLayout.newTab());

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WhichOneApp.getUserComponent()
                .plus(new WallModule(this))
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wall_fragment, container, false);
        ButterKnife.bind(this, view);
        showSwipeLayoutProgress();
        swipeLayout.setOnRefreshListener(getOnRefreshListener());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(recyclerViewAdapter);

        presenter.loadLastRecords(user.getUsername());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshVotedRecords();
    }

    @Override
    public void updateRecord(Record record, Integer position, Boolean notify) {
        recyclerViewAdapter.updateRecord(position, record, notify);
    }

    public void updateWall() {
        presenter.loadLastRecords(user.getUsername());
    }

    @Override
    public void showRecords(List<Record> records) {
        recyclerViewAdapter.updateData(records);
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
        recyclerViewAdapter.cleanData();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
        recyclerViewAdapter.notifyDataSetChanged();
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

    @Override
    public void onError(Throwable throwable) {
        Toast.makeText(getContext(), getString(R.string.general_error), Toast.LENGTH_SHORT).show();
        throwable.printStackTrace();
    }

    public void showSwipeLayoutProgress() {
        swipeLayout.setRefreshing(true);
    }

    private void refreshVotedRecords() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        recyclerViewAdapter.refreshRecords(sharedPreferences.getStringSet(RECORD_ID, new HashSet<>()));

        sharedPreferences.edit()
                .remove(RECORD_ID)
                .apply();
    }

    private SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return () -> {
            presenter.onStop();
            clearWall();
            updateWall();
        };
    }
}
