package com.google.eldeveloper13.photocabulary.database;

import android.provider.BaseColumns;

/**
 * Created by Eric on 27/12/2015.
 */
public abstract class VocabColumns implements BaseColumns {

    public static final String TABLE_NAME = "Vocab";
    public static final String COLUMN_WORD = "word";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_VOCAB_SET_ID = "vocab_set_id";

    public static final String[] COLUMNS = new String[]{_ID, COLUMN_WORD, COLUMN_IMAGE, COLUMN_VOCAB_SET_ID};
}
