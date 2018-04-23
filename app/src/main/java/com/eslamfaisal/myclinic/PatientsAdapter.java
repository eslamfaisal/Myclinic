package com.eslamfaisal.myclinic;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
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
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView nameText = view.findViewById(R.id.name);
        TextView phoneText = view.findViewById(R.id.phone);
        ImageView call = view.findViewById(R.id.imageView_call);
        ImageView message = view.findViewById(R.id.imageView_messag);

        int nameColumnIndex = cursor.getColumnIndex(DentalContract.PatientsEntry.COLUMN_PATIENT_NAME);
        int phoneColumnIndex = cursor.getColumnIndex(DentalContract.PatientsEntry.COLUMN_PATIENT_PHONE);

        String name = cursor.getString(nameColumnIndex);
        String phone = cursor.getString(phoneColumnIndex);

        nameText.setText(name);
        final String phoneNumber = "0"+phone;
        phoneText.setText(phoneNumber);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+Uri.encode(phoneNumber.trim())));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(callIntent);
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
                intent.putExtra("sms_body", "");
                context.startActivity(intent);
            }
        });
    }
}
