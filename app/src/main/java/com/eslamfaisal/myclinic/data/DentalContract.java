package com.eslamfaisal.myclinic.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class DentalContract {

    private DentalContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.eslamfaisal.myclinic";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATIENTS_PATH = "patients";

    public static final class PatientsEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATIENTS_PATH);

        public static final String TABLE_NAME = "patients";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PATIENT_AGE = "age";
        public static final String COLUMN_PATIENT_NAME = "name";
        public static final String COLUMN_PATIENT_DATE = "date";
        public static final String COLUMN_PATIENT_PAID = "paid";
        public static final String COLUMN_PATIENT_PHONE = "phone";
        public static final String COLUMN_PATIENT_NOTES = "notes";
        public final static String COLUMN_PATIENT_GENDER = "gender";
        public static final String COLUMN_PATIENT_AMOUNT = "amount";
        public static final String COLUMN_PATIENT_REMAINING = "remaining";
        public static final String COLUMN_PATIENT_DESCRIPTION = "description";

        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;
    }


}
