package com.google.eldeveloper13.photocabulary.models;

import android.provider.BaseColumns;

public abstract class VocabSet implements BaseColumns {
    public static final String TABLE_NAME = "VocabSet";
    public static final String COLUMN_TITLE = "title";
    public static final String[] COLUMNS = new String[]{_ID, COLUMN_TITLE};
}