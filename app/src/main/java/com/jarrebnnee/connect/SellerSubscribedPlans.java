package com.jarrebnnee.connect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SellerSubscribedPlans extends AppCompatActivity {
    RecyclerView rv_subscribe;
    Toolbar toolbar;
    ImageView ivBack;
    TextView tvTitle;
    SaveSharedPrefrence saveSharedPrefrence;
    String lang = "", u_id, obj;
    ArrayList<HashMap<String, String>> list;
    Button btn_new;
    SellerSubscribedPlans.SellerSubscribePackageAdaper packageAdaper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_subscribed_plans);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        setSupportActionBar(toolbar);

        saveSharedPrefrence = new SaveSharedPrefrence();
        lang = saveSharedPrefrence.getlang(getApplicationContext());
        u_id = saveSharedPrefrence.getUserID(getApplicationContext());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "" + "</font>")));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/16023_futuran_0.ttf");
        tvTitle.setTypeface(custom_font);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list = new ArrayList<HashMap<String, String>>();
        rv_subscribe = (RecyclerView) findViewById(R.id.rv_subscribe);
        btn_new = (Button) findViewById(R.id.btn_new);
        int numberOfColumns = 2;
        rv_subscribe.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        packageAdaper = new SellerSubscribedPlans.SellerSubscribePackageAdaper(list, SellerSubscribedPlans.this);
        rv_subscribe.setAdapter(packageAdaper);

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerSubscribedPlans.this, SellerSubscribeActivity.class);
                startActivity(intent);
            }
        });

        callUserSubscribed();
    }


    private void callUserSubscribed() {
        String url = Urlcollection.url + "action=getSubscribeUserPlanList&lang=" + lang+"&u_id="+u_id;
        Log.e("url", url);

        final ProgressDialog pDialog = new ProgressDialog(SellerSubscribedPlans.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest request= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equals("0")) {
                        Toast.makeText(SellerSubscribedPlans.this, message, Toast.LENGTH_SHORT).show();
                        JSONArray data = jsonObject.getJSONArray("PackData");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);

                            String pkg_id = object.getString("pkg_id");
                            String pkg_radios = object.getString("pkg_radios");
                            String pkg_min_advertise = object.getString("pkg_min_advertise");
                            String pkg_price = object.getString("pkg_price");
                            String pkg_desc = object.getString("pkg_desc");
                            String pkg_title = object.getString("pkg_title");
                            String pkg_expire_date = object.getString("pkg_expire_date");

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("pkg_id", pkg_id);
                            map.put("pkg_radios", pkg_radios);
                            map.put("pkg_min_advertise", pkg_min_advertise);
                            map.put("pkg_price", pkg_price);
                            map.put("pkg_desc", pkg_desc);
                            map.put("pkg_title", pkg_title);
                            map.put("pkg_expire_date", pkg_expire_date);
                            list.add(map);
                            Log.e("url", "added"+list.size());
                            packageAdaper.notifyDataSetChanged();
                        }


                    } else {
                        pDialog.dismiss();
                        Toast.makeText(SellerSubscribedPlans.this, message, Toast.LENGTH_SHORT).show();
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




    class SellerSubscribePackageAdaper extends RecyclerView.Adapter<SellerSubscribedPlans.SellerSubscribePackageAdaper.MyViewHolder2>{


        ArrayList<HashMap<String, String>> list;
        Context context;
        HashMap<String, String> map;

        public SellerSubscribePackageAdaper(ArrayList<HashMap<String, String>> list, Context context) {
            this.list = list;
            this.context = context;
        }

        public class MyViewHolder2 extends RecyclerView.ViewHolder {
            TextView tv_package_name, tv_package_desc, tv_package_radius, tv_package_minimum, tv_package_price,tv_package_expire_date;
            Button btnAdd;
            CardView cardView;

            public MyViewHolder2(View itemView) {
                super(itemView);
                tv_package_name = (TextView) itemView.findViewById(R.id.tv_package_name);
                tv_package_desc = (TextView) itemView.findViewById(R.id.tv_package_desc);
                tv_package_radius = (TextView) itemView.findViewById(R.id.tv_package_radius);
                tv_package_minimum = (TextView) itemView.findViewById(R.id.tv_package_minimum);
                tv_package_price = (TextView) itemView.findViewById(R.id.tv_package_price);
                cardView = (CardView) itemView.findViewById(R.id.card_view);
                tv_package_expire_date = (TextView) itemView.findViewById(R.id.tv_package_expire_date);
                btnAdd = (Button) itemView.findViewById(R.id.btn_package_add);
            }
        }



        @Override
        public SellerSubscribedPlans.SellerSubscribePackageAdaper.MyViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_subscribed, parent, false);

            return new SellerSubscribedPlans.SellerSubscribePackageAdaper.MyViewHolder2(itemView);
        }

        @Override
        public void onBindViewHolder(SellerSubscribedPlans.SellerSubscribePackageAdaper.MyViewHolder2 holder, final int position) {
            map = list.get(position);
            Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "fonts/16023_futuran_0.ttf");
            holder.tv_package_name.setTypeface(custom_font);
            holder.tv_package_desc.setTypeface(custom_font);
            holder.tv_package_radius.setTypeface(custom_font);
            holder.tv_package_minimum.setTypeface(custom_font);
            holder.tv_package_price.setTypeface(custom_font);
            holder.tv_package_expire_date.setTypeface(custom_font);


            holder.tv_package_name.setText(map.get("pkg_title"));
            holder.tv_package_desc.setText(map.get("pkg_desc"));
            holder.tv_package_radius.setText(map.get("pkg_radios")+" Radius");
            holder.tv_package_minimum.setText(map.get("pkg_min_advertise")+" Minimum Advertise");
            holder.tv_package_price.setText("$ "+map.get("pkg_price"));
            holder.tv_package_expire_date.setText("Expires: "+map.get("pkg_expire_date"));


            final String pkg_id = map.get("pkg_id");
            final String paymentAmount = map.get("pkg_price");
            final String packageName = map.get("pkg_title");
            final String no_of_advertise=map.get("pkg_min_advertise");
            final String package_radius=map.get("pkg_radios");

/*
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            */
            holder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SellerSubscribedPlans.this, SellerAddAdvertise.class);
                    startActivity(intent);

                    saveSharedPrefrence.savepackageName(SellerSubscribedPlans.this, packageName);
                    saveSharedPrefrence.saveno_of_advertise(SellerSubscribedPlans.this,no_of_advertise);
                    saveSharedPrefrence.savepackageRadius(SellerSubscribedPlans.this, package_radius);
                    saveSharedPrefrence.savepkg_id(SellerSubscribedPlans.this, pkg_id);
                    saveSharedPrefrence.savepaymentAmount(SellerSubscribedPlans.this, paymentAmount);
                }
            });

        }



        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}
