package com.eslamfaisal.myclinic;

import com.eslamfaisal.myclinic.data.DentalContract;
import com.eslamfaisal.myclinic.data.DentalContract.PatientsEntry;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Details extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    Uri currentUri;

    TextView name, phone, notes, description, age, paid, amount, remaining, gender, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        currentUri = intent.getData();

        name = findViewById(R.id.name_text);
        phone = findViewById(R.id.phone_text);
        notes = findViewById(R.id.notes_text);
        description = findViewById(R.id.description_text);
        paid = findViewById(R.id.paid_text);
        age = findViewById(R.id.age_text);
        amount = findViewById(R.id.amount_text);
        date = findViewById(R.id.date_text);
        gender = findViewById(R.id.gender_text);
        remaining = findViewById(R.id.remain_text);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                PatientsEntry._ID,
                PatientsEntry.COLUMN_PATIENT_NAME,
                PatientsEntry.COLUMN_PATIENT_PHONE,
                PatientsEntry.COLUMN_PATIENT_DATE,
                PatientsEntry.COLUMN_PATIENT_PAID,
                PatientsEntry.COLUMN_PATIENT_REMAINING,
                PatientsEntry.COLUMN_PATIENT_NOTES,
                PatientsEntry.COLUMN_PATIENT_DESCRIPTION,
                PatientsEntry.COLUMN_PATIENT_AMOUNT,
                PatientsEntry.COLUMN_PATIENT_AGE,
                PatientsEntry.COLUMN_PATIENT_GENDER};
        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                currentUri,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        cursor.moveToNext();
        int nameColumnIndex = cursor.getColumnIndex(PatientsEntry.COLUMN_PATIENT_NAME);
        int phoneColumnIndex = cursor.getColumnIndex(PatientsEntry.COLUMN_PATIENT_PHONE);
        int ageColumnIndex = cursor.getColumnIndex(PatientsEntry.COLUMN_PATIENT_AGE);
        int genderColumnIndex = cursor.getColumnIndex(PatientsEntry.COLUMN_PATIENT_GENDER);
        int amountColumnIndex = cursor.getColumnIndex(PatientsEntry.COLUMN_PATIENT_AMOUNT);
        int paidColumnIndex = cursor.getColumnIndex(PatientsEntry.COLUMN_PATIENT_PAID);
        int remainingColumnIndex = cursor.getColumnIndex(PatientsEntry.COLUMN_PATIENT_REMAINING);
        int dateColumnIndex = cursor.getColumnIndex(PatientsEntry.COLUMN_PATIENT_DATE);
        int notesColumnIndex = cursor.getColumnIndex(PatientsEntry.COLUMN_PATIENT_NOTES);
        int descriptionColumnIndex = cursor.getColumnIndex(PatientsEntry.COLUMN_PATIENT_DESCRIPTION);

        String name1 = cursor.getString(nameColumnIndex);
        Integer phone1 = cursor.getInt(phoneColumnIndex);
        String notes1 = cursor.getString(notesColumnIndex);
        String description1 = cursor.getString(descriptionColumnIndex);
        Integer age1 = cursor.getInt(ageColumnIndex);
        Integer gender1 = cursor.getInt(genderColumnIndex);
        Integer amount1 = cursor.getInt(amountColumnIndex);
        Integer paid1 = cursor.getInt(paidColumnIndex);
        Integer remain1 = cursor.getInt(remainingColumnIndex);
        String date1 = cursor.getString(dateColumnIndex);

        name.setText(name1);
        phone.setText("0" + phone1);
        notes.setText(notes1);
        description.setText(description1);
        age.setText("" + age1);
        amount.setText("" + amount1);
        paid.setText("" + paid1);
        remaining.setText("" + remain1);
        date.setText(date1);
        switch (gender1) {
            case 1:
                gender.setText("Male");
                break;
            case 2:
                gender.setText("Female");
                break;
            default:
                gender.setText("Unknown");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
