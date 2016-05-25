package com.stalker;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.stalker.db.NotesContract;
import com.stalker.db.NotesDBHelper;
import com.stalker.db.NotesContract.NoteTable;

public class NewNoteActivity extends Activity {
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
        addNoteButton = (Button) findViewById(R.id.btn_add);
        cancelButton = (Button) findViewById(R.id.btn_cancel);
        noteText = (EditText) findViewById(R.id.addText);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PhotoListActivity.class);
//                intent.putExtra (Text, AddText.Text.ToString());
//                intent.putExtra (PhotoPath, CurrentPhotoPath);
                setResult(RESULT_OK, intent);

                NotesDBHelper dbHelper = NotesDBHelper.getInstance(getApplicationContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                long newRowId = db.insert(
                        NotesContract.NoteTable.TABLE_NAME,
                        NotesContract.NoteTable.COLUMN_NAME_PHOTO_URL,
                        getCurrentValues());
                //db.close();
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

    @NonNull
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
}
