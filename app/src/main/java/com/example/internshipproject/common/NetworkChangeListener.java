package com.example.internshipproject.common;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.example.internshipproject.R;

public class NetworkChangeListener extends BroadcastReceiver {
    AppCompatButton btnRetry;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!NetworkDetails.isConnectedToInternet(context)) {
            AlertDialog.Builder ab = new AlertDialog.Builder(context);
            View alertDialog_layout = LayoutInflater.from(context).inflate(R.layout.internet_not_found_dialog, null);
            ab.setView(alertDialog_layout);

            btnRetry = alertDialog_layout.findViewById(R.id.checkInternetConnection);

            AlertDialog alertDialog = ab.create();
            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);

            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    onReceive(context, intent);
                }
            });
        } else {
            Toast.makeText(context, "Your Internet is Connected", Toast.LENGTH_SHORT).show();
        }

    }
}
