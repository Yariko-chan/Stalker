package com.stalker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.stalker.NotesContract.*;
import com.stalker.R;

/**
 * Created by Diana on 19.05.2016 at 19:27.
 */
public class PhotoListAdapter extends CursorAdapter {

    public PhotoListAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.photo_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView infoTV = (TextView) view.findViewById(R.id.info);
        TextView changeDateTV = (TextView) view.findViewById(R.id.change_date);
        TextView locationTV = (TextView) view.findViewById(R.id.location);
        // Extract properties from cursor
        String info = cursor.getString(cursor.getColumnIndexOrThrow(NoteTable.COLUMN_NAME_INFO));
        long changeDate = cursor.getLong(cursor.getColumnIndexOrThrow(NoteTable.COLUMN_NAME_CHANGE_TIMESTAMP));
        int location = cursor.getInt(cursor.getColumnIndexOrThrow(NoteTable.COLUMN_NAME_LATITUDE));
        // Populate fields with extracted properties
        infoTV.setText(info);
    }
}
