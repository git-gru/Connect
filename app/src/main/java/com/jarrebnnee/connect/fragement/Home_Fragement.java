package com.jarrebnnee.connect.fragement;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.jarrebnnee.connect.Adapter.CustomPagerAdapter;
import com.jarrebnnee.connect.Adapter.HorizonatalListAdapter;
import com.jarrebnnee.connect.Adapter.HorizonatalListAdapter2;
import com.jarrebnnee.connect.HomeActivity;
import com.jarrebnnee.connect.MyApplication;
import com.jarrebnnee.connect.R;
import com.jarrebnnee.connect.SellerJobListActivity;
import com.jarrebnnee.connect.Service.GetSet;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.ServiceHandler;
import com.jarrebnnee.connect.ServiceListActivity;
import com.jarrebnnee.connect.SubCateActivity;
import com.jarrebnnee.connect.Urlcollection;
import com.viewpagerindicator.CirclePageIndicator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Vardhman Infonet 4 on 22-Feb-17.
 */

public class Home_Fragement extends Fragment {

    ViewPager vpPager;
    CirclePageIndicator mIndicator;
    GridView rvSList;
    int[] icons= {R.drawable.img2,R.drawable.img3,R.drawable.img2,R.drawable.img4,R.drawable.img5,R.drawable.img4};
    int[] icons2= {R.drawable.img4,R.drawable.img5,R.drawable.img4};
    String[] cat_name= {"Electrician","Plumber","Electrician","Yoga Trainer at Home","Fitness Trainer at Home","Yoga Trainer at Home"};
    String[] cat_name2= {"Yoga Trainer at Home","Fitness Trainer at Home","Yoga Trainer at Home"};
    CustomPagerAdapter mCustomPagerAdapter;
    HorizonatalListAdapter horizonatalListAdapter;
    HorizonatalListAdapter2 horizonatalListAdapter2;
    ArrayList<HashMap<String, String>> sl_list;
    HashMap<String, String> sl_map;
    ArrayList<HashMap<String, String>> cat_list;
    HashMap<String, String> map;
    GetSet getSet;
    String u_type, u_id;
    SaveSharedPrefrence prefrence;
    String lang;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;


    //  TextView tvRep,tvRepall,tvHealth,tvHealthall;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        getSet = GetSet.getInstance();
        prefrence = new SaveSharedPrefrence();
        u_type = prefrence.getUserType(getActivity());
        lang = prefrence.getlang(getActivity());
        vpPager = (ViewPager)rootView.findViewById(R.id.vpPager);
        mIndicator = (CirclePageIndicator)rootView.findViewById(R.id.indicator);
        rvSList = (GridView)rootView.findViewById(R.id.rvSList);

        rvSList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                map = cat_list.get(position);
                String c_id = map.get("c_id");
                String c_name = map.get("c_title");
                String c_ar_title = map.get("c_ar_title");
                String c_is_sub_category = map.get("c_is_sub_category");
                int temp = MyApplication.getDefaultLanguage();
                if (temp == 1) {
                    getSet.setsubc_id(c_id);
                    getSet.setsubc_name(c_ar_title);

                } else {
                    getSet.setsubc_id(c_id);
                    getSet.setsubc_name(c_name);
                }


