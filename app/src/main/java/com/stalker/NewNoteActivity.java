package com.stalker;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.stalker.db.NotesDBHelper;
import com.stalker.db.NotesContract.NoteTable;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewNoteActivity extends Activity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PERMISSION_CAMERA = 2;
    private static String TAG = NewNoteActivity.class.getSimpleName();

    private ImageButton addPhotoButton;
    private Button addNoteButton;
    private Button cancelButton;
    private EditText noteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        initControls();
    }

    private void initControls() {
        addPhotoButton = (ImageButton) findViewById(R.id.btn_add_photo);
        addNoteButton = (Button) findViewById(R.id.btn_add_note);
        cancelButton = (Button) findViewById(R.id.btn_cancel);
        noteText = (EditText) findViewById(R.id.add_text);
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.CAMERA);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NewNoteActivity.this,
                            new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
                } else {
                    startCamera();
                }
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

    private ContentValues getCurrentValues() {
        ContentValues values = new ContentValues();
        values.put(NoteTable.COLUMN_NAME_PHOTO_URL, "null");
        values.put(NoteTable.COLUMN_NAME_INFO, noteText.getText().toString());
        values.put(NoteTable.COLUMN_NAME_CREATE_TIMESTAMP, System.currentTimeMillis());
        values.put(NoteTable.COLUMN_NAME_CHANGE_TIMESTAMP                                                                                                                                                           , System.currentTimeMillis() + 60*60*24);
        values.put(NoteTable.COLUMN_NAME_LATITUDE, 44.418088);
        values.put(NoteTable.COLUMN_NAME_LONGITUDE, 26.103516);
        return values;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_PERMISSION_CAMERA: {
                startCamera();
            }
        }
    }

    private void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast errorToast = Toast.makeText(getApplicationContext(), R.string.error_file_creating, Toast.LENGTH_LONG);
                errorToast.show();
                NewNoteActivity.this.finish();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_IMAGE_CAPTURE:{
                Toast.makeText(getApplicationContext(), "int requestCode = " + requestCode + ", resultCode = " + resultCode, Toast.LENGTH_LONG);
            }
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStorageDirectory();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void getWriteExternalStoragePermission (){
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NewNoteActivity.this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
        } else {
            createImageFile();
        }
    }
}