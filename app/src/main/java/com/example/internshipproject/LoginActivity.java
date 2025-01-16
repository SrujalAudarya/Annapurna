package com.example.internshipproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.internshipproject.common.NetworkChangeListener;
import com.example.internshipproject.common.Urls;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    TextView new_user, forget_pass;
    EditText username, password;
    AppCompatButton login_btn, signInBtn;
    ImageView eye;
    boolean isPasswordVisible = false;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        new_user = findViewById(R.id.new_user);
        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        login_btn = findViewById(R.id.login_btn);
        signInBtn = findViewById(R.id.signInWithGoogle);
        eye = findViewById(R.id.eye_check);
        forget_pass = findViewById(R.id.forget_password);

        preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        editor = preferences.edit();

        if (preferences.getBoolean("Login", false)) {
            Intent in = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(in);
        }

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this, googleSignInOptions);

        eye.setOnClickListener(v -> {
            if (isPasswordVisible) {
                eye.setImageResource(R.drawable.eye_icon); // Set the eye icon to 'closed'
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                isPasswordVisible = false; // Update the state
            } else {
                eye.setImageResource(R.drawable.disable_eye); // Set the eye icon to 'open'
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                isPasswordVisible = true; // Update the state
            }
        });

        login_btn.setOnClickListener(v -> {
            if (username.getText().toString().isEmpty()) {
                username.setError("Please Enter Username");
            } else if (username.getText().toString().length() < 8) {
                username.setError("Username must be greater then 8");
            } else if (password.getText().toString().isEmpty()) {
                password.setError("Please Enter your Password");
            } else if (password.getText().toString().length() < 8) {
                password.setError("Password must be Greater then 8 or Equal");
            } else {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle("Please Wait...");
                progressDialog.setMessage("Login Under Process");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                userLogin();
            }
        });

        forget_pass.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterMobileNoActivity.class);
            startActivity(intent);
        });

        new_user.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

    private void signIn() {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            } catch (ApiException e) {
                Toast.makeText(this, "Something Went Wrong...", Toast.LENGTH_SHORT).show();
            }
        }
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

    public void userLogin() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username", username.getText().toString());
        params.put("password", password.getText().toString());


        client.post(Urls.loginUserWebService, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressDialog.dismiss();
                try {
                    String status = response.getString("success");
                    if (status.equals("1")) {
                        Toast.makeText(LoginActivity.this, "Login Successfully Done...", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        editor.putString("Username", username.getText().toString()).commit();
                        editor.putString("Password", password.getText().toString()).commit();
                        editor.putBoolean("Login", true).commit();
                        startActivity(i);
                    } else {
                        Toast.makeText(LoginActivity.this, "Username or Password are Invalid", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(LoginActivity.this, "Server Error..!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}