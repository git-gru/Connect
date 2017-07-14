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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jarrebnnee.connect.Adapter.SerachCateGVAdapter;
import com.jarrebnnee.connect.MyApplication;
import com.jarrebnnee.connect.R;
import com.jarrebnnee.connect.SellerJobListActivity;
import com.jarrebnnee.connect.Service.GetSet;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.ServiceHandler;
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

public class Search_Fragement extends Fragment {

    GridView gvCate;
    SerachCateGVAdapter adapter;
    ArrayList<HashMap<String, String>> cat_list;
    HashMap<String, String> map;
    String[] cate_name = {"Repairs & Home Service", "Beauty and Health", "Lessons and Hobbies", "Events and Weddings", "Business Services", "Personal and More"};
    SubCateActivity subCateActivity;
    GetSet getSet;
    String key;
    String u_type, u_id;
    SaveSharedPrefrence prefrence;
    LinearLayout ll_no;
    public static LinearLayout layout_home, layout_project, layout_search, layout_book, layout_account;
    EditText etSearch;
    String lang;
    TextView tv;
    Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        getSet = GetSet.getInstance();
        prefrence = new SaveSharedPrefrence();
        u_type = prefrence.getUserType(getActivity());
        u_id = prefrence.getUserID(getActivity());
        lang = prefrence.getlang(getActivity());
        gvCate = (GridView) rootView.findViewById(R.id.gvCate);

        layout_home = (LinearLayout) getActivity().findViewById(R.id.layout_home);
        layout_search = (LinearLayout) getActivity().findViewById(R.id.layout_search);
        layout_project = (LinearLayout) getActivity().findViewById(R.id.layout_project);
        layout_book = (LinearLayout) getActivity().findViewById(R.id.layout_book);
        layout_account = (LinearLayout) getActivity().findViewById(R.id.layout_account);

        tv = (TextView) rootView.findViewById(R.id.tv_no_service);
        btn = (Button) rootView.findViewById(R.id.btn_set_new);
        etSearch = (EditText) rootView.findViewById(R.id.tv__Search);

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/16023_futuran_0.ttf");
        tv.setTypeface(custom_font);
        btn.setTypeface(custom_font);
        cat_list = new ArrayList<HashMap<String, String>>();
        adapter = new SerachCateGVAdapter(getActivity(), cat_list);
        gvCate.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                    getSet.setSearch("");
                    String str = etSearch.getText().toString();
                    getSet.setSearch(str);
              /*      InputMethodManager inputManager =
                            (InputMethodManager) getActivity().
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);*/
                  /*  if (u_type.equals("2")){
                        Search_Job_Fragement fragemebt_search = new Search_Job_Fragement();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragemebt_search);
                        fragmentTransaction.commit();
                    }else {
                        Search_Seller_Fragement fragemebt_search = new Search_Seller_Fragement();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragemebt_search);
                        fragmentTransaction.commit();
                    }*/

                    getMainCategory();

