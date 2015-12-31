package com.google.eldeveloper13.photocabulary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.eldeveloper13.photocabulary.adaptors.VocabListAdapter;
import com.google.eldeveloper13.photocabulary.database.VocabColumns;
import com.google.eldeveloper13.photocabulary.factory.DatabaseFactory;
import com.google.eldeveloper13.photocabulary.models.Vocab;
import com.google.eldeveloper13.photocabulary.database.DatabaseInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VocabularyListActivity extends AppCompatActivity {


    private static final String TAG = VocabularyListActivity.class.getName();
    public static String EXTRA_VOCAB_SET_ID = "EXTRA_VOCAB_SET_ID";

    public static Intent startVocabularyListActivity(Context context, int id) {
        Intent intent = new Intent(context, VocabularyListActivity.class);
        intent.putExtra(EXTRA_VOCAB_SET_ID, id);
        return intent;
    }

    private static final int REQUEST_CREATE_VOCAB = 1000;

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
        VocabListAdapter adapter = new VocabListAdapter(list);
        mVocabListView.setAdapter(adapter);
        adapter.setItemClickListener(new VocabListAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                Vocab vocab = ((VocabListAdapter)mVocabListView.getAdapter()).getItem(position);
                Intent intent = EditActivity.startEditActivityWithId(VocabularyListActivity.this, vocab.getId());
                startActivity(intent);
            }
        });
        updateListView();
    }

    private void updateListView() {
        List<Vocab> list = new ArrayList<>();
        Cursor cursor = mDatabaseInterface.getVocabListCursor(mVocabSetId);
        try {
            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndex(VocabColumns._ID));
                    String title = cursor.getString(cursor.getColumnIndex(VocabColumns.COLUMN_WORD));
                    String imagePath= cursor.getString(cursor.getColumnIndex(VocabColumns.COLUMN_IMAGE_PATH));
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap image = BitmapFactory.decodeFile(imagePath, options);
                    int vocabSetId = cursor.getInt(cursor.getColumnIndex(VocabColumns.COLUMN_VOCAB_SET_ID));
                    Vocab vocab = new Vocab(id, title, image, vocabSetId);
                    list.add(vocab);
                } while (cursor.moveToNext());
            }
            ((VocabListAdapter) mVocabListView.getAdapter()).updateList(list);
        } catch (Exception e) {
            Log.e(TAG, "Update List exception : " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
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
        if (id == R.id.action_add_vocab) {
            addVocab();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CREATE_VOCAB) {
                updateListView();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addVocab() {
        Intent intent = EditActivity.startEditActivity(this, mVocabSetId);
        this.startActivityForResult(intent, REQUEST_CREATE_VOCAB);
    }
}
