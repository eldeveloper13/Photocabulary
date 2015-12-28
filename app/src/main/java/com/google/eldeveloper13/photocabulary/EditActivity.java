package com.google.eldeveloper13.photocabulary;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.eldeveloper13.photocabulary.database.DatabaseInterface;
import com.google.eldeveloper13.photocabulary.factory.DatabaseFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditActivity extends AppCompatActivity {

    private static final String EXTRA_VOCAB_SET_ID = "EXTRA_VOCAB_SET_ID";

    public static Intent startEditActivity(Context context, int vocabSetId) {
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra(EXTRA_VOCAB_SET_ID, vocabSetId);
        return intent;
    }

    @Bind(R.id.vocab_text)
    EditText mVocabEditText;

    int mVocabSetId;
    DatabaseInterface mDatabaseInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVocabSetId = getIntent().getIntExtra(EXTRA_VOCAB_SET_ID, -1);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);

        mDatabaseInterface = DatabaseFactory.makeDatabase(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
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

    @OnClick(R.id.save_button)
    public void onSaveButtonClicked(View view) {
        saveVocab();
    }

    private void saveVocab() {
        mDatabaseInterface.addVocab(mVocabEditText.getText().toString().trim(), null, mVocabSetId);
        setResult(RESULT_OK);
        finish();
    }
}
