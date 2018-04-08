package com.eslamfaisal.myclinic;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.eslamfaisal.myclinic.data.DentalContract.PatientsEntry;

import android.widget.TextView;
import android.widget.Toast;

import com.eslamfaisal.myclinic.data.DentalContract;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    DatePicker datePicker;
    int mGender;
    int day;
    int month;
    int year;
    EditText name, age, phone, paid, amount, remaining, notes, description;
    Uri currentUri;
    Spinner spinner;
    Cursor cursor;

    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        currentUri = intent.getData();

        datePicker = findViewById(R.id.picker);

        age = findViewById(R.id.edit_age);
        name = findViewById(R.id.edit_name);
        paid = findViewById(R.id.edit_paid);
        notes = findViewById(R.id.edit_notes);
        phone = findViewById(R.id.edit_phone);
        amount = findViewById(R.id.edit_amount);
        remaining = findViewById(R.id.edit_remaining);
        description = findViewById(R.id.edit_description);

        setupSpinner();

        if (currentUri != null) {
            getLoaderManager().initLoader(0, null, this);
        }
    }

    public void insertPatient() {


        String mName = name.getText().toString().trim();
        String mNotes = notes.getText().toString().trim();
        String mDescription = description.getText().toString().trim();
        String ageString = age.getText().toString().trim();
        int mAge = number(ageString);
        String paidString = paid.getText().toString().trim();
        int mPaid = number(paidString);
        String phoneString = phone.getText().toString().trim();
        int mPhone = number(phoneString);
        String amountString = amount.getText().toString().trim();
        int mAmount = number(amountString);
        String remain = remaining.getText().toString().trim();
        int mRemaining = number(remain);

        ContentValues values = new ContentValues();

        values.put(PatientsEntry.COLUMN_PATIENT_AGE, mAge);
        values.put(PatientsEntry.COLUMN_PATIENT_NAME, mName);
        values.put(PatientsEntry.COLUMN_PATIENT_PAID, mPaid);
        values.put(PatientsEntry.COLUMN_PATIENT_NOTES, mNotes);
        values.put(PatientsEntry.COLUMN_PATIENT_PHONE, mPhone);
        values.put(PatientsEntry.COLUMN_PATIENT_AMOUNT, mAmount);
        values.put(PatientsEntry.COLUMN_PATIENT_GENDER, mGender);
        values.put(PatientsEntry.COLUMN_PATIENT_REMAINING, mRemaining);
        if (currentUri == null) {
            values.put(PatientsEntry.COLUMN_PATIENT_DATE, setupPiker());
        } else {
            values.put(PatientsEntry.COLUMN_PATIENT_DATE, date);
        }
        values.put(PatientsEntry.COLUMN_PATIENT_DESCRIPTION, mDescription);

        if (currentUri == null) {
            Uri newUri = getContentResolver().insert(PatientsEntry.CONTENT_URI, values);
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }else {
            getContentResolver().update(currentUri,values,null,null);
        }
    }

    private int number(String textNumber) {
        int number = 0;
        if (!TextUtils.isEmpty(textNumber)) {
            number = Integer.parseInt(textNumber);
        }
        return number;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (currentUri != null) {
            MenuItem menuItem = menu.findItem(R.id.delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.save_data:
                insertPatient();
                finish();
                return true;
            case R.id.delete:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public String setupPiker() {
        String date = "";
        day = datePicker.getDayOfMonth();
        month = 1 + datePicker.getMonth();
        year = datePicker.getYear();
        date = "" + day + " / " + month + " / " + year;
        return date;
    }

    public void setupSpinner() {
        spinner = findViewById(R.id.gender_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = DentalContract.PatientsEntry.GENDER_MALE;
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = DentalContract.PatientsEntry.GENDER_FEMALE;
                    } else {
                        mGender = DentalContract.PatientsEntry.GENDER_UNKNOWN;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = DentalContract.PatientsEntry.GENDER_UNKNOWN;
            }
        });
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
        date = date1;
        name.setText(name1);
        phone.setText("0" + phone1);

        notes.setText(notes1);
        description.setText(description1);
        age.setText(String.valueOf( age1));
        amount.setText(String.valueOf( amount1));
        paid.setText(String.valueOf( paid1));
        remaining.setText(String.valueOf(remain1));

        switch (gender1) {
            case PatientsEntry.GENDER_MALE:
                spinner.setSelection(1);
                break;
            case PatientsEntry.GENDER_FEMALE:
                spinner.setSelection(2);
                break;
            default:
                spinner.setSelection(0);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
