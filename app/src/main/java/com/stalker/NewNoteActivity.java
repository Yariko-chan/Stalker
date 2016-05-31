package com.stalker;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.stalker.db.NotesDBHelper;
import com.stalker.db.NotesContract.NoteTable;

import java.io.File;

public class NewNoteActivity extends Activity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PERMISSIONS_CAMERA = 2;
    private static String TAG = NewNoteActivity.class.getSimpleName();

    private ImageButton addPhotoButton;
    private Button addNoteButton;
    private Button cancelButton;
    private EditText noteText;

    private File photoFile;

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
        values.put(NoteTable.COLUMN_NAME_PHOTO_URL, "null");
        values.put(NoteTable.COLUMN_NAME_INFO, noteText.getText().toString());
        values.put(NoteTable.COLUMN_NAME_CREATE_TIMESTAMP, System.currentTimeMillis());
        values.put(NoteTable.COLUMN_NAME_CHANGE_TIMESTAMP                                                                                                                                                           , System.currentTimeMillis() + 60*60*24);
        values.put(NoteTable.COLUMN_NAME_LATITUDE, 44.418088);
        values.put(NoteTable.COLUMN_NAME_LONGITUDE, 26.103516);
        return values;
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
        Toast.makeText(getApplicationContext(), "permissions received, start camera", Toast.LENGTH_LONG).show();
        photoFile = createPhotoFile();
    }

    private File createPhotoFile() {

        return null;
    }
}