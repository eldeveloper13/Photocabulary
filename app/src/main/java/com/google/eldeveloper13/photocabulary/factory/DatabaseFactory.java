package com.google.eldeveloper13.photocabulary.factory;

import android.content.Context;

import com.google.eldeveloper13.photocabulary.services.DatabaseImpl;
import com.google.eldeveloper13.photocabulary.services.DatabaseInterface;

/**
 * Created by Eric on 26/12/2015.
 */
public class DatabaseFactory {

    public static DatabaseInterface makeDatabase(Context context) {
        return new DatabaseImpl(context);
    }
}
