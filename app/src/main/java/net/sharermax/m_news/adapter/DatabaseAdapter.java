package net.sharermax.m_news.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.sharermax.m_news.network.WebResolve;
import net.sharermax.m_news.support.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: SharerMax
 * Time  : 2015/4/30
 * E-Mail: mdcw1103@gmail.com
 */
public class DatabaseAdapter {
    public static final String CLASS_NAME = "DatabaseAdapter";
    private static final String FIELD_TITLE = WebResolve.FIELD_TITLE;
    private static final String FIELD_URL = WebResolve.FIELD_URL;
    private static final String FIELD_TIME = "star_time";
    private SQLiteDatabase mDataBase;
    private DatabaseHelper mDatabaseHelper;
    private ContentValues mContentValues;
    private List<NewsDataRecord> mRecordList;
    private String mSendData;
    private NewsDataRecord mNewsDataRecord;
    private static DatabaseAdapter sDatabaseAdapter;

    public static DatabaseAdapter getInstance(Context context) {
        if (null == sDatabaseAdapter) {
            sDatabaseAdapter = new DatabaseAdapter(context.getApplicationContext());
        }
        return sDatabaseAdapter;
    }
    private DatabaseAdapter(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
        mDataBase = mDatabaseHelper.getWritableDatabase();
        mContentValues = new ContentValues();
        mRecordList = new ArrayList<>();
    }

    public SQLiteDatabase getDataBase() {
        return this.mDataBase;
    }

    public long insert (NewsDataRecord record) {
        if (null == record) {
            return -1;
        }
        mContentValues.clear();
        mContentValues.put(DatabaseHelper.MyBaseColumns.COLUMN_NAME_TITLE, record.title);
        mContentValues.put(DatabaseHelper.MyBaseColumns.COLUMN_NAME_URL, record.url);
        mContentValues.put(DatabaseHelper.MyBaseColumns.COLUMN_NAME_TIME, record.time);
        return mDataBase.insert(DatabaseHelper.MyBaseColumns.TABLE_NAME, null, mContentValues);
    }

    public long insert() {
        return insert(mNewsDataRecord);
    }

    public int delete(long _id) {
        return mDataBase.delete(DatabaseHelper.MyBaseColumns.TABLE_NAME,
                DatabaseHelper.MyBaseColumns._ID + "==?", new String[] {"" + _id});
    }

    public List<NewsDataRecord> queryAllData() {
        mRecordList.clear();
        String[] projection = {
                DatabaseHelper.MyBaseColumns._ID,
                DatabaseHelper.MyBaseColumns.COLUMN_NAME_TITLE,
                DatabaseHelper.MyBaseColumns.COLUMN_NAME_URL,
                DatabaseHelper.MyBaseColumns.COLUMN_NAME_TIME
        };
        Cursor result = mDataBase.query(DatabaseHelper.MyBaseColumns.TABLE_NAME, projection,
                null, null, null, null, null);
        int count = result.getCount();
        Log.v(CLASS_NAME, count + "");
        result.moveToFirst();
        for (int i = 0; i<count; i++) {
            NewsDataRecord record = new NewsDataRecord();
            record._id = result.getLong(result.getColumnIndex(DatabaseHelper.MyBaseColumns._ID));
            record.title =result.getString(result.getColumnIndex(DatabaseHelper.MyBaseColumns.COLUMN_NAME_TITLE));
            record.url = result.getString(result.getColumnIndex(DatabaseHelper.MyBaseColumns.COLUMN_NAME_URL));
            record.time = result.getLong(result.getColumnIndex(DatabaseHelper.MyBaseColumns.COLUMN_NAME_TIME));
            mRecordList.add(record);
            result.moveToNext();
        }
        result.close();
        return mRecordList;
    }

    public void open() {
        if (!mDataBase.isOpen()) {
            mDataBase = mDatabaseHelper.getWritableDatabase();
        }
    }

    public boolean isOpen() {
        return mDataBase.isOpen();
    }

    public static void close() {
        if (null == sDatabaseAdapter) {
            return;
        }
        sDatabaseAdapter.mDatabaseHelper.close();
    }

    public void setItemRecord(NewsDataRecord record) {
        mNewsDataRecord = record;
    }


    public NewsDataRecord getItemRecord() {
        return mNewsDataRecord;
    }

    public boolean isExist() {
        Cursor cursor = mDataBase.query(DatabaseHelper.MyBaseColumns.TABLE_NAME,
                new String[]{DatabaseHelper.MyBaseColumns.COLUMN_NAME_URL},
                DatabaseHelper.MyBaseColumns.COLUMN_NAME_URL + " LIKE ?",
                new String[] {mNewsDataRecord.url}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public void setSendData(String sendData) {
        mSendData = sendData;
    }

    public String getSendData() {
        return mSendData;
    }

    public static class NewsDataRecord{
        public long _id = 0;
        public String title = "";
        public String url = "";
        public long time = 0;
    }
 }