                if (c_is_sub_category.equals("1")) {
                    Intent i = new Intent(getActivity(), SubCateActivity.class);
                    startActivity(i);
                } else {
                    diaplay();
                }
                /*if (c_is_sub_category.equals("1")) {
                    getSet.setc_id(c_id);
                    getSet.setC_name(c_name);
                    Intent i = new Intent(getActivity(), SubCateActivity.class);
                    startActivity(i);
                }else{
                    getSet.setsubc_id(c_id);
                    getSet.setsubc_name(c_name);

                    Intent i =new Intent(getActivity(),ServiceListActivity.class);
                    startActivity(i);
                }*/

            }
        });




        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//we are connected to a network
            //  url=URLCollection.LoginUrl+"&u_email="+email+"&u_password="+password+"&device_type=0&device_token="+regId;
            //   SignInUrlCall(url);

            new Slider(getActivity()).execute();
            new MainCategory(getActivity()).execute();

        } else {
            Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
        }


        return rootView;
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

    @Override
    public void onResume() {
        super.onResume();
        HomeActivity.layout_home.setBackgroundResource(R.color.colorPrimary);
        HomeActivity.layout_project.setBackgroundResource(R.color.black);
        HomeActivity.layout_search.setBackgroundResource(R.color.black);
        HomeActivity.layout_book.setBackgroundResource(R.color.black);
        HomeActivity.layout_account.setBackgroundResource(R.color.black);
        HomeActivity.ivTitle.setVisibility(View.VISIBLE);
        HomeActivity.tvTitle.setVisibility(View.GONE);
    }
    class MainCategory extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg,c_id,c_ar_title,c_images,c_type,c_title,c_is_parent_id,c_total_services,c_created,s_modified,c_is_sub_category;// u_id, u_first_name, u_last_name, u_email1, u_password1, u_address, u_latitute, u_longitute, u_city, u_phone, u_postcode, u_country, u_status, u_type, u_is_notification_sound, u_created, u_modified;

        MainCategory(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            //pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            cat_list = new ArrayList<HashMap<String, String>>();
            String uri =  Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", "getMainCategoryList"));
            nameValuePairs.add(new BasicNameValuePair("lang",lang));

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
                    for (int i = 0;i<data.length();i++){
                        JSONObject object = data.getJSONObject(i);
                        c_id = object.getString("c_id");
                        c_images = object.getString("c_images");
                        c_type = object.getString("c_type");
                        c_title = object.getString("c_title");
                        c_is_parent_id = object.getString("c_is_parent_id");
                        c_total_services = object.getString("c_total_services");
                        c_created = object.getString("c_created");
                        s_modified = object.getString("s_modified");
                        c_ar_title = object.getString("c_ar_title");
                        c_is_sub_category = object.getString("c_is_sub_category");

                        map = new HashMap<String, String>();
                        map.put("c_id",c_id);
                        map.put("c_images",c_images);
                        map.put("c_type",c_type);
                        map.put("c_title",c_title);
                        map.put("c_is_parent_id",c_is_parent_id);
                        map.put("c_total_services",c_total_services);
                        map.put("c_created",c_created);
                        map.put("s_modified",s_modified);
                        map.put("c_ar_title",c_ar_title);
                        map.put("c_is_sub_category",c_is_sub_category);
                        cat_list.add(map);

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

            //pDialog.dismiss();
            horizonatalListAdapter = new HorizonatalListAdapter(getActivity(),cat_list);
            rvSList.setAdapter(horizonatalListAdapter);
            horizonatalListAdapter.notifyDataSetChanged();
        }
    }
    class Slider extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg,s_id,s_title,s_desc,s_image,s_created;

        Slider(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
           // pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            sl_list = new ArrayList<HashMap<String, String>>();

            String uri =  Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", "getHomeSlider"));
            nameValuePairs.add(new BasicNameValuePair("lang",lang));

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
                    for (int i = 0;i<data.length();i++){
                        JSONObject object = data.getJSONObject(i);
                        s_id = object.getString("s_id");
                        s_title = object.getString("s_title");
                        s_desc = object.getString("s_desc");
                        s_image = object.getString("s_image");
                        s_created = object.getString("s_created");

                        sl_map = new HashMap<String, String>();
                        sl_map.put("s_id",s_id);
                        sl_map.put("s_title",s_title);
                        sl_map.put("s_desc",s_desc);
                        sl_map.put("s_image",s_image);
                        sl_map.put("s_created",s_created);
                        sl_list.add(sl_map);

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

           // pDialog.dismiss();
            mCustomPagerAdapter = new CustomPagerAdapter(getActivity(),sl_list);
            vpPager.setAdapter(mCustomPagerAdapter);
            mIndicator.setViewPager(vpPager);

            NUM_PAGES =sl_list.size();

            // Auto start of viewpager
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == NUM_PAGES) {
                        currentPage = 0;
                    }
                    vpPager.setCurrentItem(currentPage++, true);
                }
            };
            Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 3000, 3000);

            // Pager listener over indicator
            mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;

                }

                @Override
                public void onPageScrolled(int pos, float arg1, int arg2) {

                }

                @Override
                public void onPageScrollStateChanged(int pos) {

                }
            });

        }
    }
}
