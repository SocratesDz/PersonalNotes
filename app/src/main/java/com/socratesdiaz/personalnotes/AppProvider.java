package com.socratesdiaz.personalnotes;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

/**
 * Created by socratesdiaz on 10/26/16.
 */
public class AppProvider extends ContentProvider {
    protected AppDatabase mOpenHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int NOTES = 100;
    private static final int NOTES_ID = 101;

    private static final int ARCHIVES = 200;
    private static final int ARCHIVES_ID = 201;

    private static final int TRASH = 300;
    private static final int TRASH_ID = 301;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = NotesContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "notes", NOTES);
        matcher.addURI(authority, "notes/*", NOTES_ID);

        authority = ArchivesContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "archives", ARCHIVES);
        matcher.addURI(authority, "archives/*", ARCHIVES_ID);

        authority = TrashContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "trash", TRASH);
        matcher.addURI(authority, "trash/*", TRASH_ID);

        return matcher;
    }

    private void deleteDatabase() {
        mOpenHelper.close();
        AppDatabase.deleteDatabase(getContext());
        mOpenHelper = new AppDatabase(getContext());
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new AppDatabase((getContext()));
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (match) {
            case NOTES:
                queryBuilder.setTables(AppDatabase.Tables.NOTES);
                break;
            case NOTES_ID:
                queryBuilder.setTables(AppDatabase.Tables.NOTES);
                String notes_id = NotesContract.Notes.getNoteId(uri);
                queryBuilder.appendWhere(BaseColumns._ID + "=" + notes_id);
                break;
            case ARCHIVES:
                queryBuilder.setTables(AppDatabase.Tables.ARCHIVES);
                break;
            case ARCHIVES_ID:
                queryBuilder.setTables(AppDatabase.Tables.ARCHIVES);
                String archive_id = ArchivesContract.Archives.getArchiveId(uri);
                queryBuilder.appendWhere(BaseColumns._ID + "=" + archive_id);
                break;
            case TRASH:
                queryBuilder.setTables(AppDatabase.Tables.TRASH);
                break;
            case TRASH_ID:
                queryBuilder.setTables(AppDatabase.Tables.TRASH);
                String trash_id = TrashContract.Trash.getDeletedId(uri);
                queryBuilder.appendWhere(BaseColumns._ID + "=" + trash_id);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NOTES:
                return NotesContract.Notes.CONTENT_TYPE;
            case NOTES_ID:
                return NotesContract.Notes.CONTENT_ITEM_TYPE;
            case ARCHIVES:
                return ArchivesContract.Archives.CONTENT_TYPE;
            case ARCHIVES_ID:
                return ArchivesContract.Archives.CONTENT_ITEM_TYPE;
            case TRASH:
                return TrashContract.Trash.CONTENT_TYPE;
            case TRASH_ID:
                return TrashContract.Trash.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);

        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
