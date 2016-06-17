package com.stalker;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
//import android.widget.Toast;

import com.stalker.db.NotesDBHelper;
import com.stalker.db.NotesContract.NoteTable;
import com.stalker.utils.ImageDecode;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewNoteActivity extends Activity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PERMISSIONS_CAMERA = 2;
    private static final String CURRENT_PHOTO_PATH = "current_photo_path";
    private static String TAG = NewNoteActivity.class.getSimpleName();

    private ImageButton addPhotoButton;
    private Button addNoteButton;
    private Button cancelButton;
    private EditText noteText;
    private ImageView currentPhoto;

    private String mCurrentPhotoPath;
    private int photoWidth;
    private int photoHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        initControls();
        if (savedInstanceState != null) {
            mCurrentPhotoPath = savedInstanceState.getString(CURRENT_PHOTO_PATH);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_PHOTO_PATH, mCurrentPhotoPath);
    }

    private void initControls() {
        addPhotoButton = (ImageButton) findViewById(R.id.btn_add_photo);
        addNoteButton = (Button) findViewById(R.id.btn_add_note);
        cancelButton = (Button) findViewById(R.id.btn_cancel);
        noteText = (EditText) findViewById(R.id.add_text);
        currentPhoto = (ImageView) findViewById(R.id.current_photo);
        photoWidth = currentPhoto.getWidth();
        photoHeight = currentPhoto.getHeight();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissiomns();
            }
        });
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PhotoListActivity.class);
                setResult(RESULT_OK, intent);

                NotesDBHelper dbHelper = NotesDBHelper.getInstance(getApplicationContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                long newRowId = db.insert(
                        NoteTable.TABLE_NAME,
                        NoteTable.COLUMN_NAME_PHOTO_URL,
                        getCurrentValues());
                db.close();
                finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PhotoListActivity.class);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    private void checkPermissiomns() {
        int cameraPermissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA);
        int storagePermissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (cameraPermissionCheck != PackageManager.PERMISSION_GRANTED ||
                storagePermissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NewNoteActivity.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS_CAMERA);
        } else {
            onCameraPermissionsReceived();
        }
    }

    private ContentValues getCurrentValues() {
        ContentValues values = new ContentValues();
        values.put(NoteTable.COLUMN_NAME_PHOTO_URL, mCurrentPhotoPath);
        values.put(NoteTable.COLUMN_NAME_INFO, noteText.getText().toString());
        values.put(NoteTable.COLUMN_NAME_CREATE_TIMESTAMP, System.currentTimeMillis());
        values.put(NoteTable.COLUMN_NAME_CHANGE_TIMESTAMP, System.currentTimeMillis() + 60*60*24);
        values.put(NoteTable.COLUMN_NAME_LATITUDE, 44.418088);
        values.put(NoteTable.COLUMN_NAME_LONGITUDE, 26.103516);
        return values;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_IMAGE_CAPTURE:{
                if (resultCode == RESULT_OK){
                    //switchButtonVisibility();
                    Bitmap b = null;
                    b = ImageDecode.decodeSampledBitmapFromResource(mCurrentPhotoPath, 300, 300);
                    currentPhoto.setImageBitmap(b);
                } else {
                    Toast.makeText(getApplicationContext(),
                            R.string.message_error,
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        }
    }

    private void switchButtonVisibility() {
        addPhotoButton.setVisibility(View.GONE);
        currentPhoto.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_CAMERA) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "show here dialog with explanation", Toast.LENGTH_LONG).show();
            } else {
                onCameraPermissionsReceived();
            }
        }
    }

    private void onCameraPermissionsReceived() {


        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photoIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createPhotoFile();
                Toast.makeText(getApplicationContext(), "File successfully created", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            } finally {
                if (photoFile != null) {
                    photoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                    startActivityForResult(photoIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }

    }

    private File createPhotoFile() throws IOException{
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}