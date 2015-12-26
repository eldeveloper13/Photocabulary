package com.google.eldeveloper13.photocabulary.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.google.eldeveloper13.photocabulary.models.VocabSet;

/**
 * Created by Eric on 26/12/2015.
 */
public class DatabaseImpl extends SQLiteOpenHelper implements DatabaseInterface {

    private static final String DATABASE_NAME = "Photocabulary.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_VOCAB_SET_STATEMENT = "CREATE TABLE " + VocabSet.TABLE_NAME + " (" +
            VocabSet._ID + " INTEGER PRIMARY KEY, " +
            VocabSet.COLUMN_TITLE + " TEXT" +
            " )";
    private static final String DROP_VOCAB_SET_STATEMENT = "DROP TABLE IF EXISTS " + VocabSet.TABLE_NAME;

    public DatabaseImpl(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_VOCAB_SET_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(DROP_VOCAB_SET_STATEMENT);
        onCreate(sqLiteDatabase);
    }

    //--------- DatabaseInterface ------------//
    @Override
    public Cursor getVocabSetCursor() {
         return getReadableDatabase().query(VocabSet.TABLE_NAME, null, null, null, null, null, null);
    }

    @Override
    public long addVocabSet(String title) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(VocabSet.COLUMN_TITLE, title);
        return getWritableDatabase().insert(VocabSet.TABLE_NAME, null, contentValues);
    }
}
