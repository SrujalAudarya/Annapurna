package com.example.internshipproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.internshipproject.common.NetworkChangeListener;
import com.example.internshipproject.common.Urls;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class ForgetPasswordVerifyActivity extends AppCompatActivity {

    EditText InputCode1, InputCode2, InputCode3, InputCode4, InputCode5, InputCode6;
    TextView resendOTP, verifyNumber;
    AppCompatButton verifyOTP;
    String strVerifyCode, strMobileNo;
    ProgressDialog progressDialog;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        InputCode1 = findViewById(R.id.OTPInputCode1);
        InputCode2 = findViewById(R.id.OTPInputCode2);
        InputCode3 = findViewById(R.id.OTPInputCode3);
        InputCode4 = findViewById(R.id.OTPInputCode4);
        InputCode5 = findViewById(R.id.OTPInputCode5);
        InputCode6 = findViewById(R.id.OTPInputCode6);
        resendOTP = findViewById(R.id.resendOTP);
        verifyNumber = findViewById(R.id.ReceiveOTPNumber);
        verifyOTP = findViewById(R.id.verifyOTP);

        strVerifyCode = getIntent().getStringExtra("verificationCode");
        strMobileNo = getIntent().getStringExtra("phoneNumber");

        String mobileNo = "+91 " + strMobileNo;

        verifyNumber.setText(mobileNo);

        setUpInputOTP();

        verifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InputCode1.getText().toString().trim().isEmpty() || InputCode2.getText().toString().trim().isEmpty() ||
                        InputCode3.getText().toString().trim().isEmpty() || InputCode4.getText().toString().trim().isEmpty() ||
                        InputCode5.getText().toString().trim().isEmpty() || InputCode6.getText().toString().trim().isEmpty()) {
                    Toast.makeText(ForgetPasswordVerifyActivity.this, "Please Enter Valid OTP", Toast.LENGTH_SHORT).show();
                }
                String OTPCode = InputCode1.getText().toString() + InputCode2.getText().toString() + InputCode3.getText().toString()
                        + InputCode4.getText().toString() + InputCode5.getText().toString() + InputCode6.getText().toString();

                if (strVerifyCode != null) {
                    progressDialog = new ProgressDialog(ForgetPasswordVerifyActivity.this);
                    progressDialog.setTitle("Verify OTP");
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(strVerifyCode, OTPCode);

                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgetPasswordVerifyActivity.this, "Verification Successfully Done...", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent i = new Intent(ForgetPasswordVerifyActivity.this, ForgetPassword.class);
                                i.putExtra("mobile", strMobileNo);
                                startActivity(i);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(ForgetPasswordVerifyActivity.this, "Invalid OTP, Verification Fail", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + strMobileNo, 60, TimeUnit.SECONDS, ForgetPasswordVerifyActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressDialog.dismiss();
                                Toast.makeText(ForgetPasswordVerifyActivity.this, "Verification Completed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressDialog.dismiss();
                                Toast.makeText(ForgetPasswordVerifyActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String newVerificationCode, @NonNull
                            PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                strVerifyCode = newVerificationCode;
                                Toast.makeText(ForgetPasswordVerifyActivity.this, "OTP Resent", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void setUpInputOTP() {

        InputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    InputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        InputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    InputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        InputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    InputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        InputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    InputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        InputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    InputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }
}