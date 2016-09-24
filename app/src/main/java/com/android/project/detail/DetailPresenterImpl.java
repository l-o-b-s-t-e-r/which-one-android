package com.android.project.detail;

import com.android.project.cofig.DatabaseManager;
import com.android.project.cofig.MainComponent;
import com.android.project.model.Option;
import com.android.project.util.RequestService;

import javax.inject.Inject;

/**
 * Created by Lobster on 22.06.16.
 */

public class DetailPresenterImpl implements DetailPresenter.ActionListener {

    private static final String TAG = DetailPresenterImpl.class.getName();
    @Inject
    public RequestService requestService;
    @Inject
    public DatabaseManager databaseManager;

    private DetailPresenter.View mDetailView;

    public DetailPresenterImpl(DetailPresenter.View detailView, MainComponent mainComponent) {
        mDetailView = detailView;
        mainComponent.inject(this);

    }

    @Override
    public void loadRecord(Long recordId) {
        mDetailView.showRecord(databaseManager.getById(recordId));
    }

    @Override
    public void sendVote(Long recordId, Option option, String userName) {
        requestService.sendVote(recordId, option, userName, new RequestService.NewVote() {
            @Override
            public void voteSent(Long recordId, Option option, String userName) {
                databaseManager.addVote(recordId, option, userName);
            }
        });
    }
}
