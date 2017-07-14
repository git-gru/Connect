package com.jarrebnnee.connect.fragement;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jarrebnnee.connect.Adapter.SearchAdapter;
import com.jarrebnnee.connect.Adapter.SellerJobListAdapter;
import com.jarrebnnee.connect.Adapter.ServiceListAdapter;
import com.jarrebnnee.connect.MyApplication;
import com.jarrebnnee.connect.R;
import com.jarrebnnee.connect.SellerJobListActivity;
import com.jarrebnnee.connect.Service.GetSet;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.ServiceHandler;
import com.jarrebnnee.connect.Service.TrackGPS;
import com.jarrebnnee.connect.ServiceListActivity;
import com.jarrebnnee.connect.SubCateActivity;
import com.jarrebnnee.connect.Urlcollection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Vardhman Infonet 4 on 22-Feb-17.
 */

public class Search_Job_Fragement extends Fragment {
    GridView lvService;
    ServiceListAdapter adapter;
    ArrayList<HashMap<String,String>> list, cat_list;
    HashMap<String,String> map, map1;
    SaveSharedPrefrence saveSharedPrefrence;
    GetSet getSet;
    TextView tvno;
    String category_id,u_latitute,u_longitute,user_id,radius,key;
    TrackGPS gps;
    String u_type,action,lang;
    SearchAdapter searchAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_job, container, false);
        saveSharedPrefrence = new SaveSharedPrefrence();
        getSet = GetSet.getInstance();
        lang = saveSharedPrefrence.getlang(getActivity());
        user_id = saveSharedPrefrence.getUserID(getActivity());
        u_type = saveSharedPrefrence.getUserType(getActivity());
        //radius =
        category_id = getSet.getsubc_id();
        key = getSet.getSearch();

        lvService = (GridView) rootView.findViewById(R.id.lvService);
        cat_list=new ArrayList<>();
        searchAdapter = new SearchAdapter(getActivity(), cat_list);
        lvService.setAdapter(searchAdapter);
        tvno = (TextView) rootView.findViewById(R.id.tvno);

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/16023_futuran_0.ttf");
       // tvTitle.setTypeface(custom_font);
        tvno.setTypeface(custom_font);
        gps = new TrackGPS(getActivity());
        if(gps.canGetLocation()){

            u_longitute = String.valueOf(gps.getLongitude());
            u_latitute = String.valueOf(gps .getLatitude());

           // Toast.makeText(getActivity(),"Longitude:"+u_longitute+"\nLatitude:"+u_latitute,Toast.LENGTH_SHORT).show();
            Log.e("latlong","Longitude:"+u_longitute+"\nLatitude:"+u_latitute);
        }
        else
        {
            gps.showSettingsAlert();
        }


        lvService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                map1 = cat_list.get(position);
                String c_id = map1.get("c_id");
                String c_name = map1.get("c_title");
                String c_is_sub_category = map1.get("c_is_sub_category");
                getSet.setsubc_id(c_id);
                getSet.setsubc_name(c_name);

                if (c_is_sub_category.equals("1")) {
                    Intent i = new Intent(getActivity(), SubCateActivity.class);
                    startActivity(i);
                } else {
                    diaplay();
                    /*// if (u_type.equals("2")){
                    Intent i = new Intent(getActivity(), SellerJobListActivity.class);
                    startActivity(i);
                    //}*//*
                   *//* else {
                        Intent i = new Intent(getActivity(), ServiceListActivity.class);
                        startActivity(i);
                    }*/
                }
            }
        });
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//we are connected to a network
            //  url=URLCollection.LoginUrl+"&u_email="+email+"&u_password="+password+"&device_type=0&device_token="+regId;
            //   SignInUrlCall(url);

            //    new JobList(getActivity()).execute();
            getSearchResult();

        } else {
            Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
        }

        return rootView;
    }

    private void getSearchResult() {

        String url = Urlcollection.urlHK + "action=getMainCategoryList&key=" + key;
        Log.e("search_url", url);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObj = new JSONObject(response);
                    String status = jsonObj.getString("status");
                    String msg = jsonObj.getString("message");
                    Log.e("msg", msg);
                    JSONArray data = jsonObj.getJSONArray("data");
                    int arraySize = data.length();
                    if (arraySize == 0) {
                        lvService.setVisibility(View.GONE);
                        tvno.setVisibility(View.VISIBLE);
                    } else {
                        lvService.setVisibility(View.VISIBLE);
                        tvno.setVisibility(View.GONE);
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            String c_id = object.getString("c_id");
                            String c_images = object.getString("c_images");
                            String c_type = object.getString("c_type");
                            String c_title = object.getString("c_title");
                            String c_is_parent_id = object.getString("c_is_parent_id");
                            String c_total_services = object.getString("c_total_services");
                            String c_created = object.getString("c_created");
                            String c_ar_title = object.getString("c_ar_title");
                            String s_modified = object.getString("s_modified");
                            String c_is_sub_category = object.getString("c_is_sub_category");

                            map = new HashMap<String, String>();
                            map.put("c_id", c_id);
                            map.put("c_images", c_images);
                            map.put("c_type", c_type);
                            map.put("c_title", c_title);
                            map.put("c_is_parent_id", c_is_parent_id);
                            map.put("c_total_services", c_total_services);
                            map.put("c_created", c_created);
                            map.put("c_ar_title", c_ar_title);
                            map.put("s_modified", s_modified);
                            map.put("c_is_sub_category", c_is_sub_category);
                            cat_list.add(map);
                            searchAdapter.notifyDataSetChanged();

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
    private void diaplay() {
        if (u_type.equals("2")) {
            Intent i = new Intent(getActivity(), SellerJobListActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(getActivity(), ServiceListActivity.class);
            startActivity(i);
        }
    }
  /*  class JobList extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg,s_id,s_city_id,s_country_id,s_category_id,s_user_id,s_created,s_modified,c_id,c_images,c_type,c_title,c_is_parent_id,c_total_services,c_created,u_id,u_first_name,u_last_name,u_email,u_password,u_address,u_gender,u_latitute1,u_longitute1,u_city,u_phone,
                u_postcode,u_country,u_img,u_status,u_type,u_is_notification_sound,u_seller_services,u_created,u_modified,distance,jsr_id,u_city_name,avg_rating,total_review;

        JobList(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getResources().getString(R.string.pdialog));
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            list = new ArrayList<HashMap<String, String>>();

            String uri =  Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("action","searchJobsHome"));
            nameValuePairs.add(new BasicNameValuePair("key",key));



            ServiceHandler handler = new ServiceHandler();
            String jsonSt = handler.makeServiceCall(
                    uri, ServiceHandler.POST,nameValuePairs);
            Log.e("nameValuePairs",""+nameValuePairs);
            Log.e("Response: ", "> " + jsonSt);

            if (jsonSt != null) {

                try {

                    JSONObject jsonObj = new JSONObject(jsonSt);
                    status = jsonObj.getString("status");
                    msg = jsonObj.getString("message");
                    Log.e("msg", msg);

                    JSONArray data = jsonObj.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject da = data.getJSONObject(i);
                        String js_id = da.getString("js_id");
                        String js_user_id = da.getString("js_user_id");
                        String js_category_id = da.getString("js_category_id");
                        String js_title = da.getString("js_title");
                        String js_description = da.getString("js_description");
                        String js_price = da.getString("js_price");
                        String js_appointment_date = da.getString("js_appointment_date");
                        String js_image = da.getString("js_image");
                        String js_radius = da.getString("js_radius");
                        String js_status = da.getString("js_status");

                        u_id = da.getString("u_id");
                        u_first_name = da.getString("u_first_name");
                        u_last_name = da.getString("u_last_name");
                        u_email = da.getString("u_email");
                        u_password = da.getString("u_password");
                        u_address = da.getString("u_address");
                        u_gender = da.getString("u_gender");
                        u_latitute1 = da.getString("u_latitute");
                        u_longitute1 = da.getString("u_longitute");
                        u_city = da.getString("u_city");
                        u_phone = da.getString("u_phone");
                        u_postcode = da.getString("u_postcode");
                        u_country = da.getString("u_country");
                        u_img = da.getString("u_img");
                        u_status = da.getString("u_status");
                        u_type = da.getString("u_type");
                        u_is_notification_sound = da.getString("u_is_notification_sound");
                        u_seller_services = da.getString("u_seller_services");
                        u_created = da.getString("u_created");
                        u_modified = da.getString("u_modified");

                        c_id = da.getString("c_id");
                        c_images = da.getString("c_images");
                        c_type = da.getString("c_type");
                        c_title = da.getString("c_title");
                        c_is_parent_id = da.getString("c_is_parent_id");
                        c_total_services = da.getString("c_total_services");
                        c_created = da.getString("c_created");

                        distance = da.getString("distance");
                        u_city_name = da.getString("u_city_name");
                        jsr_id = da.getString("jsr_id");

                        map = new HashMap<String, String>();
                        map.put("js_id", js_id);
                        map.put("js_user_id", js_user_id);
                        map.put("js_category_id", js_category_id);
                        map.put("js_title", js_title);
                        map.put("js_description", js_description);
                        map.put("js_price", js_price);
                        map.put("js_appointment_date", js_appointment_date);
                        map.put("js_radius", js_radius);
                        map.put("js_image", js_image);
                        map.put("js_status", js_status);

                        map.put("c_id", c_id);
                        map.put("c_images", c_images);
                        map.put("c_type", c_type);
                        map.put("c_title", c_title);
                        map.put("c_is_parent_id", c_is_parent_id);
                        map.put("c_total_services", c_total_services);
                        map.put("c_created", c_created);
                        map.put("u_id", u_id);
                        map.put("u_first_name", u_first_name);
                        map.put("u_last_name", u_last_name);
                        map.put("u_email", u_email);
                        map.put("u_password", u_password);
                        map.put("u_address", u_address);
                        map.put("u_gender", u_gender);
                        map.put("u_latitute", u_latitute1);
                        map.put("u_longitute", u_longitute1);
                        map.put("u_city", u_city);
                        map.put("u_phone", u_phone);
                        map.put("u_postcode", u_postcode);
                        map.put("u_country", u_country);
                        map.put("u_img", u_img);
                        map.put("u_status", u_status);
                        map.put("u_type", u_type);
                        map.put("u_is_notification_sound", u_is_notification_sound);
                        map.put("u_seller_services", u_seller_services);
                        map.put("u_created", u_created);
                        map.put("u_modified", u_modified);
                        map.put("distance", distance);
                        map.put("u_city_name", u_city_name);
                        map.put("jsr_id",jsr_id);


                        list.add(map);
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
                tvno.setVisibility(View.GONE);
                lvService.setVisibility(View.VISIBLE);
                SellerJobListAdapter adapter = new SellerJobListAdapter(getActivity(),list);
                lvService.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            } else {
                tvno.setVisibility(View.VISIBLE);
                lvService.setVisibility(View.GONE);
                *//*Toast.makeText(getApplicationContext(),"No Items..",Toast.LENGTH_LONG).show();*//*
            }
        }
    }
    class ServiceList extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg,s_id,s_city_id,s_country_id,s_category_id,s_user_id,s_created,s_modified,c_id,c_images,c_type,c_title,c_is_parent_id,c_total_services,c_created,u_id,u_first_name,u_last_name,u_email,u_password,u_address,u_gender,u_latitute1,u_longitute1,u_city,u_phone,
                u_postcode,u_country,u_img,u_status,u_type,u_is_notification_sound,u_seller_services,u_created,u_modified,distance,u_city_name,avg_rating,total_review;

        ServiceList(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getResources().getString(R.string.pdialog));
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            list = new ArrayList<HashMap<String, String>>();

            String uri =  Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("action","searchSellersHome"));
            nameValuePairs.add(new BasicNameValuePair("key",key));
            // nameValuePairs.add(new BasicNameValuePair("radius",radius));



            ServiceHandler handler = new ServiceHandler();
            String jsonSt = handler.makeServiceCall(
                    uri, ServiceHandler.POST,nameValuePairs);
            Log.e("nameValuePairs",""+nameValuePairs);
            Log.e("Response: ", "> " + jsonSt);

            if (jsonSt != null) {

                try {

                    JSONObject jsonObj = new JSONObject(jsonSt);
                    status = jsonObj.getString("status");
                    msg = jsonObj.getString("message");
                    Log.e("msg", msg);

                    JSONArray data = jsonObj.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject da = data.getJSONObject(i);
                        u_id = da.getString("u_id");
                        u_first_name = da.getString("u_first_name");
                        u_last_name = da.getString("u_last_name");
                        u_email = da.getString("u_email");
                        u_password = da.getString("u_password");
                        u_address = da.getString("u_address");
                        u_gender = da.getString("u_gender");
                        u_latitute1 = da.getString("u_latitute");
                        u_longitute1 = da.getString("u_longitute");
                        u_city = da.getString("u_city");
                        u_phone = da.getString("u_phone");
                        u_postcode = da.getString("u_postcode");
                        u_country = da.getString("u_country");
                        u_img = da.getString("u_img");
                        u_status = da.getString("u_status");
                        u_type = da.getString("u_type");
                        u_is_notification_sound = da.getString("u_is_notification_sound");
                        u_seller_services = da.getString("u_seller_services");
                        u_created = da.getString("u_created");
                        u_modified = da.getString("u_modified");

                        s_id = da.getString("s_id");
                        s_city_id = da.getString("s_city_id");
                        s_country_id = da.getString("s_country_id");
                        s_category_id = da.getString("s_category_id");
                        s_user_id = da.getString("s_user_id");
                        s_created = da.getString("s_created");
                        s_modified = da.getString("s_modified");
                        c_id = da.getString("c_id");
                        c_images = da.getString("c_images");
                        c_type = da.getString("c_type");
                        c_title = da.getString("c_title");
                        c_is_parent_id = da.getString("c_is_parent_id");
                        c_total_services = da.getString("c_total_services");
                        c_created = da.getString("c_created");

                        distance = da.getString("distance");
                        u_city_name = da.getString("u_city_name");
                        total_review = da.getString("total_review");
                        avg_rating = da.getString("avg_rating");


                        map = new HashMap<String, String>();
                        map.put("s_id", s_id);
                        map.put("s_city_id", s_city_id);
                        map.put("s_country_id", s_country_id);
                        map.put("s_category_id", s_category_id);
                        map.put("s_user_id", s_user_id);
                        map.put("s_created", s_created);
                        map.put("s_modified", s_modified);
                        map.put("c_id", c_id);
                        map.put("c_images", c_images);
                        map.put("c_type", c_type);
                        map.put("c_title", c_title);
                        map.put("c_is_parent_id", c_is_parent_id);
                        map.put("c_total_services", c_total_services);
                        map.put("c_created", c_created);
                        map.put("u_id", u_id);
                        map.put("u_first_name", u_first_name);
                        map.put("u_last_name", u_last_name);
                        map.put("u_email", u_email);
                        map.put("u_password", u_password);
                        map.put("u_address", u_address);
                        map.put("u_gender", u_gender);
                        map.put("u_latitute", u_latitute1);
                        map.put("u_longitute", u_longitute1);
                        map.put("u_city", u_city);
                        map.put("u_phone", u_phone);
                        map.put("u_postcode", u_postcode);
                        map.put("u_country", u_country);
                        map.put("u_img", u_img);
                        map.put("u_status", u_status);
                        map.put("u_type", u_type);
                        map.put("u_is_notification_sound", u_is_notification_sound);
                        map.put("u_seller_services", u_seller_services);
                        map.put("u_created", u_created);
                        map.put("u_modified", u_modified);
                        map.put("distance", distance);
                        map.put("u_city_name", u_city_name);
                        map.put("total_review", total_review);
                        map.put("avg_rating", avg_rating);


                        list.add(map);

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
                tvno.setVisibility(View.GONE);
                lvService.setVisibility(View.VISIBLE);
                adapter = new ServiceListAdapter(getActivity(),list);
                lvService.setAdapter(adapter);

            } else {
                tvno.setVisibility(View.VISIBLE);
                lvService.setVisibility(View.GONE);
                *//*Toast.makeText(getApplicationContext(),"No Items..",Toast.LENGTH_LONG).show();*//*
            }
        }
    }*/

}