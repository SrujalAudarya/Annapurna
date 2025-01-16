package com.example.internshipproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.internshipproject.common.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ForgetPassword extends AppCompatActivity {

    AppCompatButton updatePassword;
    EditText newPassword, confirmPassword;
    ProgressDialog progressDialog;
    String strMobileNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        strMobileNo = getIntent().getStringExtra("mobile");

        updatePassword = findViewById(R.id.updatePassword);
        newPassword = findViewById(R.id.etNewPassword);
        confirmPassword = findViewById(R.id.et_confirmPassword);

        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newPassword.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty()) {
                    Toast.makeText(ForgetPassword.this, "Please Enter New or Confirm Password", Toast.LENGTH_SHORT).show();
                } else if (!newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                    Toast.makeText(ForgetPassword.this, "Both Password didn't Match", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog = new ProgressDialog(ForgetPassword.this);
                    progressDialog.setTitle("Updating Password");
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    updateNewPassword();
                }
            }
        });
    }

    private void updateNewPassword() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("mobiley", strMobileNo);
        params.put("password", confirmPassword.getText().toString());

        client.post(Urls.forgetPasswordWebService, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    String status = response.getString("success");
                    if (status.equals("1")) {
                        Toast.makeText(ForgetPassword.this, "Password Successfully Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgetPassword.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ForgetPassword.this, "Mobile Number is Not Found", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                Toast.makeText(ForgetPassword.this, "Server Error...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}