                  /*  layout_home.setBackgroundResource(R.color.black);
                    layout_project.setBackgroundResource(R.color.black);
                    layout_search.setBackgroundResource(R.color.black);
                    layout_book.setBackgroundResource(R.color.black);
                    layout_account.setBackgroundResource(R.color.black);*/



            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetServiceForSellerFragement fragemebt_seller = new SetServiceForSellerFragement();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragemebt_seller);
                // fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                layout_home.setBackgroundResource(R.color.black);
                layout_project.setBackgroundResource(R.color.black);
                layout_search.setBackgroundResource(R.color.black);
                layout_book.setBackgroundResource(R.color.colorPrimary);
                layout_account.setBackgroundResource(R.color.black);
            }
        });



        ll_no = (LinearLayout) rootView.findViewById(R.id.ll_no_service_set);
        ll_no.setVisibility(View.GONE);
        gvCate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                map = cat_list.get(position);
                String c_id = map.get("c_id");
                String c_name = map.get("c_title");
                String c_is_sub_category = map.get("c_is_sub_category");
                String c_ar_title = map.get("c_ar_title");
                int temp = MyApplication.getDefaultLanguage();
                if (temp == 1) {
                    getSet.setsubc_id(c_id);
                    getSet.setsubc_name(c_ar_title);

                } else {
                    getSet.setsubc_id(c_id);
                    getSet.setsubc_name(c_name);
                }

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
            /*if (u_type.equals("2")) {
                new SellerMainCategory(getActivity()).execute();
            }else {
                new MainCategory(getActivity()).execute();
            }*/
          getMainCategory();
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

    void getMainCategory() {
        if (cat_list.size() > 0||cat_list!=null) {
            cat_list.clear();
            adapter.notifyDataSetChanged();
        }
        String key = etSearch.getText().toString();
        String url=Urlcollection.url+"action=getMainCategoryList&u_id="+u_id+"&key="+key;
        Log.e("harshad", "url: " + url);
        StringRequest request= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("harshad", "response: " + response);
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    String status = jsonObj.getString("status");
                    String msg = jsonObj.getString("message");
                    Log.e("msg", msg);
                    JSONArray data = jsonObj.getJSONArray("data");
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
                        adapter.notifyDataSetChanged();
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

    class MainCategory extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg, c_ar_title, c_id, c_images, c_type, c_title, c_is_parent_id, c_total_services, c_created, s_modified, c_is_sub_category;// u_id, u_first_name, u_last_name, u_email1, u_password1, u_address, u_latitute, u_longitute, u_city, u_phone, u_postcode, u_country, u_status, u_type, u_is_notification_sound, u_created, u_modified;

        MainCategory(Context context) {

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

            String uri = Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", "getMainCategoryList"));
            nameValuePairs.add(new BasicNameValuePair("key", key));
            nameValuePairs.add(new BasicNameValuePair("u_id",u_id));
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
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        c_id = object.getString("c_id");
                        c_images = object.getString("c_images");
                        c_type = object.getString("c_type");
                        c_title = object.getString("c_title");
                        c_is_parent_id = object.getString("c_is_parent_id");
                        c_total_services = object.getString("c_total_services");
                        c_created = object.getString("c_created");
                        c_ar_title = object.getString("c_ar_title");
                        s_modified = object.getString("s_modified");
                        c_is_sub_category = object.getString("c_is_sub_category");

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
                        adapter.notifyDataSetChanged();
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
          /*  adapter = new SerachCateGVAdapter(getActivity(), cat_list);
            gvCate.setAdapter(adapter);*/
        }
    }
    class SellerMainCategory extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg, c_id, c_ar_title, c_images, c_type, c_title, c_is_parent_id, c_total_services, c_created, s_modified, c_is_sub_category;// u_id, u_first_name, u_last_name, u_email1, u_password1, u_address, u_latitute, u_longitute, u_city, u_phone, u_postcode, u_country, u_status, u_type, u_is_notification_sound, u_created, u_modified;

        SellerMainCategory(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            key = etSearch.getText().toString();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getResources().getString(R.string.pdialog));
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            cat_list = new ArrayList<HashMap<String, String>>();
            String uri = Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", "getMainCategoryList"));
                nameValuePairs.add(new BasicNameValuePair("u_id", u_id));
            nameValuePairs.add(new BasicNameValuePair("key", key));
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
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        c_id = object.getString("c_id");
                        c_images = object.getString("c_images");
                        c_type = object.getString("c_type");
                        c_title = object.getString("c_title");
                        c_is_parent_id = object.getString("c_is_parent_id");
                        c_total_services = object.getString("c_total_services");
                        c_ar_title = object.getString("c_ar_title");
                        c_created = object.getString("c_created");
                        s_modified = object.getString("s_modified");
                        c_is_sub_category = object.getString("c_is_sub_category");

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
                        adapter.notifyDataSetChanged();
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
            if (cat_list.size() <= 0) {
                gvCate.setVisibility(View.GONE);
                ll_no.setVisibility(View.VISIBLE);
            } else {
                gvCate.setVisibility(View.VISIBLE);
                ll_no.setVisibility(View.GONE);

            }

        }
    }

}
