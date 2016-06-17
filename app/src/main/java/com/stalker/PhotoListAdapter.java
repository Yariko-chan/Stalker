package com.stalker;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stalker.db.NotesContract.*;
import com.stalker.utils.ImageDecode;

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
        ImageView photo = (ImageView) view.findViewById(R.id.photo);
        String info = cursor.getString(cursor.getColumnIndexOrThrow(NoteTable.COLUMN_NAME_INFO));
        String photoPath = cursor.getString(cursor.getColumnIndexOrThrow(NoteTable.COLUMN_NAME_PHOTO_URL));
        Bitmap b = ImageDecode.decodeSampledBitmapFromResource(photoPath, 50, 50);
        infoTV.setText(info);
        photo.setImageBitmap(b);
    }
}
