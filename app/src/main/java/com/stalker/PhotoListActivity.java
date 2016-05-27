package com.stalker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.stalker.db.NotesContract;
import com.stalker.db.NotesDBHelper;
import com.stalker.utils.AndroidDatabaseManager;

import static com.stalker.db.NotesContract.NoteTable;

public class PhotoListActivity  extends AppCompatActivity {
    private static final int REQUEST_NEW_NOTE = 1;
    Button addButton;
    Button dbButton;
    private ListView photoList;


    String[] columns = {
            NoteTable._ID,
            NoteTable.COLUMN_NAME_PHOTO_URL,
            NoteTable.COLUMN_NAME_INFO,
            NoteTable.COLUMN_NAME_CREATE_TIMESTAMP,
            NoteTable.COLUMN_NAME_CHANGE_TIMESTAMP,
            NoteTable.COLUMN_NAME_LATITUDE,
            NoteTable.COLUMN_NAME_LONGITUDE,};

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);
        initControls();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        NotesDBHelper.getInstance(getApplicationContext()).close();
        super.onDestroy();
    }

    private void initControls() {
        addButton = (Button) findViewById(R.id.add_button);
        dbButton = (Button) findViewById(R.id.db_button);
        photoList = (ListView) findViewById(R.id.listView);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoListActivity.this, NewNoteActivity.class);
                startActivityForResult(intent, REQUEST_NEW_NOTE);
            }
        });
        dbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dbManager = new Intent(getBaseContext() ,AndroidDatabaseManager.class);
                startActivity(dbManager);
            }
        });
        initPhotoListAdapter();
    }

    private void initPhotoListAdapter() {
        NotesDBHelper handler = NotesDBHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = handler.getReadableDatabase();
        Cursor cursor = db.query(NoteTable.TABLE_NAME, columns, null, null, null, null, null);
        PhotoListAdapter adapter = new PhotoListAdapter(this, cursor, 0);
        photoList.setAdapter(adapter);
    }
}
