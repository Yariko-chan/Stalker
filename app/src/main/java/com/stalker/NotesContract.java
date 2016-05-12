package com.stalker;

import android.provider.BaseColumns;

/**
 * Created by Diana on 26.04.2016 at 22:03.
 */
public class NotesContract {

    public NotesContract() {}

    public static abstract class NoteTable implements BaseColumns {
        public static final String TABLE_NAME = "note";
        public static final String COLUMN_NAME_PHOTO_URL = "photo_url";
        public static final String COLUMN_NAME_INFO = "info";
        public static final String COLUMN_NAME_CREATE_TIMESTAMP = "create_date";
        public static final String COLUMN_NAME_CHANGE_TIMESTAMP = "change_date";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
    }
}
