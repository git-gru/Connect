package com.jarrebnnee.connect;

import android.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.jarrebnnee.connect.Helper.Device.Device;
import com.jarrebnnee.connect.Service.Config;
import com.jarrebnnee.connect.Service.NotificationUtils;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.TrackGPS;
import com.google.firebase.database.Logger;
import com.google.firebase.messaging.FirebaseMessaging;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SplashActivity extends AppCompatActivity {

    String token;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String action, mr_device_type, mr_device_token;
    SaveSharedPrefrence sharedPreferences;

    public static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        sharedPreferences = new SaveSharedPrefrence();

        //  =================================================================================
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.jarrebnnee.connect", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
        //==================================================================================

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(7000);
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//we are connected to a network
                        //  url=URLCollection.LoginUrl+"&u_email="+email+"&u_password="+password+"&device_type=0&device_token="+regId;
                        //   SignInUrlCall(url);

                        if (Build.VERSION.SDK_INT >= 23) {
                            if (isAllowed()) {
                                DeviceRegisterRetrofit();
                            } else {
                                requestPermission();
                            }
                        } else {
                            DeviceRegisterRetrofit();
                        }


                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
                    }
                    /*Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();*/
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                }
            }
        };
        timerThread.start();
        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

// checking for type intent filter
                Log.e("iff", "iff");
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
// gcm successfully registered
// now subscribe to `global` topic to receive app wide notifications
                    Log.e("iff enter", "iff enter");
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    //displayFirebaseRegId();
                    DeviceRegisterRetrofit();


                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
// new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

//txtMessage.setText(message);
                }
            }
        };
       // displayFirebaseRegId();


    }
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(SplashActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_COARSE_LOCATION);
        }

    }
    private boolean isAllowed() {
        int result = ContextCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                 /*   Toast.makeText(this, "Prmission Granded!", Toast.LENGTH_SHORT).show();*/
                    DeviceRegisterRetrofit();
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    void DeviceRegisterRetrofit() {
        String id = sharedPreferences.getUserID(getApplicationContext());
        if(id!="")
        {
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);
            finish();
        }else {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
       /* String url1 = Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(url1).build();
        RestInterface restInterface = adapter.create(RestInterface.class);

        HashMap<String, String> prm = new HashMap<String, String>();

        action = "deviceragister";
        mr_device_type = "0";
        mr_device_token = sharedPreferences.getDeviceToken(getApplicationContext());
        prm.put("action", action);
        prm.put("mr_device_type", mr_device_type);
        prm.put("mr_device_token", mr_device_token);

        Log.e("prm", "" + prm);
        Log.e("adapter", "" + adapter.toString());

        restInterface.getDeviceResponce(prm, new Callback<Device>()

        {
            @Override
            public void success(Device model, Response response) {
                // progressDialog.dismiss();
                Log.e("get status", "" + model.getStatus());
                Log.e("get message", "" + model.getMessage());
                if (model.getStatus().equals("0")) {
                    mr_device_token = sharedPreferences.getDeviceToken(getApplicationContext());

                    String id = sharedPreferences.getUserID(getApplicationContext());

                    if(id!="")
                    {
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(i);
                        finish();
                    }else {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }


                } else {
                    //Toast.makeText(getApplicationContext(), model.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {

                String merror = error.getMessage();
                Log.e("errer", "" + merror);
                Toast.makeText(getApplicationContext(), "I’m sorry, something has gone wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
        });*/

    }


    //********** For notification (*************************.//
  /*  private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e("Splash", "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId)) {
          //  sharedPreferences.saveDeviceToken(getApplicationContext(),regId);
            Toast.makeText(getApplicationContext(), "Firebase Reg Id: " + regId, Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(getApplicationContext(), "Firebase Reg Id is not received yet!", Toast.LENGTH_LONG).show();
        }
    }*/
    @Override
    protected void onResume() {
        super.onResume();

// register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

// register new push message receiver
// by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

// clear the notification area when the app is opened
      //  NotificationUtils.clearNotifications(getApplicationContext());
// startRepeatingTask();


    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
// stopRepeatingTask();
        super.onPause();
// stopRepeatingTask();
    }

}
