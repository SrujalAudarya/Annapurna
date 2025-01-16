package com.example.internshipproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.internshipproject.common.NetworkChangeListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottom_nav;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    boolean doubletab = false;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String str_username, str_password;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        str_username = getIntent().getStringExtra("Username");
        str_password = getIntent().getStringExtra("Password");

        preferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        editor = preferences.edit();

        preferences = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean isFirstTime = preferences.getBoolean("firsttime", true);

        if (isFirstTime) {
            welcome();
        }

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF5722")));

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(HomeActivity.this, googleSignInOptions);

        bottom_nav = findViewById(R.id.home_nav);
        bottom_nav.setOnNavigationItemSelectedListener(this);
        bottom_nav.setSelectedItemId(R.id.home);
    }

    private void welcome() {
        AlertDialog.Builder ab = new AlertDialog.Builder(HomeActivity.this);
        ab.setTitle("Hotel Annapurna");
        ab.setIcon(R.drawable.hotelannapurna);
        ab.setMessage("Welcome to your App....");
        ab.setPositiveButton("Thank You...", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create().show();

        editor = preferences.edit();
        editor.putBoolean("firsttime", false).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profile) {
            Intent i = new Intent(HomeActivity.this, MyProfileActivity.class);
            startActivity(i);
        }
        if (item.getItemId() == R.id.about) {
            Intent i = new Intent(HomeActivity.this, AboutActivity.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.contact_us) {
            Intent i = new Intent(HomeActivity.this, ContactUsActivity.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.setting) {
            Intent i = new Intent(HomeActivity.this, SettingActivity.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.logout) {
            logout();
        } else if (item.getItemId() == R.id.sign_out) {
            signOut();
        }
        return true;
    }

    private void signOut() {
        AlertDialog.Builder ad = new AlertDialog.Builder(HomeActivity.this);
        ad.setTitle("Sign Out");
        ad.setIcon(R.drawable.logout);
        ad.setMessage("Are you Sure you want To Sign Out");
        ad.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        ad.setNegativeButton("Sign Out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                finish();
            }
        }).create().show();
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

    private void logout() {
        AlertDialog.Builder ad = new AlertDialog.Builder(HomeActivity.this);
        ad.setTitle("Logout");
        ad.setIcon(R.drawable.logout);
        ad.setMessage("Are you Sure you want To Logout");
        ad.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        ad.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                editor.putBoolean("Login", false).commit();
                startActivity(i);
                finish();
            }
        }).create().show();
    }

    HomeFragment homeFragment = new HomeFragment();
    MenuFragment menuFragment = new MenuFragment();
    Order_historyFragment historyFragment = new Order_historyFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment, homeFragment).commit();
        }
        if (item.getItemId() == R.id.menu) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment, menuFragment).commit();
        }
        if (item.getItemId() == R.id.order_history) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment, historyFragment).commit();
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        if (doubletab) {
            super.onBackPressed();
        } else {
            doubletab = true;
            Toast.makeText(HomeActivity.this, "Press again to exit app", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubletab = false;
                }
            }, 2000); // 2000 ms delay
        }
    }
}