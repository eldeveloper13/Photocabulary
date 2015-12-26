package com.google.eldeveloper13.photocabulary.services;

import android.database.Cursor;

public interface DatabaseInterface {

    Cursor getVocabSetCursor();

    long addVocabSet(String title);

}
