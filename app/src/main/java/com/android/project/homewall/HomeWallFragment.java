package com.android.project.homewall;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.project.R;
import com.android.project.detail.RecordDetailActivity;
import com.android.project.login.LogInActivity;
import com.android.project.model.Record;
import com.android.project.userpage.UserPageActivity;
import com.android.project.util.RecordServiceImpl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeWallFragment extends Fragment implements HomeWallPresenter.View {

    @BindView(R.id.homewall_recycler)
    RecyclerView recyclerView;
    private HomeWallPresenter.ActionListener mActionListener;
    private HomeWallRecyclerViewAdapter mRecyclerViewAdapter;

    public HomeWallFragment() {

    }

    public static Fragment newInstance(String userName, TabLayout tabLayout) {
        HomeWallFragment fragment = new HomeWallFragment();
        tabLayout.addTab(tabLayout.newTab());

        Bundle args = new Bundle();
        args.putString(LogInActivity.USER_NAME, userName);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_wall, container, false);
        ButterKnife.bind(this, view);

        String userName;
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle == null) {
            bundle = getArguments();
            userName = bundle.getString(LogInActivity.USER_NAME);
        } else {
            userName = bundle.getString(UserPageActivity.USER_NAME);
        }

        mActionListener = new HomeWallPresenterImpl(new RecordServiceImpl(getString(R.string.base_uri)), this);
        mRecyclerViewAdapter = new HomeWallRecyclerViewAdapter(getContext(), mActionListener);
        mActionListener.loadLastRecords(userName);

        recyclerView = (RecyclerView) view.findViewById(R.id.homewall_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(mRecyclerViewAdapter);


        return view;
    }

    @Override
    public void updateRecords(List<Record> records) {
        mRecyclerViewAdapter.updateData(records);
    }

    @Override
    public void openRecordDetail(Long recordId) {
        Intent intent = new Intent(getContext(), RecordDetailActivity.class);
        intent.putExtra(RecordDetailActivity.RECORD_ID, recordId);
        startActivity(intent);
    }
}
