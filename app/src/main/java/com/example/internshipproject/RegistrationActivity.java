package com.example.internshipproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.internshipproject.common.NetworkChangeListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class RegistrationActivity extends AppCompatActivity {

    AppCompatButton register;
    EditText et_name, et_email, et_mobile, et_username, et_password;
    CheckBox check_password;
    ProgressDialog progressDialog;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        register = findViewById(R.id.register_button);
        check_password = findViewById(R.id.check_pass);
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email_id);
        et_mobile = findViewById(R.id.et_mobile_no);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);

        check_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_name.getText().toString().isEmpty()) {
                    et_name.setError("Please Enter Name");
                } else if (et_mobile.getText().toString().isEmpty()) {
                    et_mobile.setError("Please Enter Mobile Number");
                } else if (et_mobile.getText().toString().length() != 10) {
                    et_mobile.setError("Mobile Number must be 10 digits");
                } else if (et_email.getText().toString().isEmpty()) {
                    et_email.setError("Please Enter Email ID");
                } else if (!et_email.getText().toString().contains("@") ||
                        !et_email.getText().toString().contains(".com")) {
                    et_email.setError("Email ID must be End with// @gmail.com");
                } else if (et_username.getText().toString().isEmpty()) {
                    et_username.setError("Please Enter Username");
                } else if (et_username.getText().toString().length() < 8) {
                    et_username.setError("Username must be greater then 8");
                } else if (!et_username.getText().toString().matches(".*[A-Z].*")) {
                    et_username.setError("Please Enter At Least one Uppercase Letter");
                } else if (!et_username.getText().toString().matches(".*[a-z].*")) {
                    et_username.setError("Please Enter At Least one Lowercase Letter");
                } else if (!et_username.getText().toString().matches(".*[0-9].*")) {
                    et_username.setError("Please Enter At Least one Number Digit");
                } else if (!et_username.getText().toString().matches(".*[!,@,#,$,%,^,&,*,-].*")) {
                    et_username.setError("Please Enter At Least one Special Symbol Like '@','#' ");
                } else if (et_password.getText().toString().isEmpty()) {
                    et_password.setError("Please Enter your Password");
                } else if (et_password.getText().toString().length() < 8) {
                    et_password.setError("Password must be Greater then 8 or Equal");
                } else {
                    progressDialog = new ProgressDialog(RegistrationActivity.this);
                    progressDialog.setTitle("Please Wait...");
                    progressDialog.setMessage("Registration in Process");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + et_mobile.getText().toString(), 60, TimeUnit.SECONDS, RegistrationActivity.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegistrationActivity.this, "Verification Completed", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegistrationActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String verificationCode, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    Intent i = new Intent(RegistrationActivity.this, VerifyOTPActivity.class);
                                    i.putExtra("verificationCode", verificationCode);
                                    i.putExtra("name", et_name.getText().toString());
                                    i.putExtra("email", et_email.getText().toString());
                                    i.putExtra("phoneNumber", et_mobile.getText().toString());
                                    i.putExtra("username", et_username.getText().toString());
                                    i.putExtra("password", et_password.getText().toString());
                                    startActivity(i);
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }
}