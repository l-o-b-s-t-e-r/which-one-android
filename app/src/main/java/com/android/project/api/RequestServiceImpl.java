package com.android.project.api;

import com.android.project.model.Record;
import com.android.project.model.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by Lobster on 31.05.16.
 */
public class RequestServiceImpl implements RequestService {

    public static final String BASE_URL = "http://10.0.3.2:8080/which-one/"; // for Genimotion
    //public static final String BASE_URL = "http://10.0.2.2:8080/project/"; // for native emulator
    //public static final String BASE_URL = "http://52.27.160.199:8080/project/";
    public static final String IMAGE_FOLDER = "images/";
    private static final String TAG = RequestServiceImpl.class.getName();
    private RequestAPI mRequest;

    public RequestServiceImpl(String url) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRequest = retrofit.create(RequestAPI.class);
    }

    @Override
    public Observable<Void> signUp(String name, String password, String email) {
        return mRequest
                .signUp(name, password, email)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<User> signIn(String name, String password) {
        return mRequest
                .signIn(name, password)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Void> checkName(String name) {
        return mRequest
                .checkName(name)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Void> checkEmail(String email) {
        return mRequest
                .checkEmail(email)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<User> remindInfo(String email) {
        return mRequest
                .remindInfo(email)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Record> getRecord(Long recordId, String targetUsername) {
        return mRequest
                .getRecord(recordId, targetUsername)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<Record>> getLastRecords(String targetUsername) {
        return mRequest
                .getLastRecords(targetUsername)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<Record>> getNextRecords(Long recordId, String targetUsername) {
        return mRequest
                .getNextRecords(recordId, targetUsername)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<Record>> getLastUserRecords(String requestedUsername, String targetUsername) {
        return mRequest
                .getLastUserRecords(requestedUsername, targetUsername)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<Record>> getNextUserRecords(String requestedUsername, Long recordId, String targetUsername) {
        return mRequest
                .getNextUserRecords(requestedUsername, recordId, targetUsername)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<User> getUserInfo(String name) {
        return mRequest
                .getUserInfo(name)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Void> addRecord(List<File> files, List<String> options, String name, String description) {
        List<RequestBody> requestFiles = new ArrayList<>();
        List<RequestBody> requestOptions = new ArrayList<>();

        RequestBody requestName =
                RequestBody.create(MediaType.parse("text/plain"), name);

        RequestBody requestDescription =
                RequestBody.create(MediaType.parse("text/plain"), description);

        for (File f : files) {
            requestFiles.add(RequestBody.create(MediaType.parse("image/png"), f));
        }

        for (String s : options) {
            requestOptions.add(RequestBody.create(MediaType.parse("text/plain"), s));
        }

        return mRequest
                .addRecord(requestFiles, requestOptions, requestName, requestDescription)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<User> updateBackground(File background, String name) {
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/png"), background);

        RequestBody requestName =
                RequestBody.create(MediaType.parse("text/plain"), name);


        return mRequest
                .updateBackground(requestFile, requestName)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<User> updateAvatar(File avatar, String name) {
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/png"), avatar);

        RequestBody requestName =
                RequestBody.create(MediaType.parse("text/plain"), name);

        return mRequest
                .updateAvatar(requestFile, requestName)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<User>> getUsers(String searchQuery) {
        return mRequest
                .getUsers(searchQuery)
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<User>> getUsersFromUsername(String searchQuery, String lastUsername) {
        return mRequest
                .getUsersFromId(searchQuery, lastUsername)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Record> sendVote(Long recordId, String optionName, String userName) {
        return mRequest
                .sendVote(userName, recordId, optionName)
                .subscribeOn(Schedulers.io());
    }
}


