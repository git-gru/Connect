package com.jarrebnnee.connect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jarrebnnee.connect.Adapter.ReviewListAdapter;
import com.jarrebnnee.connect.Service.GetSet;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.ServiceHandler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SellerDetailNotificationActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextView tvName, tvLoc, tvRating, tvDes, tvDescription, tvReviews, tvService, tvSer, tvNoReviews;
    Button btnCall, btnSms;
    RecyclerView rcReview;
    Toolbar toolbar;
    ImageView ivBack, iv1;
    TextView tvTitle;
    String sp_id, sp_name;
    GetSet getSet;
    ArrayList<HashMap<String, String>> slist;
    HashMap<String, String> smap;
    ArrayList<HashMap<String, String>> reviewlist;
    HashMap<String, String> rmap;
    ReviewListAdapter reviewListAdapter;
    RatingBar rat;
    String lang;
    SaveSharedPrefrence prefrence;
    String number, u_latitute, u_longitute;
    GoogleMap mMap;
    Double lat,lng;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_detail_notification);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        setSupportActionBar(toolbar);
        getSet = GetSet.getInstance();
        prefrence = new SaveSharedPrefrence();
        lang = prefrence.getlang(getApplicationContext());

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "" + "</font>")));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sp_id = getSet.getc_id();

        tvTitle.setText(sp_name);

        tvName = (TextView) findViewById(R.id.tvName);
        tvLoc = (TextView) findViewById(R.id.tvLoc);
        tvRating = (TextView) findViewById(R.id.tvRating);
        tvDes = (TextView) findViewById(R.id.tvDes);
        tvReviews = (TextView) findViewById(R.id.tvReviews);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvService = (TextView) findViewById(R.id.tvService);
        tvSer = (TextView) findViewById(R.id.tvSer);
        rcReview = (RecyclerView) findViewById(R.id.rcReview);
        iv1 = (ImageView) findViewById(R.id.iv1);
        tvNoReviews = (TextView) findViewById(R.id.tvNoReviews);
        rat = (RatingBar) findViewById(R.id.rat);

        btnCall = (Button) findViewById(R.id.btnCall);
        btnSms = (Button) findViewById(R.id.btnSms);


        Typeface custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/16023_futuran_0.ttf");
        tvLoc.setTypeface(custom_font);
        tvRating.setTypeface(custom_font);
        tvDescription.setTypeface(custom_font);
        btnCall.setTypeface(custom_font);
        btnSms.setTypeface(custom_font);

        tvTitle.setTypeface(custom_font);

        tvSer.setTypeface(custom_font);
        tvNoReviews.setTypeface(custom_font);

        Typeface custom_fontbold = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/tt0144m.ttf");
        tvName.setTypeface(custom_fontbold);
        tvDes.setTypeface(custom_fontbold);
        tvReviews.setTypeface(custom_fontbold);
        tvService.setTypeface(custom_fontbold);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.length() > 3) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
                } else {
                    Toast.makeText(SellerDetailNotificationActivity.this, "Sorry, we do not have contact details for this seller", Toast.LENGTH_SHORT).show();
                }

            }
        });




        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("call", "clicked : "+number);
                if (number.length() > 3) {
                //    Toast.makeText(SellerDetailNotificationActivity.this, "Dial: " + number, Toast.LENGTH_SHORT).show();
                    try {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + number));
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(callIntent);
                    } catch (Exception e) {
                        Log.e("ssssss", "dialer exception: " + e);
                    }

