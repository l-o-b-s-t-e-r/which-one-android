package com.android.project.database;

import android.os.Build;

import com.android.project.BuildConfig;
import com.android.project.TestApplication;
import com.android.project.TestUtils;
import com.android.project.model.Record;
import com.android.project.model.User;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import rx.observers.TestSubscriber;

/**
 * Created by Lobster on 10.11.16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(
        constants = BuildConfig.class,
        application = TestApplication.class,
        sdk = Build.VERSION_CODES.M
)
public class DatabaseTest {

    private DatabaseManager databaseManager;
    private TestUtils testUtils = new TestUtils();

    @Before
    public void setUp() {
        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(RuntimeEnvironment.application, DatabaseHelper.class);
        databaseManager = new DatabaseManagerImpl(databaseHelper);
    }

    @After
    public void tearDown() {
        OpenHelperManager.releaseHelper();
    }

    @Test
    public void saveUserTest() {
        User user = testUtils.getUser();
        TestSubscriber<User> testSubscriber = TestSubscriber.create();

        databaseManager.save(user)
                .subscribe();

        databaseManager.getUserByName(user.getUsername())
                .subscribe(testSubscriber);

        testSubscriber.assertValue(user);
    }

    @Test
    public void updateUserTest() {
        User user = testUtils.getUser();
        TestSubscriber<User> testSubscriber = TestSubscriber.create();

        databaseManager.save(user)
                .subscribe();

        user.setAvatar("new-avatar");

        databaseManager.save(user)
                .subscribe();

        databaseManager.getUserByName(user.getUsername())
                .subscribe(testSubscriber);

        testSubscriber.assertValue(user);
    }

    @Test
    public void saveRecordsTest() {
        List<Record> records = testUtils.getRecords();
        TestSubscriber<Record> testSubscriber;

        databaseManager.saveAll(records)
                .subscribe();

        for (Record record : records) {
            testSubscriber = TestSubscriber.create();

            databaseManager.getRecordById(record.getRecordId())
                    .subscribe(testSubscriber);

            testSubscriber.assertValue(record);
        }
    }

    @Test
    public void updateRecordTest() {
        List<Record> records = testUtils.getRecords();
        TestSubscriber<Record> testSubscriber = TestSubscriber.create();

        databaseManager.saveAll(records)
                .subscribe();

        Record record = records.get(1);
        record.setSelectedOption(record.getOptions().get(0).getOptionName());

        databaseManager.update(record)
                .subscribe();

        databaseManager.getRecordById(record.getRecordId())
                .subscribe(testSubscriber);

        testSubscriber.assertValue(record);
    }

    @Test
    public void clearAllTest() {
        TestSubscriber<Object> testSubscriber = TestSubscriber.create();

        databaseManager.clearAll()
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
    }
}
