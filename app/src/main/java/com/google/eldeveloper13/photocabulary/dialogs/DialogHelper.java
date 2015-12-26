package com.google.eldeveloper13.photocabulary.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;

import com.google.eldeveloper13.photocabulary.R;

public class DialogHelper {

    public static AlertDialog createAddNewVocabSetDialog(Context context, DialogInterface.OnClickListener positiveListener) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Create Vocabulary Set")
                .setView(LayoutInflater.from(context).inflate(R.layout.create_vocab_set_dialog, null))
                .setPositiveButton(android.R.string.ok, positiveListener)
                .setNegativeButton(android.R.string.cancel, new DismissOnClickListener())
                .create();
        return dialog;
    }

    static class DismissOnClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            dialogInterface.dismiss();
        }
    }
}
