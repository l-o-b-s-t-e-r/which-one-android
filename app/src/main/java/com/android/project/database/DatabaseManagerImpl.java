package com.android.project.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android.project.model.Image;
import com.android.project.model.ImageEntity;
import com.android.project.model.Option;
import com.android.project.model.OptionEntity;
import com.android.project.model.Record;
import com.android.project.model.RecordEntity;
import com.android.project.util.ImageManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Lobster on 15.09.16.
 */
public class DatabaseManagerImpl implements DatabaseManager {

    private static final String TAG = DatabaseManagerImpl.class.getSimpleName();

    private Context mContext;
    private DatabaseHelper mDatabaseHelper;
    private Dao<RecordEntity, Integer> mDaoRecord;
    private Dao<OptionEntity, Integer> mDaoOption;
    private Dao<ImageEntity, Integer> mDaoImage;

    public DatabaseManagerImpl(Context context) {
        mContext = context;
        mDatabaseHelper = DatabaseHelper.getInstance(context);

        // - temporary ->>
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.delete("recordentity", null, null);
        db.delete("imageentity", null, null);
        db.delete("optionentity", null, null);
        db.delete("voteentity", null, null); // <--

        try {
            mDaoRecord = mDatabaseHelper.getRecordDao();
            mDaoOption = mDatabaseHelper.getOptionDao();
            mDaoImage = mDatabaseHelper.getImageDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Record save(Record record) {
        try {
            if (!mDaoRecord.idExists(record.getRecordId().intValue())) {
                RecordEntity recordEntity = new RecordEntity(
                        record.getRecordId(),
                        record.getUsername(),
                        ImageManager.getInstance().startLoadImage(record.getAvatar()),
                        record.getDescription(),
                        record.getSelectedOption()
                );

                mDaoRecord.create(recordEntity);

                for (Image image : record.getImages()) {
                    mDaoImage.create(new ImageEntity(recordEntity, ImageManager.getInstance().startLoadImage(image.getImage())));
                }

                for (Option option : record.getOptions()) {
                    mDaoOption.create(new OptionEntity(recordEntity, option.getOptionName(), option.getVoteCount()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return record;
    }

    @Override
    public Record update(Record record) {
        delete(record.getRecordId().intValue());

        return save(record);
    }

    @Override
    public void delete(Integer recordId) {
        try {
            RecordEntity recordEntity = mDaoRecord.queryForId(recordId);
            recordEntity.getOptions().clear();
            recordEntity.getImages().clear();

            mDaoRecord.delete(recordEntity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    synchronized public List<Record> saveAll(List<Record> records) {
        try {
            for (Record record : records) {
                if (!mDaoRecord.idExists(record.getRecordId().intValue())) {
                    save(record);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return records;
    }

    @Override
    public Record getRecordById(Long id) {
        RecordEntity recordEntity = null;

        try {
            recordEntity = mDaoRecord.queryForId(id.intValue());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Record(recordEntity);
    }
}
