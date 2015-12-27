package com.google.eldeveloper13.photocabulary;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.eldeveloper13.photocabulary.adaptors.VocabListAdapter;
import com.google.eldeveloper13.photocabulary.database.VocabColumns;
import com.google.eldeveloper13.photocabulary.database.VocabSetColumns;
import com.google.eldeveloper13.photocabulary.factory.DatabaseFactory;
import com.google.eldeveloper13.photocabulary.models.Vocab;
import com.google.eldeveloper13.photocabulary.database.DatabaseInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VocabularyListActivity extends AppCompatActivity {

    public static String EXTRA_VOCAB_SET_ID = "EXTRA_VOCAB_SET_ID";

    public static Intent startVocabularyListActivity(Context context, int id) {
        Intent intent = new Intent(context, VocabularyListActivity.class);
        intent.putExtra(EXTRA_VOCAB_SET_ID, id);
        return intent;
    }

    @Bind(R.id.vocab_list)
    RecyclerView mVocabListView;

    int mVocabSetId;
    DatabaseInterface mDatabaseInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mVocabSetId = getIntent().getIntExtra(EXTRA_VOCAB_SET_ID, -1);

        setContentView(R.layout.activity_vocabulary_list);
        ButterKnife.bind(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mVocabListView.setLayoutManager(linearLayoutManager);

        mDatabaseInterface = DatabaseFactory.makeDatabase(this);

        List<Vocab> list = new ArrayList<>();
        Cursor cursor = mDatabaseInterface. getVocabListCursor(mVocabSetId);
        for (int i = 0; i < 30; i++) {
            mDatabaseInterface.addVocab("i = " + i, null, mVocabSetId);
        }

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(VocabColumns.COLUMN_WORD));
                Vocab vocab = new Vocab(title);
                list.add(vocab);
            } while (cursor.moveToNext());
        }

        VocabListAdapter adapter = new VocabListAdapter(list);
        mVocabListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vocabulary_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
