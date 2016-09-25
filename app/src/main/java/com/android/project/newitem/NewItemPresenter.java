package com.android.project.newitem;

import java.io.File;
import java.util.List;

/**
 * Created by Lobster on 23.07.16.
 */

public interface NewItemPresenter {

    interface View {
        void loadMainActivity();
    }

    interface ActionListener {
        void sendRecord(List<File> images, List<String> options, String name, String title);
    }

}
