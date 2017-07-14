package com.jarrebnnee.connect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jarrebnnee.connect.Adapter.NotificationListAdapter;
import com.jarrebnnee.connect.Adapter.SellerJobListAdapter;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.ServiceHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView ivBack;
    TextView tvTitle, tvno;
    String u_id;
    SaveSharedPrefrence prefrence;
    ArrayList<HashMap<String, String>> list;
    HashMap<String, String> map;
    RecyclerView lvNotify;
    String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvno = (TextView) findViewById(R.id.tvno);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "" + "</font>")));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        prefrence = new SaveSharedPrefrence();
        lang = prefrence.getlang(getApplicationContext());
        u_id = prefrence.getUserID(getApplicationContext());
        Typeface custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/16023_futuran_0.ttf");
        tvTitle.setTypeface(custom_font);
        tvno.setTypeface(custom_font);
        lvNotify = (RecyclerView) findViewById(R.id.lvNotify);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new getAllNotification(getApplicationContext()).execute();
    }

    class getAllNotification extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg, cn_id, cn_device_id, cn_user_id, cn_title, cn_description, cn_device_type, cn_status, cn_noti_type, cn_noti_type_id,
                cn_created, cn_modified, totalRecord;

        getAllNotification(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(NotificationActivity.this);
            pDialog.setMessage(getResources().getString(R.string.pdialog));
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            list = new ArrayList<HashMap<String, String>>();
            String uri = Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", "getAllNotification"));
            nameValuePairs.add(new BasicNameValuePair("u_id", u_id));
            nameValuePairs.add(new BasicNameValuePair("lang", lang));


            ServiceHandler handler = new ServiceHandler();
            String jsonSt = handler.makeServiceCall(
                    uri, ServiceHandler.POST, nameValuePairs);
            Log.d("nameValuePairs: ", "> " + nameValuePairs);
            Log.d("Response: ", "> " + jsonSt);

            if (jsonSt != null) {

                try {

                    JSONObject jsonObj = new JSONObject(jsonSt);
                    status = jsonObj.getString("status");
                    msg = jsonObj.getString("message");
                    Log.e("msg", msg);
                    JSONArray data = jsonObj.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        cn_id = object.getString("cn_id");
                        cn_device_id = object.getString("cn_device_id");
                        cn_user_id = object.getString("cn_user_id");
                        cn_title = object.getString("cn_title");
                        cn_description = object.getString("cn_description");
                        cn_device_type = object.getString("cn_device_type");
                        cn_status = object.getString("cn_status");
                        cn_noti_type = object.getString("cn_noti_type");
                        cn_noti_type_id = object.getString("cn_noti_type_id");
                        cn_created = object.getString("cn_created");
                        cn_modified = object.getString("cn_modified");

                        map = new HashMap<String, String>();
                        map.put("cn_id", cn_id);
                        map.put("cn_device_id", cn_device_id);
                        map.put("cn_user_id", cn_user_id);
                        map.put("cn_title", cn_title);
                        map.put("cn_description", cn_description);
                        map.put("cn_status", cn_status);
                        map.put("cn_noti_type", cn_noti_type);
                        map.put("cn_noti_type_id", cn_noti_type_id);
                        map.put("cn_created", cn_created);
                        map.put("cn_modified", cn_modified);
                        map.put("cn_device_type", cn_device_type);
                        list.add(map);
                    }
                    totalRecord = jsonObj.getString("totalRecord");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (msg.equals("No notification available")) {
                tvno.setVisibility(View.VISIBLE);
                lvNotify.setVisibility(View.GONE);
            } else {
                tvno.setVisibility(View.GONE);
                lvNotify.setVisibility(View.VISIBLE);
                NotificationListAdapter adapter = new NotificationListAdapter(NotificationActivity.this, list);
                lvNotify.setAdapter(adapter);
            }

        }
    }
}
