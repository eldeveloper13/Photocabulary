package com.google.eldeveloper13.photocabulary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteCursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.eldeveloper13.photocabulary.dialogs.DialogHelper;
import com.google.eldeveloper13.photocabulary.factory.DatabaseFactory;
import com.google.eldeveloper13.photocabulary.database.VocabSetColumns;
import com.google.eldeveloper13.photocabulary.database.DatabaseInterface;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int MENU_RENAME_INDEX = 0;
    private static final int MENU_DELETE_INDEX = 1;

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
        setupListView();
    }

    private void setupListView() {
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.vocab_set_row, mDatabaseInterface.getVocabSetCursor(), new String[] {VocabSetColumns.COLUMN_TITLE}, new int[] { R.id.vocab_set_title }, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = VocabularyListActivity.startVocabularyListActivity(MainActivity.this, getItemIdForPosition(position));
                MainActivity.this.startActivity(intent);
            }
        });

        registerForContextMenu(mListView);
        updateListView();
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        if (view.getId() == android.R.id.list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            String title = getItemTitleForPosition(info.position);
            menu.setHeaderTitle(title);
            String[] menuItem = getResources().getStringArray(R.array.menu);
            for (int i = 0; i < menuItem.length; i++) {
                menu.add(Menu.NONE, i, i, menuItem[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String title =  getItemTitleForPosition(info.position);
        int id = getItemIdForPosition(info.position);
        switch (menuItemIndex) {
            case MENU_RENAME_INDEX:
                Toast.makeText(this, "Rename item " + title, Toast.LENGTH_SHORT).show();
                break;
            case MENU_DELETE_INDEX:
                mDatabaseInterface.deleteVocabSet(id);
                updateListView();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @OnClick(android.R.id.empty)
    public void onClickedEmpty(View view) {
        showAddVocabSetDialog();
    }

    private String getItemTitleForPosition(int position) {
        SQLiteCursor item = (SQLiteCursor) mListView.getItemAtPosition(position);
        return item.getString(item.getColumnIndex(VocabSetColumns.COLUMN_TITLE));
    }

    private int getItemIdForPosition(int position) {
        SQLiteCursor item = (SQLiteCursor) mListView.getItemAtPosition(position);
        return item.getInt(item.getColumnIndex(VocabSetColumns._ID));
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
            try {
                mDatabaseInterface.addVocabSet(title);
                updateListView();
            } catch (SQLiteConstraintException e) {
                Toast.makeText(this, "Vocabulary set with title \'" + title + "\' already exists.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void updateListView() {
        ((SimpleCursorAdapter) mListView.getAdapter()).swapCursor(mDatabaseInterface.getVocabSetCursor());
        if (mListView.getCount() > 0) {
            mEmptyView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }
}
