package com.android.project.wall;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.project.detail.RecordDetailActivity;
import com.android.project.detail.RecordDetailActivityFragment;
import com.android.project.model.Record;
import com.android.project.R;
import com.android.project.adapter.WallRecyclerViewAdapter;
import com.android.project.userpage.UserPageActivity;
import com.android.project.util.RecordServiceImpl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class WallFragment extends Fragment implements WallPresenter.View{

    private WallRecyclerViewAdapter mRecyclerViewAdapter;

    public WallFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wall_fragment, container, false);
        ButterKnife.bind(this, view);

        WallPresenter.ActionListener actionListener = new WallPresenterImpl(new RecordServiceImpl(getString(R.string.base_uri)), this);
        mRecyclerViewAdapter = new WallRecyclerViewAdapter(view.getContext(), actionListener);
        actionListener.loadLastRecords();

        RecyclerView recyclerView = ButterKnife.findById(view, R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mRecyclerViewAdapter);

        return view;
    }

    public static Fragment newInstance(TabLayout tabLayout){
        WallFragment fragment = new WallFragment();
        tabLayout.addTab(tabLayout.newTab());

        return fragment;
    }


    @Override
    public void showRecords(List<Record> records) {
        mRecyclerViewAdapter.updateData(records);
    }

    @Override
    public void showRecordDetail(Long recordId) {
        Intent intent = new Intent(getContext(), RecordDetailActivity.class);
        intent.putExtra(RecordDetailActivityFragment.RECORD_ID, recordId);
        startActivity(intent);
    }

    @Override
    public void showUserPage(String userName) {
        Intent intent = new Intent(getContext(), UserPageActivity.class);
        intent.putExtra(UserPageActivity.USER_NAME, userName);
        startActivity(intent);
    }

}
