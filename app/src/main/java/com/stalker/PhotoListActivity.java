package com.stalker;

import android.content.ContentValues;
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

import com.stalker.db.NotesDBHelper;
import com.stalker.utils.AndroidDatabaseManager;

import static com.stalker.db.NotesContract.NoteTable;

public class PhotoListActivity  extends AppCompatActivity {
    Button addButton;
    Button dbButton;
    private ListView photoList;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);
        addButton = (Button) findViewById(R.id.add_button);
        dbButton = (Button) findViewById(R.id.db_button);
        photoList = (ListView) findViewById(R.id.listView);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotesDBHelper dbHelper = NotesDBHelper.getInstance(getApplicationContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(NoteTable.COLUMN_NAME_PHOTO_URL, "null");
                values.put(NoteTable.COLUMN_NAME_INFO, "some info about photo");
                values.put(NoteTable.COLUMN_NAME_CREATE_TIMESTAMP, System.currentTimeMillis());
                values.put(NoteTable.COLUMN_NAME_CHANGE_TIMESTAMP, System.currentTimeMillis() + 60*60*24);
                values.put(NoteTable.COLUMN_NAME_LATITUDE, 44.418088);
                values.put(NoteTable.COLUMN_NAME_LONGITUDE, 26.103516);

                long newRowId = db.insert(
                        NoteTable.TABLE_NAME,
                        NoteTable.COLUMN_NAME_PHOTO_URL,
                        values);
                db.close();
            }
        });
        dbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dbManager = new Intent(getBaseContext() ,AndroidDatabaseManager.class);
                startActivity(dbManager);
            }
        });

        String[] columns = {
                NoteTable._ID,
                NoteTable.COLUMN_NAME_PHOTO_URL,
                NoteTable.COLUMN_NAME_INFO,
                NoteTable.COLUMN_NAME_CREATE_TIMESTAMP,
                NoteTable.COLUMN_NAME_CHANGE_TIMESTAMP,
                NoteTable.COLUMN_NAME_LATITUDE,
                NoteTable.COLUMN_NAME_LONGITUDE,};
        NotesDBHelper handler = NotesDBHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = handler.getReadableDatabase();
        Cursor cursor = db.query(NoteTable.TABLE_NAME, columns, null, null, null, null, null);

        PhotoListAdapter adapter = new PhotoListAdapter(this, cursor, 0);
        photoList.setAdapter(adapter);
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
}
