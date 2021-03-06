package com.google.eldeveloper13.photocabulary;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.eldeveloper13.photocabulary.database.DatabaseInterface;
import com.google.eldeveloper13.photocabulary.dialogs.DialogHelper;
import com.google.eldeveloper13.photocabulary.factory.DatabaseFactory;
import com.google.eldeveloper13.photocabulary.models.Vocab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditActivity extends AppCompatActivity {

    private Uri mImageUri;

    public static Intent startEditActivity(Context context, int vocabSetId) {
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra(EXTRA_VOCAB_SET_ID, vocabSetId);
        return intent;
    }

    public static Intent startEditActivityWithId(Context context, long vocabId) {
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra(EXTRA_VOCAB_ID, vocabId);
        return intent;
    }

    private static final String EXTRA_VOCAB_SET_ID = "EXTRA_VOCAB_SET_ID";
    private static final String EXTRA_VOCAB_ID = "EXTRA_VOCAB_ID";

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;

    @Bind(R.id.vocab_text)
    EditText mVocabEditText;
    @Bind(R.id.vocab_imageview)
    ImageView mVocabImageView;

    int mVocabSetId;
    DatabaseInterface mDatabaseInterface;
    Bitmap mVocabImage;
    String mVocabImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
        mDatabaseInterface = DatabaseFactory.makeDatabase(this);

        if (getIntent().hasExtra(EXTRA_VOCAB_SET_ID)) {
            mVocabSetId = getIntent().getIntExtra(EXTRA_VOCAB_SET_ID, -1);
        } else if (getIntent().hasExtra(EXTRA_VOCAB_ID)) {
            Vocab vocab = mDatabaseInterface.getVocab(getIntent().getLongExtra(EXTRA_VOCAB_ID, -1L));
            if (vocab != null) {
                mVocabSetId = vocab.getVocabSetId();
                mVocabImage = vocab.getImage();
                mVocabImageView.setImageBitmap(mVocabImage);
                mVocabEditText.setText(vocab.getWord());
            }
        }


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
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                try {
                    getContentResolver().notifyChange(mImageUri, null);
                    ContentResolver contentResolver = getContentResolver();
                    mVocabImage = android.provider.MediaStore.Images.Media.getBitmap(contentResolver, mImageUri);
                    mVocabImageView.setImageBitmap(mVocabImage);
                } catch (IOException e) {

                }
            }
        } else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                mVocabImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                mVocabImageView.setImageBitmap(mVocabImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                    try {
                        openCamera();
                    } catch (IOException e) {
                        Toast.makeText(EditActivity.this, "Cannot Open Camera : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (which == 2) {
                    clearImage();
                }
            }
        });
        dialog.show();
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        if (galleryIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY);
        }
    }

    private void openCamera() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getCacheImagePath();
        mImageUri = Uri.fromFile(file);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private File getCacheImagePath() throws IOException {
        File file = null;
        try {
            file = File.createTempFile("tempFile", ".jpg", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
            mVocabImagePath = file.getAbsolutePath();
        } catch (Exception e) {

        }
        return file;
    }

    private void clearImage() {
        mVocabImageView.setImageResource(android.R.drawable.ic_menu_gallery);
    }

    private void saveVocab() {
        String vocab = mVocabEditText.getText().toString().trim();
        if (vocab != null && !vocab.isEmpty()) {
            mDatabaseInterface.addVocab(vocab, mVocabImagePath, mVocabSetId);
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }
}
