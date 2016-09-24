package com.android.project.wall;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.project.R;
import com.android.project.cofig.WhichOneApp;
import com.android.project.detail.RecordDetailActivity;
import com.android.project.userpage.UserPageActivity;

import java.util.List;

import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class WallFragment extends Fragment implements WallPresenter.View {


    public static final String RECORD_ID = "RECORD_ID";
    private static final String TAG = WallFragment.class.getName();

    private String mUsername;
    private BroadcastReceiver mReceiver;
    private WallPresenter.ActionListener mActionListener;
    private WallRecyclerViewAdapter mRecyclerViewAdapter;

    public WallFragment() {

    }

    public static Fragment newInstance(TabLayout tabLayout) {
        WallFragment fragment = new WallFragment();
        tabLayout.addTab(tabLayout.newTab());

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mRecyclerViewAdapter.updateRecord(intent.getLongExtra(RECORD_ID, -1L));
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, new IntentFilter("com.android.project.wall"));

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wall_fragment, container, false);
        ButterKnife.bind(this, view);

        mUsername = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getString(R.string.user_name), "");

        mActionListener = new WallPresenterImpl(this, ((WhichOneApp) getActivity().getApplication()).getMainComponent());
        mRecyclerViewAdapter = new WallRecyclerViewAdapter(view.getContext(), mActionListener, mUsername);
        mActionListener.loadLastRecords();

        RecyclerView recyclerView = ButterKnife.findById(view, R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mRecyclerViewAdapter);

        return view;
    }

    @Override
    public void showRecords(List<Long> recordIds) {
        mRecyclerViewAdapter.updateData(recordIds);
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

    public void updateWall() {
        mRecyclerViewAdapter.cleanData();
        mActionListener.loadLastRecords();
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
