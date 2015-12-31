package com.google.eldeveloper13.photocabulary.database;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import com.google.eldeveloper13.photocabulary.models.Vocab;

public interface DatabaseInterface {

    Cursor getVocabSetCursor();
    long addVocabSet(String title);
    int deleteVocabSet(String title);
    int deleteVocabSet(int setId);

    Cursor getVocabListCursor(int vocabSetId);
    long addVocab(String vocab, String imagePath, int vocabSetId);

    @Nullable
    Vocab getVocab(long vocabId);
}
