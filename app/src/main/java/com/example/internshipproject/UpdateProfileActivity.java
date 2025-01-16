package com.example.internshipproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.internshipproject.common.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class UpdateProfileActivity extends AppCompatActivity {

    AppCompatButton btnsave;
    EditText etname, etmobile, etemail, etusername;
    String strname, strmobile, stremail, strusername;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        btnsave = findViewById(R.id.update_button);
        etname = findViewById(R.id.etUpdateName);
        etmobile = findViewById(R.id.etUpdateMobile_no);
        etemail = findViewById(R.id.etUpdateEmail_id);
        etusername = findViewById(R.id.etUpdateUsername);

        strname = getIntent().getStringExtra("name");
        strmobile = getIntent().getStringExtra("mobile");
        stremail = getIntent().getStringExtra("email");
        strusername = getIntent().getStringExtra("username");

        etname.setText(strname);
        etmobile.setText(strmobile);
        etemail.setText(stremail);
        etusername.setText(strusername);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new ProgressDialog(UpdateProfileActivity.this);
                dialog.setTitle("Update Profile");
                dialog.setMessage("Please Wait...");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                updateProfile();
            }
        });
    }

    private void updateProfile() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("name", etname.getText().toString());
        params.put("phone", etmobile.getText().toString());
        params.put("email", etemail.getText().toString());
        params.put("username", etusername.getText().toString());

        client.post(Urls.updateProfileWebService, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String status = response.getString("success");
                    if (status.equals("1")) {
                        dialog.dismiss();
                        Toast.makeText(UpdateProfileActivity.this, "Profile Updated Successfully...", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(UpdateProfileActivity.this, MyProfileActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(UpdateProfileActivity.this, "Something Went Wrong...", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                dialog.dismiss();
                Toast.makeText(UpdateProfileActivity.this, "Sever Error...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}