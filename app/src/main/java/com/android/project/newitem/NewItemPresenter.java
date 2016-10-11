package com.android.project.newitem;

import com.android.project.util.CommonPresenter;

import java.io.File;
import java.util.List;

/**
 * Created by Lobster on 23.07.16.
 */

public interface NewItemPresenter {

    interface View extends CommonPresenter.View {
        void loadMainActivity();
    }

    interface ActionListener extends CommonPresenter.ActionListener {
        void sendRecord(List<File> images, List<String> options, String name, String title);
    }

}
