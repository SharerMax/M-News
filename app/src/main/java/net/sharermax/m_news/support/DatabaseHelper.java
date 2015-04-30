package net.sharermax.m_news.support;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Author: SharerMax
 * Time  : 2015/4/29
 * E-Mail: mdcw1103@gmail.com
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "star.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String TIME_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + MyBaseColumns.TABLE_NAME + " (" +
            MyBaseColumns._ID + " INTEGER PRIMARY KEY," +
            MyBaseColumns.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
            MyBaseColumns.COLUMN_NAME_URL + TEXT_TYPE + COMMA_SEP +
            MyBaseColumns.COLUMN_NAME_TIME + TIME_TYPE + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static abstract class MyBaseColumns implements BaseColumns {
        public static final String TABLE_NAME = "star";
        public static final String COLUMN_NAME_TITLE = "star_title";
        public static final String COLUMN_NAME_URL = "star_url";
        public static final String COLUMN_NAME_TIME = "star_time";
    }
}
