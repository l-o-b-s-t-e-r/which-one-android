package com.android.project.database;

import android.database.sqlite.SQLiteDatabase;

import com.android.project.model.Image;
import com.android.project.model.ImageEntity;
import com.android.project.model.Option;
import com.android.project.model.OptionEntity;
import com.android.project.model.Record;
import com.android.project.model.RecordEntity;
import com.android.project.model.User;
import com.android.project.model.UserEntity;
import com.android.project.util.ImageManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by Lobster on 15.09.16.
 */
public class DatabaseManagerImpl implements DatabaseManager {

    private static final String TAG = DatabaseManagerImpl.class.getSimpleName();

    private DatabaseHelper mDatabaseHelper;
    private Dao<RecordEntity, Integer> mRecordDao;
    private Dao<OptionEntity, Integer> mOptionDao;
    private Dao<ImageEntity, Integer> mImageDao;
    private Dao<UserEntity, Integer> mUserDao;

    public DatabaseManagerImpl(DatabaseHelper databaseHelper) {
        mDatabaseHelper = databaseHelper;

        // - temporary ->>
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.delete("recordentity", null, null);
        db.delete("imageentity", null, null);
        db.delete("optionentity", null, null);
        db.delete("voteentity", null, null); // <--

        try {
            mRecordDao = mDatabaseHelper.getRecordDao();
            mOptionDao = mDatabaseHelper.getOptionDao();
            mImageDao = mDatabaseHelper.getImageDao();
            mUserDao = mDatabaseHelper.getUserDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Observable<User> save(final User user) {
        return Observable.fromCallable(() -> {
            DatabaseManagerImpl.this.save(user.toEntity(new UserEntity()));
            return user;
        }).subscribeOn(Schedulers.computation());
    }

    private void save(UserEntity userEntity) {
        try {
            mUserDao.create(userEntity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Observable<User> update(User user) {
        return Observable.fromCallable(() -> {
            mUserDao.update(user.toEntity(new UserEntity()));
            return user;
        }).subscribeOn(Schedulers.computation());
    }

    @Override
    public Observable<User> getUserByName(String username) {
        return Observable.fromCallable(() -> {
            UserEntity userEntity = mUserDao.queryBuilder()
                    .where()
                    .eq(UserEntity.USERNAME_FIELD_NAME, username)
                    .queryForFirst();

            return new User(userEntity);
        }).subscribeOn(Schedulers.computation());
    }

    @Override
    public Observable<Record> getRecordById(Long id) {
        return Observable.fromCallable(() ->
                new Record(mRecordDao.queryForId(id.intValue()))
        ).subscribeOn(Schedulers.computation());
    }

    @Override
    public Observable<List<Record>> saveAll(List<Record> records) {
        return Observable.fromCallable(() -> {
            for (Record record : records) {
                if (!mRecordDao.idExists(record.getRecordId().intValue())) {
                    DatabaseManagerImpl.this.save(record);
                }
            }

            return records;
        }).subscribeOn(Schedulers.computation());
    }

    synchronized private Record save(Record record) throws SQLException, NullPointerException {
        if (!mRecordDao.idExists(record.getRecordId().intValue())) {
            RecordEntity recordEntity = record.toEntity(new RecordEntity());

            mRecordDao.create(recordEntity);

            for (Image image : record.getImages()) {
                mImageDao.create(new ImageEntity(recordEntity, ImageManager.getInstance().startLoadImage(image.getImage())));
            }

            for (Option option : record.getOptions()) {
                mOptionDao.create(new OptionEntity(recordEntity, option.getOptionName(), option.getVoteCount()));
            }
        }

        return record;
    }

    @Override
    public Observable<Record> update(Record record) {
        return Observable.fromCallable(() -> {
            RecordEntity recordEntity = mRecordDao.queryForId(record.getRecordId().intValue());
            recordEntity.getOptions().clear();
            recordEntity.getImages().clear();

            mRecordDao.delete(recordEntity);
            DatabaseManagerImpl.this.save(record);

            return record;
        }).subscribeOn(Schedulers.computation());
    }

    @Override
    public void clearAll() {
        Observable.empty()
                .observeOn(Schedulers.computation())
                .doOnCompleted(mDatabaseHelper::onClear)
                .subscribe();
    }
}
