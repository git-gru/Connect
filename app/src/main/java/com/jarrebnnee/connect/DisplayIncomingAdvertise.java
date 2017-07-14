package com.jarrebnnee.connect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jarrebnnee.connect.Adapter.ReviewListAdapter;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.TrackGPS;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayIncomingAdvertise extends AppCompatActivity {

    Toolbar toolbar;
    TextView tv_title, tv_desc, tvTitle, tv_posted_name, tvNoReviews;
    ImageView iv_incoming_ad_image,ivBack;
    String cn_noti_type_id="";
    Bundle bundle;
    LinearLayout lll;
    String uar_u_id;
    ArrayList<HashMap<String, String>> reviewlist;
    HashMap<String, String> rmap;
    SaveSharedPrefrence saveSharedPrefrence;
    RecyclerView rcReview;
    ReviewListAdapter reviewListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_incoming_advertise);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        lll = (LinearLayout) findViewById(R.id.lll);
        tv_posted_name = (TextView) findViewById(R.id.tv_posted_name);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "" + "</font>")));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ivBack.setVisibility(View.INVISIBLE);
        saveSharedPrefrence = new SaveSharedPrefrence();
        bundle = getIntent().getExtras();
        tv_title = (TextView) findViewById(R.id.tv_incoming_ad_title);
        tv_desc = (TextView) findViewById(R.id.tv_incoming_ad_desc);
        iv_incoming_ad_image = (ImageView) findViewById(R.id.iv_incoming_ad_image);
        reviewlist=new ArrayList<>();
        rcReview = (RecyclerView) findViewById(R.id.rcReview);
        tvNoReviews = (TextView) findViewById(R.id.tvNoReviews);

        lll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayIncomingAdvertise.this, ServiceItemDetailActivity.class);
                intent.putExtra("sp_id", uar_u_id);
                startActivity(intent);
            }
        });
        reviewListAdapter = new ReviewListAdapter(getApplicationContext(), reviewlist);
        rcReview.setAdapter(reviewListAdapter);
        getData();



     //   CallGetAdvertise(cn_noti_type_id);







    }

    private void getData() {
        cn_noti_type_id = bundle.getString("type_id");
        Log.e("hhhhhh",cn_noti_type_id);
        CallGetAdvertise(cn_noti_type_id);
    }

    private void CallGetAdvertise(String cn_noti_type_id) {

        String url = Urlcollection.url + "action=getAdvertiseById&uar_id="+cn_noti_type_id;
        Log.e("url", url);
        final ProgressDialog pDialog = new ProgressDialog(DisplayIncomingAdvertise.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest request= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    pDialog.dismiss();
                    String status = object.getString("status");
                    String message = object.getString("message");
                    JSONObject data = object.getJSONObject("data");

                    String uar_title  = data.getString("uar_title");
                    String uar_desc = data.getString("uar_desc");
                    uar_u_id = data.getString("uar_u_id");
                    final String uar_image = data.getString("uar_image");
                    tv_title.setText(uar_title);
                    tv_desc.setText(uar_desc);
                    Log.e("uar_title",uar_title);
                    Log.e("uar_desc",uar_desc);
                    Log.e("uar_image",uar_image);
                    Picasso.with(DisplayIncomingAdvertise.this)
                            .load(uar_image)/*.placeholder(R.drawable.logo)*/
                            .into(iv_incoming_ad_image);
                    iv_incoming_ad_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           /* iv_incoming_ad_image.setDrawingCacheEnabled(true);
                            iv_incoming_ad_image.buildDrawingCache();
                            Bitmap bitmap = Bitmap.createBitmap(iv_incoming_ad_image.getDrawingCache());
                            *//* iv_incoming_ad_image.buildDrawingCache();
                            Bitmap bitmap=iv_incoming_ad_image.getDrawingCache();*/
                            Intent in=new Intent(DisplayIncomingAdvertise.this,FullImageActivity.class);
                            in.putExtra("Image",uar_image);
                            startActivity(in);
                        }
                    });
                    String name  = data.getString("name");
                    tv_posted_name.setText(name);
                    JSONArray reviews = data.getJSONArray("reviews");
                    for (int j = 0;j<reviews.length();j++){
                        JSONObject obj = reviews.getJSONObject(j);
                        String u_id = obj.getString("u_id");
                        String u_first_name = obj.getString("u_first_name");
                        String u_last_name = obj.getString("u_last_name");
                        String u_img = obj.getString("u_img");
                        String jsr_comment_date = obj.getString("jsr_comment_date");
                        String jsr_comment_text = obj.getString("jsr_comment_text");
                        String jsr_rating = obj.getString("jsr_rating");

                        rmap = new HashMap<String, String>();
                        rmap.put("u_id",u_id);
                        rmap.put("u_first_name",u_first_name);
                        rmap.put("u_last_name",u_last_name);
                        rmap.put("jsr_comment_text",jsr_comment_text);
                        rmap.put("u_img",u_img);
                        rmap.put("date",jsr_comment_date);
                        rmap.put("jsr_rating",jsr_rating);
                        reviewlist.add(rmap);
                        reviewListAdapter.notifyDataSetChanged();
                    }
                    if (reviewlist.size()==0){
                        tvNoReviews.setVisibility(View.VISIBLE);
                        rcReview.setVisibility(View.GONE);
                    }else {
                        tvNoReviews.setVisibility(View.GONE);
                        rcReview.setVisibility(View.VISIBLE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MyApplication.getInstance().addToRequestQueue(request);
    }

    @Override
    protected void onDestroy() {
        ivBack.setVisibility(View.VISIBLE);
        super.onDestroy();
    }


}
