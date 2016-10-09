package com.android.project.cofig;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android.project.model.Image;
import com.android.project.model.ImageEntity;
import com.android.project.model.Option;
import com.android.project.model.OptionEntity;
import com.android.project.model.Record;
import com.android.project.model.RecordEntity;
import com.android.project.model.VoteEntity;
import com.android.project.util.ImageLoader;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lobster on 15.09.16.
 */
public class DatabaseManagerImpl implements DatabaseManager {

    private Context mContext;
    private DatabaseHelper mDatabaseHelper;
    private Dao<RecordEntity, Integer> mDaoRecord;
    private Dao<OptionEntity, Integer> mDaoOption;
    private Dao<ImageEntity, Integer> mDaoImage;
    private Dao<VoteEntity, Integer> mDaoVote;

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
            mDaoVote = mDatabaseHelper.getVoteDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Record record) {
        try {
            mDaoRecord.createOrUpdate(convertRecordToEntity(record));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addVote(Long recordId, Option option, String votedUser) {
        try {
            OptionEntity optionEntity = mDaoOption.queryForId(option.getOptionId().intValue());
            optionEntity.getVotes().add(new VoteEntity(recordId, optionEntity, votedUser));
            mDaoOption.update(optionEntity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Long> saveAll(List<Record> records) {
        List<Long> recordIds = new ArrayList<>();
        try {
            RecordEntity recordEntity;
            for (Record record : records) {
                recordIds.add(record.getRecordId());
                if (!mDaoRecord.idExists(record.getRecordId().intValue())) {
                    recordEntity = convertRecordToEntity(record);
                    mDaoRecord.createOrUpdate(recordEntity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recordIds;
    }

    @Override
    public Record getById(Long id) {
        RecordEntity recordEntity = null;
        List<OptionEntity> optionEntities = null;

        try {
            recordEntity = mDaoRecord.queryForId(id.intValue());
            optionEntities = mDaoOption.queryForEq(OptionEntity.RECORD_ID_FIELD_NAME, recordEntity.getRecordId().intValue());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return convertEntityToRecord(recordEntity, optionEntities);
    }

    private RecordEntity convertRecordToEntity(Record record) throws SQLException {
        RecordEntity entity = new RecordEntity();

        entity.setRecordId(record.getRecordId());
        entity.setUsername(record.getUsername());
        entity.setTitle(record.getTitle());

        RecordEntity existingRecordEntity;
        if ((existingRecordEntity = mDaoRecord.queryBuilder().where()
                .eq(RecordEntity.AVATAR_PATH_FIELD_NAME, mContext.getFilesDir() + "/" + record.getAvatar())
                .queryForFirst()) == null) {

            entity.setAvatarPath(ImageLoader.getInstance()
                    .savePictureToInternalStorage(record.getAvatar(), mContext));
        } else {
            entity.setAvatarPath(existingRecordEntity.getAvatarPath());
        }

        mDaoRecord.assignEmptyForeignCollection(entity, RecordEntity.IMAGES_FIELD_NAME);

        String imageFilePath;
        for (Image image : record.getImages()) {
            imageFilePath = ImageLoader.getInstance()
                    .savePictureToInternalStorage(image.getImage(), mContext);

            entity.getImages().add(new ImageEntity(entity, imageFilePath));
        }

        OptionEntity optionEntity;
        VoteEntity voteEntity;
        for (Option option : record.getOptions()) {
            optionEntity = new OptionEntity(entity, option.getOptionName());
            mDaoOption.assignEmptyForeignCollection(optionEntity, OptionEntity.VOTES_FIELD_NAME);
            mDaoOption.createOrUpdate(optionEntity);

            for (String vote : option.getVotes()) {
                voteEntity = new VoteEntity(record.getRecordId(), optionEntity, vote);
                optionEntity.getVotes().add(voteEntity);
                mDaoVote.createOrUpdate(voteEntity);
            }
        }

        return entity;
    }

    private Record convertEntityToRecord(RecordEntity entity, List<OptionEntity> optionEntities) {
        Record record = new Record();
        record.setRecordId(entity.getRecordId());
        record.setUsername(entity.getUsername());
        record.setAvatar(entity.getAvatarPath());
        record.setTitle(entity.getTitle());

        List<Image> images = new ArrayList<>();
        for (ImageEntity imageEntity : entity.getImages()) {
            images.add(new Image(entity.getRecordId(), imageEntity.getImagePath()));
        }
        record.setImages(images);

        List<Option> options = new ArrayList<>();
        List<String> votes;
        for (OptionEntity optionEntity : optionEntities) {
            votes = new ArrayList<>();
            for (VoteEntity voteEntity : optionEntity.getVotes()) {
                votes.add(voteEntity.getVotedUser());
            }

            options.add(new Option(optionEntity.getId().longValue(), optionEntity.getOptionName(), votes));
        }
        record.setOptions(options);

        return record;
    }

}
