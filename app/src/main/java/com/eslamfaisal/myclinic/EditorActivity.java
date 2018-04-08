package com.eslamfaisal.myclinic;

import android.content.ContentValues;
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

public class EditorActivity extends AppCompatActivity {
    DatePicker datePicker;
    int mGender;
    int day;
    int month;
    int year;
    EditText name, age, phone, paid, amount, remaining, notes, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
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
    }

    public void insertPatient() {

        String mName = name.getText().toString().trim();
        String mNotes = notes.getText().toString().trim();
        String mDescription = description.getText().toString().trim();
        int mAge = Integer.parseInt(age.getText().toString().trim());
        int mPaid = Integer.parseInt(paid.getText().toString().trim());
        int mPhone = Integer.parseInt(phone.getText().toString().trim());
        int mAmount = Integer.parseInt(amount.getText().toString().trim());
        int mRemaining = Integer.parseInt(remaining.getText().toString().trim());

        ContentValues values = new ContentValues();

        values.put(PatientsEntry.COLUMN_PATIENT_AGE, mAge);
        values.put(PatientsEntry.COLUMN_PATIENT_NAME, mName);
        values.put(PatientsEntry.COLUMN_PATIENT_PAID, mPaid);
        values.put(PatientsEntry.COLUMN_PATIENT_NOTES, mNotes);
        values.put(PatientsEntry.COLUMN_PATIENT_PHONE, mPhone);
        values.put(PatientsEntry.COLUMN_PATIENT_AMOUNT, mAmount);
        values.put(PatientsEntry.COLUMN_PATIENT_GENDER, mGender);
        values.put(PatientsEntry.COLUMN_PATIENT_REMAINING, mRemaining);
        values.put(PatientsEntry.COLUMN_PATIENT_DATE, setupPiker());
        values.put(PatientsEntry.COLUMN_PATIENT_DESCRIPTION, mDescription);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
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
        Spinner spinner = (Spinner) findViewById(R.id.gender_spinner);
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
}
