package com.google.eldeveloper13.photocabulary.models;

import android.graphics.Bitmap;

/**
 * Created by Eric on 27/12/2015.
 */
public class Vocab {

    private long mId;
    private String mWord;
    private Bitmap mImage;
    private int mVocabSetId;

    public Vocab(long id, String word, Bitmap image, int vocabSetId) {
        mId = id;
        mWord = word;
        mImage = image;
        mVocabSetId = vocabSetId;
    }

    public long getId() {
        return mId;
    }

    public String getWord() {
        return mWord;
    }

    public void setWord(String word) {
        mWord = word;
    }

    public Bitmap getImage() {
        return mImage;
    }

    public void setImage(Bitmap image) {
        mImage = image;
    }

    public int getVocabSetId() {
        return mVocabSetId;
    }

    public void setVocabSetId(int vocabSetId) {
        mVocabSetId = vocabSetId;
    }
}
