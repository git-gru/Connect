package com.jarrebnnee.connect;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.jarrebnnee.connect.Service.SaveSharedPrefrence;

public class NotiSettingsActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView ivBack;
    TextView tv_noti, tvTitle;
    Switch switch_notification;
    LinearLayout llayout;
    EditText et_noti_settings;
    Button btn_submit;
    TextView tv_current;
    SaveSharedPrefrence prefrence;
    String current_timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti_settings);

        llayout = (LinearLayout) findViewById(R.id.llayout);
        tv_noti = (TextView) findViewById(R.id.tv_noti);
        switch_notification = (Switch) findViewById(R.id.switch_noti);
        et_noti_settings = (EditText) findViewById(R.id.et_noti_settings);
        btn_submit = (Button) findViewById(R.id.btn_settings_submit);
        tv_current = (TextView) findViewById(R.id.tv_noti_current);

        prefrence = new SaveSharedPrefrence();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "" + "</font>")));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Typeface custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/16023_futuran_0.ttf");
        tvTitle.setTypeface(custom_font);
        tv_noti.setTypeface(custom_font);
        et_noti_settings.setTypeface(custom_font);
        btn_submit.setTypeface(custom_font);
        tv_current.setTypeface(custom_font);

        final Intent intent = new Intent(NotiSettingsActivity.this, LocationUpdaterService.class);

        if (switch_notification.isChecked()) {
            llayout.setVisibility(View.VISIBLE);
        } else if (!switch_notification.isChecked()) {
            llayout.setVisibility(View.GONE);
        }

        current_timer = prefrence.getNotificationSeconds(NotiSettingsActivity.this);

        tv_current.setText("Current notification time : "+current_timer);

        switch_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    llayout.setVisibility(View.VISIBLE);
                    if (!isMyServiceRunning(LocationUpdaterService.class)) {
                        Log.e("service", "from notification starting");
                        startService(intent);
                    }
                }else if(!isChecked){
                    llayout.setVisibility(View.GONE);
                    if (isMyServiceRunning(LocationUpdaterService.class)) {
                        stopService(intent);
                        Log.e("service", "from notification stopping");
                    }
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String seconds = et_noti_settings.getText().toString().trim();
                prefrence.saveNotificationSeconds(NotiSettingsActivity.this, ""+seconds);
                if (!isMyServiceRunning(LocationUpdaterService.class)) {
                    Log.e("service", "from notification starting");
                    startService(intent);
                } else {
                    stopService(intent);
                    Log.e("service", "stopped from notification");
                    startService(intent);
                    Log.e("service", "restarted from notification");
                }
                Toast.makeText(NotiSettingsActivity.this, getString(R.string.servicetimerstarted), Toast.LENGTH_LONG).show();
                finish();
            }
        });


    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
