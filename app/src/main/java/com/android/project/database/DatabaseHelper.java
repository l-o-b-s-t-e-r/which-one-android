package com.android.project.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.project.R;
import com.android.project.model.ImageEntity;
import com.android.project.model.OptionEntity;
import com.android.project.model.RecordEntity;
import com.android.project.model.UserEntity;
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
    private static final int DATABASE_VERSION = 41;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, ImageEntity.class);
            TableUtils.createTable(connectionSource, OptionEntity.class);
            TableUtils.createTable(connectionSource, RecordEntity.class);
            TableUtils.createTable(connectionSource, UserEntity.class);
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
            TableUtils.dropTable(connectionSource, UserEntity.class, true);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new "
                    + newVer, e);
        }

        onCreate(sqliteDatabase, connectionSource);
    }

    public void onClear() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), ImageEntity.class);
        TableUtils.clearTable(getConnectionSource(), OptionEntity.class);
        TableUtils.clearTable(getConnectionSource(), RecordEntity.class);
        TableUtils.clearTable(getConnectionSource(), UserEntity.class);
    }

    public Dao<ImageEntity, Integer> getImageDao() throws SQLException {
        return getDao(ImageEntity.class);
    }

    ;

    public Dao<OptionEntity, Integer> getOptionDao() throws SQLException {
        return getDao(OptionEntity.class);
    }


    public Dao<RecordEntity, Integer> getRecordDao() throws SQLException {
        return getDao(RecordEntity.class);
    }

    public Dao<UserEntity, Integer> getUserDao() throws SQLException {
        return getDao(UserEntity.class);
    }
}
