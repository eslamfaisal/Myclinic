package com.eslamfaisal.myclinic.data;

import com.eslamfaisal.myclinic.data.DentalContract.PatientsEntry;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


public class DentalProvider extends ContentProvider {

    DentalDbHelper dbHelper;

    public static final int PATIENTS = 100;
    public static final int PATIENT_ID = 101;

    public static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        matcher.addURI(DentalContract.CONTENT_AUTHORITY, DentalContract.PATIENTS_PATH, PATIENTS);

        matcher.addURI(DentalContract.CONTENT_AUTHORITY, DentalContract.PATIENTS_PATH + "/#", PATIENT_ID);

    }

    @Override
    public boolean onCreate() {
        dbHelper = new DentalDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Get readable database
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = matcher.match(uri);
        switch (match) {
            case PATIENTS:
                cursor = database.query(PatientsEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case PATIENT_ID:
                selection = PatientsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(PatientsEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = matcher.match(uri);
        switch (match) {
            case PATIENTS:
                SQLiteDatabase database = dbHelper.getWritableDatabase();

                long id = database.insert(PatientsEntry.TABLE_NAME, null, values);
                if (id == -1) {
                    Log.e("ssssssssssssss", "Failed to insert row for " + uri);
                    return null;
                }

                // Notify all listeners that the data has changed for the pet content URI
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }



    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int numberDeleted;
        final int mach = matcher.match(uri);
        switch (mach) {
            case PATIENTS:
                numberDeleted = database.delete(PatientsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PATIENT_ID:
                selection = PatientsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                numberDeleted = database.delete(PatientsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);

        }
        return numberDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        final int match = matcher.match(uri);
        switch (match) {
            case PATIENTS:
                return updatePatient(uri, values, selection, selectionArgs);
            case PATIENT_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = PatientsEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePatient(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updatePatient(Uri uri, ContentValues values, String selection, String[] selectionArgs){

        // No need to check the breed, any value is valid (including null).

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(PatientsEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }
}
