package com.jarrebnnee.connect;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jarrebnnee.connect.Service.SaveSharedPrefrence;

public class MainActivity extends AppCompatActivity {

    LinearLayout layout_buyer,layout_seller;
    TextView tvLogw,tvSeller,tvOr,tvBuyer;
    SaveSharedPrefrence saveSharedPrefrence;
    boolean flag = false;
    public static final int PERMISSION_REQUEST_CODE=20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saveSharedPrefrence = new SaveSharedPrefrence();

        layout_seller = (LinearLayout)findViewById(R.id.layout_seller);
        layout_buyer = (LinearLayout)findViewById(R.id.layout_buyer);
        tvLogw = (TextView)findViewById(R.id.tvLogw);
        tvSeller = (TextView)findViewById(R.id.tvSeller);
        tvBuyer = (TextView)findViewById(R.id.tvBuyer);
        tvOr = (TextView)findViewById(R.id.tvOr);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/16023_futuran_0.ttf");
        tvLogw.setTypeface(custom_font);
        tvSeller.setTypeface(custom_font);
        tvBuyer.setTypeface(custom_font);
        tvOr.setTypeface(custom_font);

        if (Build.VERSION.SDK_INT >= 23) {
            if (!isAllowed()) {
                requestPermission();
            }
        } else {
            flag = true;
        }

        layout_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (Build.VERSION.SDK_INT >= 23) {
                    if (isAllowed()) {
                        saveSharedPrefrence.saveUserType(getApplicationContext(), "2");
                        Toast.makeText(MainActivity.this, "UserType: 2", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        requestPermission();
                    }
                } else {
                    saveSharedPrefrence.saveUserType(getApplicationContext(), "2");
                    Toast.makeText(MainActivity.this, "UserType: 2", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(i);
                    finish();
                }*/
                if (flag) {
                    saveSharedPrefrence.saveUserType(getApplicationContext(), "2");
                 //   Toast.makeText(MainActivity.this, "UserType: 2", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(i);
                    finish();
                } else {
             //       Toast.makeText(MainActivity.this, "You need to provide permission first", Toast.LENGTH_SHORT).show();
                    requestPermission();
                }
            }
        });
        layout_buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (Build.VERSION.SDK_INT >= 23) {
                    if (isAllowed()) {
                        saveSharedPrefrence.saveUserType(getApplicationContext(), "1");
                        Toast.makeText(MainActivity.this, "UserType: 1", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        requestPermission();
                    }
                } else {
                    saveSharedPrefrence.saveUserType(getApplicationContext(),"1");
                    Toast.makeText(MainActivity.this, "UserType: 1", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(),SignInActivity.class);
                    startActivity(i);
                    finish();
                }*/
                if (flag) {
                    saveSharedPrefrence.saveUserType(getApplicationContext(), "1");
              //      Toast.makeText(MainActivity.this, "UserType: 1", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(i);
                    finish();
                } else {
            //        Toast.makeText(MainActivity.this, "You need to provide permission first", Toast.LENGTH_SHORT).show();
                    requestPermission();
                }

            }
        });

    }

    private void requestPermission() {
        /*if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {*/
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        //}

    }

    private boolean isAllowed() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //     Log.e("value", "Permission Granted, Now you can save image .");
                    /*Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(i);
                    finish();*/
                    flag = true;
                    Toast.makeText(MainActivity.this, "Granted", Toast.LENGTH_SHORT).show();

                } else {
                    //      Log.e("value", "Permission Denied, You cannot save image.");
                    flag = false;
                    Toast.makeText(MainActivity.this, "Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
