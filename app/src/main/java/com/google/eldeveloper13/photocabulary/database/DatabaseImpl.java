package com.google.eldeveloper13.photocabulary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import com.google.eldeveloper13.photocabulary.models.Vocab;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * Created by Eric on 26/12/2015.
 */
public class DatabaseImpl extends SQLiteOpenHelper implements DatabaseInterface {

    private static final String DATABASE_NAME = "Photocabulary.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_VOCAB_SET_STATEMENT = "CREATE TABLE " + VocabSetColumns.TABLE_NAME + " (" +
            VocabSetColumns._ID + " INTEGER PRIMARY KEY, " +
            VocabSetColumns.COLUMN_TITLE + " TEXT NOT NULL UNIQUE " +
            " )";
    private static final String DROP_VOCAB_SET_STATEMENT = "DROP TABLE IF EXISTS " + VocabSetColumns.TABLE_NAME;

    private static final String CREATE_VOCAB_TABLE_STATEMENT = "CREATE TABLE " + VocabColumns.TABLE_NAME + " (" +
            VocabColumns._ID + " INTEGER PRIMARY KEY, " +
            VocabColumns.COLUMN_WORD + " TEXT, " +
            VocabColumns.COLUMN_IMAGE + " BLOB, " +
            VocabColumns.COLUMN_VOCAB_SET_ID + " INTEGER, " +
            "FOREIGN KEY(" + VocabColumns.COLUMN_VOCAB_SET_ID + ") REFERENCES " + VocabSetColumns.TABLE_NAME + "(" + VocabSetColumns._ID + ")" +
            " )";
    private static final String DROP_VOCAB_TABLE_STATEMENT = "DROP TABLE IF EXISTS " + VocabColumns.TABLE_NAME;

    public DatabaseImpl(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_VOCAB_SET_STATEMENT);
        sqLiteDatabase.execSQL(CREATE_VOCAB_TABLE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(DROP_VOCAB_TABLE_STATEMENT);
        sqLiteDatabase.execSQL(DROP_VOCAB_SET_STATEMENT);
        onCreate(sqLiteDatabase);
    }

    //--------- DatabaseInterface ------------//
    @Override
    public Cursor getVocabSetCursor() {
         return getReadableDatabase().query(VocabSetColumns.TABLE_NAME, null, null, null, null, null, null);
    }

    @Override
    public long addVocabSet(String title) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(VocabSetColumns.COLUMN_TITLE, title);
        return getWritableDatabase().insertOrThrow(VocabSetColumns.TABLE_NAME, null, contentValues);
    }

    @Override
    public int deleteVocabSet(String title) {
        return getWritableDatabase().delete(VocabSetColumns.TABLE_NAME, VocabSetColumns.COLUMN_TITLE + " = ?", new String[]{title});
    }

    @Override
    public int deleteVocabSet(int setId) {
        getWritableDatabase().delete(VocabColumns.TABLE_NAME, VocabColumns.COLUMN_VOCAB_SET_ID + " = ?", new String[] { Integer.toString(setId) } );
        return getWritableDatabase().delete(VocabSetColumns.TABLE_NAME, VocabSetColumns._ID + " = ?" , new String[] { Integer.toString(setId) } );
    }

    @Override
    public Cursor getVocabListCursor(int vocabSetId) {
        return getReadableDatabase().query(VocabColumns.TABLE_NAME, null, VocabColumns.COLUMN_VOCAB_SET_ID + " = ?", new String[] { Integer.toString(vocabSetId) }, null, null, null);
    }

    @Override
    public long addVocab(String vocab, Bitmap image, int vocabSetId) {
        byte[] byteArray = new byte[0];
        if (image != null) {
            int bytes = image.getByteCount();
            ByteBuffer buffer = ByteBuffer.allocate(bytes);
            image.copyPixelsToBuffer(buffer);
            byteArray = buffer.array();
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(VocabColumns.COLUMN_WORD, vocab);
        contentValues.put(VocabColumns.COLUMN_IMAGE, byteArray);
        contentValues.put(VocabColumns.COLUMN_VOCAB_SET_ID, vocabSetId);
        return getWritableDatabase().insert(VocabColumns.TABLE_NAME , null, contentValues);
    }
}
