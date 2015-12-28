package com.google.eldeveloper13.photocabulary.database;

import android.database.Cursor;
import android.graphics.Bitmap;

public interface DatabaseInterface {

    Cursor getVocabSetCursor();
    long addVocabSet(String title);
    int deleteVocabSet(String title);
    int deleteVocabSet(int setId);

    Cursor getVocabListCursor(int vocabSetId);
    long addVocab(String vocab, Bitmap image, int vocabSetId);

}
