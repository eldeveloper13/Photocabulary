package com.google.eldeveloper13.photocabulary;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.eldeveloper13.photocabulary.database.DatabaseInterface;
import com.google.eldeveloper13.photocabulary.dialogs.DialogHelper;
import com.google.eldeveloper13.photocabulary.factory.DatabaseFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditActivity extends AppCompatActivity {

    public static Intent startEditActivity(Context context, int vocabSetId) {
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra(EXTRA_VOCAB_SET_ID, vocabSetId);
        return intent;
    }

    private static final String EXTRA_VOCAB_SET_ID = "EXTRA_VOCAB_SET_ID";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;

    @Bind(R.id.vocab_text)
    EditText mVocabEditText;
    @Bind(R.id.vocab_imageview)
    ImageView mVocabImageView;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            mVocabImageView.setImageBitmap(bitmap);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OnClick(R.id.save_button)
    public void onSaveButtonClicked(View view) {
        saveVocab();
    }

    @OnClick(R.id.vocab_imageview)
    public void onVocabImageViewClicked(View view) {
        openImageDialog();
    }

    private void openImageDialog() {
        Dialog dialog = DialogHelper.createOpenImageViewDialog(this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (which == 0) {
                    openGallery();
                } else if (which == 1) {
                    openCamera();
                } else if (which == 2) {
                    clearImage();
                }
            }
        });
        dialog.show();
    }

    private void openGallery() {
        Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        if(galleryIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY);
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void clearImage() {
        mVocabImageView.setImageResource(android.R.drawable.ic_menu_gallery);
    }

    private void saveVocab() {
        mDatabaseInterface.addVocab(mVocabEditText.getText().toString().trim(), null, mVocabSetId);
        setResult(RESULT_OK);
        finish();
    }
}
