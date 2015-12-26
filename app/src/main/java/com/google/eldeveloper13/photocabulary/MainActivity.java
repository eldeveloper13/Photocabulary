package com.google.eldeveloper13.photocabulary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.eldeveloper13.photocabulary.dialogs.DialogHelper;
import com.google.eldeveloper13.photocabulary.factory.DatabaseFactory;
import com.google.eldeveloper13.photocabulary.models.VocabSet;
import com.google.eldeveloper13.photocabulary.services.DatabaseInterface;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(android.R.id.list)
    ListView mListView;
    @Bind(android.R.id.empty)
    View mEmptyView;

    DatabaseInterface mDatabaseInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDatabaseInterface = DatabaseFactory.makeDatabase(this);
        Cursor cursor = mDatabaseInterface.getVocabSetCursor();

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.vocab_set_row, cursor, new String[] {VocabSet.COLUMN_TITLE}, new int[] { R.id.vocab_set_title }, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mListView.setAdapter(adapter);
        if (mListView.getCount() > 0) {
            mEmptyView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_vocab_set) {
            showAddVocabSetDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(android.R.id.empty)
    public void onClickedEmpty(View view) {
        showAddVocabSetDialog();
    }

    private void showAddVocabSetDialog() {
        DialogHelper.createAddNewVocabSetDialog(this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                EditText titleEditText = ButterKnife.findById((AlertDialog) dialogInterface, R.id.vocab_set_title_editText);
                createNewVocabSet(titleEditText.getText().toString());
            }
        }).show();
    }

    private void createNewVocabSet(String title) {
        if (title == null || title.trim().isEmpty()) {
            Toast.makeText(this, "Vocabulary set title cannot be empty", Toast.LENGTH_LONG).show();
        } else {

            Long result = mDatabaseInterface.addVocabSet(title);
            ((SimpleCursorAdapter) mListView.getAdapter()).getCursor().requery();
//            Toast.makeText(this, "Creating Vocab set " + title + " with result " + result, Toast.LENGTH_SHORT).show();
        }
    }
}
