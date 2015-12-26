package com.google.eldeveloper13.photocabulary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.eldeveloper13.photocabulary.dialogs.DialogHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(android.R.id.list)
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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
            Toast.makeText(this, "Creating Vocab set " + title, Toast.LENGTH_SHORT).show();
        }
    }
}