/*
                    if (ActivityCompat.checkSelfPermission(SellerDetailNotificationActivity.this,
                            android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }*/

                }

                else {
                    Toast.makeText(SellerDetailNotificationActivity.this, "Sorry, we do not have contact details for this seller", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//we are connected to a network
            //  url=URLCollection.LoginUrl+"&u_email="+email+"&u_password="+password+"&device_type=0&device_token="+regId;
            //   SignInUrlCall(url);
            new ServiceDetail(getApplicationContext()).execute();
        } else {
            Toast.makeText(getApplicationContext(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        lat = Double.valueOf(u_latitute);
        lng = Double.valueOf(u_longitute);

        marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat,lng))
                .title("Seller location")
                .zIndex(2.0f));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(lat, lng)).zoom(15).build();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    class ServiceDetail extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg, u_id, u_first_name, u_last_name, u_email, u_password, u_address, u_city, u_phone, u_postcode, u_country, u_description, u_img, u_status, u_type, u_is_notification_sound, u_seller_services, u_created, u_modified,
                s_id, s_city_id, s_country_id, s_category_id, s_user_id, s_created, s_modified, city_name, avg_rating, total_review, country_name, categoty_name;

        ServiceDetail(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SellerDetailNotificationActivity.this);
            pDialog.setMessage(getResources().getString(R.string.pdialog));
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            slist = new ArrayList<HashMap<String, String>>();
            reviewlist = new ArrayList<HashMap<String, String>>();
            String uri = Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", "getServiceProviderDetail"));
            nameValuePairs.add(new BasicNameValuePair("sp_id", sp_id));
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
                        u_id = object.getString("u_id");
                        u_first_name = object.getString("u_first_name");
                        u_last_name = object.getString("u_last_name");
                        u_email = object.getString("u_email");
                        u_password = object.getString("u_password");
                        u_address = object.getString("u_address");
                        u_latitute = object.getString("u_latitute");
                        u_longitute = object.getString("u_longitute");
                        u_city = object.getString("u_city");
                        u_phone = object.getString("u_phone");
                        u_postcode = object.getString("u_postcode");
                        u_country = object.getString("u_country");
                        u_img = object.getString("u_img");
                        u_description = object.getString("u_description");
                        u_status = object.getString("u_status");
                        //u_type = object.getString("u_type");
                        u_is_notification_sound = object.getString("u_is_notification_sound");
                        u_seller_services = object.getString("u_seller_services");
                        city_name = object.getString("city_name");
                        avg_rating = object.getString("avg_rating");
                        total_review = object.getString("total_review");
                        country_name = object.getString("country_name");

                        JSONArray services = object.getJSONArray("services");
                        for (int j = 0; j < services.length(); j++) {
                            JSONObject obj = services.getJSONObject(j);
                            s_id = obj.getString("s_id");
                            s_city_id = obj.getString("s_city_id");
                            s_country_id = obj.getString("s_country_id");
                            s_category_id = obj.getString("s_category_id");
                            s_user_id = obj.getString("s_user_id");
                            s_created = obj.getString("s_created");
                            s_modified = obj.getString("s_modified");
                            categoty_name = obj.getString("categoty_name");

                            smap = new HashMap<String, String>();
                            smap.put("s_id", s_id);
                            smap.put("s_city_id", s_city_id);
                            smap.put("s_country_id", s_country_id);
                            smap.put("s_category_id", s_category_id);
                            smap.put("s_user_id", s_user_id);
                            smap.put("s_created", s_created);
                            smap.put("s_modified", s_modified);
                            smap.put("categoty_name", categoty_name);
                            slist.add(smap);
                        }
                        JSONArray reviews = object.getJSONArray("reviews");
                        for (int j = 0; j < reviews.length(); j++) {
                            JSONObject obj = reviews.getJSONObject(j);
                            String u_id = obj.getString("u_id");
                            String u_first_name = obj.getString("u_first_name");
                            String u_last_name = obj.getString("u_last_name");
                            String u_img = obj.getString("u_img");
                            String jsr_comment_date = obj.getString("jsr_comment_date");
                            String jsr_comment_text = obj.getString("jsr_comment_text");
                            String jsr_rating = obj.getString("jsr_rating");

                            rmap = new HashMap<String, String>();
                            rmap.put("u_id", u_id);
                            rmap.put("u_first_name", u_first_name);
                            rmap.put("u_last_name", u_last_name);
                            rmap.put("jsr_comment_text", jsr_comment_text);
                            rmap.put("u_img", u_img);
                            rmap.put("date",jsr_comment_date);
                            rmap.put("jsr_rating", jsr_rating);
                            reviewlist.add(rmap);
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
                Picasso.with(getApplicationContext()).load(u_img).into(iv1);
                tvName.setText(u_first_name + " " + u_last_name);
                tvTitle.setText(u_first_name + " " + u_last_name);
                tvLoc.setText(city_name);
                tvDescription.setText(u_description);
                tvSer.setText("");
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.mapSeller);
                mapFragment.getMapAsync(SellerDetailNotificationActivity.this);
                iv1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
               /* iv1.setDrawingCacheEnabled(true);
                iv1.buildDrawingCache();
                Bitmap bitmap = Bitmap.createBitmap(iv1.getDrawingCache());
                *//*  iv1.buildDrawingCache();
                Bitmap bitmap = iv1.getDrawingCache();*/
                        Intent in = new Intent(SellerDetailNotificationActivity.this, FullImageActivity.class);
                        in.putExtra("Image", u_img);
                        startActivity(in);
                    }
                });
                for (int j = 0; j < slist.size(); j++) {
                    smap = slist.get(j);
                    String str = smap.get("categoty_name");
                    tvSer.append(str + "\n");
                }
                if (avg_rating.equals("")) {

                } else {
                    rat.setRating(Float.parseFloat(avg_rating));
                }
                tvRating.setText(total_review + getResources().getString(R.string.review));

                if (reviewlist.size() == 0) {
                    tvNoReviews.setVisibility(View.VISIBLE);
                    rcReview.setVisibility(View.GONE);
                } else {
                    tvNoReviews.setVisibility(View.GONE);
                    rcReview.setVisibility(View.VISIBLE);
                    reviewListAdapter = new ReviewListAdapter(getApplicationContext(), reviewlist);
                    rcReview.setAdapter(reviewListAdapter);
                }

            }
        }
    }
   /* class AwardJob extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status,msg,u_id,u_first_name, u_last_name, u_email, u_password, u_address, u_latitute, u_longitute, u_city, u_phone, u_postcode, u_country,u_description,u_img, u_status, u_type, u_is_notification_sound,u_seller_services, u_created, u_modified,
                s_id,s_city_id,s_country_id,s_category_id,s_user_id,s_created,s_modified,city_name,avg_rating,total_review,country_name,categoty_name;

        AwardJob(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SellerDetailNotificationActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            String uri =  Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", "awardSellerForJob"));
            nameValuePairs.add(new BasicNameValuePair("jsr_id", sp_id));

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
                Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_LONG).show();
            }
        }
    }*/

}
