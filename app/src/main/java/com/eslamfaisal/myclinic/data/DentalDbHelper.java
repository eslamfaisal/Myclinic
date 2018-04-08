package com.eslamfaisal.myclinic.data;

import com.eslamfaisal.myclinic.data.DentalContract.PatientsEntry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DentalDbHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "dental.db";

    private static final int DATABASE_VERSION = 1;

    public DentalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PATIENTS_TABLE = "CREATE TABLE " + PatientsEntry.TABLE_NAME + " ( "
                + PatientsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PatientsEntry.COLUMN_PATIENT_NAME + " TEXT NOT NULL ,"
                + PatientsEntry.COLUMN_PATIENT_AGE + " INTEGER NOT NULL ,"
                + PatientsEntry.COLUMN_PATIENT_AMOUNT + " INTEGER NOT NULL ,"
                + PatientsEntry.COLUMN_PATIENT_DATE + " TEXT NOT NULL ,"
                + PatientsEntry.COLUMN_PATIENT_GENDER + " INTEGER NOT NULL ,"
                + PatientsEntry.COLUMN_PATIENT_PAID + " INTEGER NOT NULL ,"
                + PatientsEntry.COLUMN_PATIENT_REMAINING + " INTEGER NOT NULL ,"
                + PatientsEntry.COLUMN_PATIENT_PHONE + " INTEGER NOT NULL ,"
                + PatientsEntry.COLUMN_PATIENT_NOTES + " TEXT NOT NULL ,"
                + PatientsEntry.COLUMN_PATIENT_DESCRIPTION + " TEXT NOT NULL );";
        db.execSQL(SQL_CREATE_PATIENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
