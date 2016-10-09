package com.android.project.util;

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
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Lobster on 31.05.16.
 */
public class RequestServiceImpl implements RequestService {

    public static final String BASE_URL = "http://10.0.3.2:8080/project/";
    //public static final String BASE_URL = "http://52.27.160.199:8080/project/";
    public static final String IMAGE_FOLDER = "images/";
    private static final String TAG = RequestServiceImpl.class.getName();
    private Observable.Transformer mSchedulersTransformer;
    private RequestAPI mRequest;

    public RequestServiceImpl() {
        mSchedulersTransformer = new Observable.Transformer() {
            @Override
            public Object call(Object o) {
                return ((Observable) o)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRequest = retrofit.create(RequestAPI.class);
    }

    @Override
    public Observable<Void> signUp(String name, String password, String email) {
        return mRequest
                .signUp(name, password, email)
                .compose(this.<Void>applySchedulers());
    }

    @Override
    public Observable<Void> signIn(String name, String password) {
        return mRequest
                .signIn(name, password)
                .compose(this.<Void>applySchedulers());
    }

    @Override
    public Observable<Void> checkName(String name) {
        return mRequest
                .checkName(name)
                .compose(this.<Void>applySchedulers());
    }

    @Override
    public Observable<Void> checkEmail(String email) {
        return mRequest
                .checkEmail(email)
                .compose(this.<Void>applySchedulers());
    }

    @Override
    public Observable<Void> remindInfo(String email) {
        return mRequest
                .remindInfo(email)
                .compose(this.<Void>applySchedulers());
    }

    @Override
    public Observable<List<Record>> getLastRecords() {
        return mRequest
                .getLastRecords()
                .compose(this.<List<Record>>applySchedulers());
    }

    @Override
    public Observable<List<Record>> getNextRecords(Long recordId) {
        return mRequest
                .getNextRecords(recordId)
                .compose(this.<List<Record>>applySchedulers());
    }

    @Override
    public Observable<List<Record>> getLastUserRecords(String userName) {
        return mRequest
                .getLastUserRecords(userName)
                .compose(this.<List<Record>>applySchedulers());
    }

    @Override
    public Observable<List<Record>> getNextUserRecords(String userName, Long recordId) {
        return mRequest
                .getNextUserRecords(userName, recordId)
                .compose(this.<List<Record>>applySchedulers());
    }

    @Override
    public Observable<User> getUserInfo(String name) {
        return mRequest
                .getUserInfo(name)
                .compose(this.<User>applySchedulers());
    }

    @Override
    public Observable<Void> addRecord(List<File> files, List<String> options, String name, String title) {
        List<RequestBody> requestFiles = new ArrayList<>();
        List<RequestBody> requestOptions = new ArrayList<>();

        RequestBody requestName =
                RequestBody.create(MediaType.parse("text/plain"), name);

        RequestBody requestTitle =
                RequestBody.create(MediaType.parse("text/plain"), title);

        for (File f : files) {
            requestFiles.add(RequestBody.create(MediaType.parse("image/png"), f));
        }

        for (String s : options) {
            requestOptions.add(RequestBody.create(MediaType.parse("text/plain"), s));
        }

        return mRequest
                .addRecord(requestFiles, requestOptions, requestName, requestTitle)
                .compose(this.<Void>applySchedulers());
    }

    @Override
    public Observable<User> updateBackground(File background, String name) {
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/png"), background);

        RequestBody requestName =
                RequestBody.create(MediaType.parse("text/plain"), name);


        return mRequest
                .updateBackground(requestFile, requestName)
                .compose(this.<User>applySchedulers());
    }

    @Override
    public Observable<User> updateAvatar(File avatar, String name) {
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/png"), avatar);

        RequestBody requestName =
                RequestBody.create(MediaType.parse("text/plain"), name);

        return mRequest
                .updateAvatar(requestFile, requestName)
                .compose(this.<User>applySchedulers());
    }

    @Override
    public Observable<List<User>> getUsers(String searchQuery) {
        return mRequest
                .getUsers(searchQuery)
                .compose(this.<List<User>>applySchedulers());
    }

    @Override
    public Observable<List<User>> getUsersFromId(String searchQuery, Long userId) {
        return mRequest
                .getUsersFromId(searchQuery, userId)
                .compose(this.<List<User>>applySchedulers());
    }

    @Override
    public Observable<Void> sendVote(Long recordId, String optionName, String userName) {
        return mRequest
                .sendVote(userName, recordId, optionName)
                .compose(this.<Void>applySchedulers());
    }

    @SuppressWarnings("unchecked")
    private <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) mSchedulersTransformer;
    }
}


