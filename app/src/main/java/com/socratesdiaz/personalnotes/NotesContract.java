package com.socratesdiaz.personalnotes;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by socratesdiaz on 10/25/16.
 */
public class NotesContract {
    interface NotesColumns {
        String NOTE_ID = "_ID";
        String NOTES_TITLE = "notes_title";
        String NOTES_DESCRIPTION = "notes_description";
        String NOTES_DATE = "notes_date";
        String NOTES_TIME = "notes_time";
        String NOTES_IMAGE = "notes_image";
        String NOTES_TYPE = "notes_type";
        String NOTES_IMAGE_STORAGE = "notes_image_storage_selection";
    }

    public static final String CONTENT_AUTHORITY = "com.socratesdiaz.personalnotes.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final String PATH_NOTES = "notes";
    public static final Uri URI_TABLE = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_NOTES).build();

    public static class Notes implements NotesColumns, BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_NOTES).build();
    }
}
