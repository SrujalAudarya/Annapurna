package com.example.internshipproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class RegisterMobileNoActivity extends AppCompatActivity {

    AppCompatButton verify_button;
    EditText register_mobile_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mobile_no);

        verify_button = findViewById(R.id.verify_button);
        register_mobile_no = findViewById(R.id.register_mobile_no);

        verify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (register_mobile_no.getText().toString().isEmpty()) {
                    register_mobile_no.setError("Please Enter Mobile No.");
                } else if (register_mobile_no.getText().toString().length() != 10) {
                    register_mobile_no.setError("Mobile No. must be 10 digit");
                } else {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + register_mobile_no.getText().toString(), 60, TimeUnit.SECONDS, RegisterMobileNoActivity.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    Toast.makeText(RegisterMobileNoActivity.this, "Verification Completed", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    Toast.makeText(RegisterMobileNoActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String verificationCode, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    Intent i = new Intent(RegisterMobileNoActivity.this, ForgetPasswordVerifyActivity.class);
                                    i.putExtra("verificationCode", verificationCode);
                                    i.putExtra("phoneNumber", register_mobile_no.getText().toString());
                                    startActivity(i);
                                }
                            });
                }
            }
        });
    }
}