package com.example.internshipproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.internshipproject.common.Urls;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MyProfileActivity extends AppCompatActivity {

    String strName, strEmail, strPhone;

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;

    ProgressDialog dialog;

    SharedPreferences preferences;
    String strUsername;
    ImageView profileImg;
    AppCompatButton btneditData;
    TextView tvName, tvEmail, tvPhone, tvUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // Inside your activity
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF5722")));

        tvName = findViewById(R.id.name);
        tvEmail = findViewById(R.id.email_id);
        tvPhone = findViewById(R.id.mobile_no);
        tvUsername = findViewById(R.id.username);
        profileImg = findViewById(R.id.profileimg);
        btneditData = findViewById(R.id.btnEditData);

        preferences = PreferenceManager.getDefaultSharedPreferences(MyProfileActivity.this);
        strUsername = preferences.getString("Username", "");

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(MyProfileActivity.this, googleSignInOptions);

        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);

        if (googleSignInAccount != null) {
            strName = googleSignInAccount.getDisplayName();
            strEmail = googleSignInAccount.getEmail();

            Glide.with(MyProfileActivity.this)
                    .load(googleSignInAccount.getPhotoUrl())
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.notfound)               // Optional error image
                    .into(profileImg);
            tvName.setText(strName);
            tvEmail.setText(strEmail);
            getSupportActionBar().setTitle(strName);

            btneditData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MyProfileActivity.this, UpdateProfileActivity.class);
                    i.putExtra("name", strName);
                    i.putExtra("email", strEmail);
                    i.putExtra("mobile", "");
                    i.putExtra("username", "");
                    startActivity(i);
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        dialog = new ProgressDialog(MyProfileActivity.this);
        dialog.setTitle("My Profile");
        dialog.setMessage("Please Wait...");
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        getDetails();
    }

    public void getDetails() {
        AsyncHttpClient client = new AsyncHttpClient(); // client sever communication
        RequestParams params = new RequestParams(); // to send data to web service using this class

        params.put("username", strUsername); // putting data in params

        client.post(Urls.getDetailsWebService, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                dialog.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("getMyDetails");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String image = jsonObject.getString("image");
                        String name = jsonObject.getString("name");
                        String mobileNo = jsonObject.getString("phone");
                        String email = jsonObject.getString("email");
                        String username = jsonObject.getString("username");

                        tvName.setText(name);
                        tvEmail.setText(email);
                        tvPhone.setText(mobileNo);
                        tvUsername.setText(username);

                        Glide.with(MyProfileActivity.this)
                                .load(Urls.imageUrls + image) // URL or URI of the image
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .error(R.drawable.notfound)               // Optional error image
                                .into(profileImg);

                        btneditData.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(MyProfileActivity.this, UpdateProfileActivity.class);
                                i.putExtra("name", name);
                                i.putExtra("mobile", mobileNo);
                                i.putExtra("email", email);
                                i.putExtra("username", username);
                                startActivity(i);
                            }
                        });
                        getSupportActionBar().setTitle(name);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(MyProfileActivity.this, "Sever Error...", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}