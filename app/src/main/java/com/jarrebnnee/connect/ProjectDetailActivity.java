package com.jarrebnnee.connect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.TextView;
import android.widget.Toast;

import com.jarrebnnee.connect.Adapter.ApplyerListAdapter;
import com.jarrebnnee.connect.Service.GetSet;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.ServiceHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProjectDetailActivity extends AppCompatActivity {
    ImageView ivBack;
    TextView tvTitle, tvNoApply, tvApply;
    Toolbar toolbar;
    ImageView iv1;
    TextView tvName, tvService, tvPrice, tvAdate, tvDescription, tvDes;

    String user_id, js_id, js_title, action, u_type;

    SaveSharedPrefrence sharedPrefrence;
    GetSet getSet;
    Date date;
    ArrayList<HashMap<String, String>> list;
    HashMap<String, String> map;
    RecyclerView rcApply;
    String lang;
    ApplyerListAdapter reviewListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        getSet = GetSet.getInstance();
        sharedPrefrence = new SaveSharedPrefrence();
        lang = sharedPrefrence.getlang(getApplicationContext());
        user_id = sharedPrefrence.getUserID(getApplicationContext());
        js_id = getSet.getjs_id();
        js_title = getSet.getjs_title();
        u_type = sharedPrefrence.getUserType(getApplicationContext());

        iv1=(ImageView)findViewById(R.id.iv1);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "" + "</font>")));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        tvName = (TextView) findViewById(R.id.tvName);
        tvService = (TextView) findViewById(R.id.tvService);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvAdate = (TextView) findViewById(R.id.tvAdate);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvNoApply = (TextView) findViewById(R.id.tvNoApply);
        tvDes = (TextView) findViewById(R.id.tvDes);
        iv1 = (ImageView) findViewById(R.id.iv1);
        rcApply = (RecyclerView) findViewById(R.id.rcApply);
        tvApply = (TextView) findViewById(R.id.tvApply);

        Typeface custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/16023_futuran_0.ttf");
        tvTitle.setTypeface(custom_font);
        tvName.setTypeface(custom_font);
        tvService.setTypeface(custom_font);
        tvPrice.setTypeface(custom_font);
        tvAdate.setTypeface(custom_font);
        tvDescription.setTypeface(custom_font);
        tvDes.setTypeface(custom_font);
        tvNoApply.setTypeface(custom_font);
        tvApply.setTypeface(custom_font);

        tvTitle.setText(js_title);



        list = new ArrayList<HashMap<String, String>>();


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (u_type.equals("2")) {
            action = "getSellerJobDetail";
        } else {
            action = "getJobDetail";
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//we are connected to a network
            //  url=URLCollection.LoginUrl+"&u_email="+email+"&u_password="+password+"&device_type=0&device_token="+regId;
            //   SignInUrlCall(url);
            new JobService(getApplicationContext()).execute();
        } else {
            Toast.makeText(getApplicationContext(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
        }
    }


    class JobService extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg, js_id1, js_user_id, js_category_id, js_title, js_description, js_price, js_appointment_date, js_image, js_radius,
                js_status, js_created, js_modified, u_id, u_first_name, u_last_name, u_email, u_password, u_address, u_gender, u_latitute, u_longitute, u_city,
                u_phone, u_postcode, u_country, u_img, u_status, u_type, u_is_notification_sound, u_seller_services, u_created, u_modified, c_id,
                c_images, c_type, c_title, c_is_parent_id, c_total_services, c_created, s_modified;


        JobService(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ProjectDetailActivity.this);
            pDialog.setMessage(getResources().getString(R.string.pdialog));
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub

            String uri = Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", action));
            nameValuePairs.add(new BasicNameValuePair("js_id", js_id));
            nameValuePairs.add(new BasicNameValuePair("u_id", user_id));
            nameValuePairs.add(new BasicNameValuePair("lang", lang));

            ServiceHandler handler = new ServiceHandler();
            String jsonSt = handler.makeServiceCall(
                    uri, ServiceHandler.POST, nameValuePairs);

            Log.d("nameValuePairs: ", "> " + nameValuePairs);
            Log.d("Response: ", "> " + jsonSt);

            if (jsonSt != null) {

                try {
                //    pDialog.dismiss();
                    JSONObject jsonObj = new JSONObject(jsonSt);
                    status = jsonObj.getString("status");
                    msg = jsonObj.getString("message");
                    Log.e("msg", msg);
                    JSONArray data = jsonObj.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        js_id1 = object.getString("js_id");
                        js_user_id = object.getString("js_user_id");
                        js_category_id = object.getString("js_category_id");
                        js_title = object.getString("js_title");
                        js_description = object.getString("js_description");
                        js_price = object.getString("js_price");
                        js_appointment_date = object.getString("js_appointment_date");
                        js_image = object.getString("js_image");
                        js_radius = object.getString("js_radius");

                        js_status = object.getString("js_status");
                        js_created = object.getString("js_created");
                        js_modified = object.getString("js_modified");
                        u_id = object.getString("u_id");
                        u_first_name = object.getString("u_first_name");
                        u_last_name = object.getString("u_last_name");
                        u_email = object.getString("u_email");
                        u_password = object.getString("u_password");
                        u_address = object.getString("u_address");
                        u_gender = object.getString("u_gender");
                        u_latitute = object.getString("u_latitute");
                        u_longitute = object.getString("u_longitute");
                        u_city = object.getString("u_city");

                        u_phone = object.getString("u_phone");
                        u_postcode = object.getString("u_postcode");
                        u_country = object.getString("u_country");
                        u_img = object.getString("u_img");
                        u_status = object.getString("u_status");
                        u_type = object.getString("u_type");
                        u_is_notification_sound = object.getString("u_is_notification_sound");
                        u_seller_services = object.getString("u_seller_services");
                        u_created = object.getString("u_created");
                        u_modified = object.getString("u_modified");
                        c_id = object.getString("c_id");
                        c_images = object.getString("c_images");
                        c_type = object.getString("c_type");
                        c_title = object.getString("c_title");
                        c_is_parent_id = object.getString("c_is_parent_id");
                        c_total_services = object.getString("c_total_services");
                        c_created = object.getString("c_created");
                        s_modified = object.getString("s_modified");
                        if (action.equals("getJobDetail")) {

                            JSONArray applyers = object.getJSONArray("applyers");
                            for (int j = 0; j < applyers.length(); j++) {

                                JSONObject object1 = applyers.getJSONObject(j);
                                String u_id = object1.getString("u_id");
                                String u_first_name = object1.getString("u_first_name");
                                String u_last_name = object1.getString("u_last_name");
                                String u_img = object1.getString("u_img");
                                String jsr_comment_text = object1.getString("jsr_comment_text");
                                String jsr_apply_price = object1.getString("jsr_apply_price");
                                String jsr_apply_text = object1.getString("jsr_apply_text");
                                String jsr_id  = object1.getString("jsr_id");
                                String jsr_status  = object1.getString("jsr_status");
                                String total_review = object1.getString("total_review");
                                String avg_rating = object1.getString("avg_rating");
                                map = new HashMap<String, String>();
                                map.put("u_id",u_id);
                                map.put("u_first_name",u_first_name);
                                map.put("u_last_name",u_last_name);
                                map.put("u_img",u_img);
                                map.put("jsr_comment_text",jsr_comment_text);
                                map.put("jsr_apply_price", jsr_apply_price);
                                map.put("jsr_apply_text", jsr_apply_text);
                                map.put("jsr_rating",avg_rating);
                                map.put("total_review",total_review);
                                map.put("jsr_id",jsr_id);
                                map.put("jsr_status",jsr_status);
                                list.add(map);
                           //     reviewListAdapter.notifyDataSetChanged();
                            }
                        }
                    }


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
            if (status.equals("0")) {

                reviewListAdapter = new ApplyerListAdapter(ProjectDetailActivity.this, list);
                rcApply.setAdapter(reviewListAdapter);

                tvName.setText(js_title);

                tvDescription.setText(js_description);
                tvPrice.setText(getResources().getString(R.string.pr) + js_price);
                tvService.setText(getResources().getString(R.string.serv) + c_title);
                Picasso.with(getApplicationContext()).load(js_image).into(iv1);
                iv1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      /*  iv1.setDrawingCacheEnabled(true);
                        iv1.buildDrawingCache();
                        Bitmap bitmap = Bitmap.createBitmap(iv1.getDrawingCache());
               *//*iv1.buildDrawingCache();
                Bitmap bitmap=iv1.getDrawingCache();*/
                        Intent in=new Intent(ProjectDetailActivity.this,FullImageActivity.class);
                        in.putExtra("Image",js_image);
                        startActivity(in);
                    }
                });
                try {
                    String time = js_appointment_date;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    date = sdf.parse(time);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                String s = sdf1.format(date.getTime());
                tvAdate.setText(getResources().getString(R.string.apdate) + s);
                if (u_type.equals("2")) {
                    tvNoApply.setVisibility(View.GONE);
                    rcApply.setVisibility(View.GONE);
                } else {
                   /* tvNoApply.setVisibility(View.GONE);
                    rcApply.setVisibility(View.GONE);
                    tvApply.setVisibility(View.GONE);*/
                    if (list.size() == 0) {
                        tvNoApply.setVisibility(View.VISIBLE);
                        rcApply.setVisibility(View.GONE);
                    } else {
                        tvNoApply.setVisibility(View.GONE);
                        rcApply.setVisibility(View.VISIBLE);

                    }
                }

            } else {

            }
        }
    }

}
