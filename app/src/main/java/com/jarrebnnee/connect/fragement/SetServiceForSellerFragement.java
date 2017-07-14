package com.jarrebnnee.connect.fragement;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jarrebnnee.connect.Adapter.BookCateListAdapter;
import com.jarrebnnee.connect.Adapter.BookCityListAdapter;
import com.jarrebnnee.connect.Adapter.BookCountryListAdapter;
import com.jarrebnnee.connect.Adapter.CateListAdapter;
import com.jarrebnnee.connect.Adapter.CateListAdapterTry;
import com.jarrebnnee.connect.Adapter.EditCityListAdapter;
import com.jarrebnnee.connect.Adapter.EditCountryListAdapter;
import com.jarrebnnee.connect.CatListGS;
import com.jarrebnnee.connect.MyApplication;
import com.jarrebnnee.connect.R;
import com.jarrebnnee.connect.SellerServiceSetActivity;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.ServiceHandler;
import com.jarrebnnee.connect.Urlcollection;
import com.google.gson.JsonArray;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class SetServiceForSellerFragement extends Fragment {

    public static ArrayList<HashMap<String, String>> list, cat_list;
    ArrayList<CatListGS> myList;
    HashMap<String, String> map;
    EditText etDesc;
    Button btn_submit, btn_my_services;
    Spinner spCountry, spCity;
    ArrayList<HashMap<String, String>> countrylist;
    HashMap<String, String> cmap;
    ArrayList<HashMap<String, String>> citylist;
    HashMap<String, String> citymap;
    ArrayList<String> userSubscribedServices;

    EditCountryListAdapter countryListAdapter;
    EditCityListAdapter cityListAdapter;
    String s_description, s_id, s_city_id, s_country_id, s_user_id, s_category_id = "";
    SaveSharedPrefrence saveSharedPrefrence;

    public static ArrayList<String> cate_id;

    RecyclerView lvcate;
    TextView tvserv;
    String lang;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragement_seller_service_set, container, false);
        saveSharedPrefrence = new SaveSharedPrefrence();
        s_user_id = saveSharedPrefrence.getUserID(getActivity());
        cate_id = new ArrayList<String>();
        lang = saveSharedPrefrence.getlang(getActivity());


        etDesc = (EditText) rootView.findViewById(R.id.etDesc);
        btn_submit = (Button) rootView.findViewById(R.id.btn_submit);
        btn_my_services = (Button) rootView.findViewById(R.id.btn_my_services);
        spCountry = (Spinner) rootView.findViewById(R.id.spCountry);
        spCity = (Spinner) rootView.findViewById(R.id.spCity);
        tvserv = (TextView) rootView.findViewById(R.id.tvserv);

        lvcate = (RecyclerView) rootView.findViewById(R.id.lvcate);
        //lvcate.setNestedScrollingEnabled(false);
        myList = new ArrayList<>();

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/16023_futuran_0.ttf");
        etDesc.setTypeface(custom_font);
        btn_submit.setTypeface(custom_font);
        btn_my_services.setTypeface(custom_font);
        tvserv.setTypeface(custom_font);
        userSubscribedServices = new ArrayList<>();
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//we are connected to a network
            //  url=URLCollection.LoginUrl+"&u_email="+email+"&u_password="+password+"&device_type=0&device_token="+regId;
            //   SignInUrlCall(url);

            new Country(getActivity()).execute();
            getUserServices();
            new MainCategory(getActivity()).execute();

        } else {
            Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
        }

        btn_my_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SellerServiceSetActivity.class);
                startActivity(intent);
            }
        });

        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map = countrylist.get(position);
                s_country_id = map.get("id");

                new City(getActivity()).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map = citylist.get(position);
                s_city_id = map.get("city_id");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_description = etDesc.getText().toString();


                Log.e("cate_id.size", "" + cate_id.size());
                for (int i = 0; i < cate_id.size(); i++) {
                    s_category_id += cate_id.get(i);
                    s_category_id += ", ";
                    Log.e("s_category_id", "" + s_category_id);
                }
                if (s_category_id.equals("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.pservice), Toast.LENGTH_LONG).show();
                } else {
                    new SetService(getActivity()).execute();
                }
            }
        });


        return rootView;
    }

    private void getUserServices() {
        String u_id = saveSharedPrefrence.getUserID(getActivity());
        String url = Urlcollection.url + "action=getMyService&s_user_id=" + u_id;
        Log.d("url", url);
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getResources().getString(R.string.pdialog));
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("data");
                    if (data.length() > 0) {
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            String c_id = object.getString("c_id");
                            userSubscribedServices.add(c_id);
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
            list = new ArrayList<HashMap<String, String>>();
            String uri = Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", "getMainCategoryList"));
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
                        map.put("isChecked", "true");
                        boolean isChecked = false;
                        if (userSubscribedServices.size() > 0) {
                            for (int k = 0; k < userSubscribedServices.size(); k++) {
                                String u_c_id = userSubscribedServices.get(k);
                                if (u_c_id.equals(c_id)) {
                                    isChecked = true;
                                }
                            }
                        }
                        CatListGS listGS = new CatListGS(c_ar_title, c_id, c_images, c_type, c_title, c_is_parent_id, c_total_services, c_created, s_modified, c_is_sub_category, isChecked);
                        myList.add(listGS);

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
            CateListAdapterTry cateListAdapter = new CateListAdapterTry(getActivity(), myList);
            lvcate.setAdapter(cateListAdapter);

        }
    }

    class Country extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg, id, iso, iso3, fips, country, continent, currency_code, currency_name, phone_prefix, postal_code, languages, geonameid, is_display;

        Country(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            countrylist = new ArrayList<HashMap<String, String>>();
            cmap = new HashMap<String, String>();
            String id1 = "0";
            String country1 = "Country";
            cmap.put("id", id1);
            cmap.put("iso", iso);
            cmap.put("iso3", iso3);
            cmap.put("fips", fips);
            cmap.put("country", country1);
            cmap.put("continent", continent);
            cmap.put("currency_code", currency_code);
            cmap.put("currency_name", currency_name);
            cmap.put("phone_prefix", phone_prefix);
            cmap.put("postal_code", postal_code);
            cmap.put("languages", languages);
            cmap.put("geonameid", geonameid);
            cmap.put("is_display", is_display);
            countrylist.add(cmap);

            String uri = Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", "country_list"));
            nameValuePairs.add(new BasicNameValuePair("lang", lang));

            ServiceHandler handler = new ServiceHandler();
            String jsonSt = handler.makeServiceCall(
                    uri, ServiceHandler.POST, nameValuePairs);
            Log.e("nameValuePairs", "" + nameValuePairs);
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

                        id = da.getString("id");
                        iso = da.getString("iso");
                        iso3 = da.getString("iso3");
                        fips = da.getString("fips");
                        country = da.getString("country");
                        continent = da.getString("continent");
                        currency_code = da.getString("currency_code");
                        currency_name = da.getString("currency_name");
                        phone_prefix = da.getString("phone_prefix");
                        postal_code = da.getString("postal_code");
                        languages = da.getString("languages");
                        geonameid = da.getString("geonameid");
                        is_display = da.getString("is_display");

                        cmap = new HashMap<String, String>();
                        cmap.put("id", id);
                        cmap.put("iso", iso);
                        cmap.put("iso3", iso3);
                        cmap.put("fips", fips);
                        cmap.put("country", country);
                        cmap.put("continent", continent);
                        cmap.put("currency_code", currency_code);
                        cmap.put("currency_name", currency_name);
                        cmap.put("phone_prefix", phone_prefix);
                        cmap.put("postal_code", postal_code);
                        cmap.put("languages", languages);
                        cmap.put("geonameid", geonameid);
                        cmap.put("is_display", is_display);
                        countrylist.add(cmap);
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
            if (status.equals("0")) {
                Log.e("get id", "" + id);
                countryListAdapter = new EditCountryListAdapter(getActivity(), countrylist);
                spCountry.setAdapter(countryListAdapter);

            } else {

            }
        }
    }

    class City extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg, city_id, city_country_id, city_name;

        City(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            citylist = new ArrayList<HashMap<String, String>>();
            citymap = new HashMap<String, String>();
            String city_id1 = "0";
            String city_name1 = "City";

            citymap.put("city_id", city_id1);
            citymap.put("city_country_id", city_country_id);
            citymap.put("city_name", city_name1);
            citylist.add(citymap);
            String uri = Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", "city_list"));
            nameValuePairs.add(new BasicNameValuePair("country_id", s_country_id));
            nameValuePairs.add(new BasicNameValuePair("lang", lang));

            ServiceHandler handler = new ServiceHandler();
            String jsonSt = handler.makeServiceCall(
                    uri, ServiceHandler.POST, nameValuePairs);
            Log.e("nameValuePairs", "" + nameValuePairs);
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
                        city_id = da.getString("city_id");
                        city_country_id = da.getString("city_country_id");
                        city_name = da.getString("city_name");

                        citymap = new HashMap<String, String>();
                        citymap.put("city_id", city_id);
                        citymap.put("city_country_id", city_country_id);
                        citymap.put("city_name", city_name);


                        citylist.add(citymap);
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
            if (status.equals("0")) {
                Log.e("get id", "" + city_id);
                cityListAdapter = new EditCityListAdapter(getActivity(), citylist);
                spCity.setAdapter(cityListAdapter);

            } else {
                cityListAdapter = new EditCityListAdapter(getActivity(), citylist);
                spCity.setAdapter(cityListAdapter);
            }
        }
    }

    class SetService extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg, c_id, c_images, c_type, c_title, c_is_parent_id, c_total_services, c_created, s_modified;// u_id, u_first_name, u_last_name, u_email1, u_password1, u_address, u_latitute, u_longitute, u_city, u_phone, u_postcode, u_country, u_status, u_type, u_is_notification_sound, u_created, u_modified;

        SetService(Context context) {

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
            String uri = Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", "setMyService"));
            nameValuePairs.add(new BasicNameValuePair("s_id ", s_id));
            nameValuePairs.add(new BasicNameValuePair("s_city_id", s_city_id));
            nameValuePairs.add(new BasicNameValuePair("s_country_id", s_country_id));
            nameValuePairs.add(new BasicNameValuePair("s_user_id", s_user_id));
            nameValuePairs.add(new BasicNameValuePair("s_category_id", s_category_id));
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
                Toast.makeText(getActivity(), getResources().getString(R.string.supdatesucess), Toast.LENGTH_LONG).show();
                Home_Fragement fragemebt_home = new Home_Fragement();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragemebt_home);
                fragmentTransaction.commit();
            }
        }
    }
}


