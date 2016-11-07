package com.android.project.util;

/**
 * Created by Lobster on 11.10.16.
 */
public interface CommonPresenter {

    interface View {

        void showProgress();

        void hideProgress();

        void onError(Throwable throwable);

    }

    interface ActionListener {
        void onStop();
    }

}
