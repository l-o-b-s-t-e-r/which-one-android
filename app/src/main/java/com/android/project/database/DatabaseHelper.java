package com.android.project.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.project.R;
import com.android.project.model.ImageEntity;
import com.android.project.model.OptionEntity;
import com.android.project.model.RecordEntity;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Lobster on 01.04.16.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "which_one.db";
    private static final int DATABASE_VERSION = 40;
    private static DatabaseHelper mDatabaseHelper;

    private Dao<ImageEntity, Integer> mImageDao;
    private Dao<OptionEntity, Integer> mOptionDao;
    private Dao<RecordEntity, Integer> mRecordDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }

        return mDatabaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, ImageEntity.class);
            TableUtils.createTable(connectionSource, OptionEntity.class);
            TableUtils.createTable(connectionSource, RecordEntity.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create databases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {
            TableUtils.dropTable(connectionSource, ImageEntity.class, true);
            TableUtils.dropTable(connectionSource, OptionEntity.class, true);
            TableUtils.dropTable(connectionSource, RecordEntity.class, true);
            onCreate(sqliteDatabase, connectionSource);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new "
                    + newVer, e);
        }
    }

    public Dao<ImageEntity, Integer> getImageDao() throws SQLException {
        if (mImageDao == null) {
            mImageDao = getDao(ImageEntity.class);
        }

        return mImageDao;
    }

    public Dao<OptionEntity, Integer> getOptionDao() throws SQLException {
        if (mOptionDao == null) {
            mOptionDao = getDao(OptionEntity.class);
        }

        return mOptionDao;
    }


    public Dao<RecordEntity, Integer> getRecordDao() throws SQLException {
        if (mRecordDao == null) {
            mRecordDao = getDao(RecordEntity.class);
        }

        return mRecordDao;
    }
}
