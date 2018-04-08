package com.eslamfaisal.myclinic;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.eslamfaisal.myclinic.data.DentalContract;

public class PatientsAdapter extends CursorAdapter {

    public PatientsAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(
                R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameText = view.findViewById(R.id.name);
        TextView phoneText = view.findViewById(R.id.phone);

        int nameColumnIndex = cursor.getColumnIndex(DentalContract.PatientsEntry.COLUMN_PATIENT_NAME);
        int phoneColumnIndex = cursor.getColumnIndex(DentalContract.PatientsEntry.COLUMN_PATIENT_PHONE);

        String name = cursor.getString(nameColumnIndex);
        String phone = cursor.getString(phoneColumnIndex);

        nameText.setText(name);
        phoneText.setText("0"+phone);
    }
}
