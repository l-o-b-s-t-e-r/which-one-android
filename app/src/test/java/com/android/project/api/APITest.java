package com.android.project.api;

import android.os.Build;

import com.android.project.BuildConfig;
import com.android.project.TestApplication;
import com.android.project.TestUtils;
import com.android.project.model.Record;
import com.android.project.model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import rx.observers.TestSubscriber;

/**
 * Created by Lobster on 11.11.16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(
        constants = BuildConfig.class,
        application = TestApplication.class,
        sdk = Build.VERSION_CODES.M
)
public class APITest {

    private MockWebServer server;
    private RequestServiceImpl mRequest;
    private TestUtils testUtils = new TestUtils();
    private User user;
    private Record record;

    @Before
    public void setUp() {
        server = new MockWebServer();
        user = testUtils.getUser();
        record = testUtils.getRecord();

        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {

                if (request.getPath().equals("/sign_in?name=" + user.getUsername() + "&password=" + user.getPassword()) ||
                        request.getPath().equals("/remind_info?email=email") ||
                        request.getPath().equals("/get_user_info?name=" + user.getUsername())) {
                    return new MockResponse().setBody(testUtils.getUserJson());
                }

                if (request.getPath().equals("/check_name?name=" + user.getUsername())) {
                    return new MockResponse().setResponseCode(HttpsURLConnection.HTTP_OK);
                }

                if (request.getPath().equals("/check_name?name=existingUsername")) {
                    return new MockResponse().setResponseCode(HttpsURLConnection.HTTP_BAD_REQUEST);
                }

                if (request.getPath().equals("/check_email?email=email")) {
                    return new MockResponse().setResponseCode(HttpsURLConnection.HTTP_OK);
                }

                if (request.getPath().equals("/check_email?email=existingEmail")) {
                    return new MockResponse().setResponseCode(HttpsURLConnection.HTTP_BAD_REQUEST);
                }

                if (request.getPath().equals("/get_users?searchQuery=" + user.getUsername()) ||
                        request.getPath().equals("/get_users_from_id?searchQuery=" + user.getUsername() + "&lastUsername=username0")) {
                    return new MockResponse().setBody(testUtils.getUsersJson());
                }

                if (request.getPath().equals("/get_record?recordId=" + record.getRecordId() + "&targetUsername=" + user.getUsername())) {
                    return new MockResponse().setBody(testUtils.getNoVotedRecordJson());
                }

                if (request.getPath().equals("/save_vote?userName=" + user.getUsername() + "&recordId=" + record.getRecordId() + "&optionName=" + record.getSelectedOption())) {
                    return new MockResponse().setBody(testUtils.getVotedRecordJson());
                }

                if (request.getPath().equals("/get_last_records?targetUsername=" + user.getUsername()) ||
                        request.getPath().equals("/get_next_records?recordId=5" + "&targetUsername=" + user.getUsername())) {
                    return new MockResponse().setBody(testUtils.getRecordsJson());
                }

                return new MockResponse().setResponseCode(HttpsURLConnection.HTTP_BAD_REQUEST);
            }
        };

        server.setDispatcher(dispatcher);

        mRequest = new RequestServiceImpl(server.url("/").toString());
    }

    @After
    public void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    public void signInTest() {
        TestSubscriber<User> testSubscriber = new TestSubscriber<>();

        mRequest.signIn(user.getUsername(), user.getPassword())
                .subscribe(testSubscriber);

        testSubscriber.assertValue(user);
    }

    @Test
    public void remindInfoTest() {
        TestSubscriber<User> testSubscriber = new TestSubscriber<>();

        mRequest.remindInfo("email")
                .subscribe(testSubscriber);

        testSubscriber.assertValue(user);
    }

    @Test
    public void getUserInfoTest() {
        TestSubscriber<User> testSubscriber = new TestSubscriber<>();

        mRequest.getUserInfo(user.getUsername())
                .subscribe(testSubscriber);

        testSubscriber.assertValue(user);
    }

    @Test
    public void checkNameTest() {
        TestSubscriber<Void> testSubscriberSuccess = new TestSubscriber<>();
        TestSubscriber<Void> testSubscriberError = new TestSubscriber<>();

        mRequest.checkName(user.getUsername())
                .subscribe(testSubscriberSuccess);

        mRequest.checkName("existingUsername")
                .subscribe(testSubscriberError);

        testSubscriberSuccess.assertNoErrors();
        testSubscriberError.assertError(Throwable.class);
    }

    @Test
    public void checkEmailTest() {
        TestSubscriber<Void> testSubscriberSuccess = new TestSubscriber<>();
        TestSubscriber<Void> testSubscriberError = new TestSubscriber<>();

        mRequest.checkEmail("email")
                .subscribe(testSubscriberSuccess);

        mRequest.checkEmail("existingEmail")
                .subscribe(testSubscriberError);

        testSubscriberSuccess.assertNoErrors();
        testSubscriberError.assertError(Throwable.class);
    }


    @Test
    public void getUsersTest() {
        TestSubscriber<List<User>> testSubscriberUsers = new TestSubscriber<>();
        TestSubscriber<List<User>> testSubscriberUsersFrom = new TestSubscriber<>();

        mRequest.getUsers(user.getUsername())
                .subscribe(testSubscriberUsers);

        mRequest.getUsersFromUsername(user.getUsername(), "username0")
                .subscribe(testSubscriberUsersFrom);

        testSubscriberUsers.assertValue(testUtils.getUsers());
        testSubscriberUsersFrom.assertValue(testUtils.getUsers());
    }

    @Test
    public void getRecordTest() {
        TestSubscriber<Record> testSubscriber = new TestSubscriber<>();

        mRequest.getRecord(record.getRecordId(), user.getUsername())
                .subscribe(testSubscriber);

        testSubscriber.assertValue(record);
    }

    @Test
    public void getRecordsTest() {
        TestSubscriber<List<Record>> testSubscriberRecords = new TestSubscriber<>();
        TestSubscriber<List<Record>> testSubscriberRecordsFrom = new TestSubscriber<>();

        mRequest.getLastRecords(user.getUsername())
                .subscribe(testSubscriberRecords);

        mRequest.getNextRecords(5L, user.getUsername())
                .subscribe(testSubscriberRecordsFrom);

        testSubscriberRecords.assertValue(testUtils.getRecords());
        testSubscriberRecordsFrom.assertValue(testUtils.getRecords());
    }

    @Test
    public void sendVoteTest() {
        TestSubscriber<Record> testSubscriber = new TestSubscriber<>();

        mRequest.sendVote(record.getRecordId(), "selected-option-4", user.getUsername())
                .subscribe(testSubscriber);

        testSubscriber.assertValue(record);
    }
}
