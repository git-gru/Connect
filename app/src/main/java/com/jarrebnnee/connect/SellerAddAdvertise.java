package com.jarrebnnee.connect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class SellerAddAdvertise extends AppCompatActivity {

    Toolbar toolbar;
    ImageView ivBack;
    TextView tvTitle;
    SaveSharedPrefrence saveSharedPrefrence;
    String u_id, lang, packageName,no_of_advertise,packageRadius,pkg_id;
    RecyclerView rv_add_adv;
    SellerAddAdvAdapter advAdapter;
    ArrayList<HashMap<String, String>> list;
    Geocoder _coder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_advertise);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        setSupportActionBar(toolbar);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveSharedPrefrence = new SaveSharedPrefrence();
        lang = saveSharedPrefrence.getlang(getApplicationContext());
        u_id = saveSharedPrefrence.getUserID(getApplicationContext());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "" + "</font>")));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/16023_futuran_0.ttf");
        tvTitle.setTypeface(custom_font);

        packageName = saveSharedPrefrence.getpackageName(SellerAddAdvertise.this);
        u_id = saveSharedPrefrence.getUserID(SellerAddAdvertise.this);
        no_of_advertise = saveSharedPrefrence.getno_of_advertise(SellerAddAdvertise.this);
        Log.e("no_of_advertise",no_of_advertise);
        packageRadius = saveSharedPrefrence.getpackageRadius(SellerAddAdvertise.this);
        pkg_id = saveSharedPrefrence.getpkg_id(SellerAddAdvertise.this);

        tvTitle.setText(packageName);
        _coder = new Geocoder(SellerAddAdvertise.this, Locale.ENGLISH);
        list= new ArrayList<HashMap<String, String>>();
        rv_add_adv = (RecyclerView) findViewById(R.id.rv_add_adv);
        rv_add_adv.setLayoutManager(new LinearLayoutManager(SellerAddAdvertise.this, LinearLayoutManager.VERTICAL,false));
        advAdapter= new SellerAddAdvAdapter(list,SellerAddAdvertise.this);
        rv_add_adv.setAdapter(advAdapter);
        prepareData();
    }

    private void prepareData() {

        String url = Urlcollection.url + "action=getAdvertise&lang=" + lang + "&u_id=" + u_id+"&pkg_id="+pkg_id;
        Log.e("url", url);
    /*    final ProgressDialog pDialog = new ProgressDialog(SellerAddAdvertise.this);
        pDialog.setMessage("Loading...");
        pDialog.show();*/
        if (list.size()>=0) {
            list.clear();
            Log.e("list", "cleared");
        }
        StringRequest request= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                  //  pDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equals("0")) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            String uar_id = object.getString("uar_id");
                            String uar_image = object.getString("uar_image");
                            String uar_u_id = object.getString("uar_u_id");
                            String uar_pkg_id = object.getString("uar_pkg_id");
                            String uar_latitude = object.getString("uar_latitude");
                            String uar_longitude = object.getString("uar_longitude");
                            String uar_c_id = object.getString("uar_c_id");
                            String uar_title = object.getString("uar_title");
                            String uar_desc = object.getString("uar_desc");
                            String uar_radius = object.getString("uar_radius");
                            HashMap<String, String> map = new HashMap<>();
                            map.put("uar_id", uar_id);
                            map.put("uar_image", uar_image);
                            map.put("uar_u_id", uar_u_id);
                            map.put("uar_pkg_id", uar_pkg_id);
                            map.put("uar_latitude", uar_latitude);
                            map.put("uar_longitude", uar_longitude);
                            map.put("uar_c_id", uar_c_id);
                            map.put("uar_title", uar_title);
                            map.put("uar_desc", uar_desc);
                            map.put("uar_radius", uar_radius);
                            list.add(map);
                            Log.e("added", "title "+uar_title);
                            advAdapter.notifyDataSetChanged();
                        }
                        int added= data.length();
                        int left = Integer.valueOf(no_of_advertise) - added;
                        for (int i=0; i<left;i++) {
                            HashMap<String,String> map1= new HashMap<>();
                            map1.put("uar_id", "");
                            map1.put("uar_image", "");
                            map1.put("uar_u_id", "");
                            map1.put("uar_pkg_id", "");
                            map1.put("uar_latitude", "");
                            map1.put("uar_longitude", "");
                            map1.put("uar_c_id", "");
                            map1.put("uar_title", "");
                            map1.put("uar_desc", "");
                            list.add(map1);
                            Log.e("added", "title "+"");
                            advAdapter.notifyDataSetChanged();
                        }
                    } else if (status.equals("1")) {
                        Log.e("status", "1");
                        for (int i=1; i<=Integer.valueOf(no_of_advertise); i++) {
                            HashMap<String,String> map1= new HashMap<>();
                            map1.put("uar_id", "");
                            map1.put("uar_image", "");
                            map1.put("uar_u_id", "");
                            map1.put("uar_pkg_id", "");
                            map1.put("uar_latitude", "");
                            map1.put("uar_longitude", "");
                            map1.put("uar_c_id", "");
                            map1.put("uar_title", "");
                            map1.put("uar_desc", "");
                            list.add(map1);
                            Log.e("added", "title "+"");
                            advAdapter.notifyDataSetChanged();
                        }

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


    class SellerAddAdvAdapter extends RecyclerView.Adapter<SellerAddAdvertise.SellerAddAdvAdapter.MyViewHolder2>{


        ArrayList<HashMap<String, String>> list;
        Context context;
        HashMap<String, String> map;

        public SellerAddAdvAdapter(ArrayList<HashMap<String, String>> list, Context context) {
            this.list = list;
            this.context = context;
        }

        public class MyViewHolder2 extends RecyclerView.ViewHolder {
            TextView tv_adv_title,tv_adv_desc,tv_adv_radius;// tv_adv_number;
            ImageView iv_adv,iv_adv_delete ;

            public MyViewHolder2(View itemView) {
                super(itemView);
                iv_adv = (ImageView) itemView.findViewById(R.id.iv_adv);
                iv_adv = (ImageView) itemView.findViewById(R.id.iv_adv);
                tv_adv_title = (TextView) itemView.findViewById(R.id.tv_adv_title);
                tv_adv_desc = (TextView) itemView.findViewById(R.id.tv_adv_desc);
                tv_adv_radius = (TextView) itemView.findViewById(R.id.tv_adv_radius);
                iv_adv_delete = (ImageView) itemView.findViewById(R.id.iv_adv_delete);
            }
        }



        @Override
        public SellerAddAdvertise.SellerAddAdvAdapter.MyViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_seller_add_advertise, parent, false);

            return new SellerAddAdvertise.SellerAddAdvAdapter.MyViewHolder2(itemView);
        }

        @Override
        public void onBindViewHolder(final SellerAddAdvertise.SellerAddAdvAdapter.MyViewHolder2 holder, final int position) {
            map = list.get(position);
            String locationName = "";
            Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "fonts/16023_futuran_0.ttf");
            holder.tv_adv_title.setTypeface(custom_font);
            holder.tv_adv_desc.setTypeface(custom_font);
         //   holder.btn_add_adv.setTypeface(custom_font);

            String lati = map.get("uar_latitude");
            String longi = map.get("uar_longitude");
            final String uar_id = map.get("uar_id");
            if (lati.equals("") && longi.equals("")) {
                holder.tv_adv_desc.setVisibility(View.GONE);
                holder.iv_adv_delete.setVisibility(View.GONE);
                holder.iv_adv.setVisibility(View.GONE);
            } else {
                try{
                    holder.tv_adv_desc.setVisibility(View.VISIBLE);
                    holder.iv_adv_delete.setVisibility(View.VISIBLE);
                    holder.iv_adv.setVisibility(View.VISIBLE);
                    holder.tv_adv_title.setText(map.get("uar_title"));
                    holder.tv_adv_desc.setText(map.get("uar_desc"));
                    holder.tv_adv_radius.setText("Radius : "+map.get("uar_radius"));
                    Picasso.with(context).load(map.get("uar_image")).into(holder.iv_adv);
                    android.location.Address address = null;
                    List<Address> addresses = _coder.getFromLocation(Double.valueOf(lati),Double.valueOf(longi),1);
                    for(int index=0; index<addresses.size(); ++index)
                    {
                        address = addresses.get(index);
                        for (int i=0; i<address.getMaxAddressLineIndex(); i++) {
                            Log.e("max", ""+address.getMaxAddressLineIndex());
                            if (i==address.getMaxAddressLineIndex()-1) {
                                locationName= locationName+(address.getAddressLine(i));
                                break;
                            }
                            locationName= locationName+(address.getAddressLine(i) + ", ");
                        }
                    }
                    Log.e("locationName",""+locationName);
                    //holder.tv_location_name.setText(locationName);
                }
                catch(Exception e){
                   // holder.tv_location_name.setText("exception : "+e);
                }
            }


            //holder.tv_location_name.setText(map.get("pkg_title"));
         //   holder.tv_adv_number.setText(map.get("adv_number")+"-");
            holder.iv_adv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = holder.tv_adv_title.getText().toString().trim();
                    if (s.equals("Add advertise")) {
                        Intent intent = new Intent(SellerAddAdvertise.this, SellerMapLocation.class);
                        startActivity(intent);
                        finish();
                    } else {
                    //    Toast.makeText(SellerAddAdvertise.this, "Delete will be called", Toast.LENGTH_SHORT).show();

                        String url = Urlcollection.url + "action=deleteAdvertise&lang=" + lang + "&uar_id=" + uar_id;
                        Log.e("url", url);
                        final ProgressDialog pDialog = new ProgressDialog(SellerAddAdvertise.this);
                        pDialog.setMessage("Loading...");
                        pDialog.show();
                        StringRequest request= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                pDialog.dismiss();
                                try {
                                    Log.e("response", response);
                                    JSONObject object = new JSONObject(response);
                                    String status = object.getString("status");
                                    String message = object.getString("message");
                                    Toast.makeText(SellerAddAdvertise.this, message, Toast.LENGTH_SHORT).show();
                                    holder.tv_adv_title.setText("Add advertise");
                                    holder.tv_adv_desc.setVisibility(View.GONE);
                                    holder.iv_adv_delete.setVisibility(View.GONE);
                                    holder.iv_adv.setVisibility(View.GONE);
                                    // holder.tv_location_name.setText("Advertise area will be displaed here");
                                    final ProgressDialog dialog = new ProgressDialog(SellerAddAdvertise.this);
                                    dialog.setTitle("Loading...");
                                    dialog.setMessage("Please wait.");
                                    dialog.setIndeterminate(true);
                                    dialog.setCancelable(false);
                                    dialog.show();

                                    long delayInMillis = 5000;
                                    Timer timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();

                                            prepareData();
                                        }
                                    }, delayInMillis);
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


                }
            });
            holder.tv_adv_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = holder.tv_adv_title.getText().toString().trim();
                    if (s.equals("Add advertise")) {
                        Intent intent = new Intent(SellerAddAdvertise.this, SellerMapLocation.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });




        }



        @Override
        public int getItemCount() {
            return list.size();
        }
    }



}
