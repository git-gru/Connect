package com.jarrebnnee.connect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jarrebnnee.connect.Adapter.CateListAdapter;
import com.jarrebnnee.connect.Adapter.EditCityListAdapter;
import com.jarrebnnee.connect.Adapter.EditCountryListAdapter;
//import com.jarrebnnee.connect.Service.MultiSelectionSpinner.MultiSpinnerListener;
import com.jarrebnnee.connect.Adapter.SellerViewListAdapter;
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

public class SellerServiceSetActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView ivBack;
    TextView tvTitle;
    public static ArrayList<HashMap<String,String>> list,cat_list;
    HashMap<String,String> map;

    ArrayList<HashMap<String,String>> countrylist;
    HashMap<String,String> cmap;
    ArrayList<HashMap<String,String>> citylist;
    HashMap<String,String> citymap;

    EditCountryListAdapter countryListAdapter;
    EditCityListAdapter cityListAdapter;
    String s_description,s_id,s_city_id,s_country_id,s_user_id,s_category_id="";
    SaveSharedPrefrence saveSharedPrefrence;

    public static ArrayList<String> cate_id;

    RecyclerView lvcate;
    TextView tvserv;
    String lang;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_service_set);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        setSupportActionBar(toolbar);

        saveSharedPrefrence = new SaveSharedPrefrence();
        s_user_id = saveSharedPrefrence.getUserID(getApplicationContext());
        lang = saveSharedPrefrence.getlang(getApplicationContext());
        cate_id = new ArrayList<String>();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "" + "</font>")));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        btn = (Button) findViewById(R.id.btn_set);

        tvserv = (TextView) findViewById(R.id.tvserv);

        lvcate = (RecyclerView)findViewById(R.id.lvcate);
       // lvcate.setNestedScrollingEnabled(false);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/16023_futuran_0.ttf");
        tvTitle.setTypeface(custom_font);
        btn.setTypeface(custom_font);
        Typeface custom_fontbold = Typeface.createFromAsset(getAssets(),  "fonts/tt0144m.ttf");
        tvserv.setTypeface(custom_fontbold);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });





        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            new getMyService(getApplicationContext()).execute();
        } else {
            Toast.makeText(getApplicationContext(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerServiceSetActivity.this, SellerSetServicesActivity.class);
                startActivity(intent);
            }
        });

    }


    class getMyService extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, c_ar_title, msg,c_id,c_images,c_type,c_title,c_is_parent_id,c_total_services,c_created,country_name,city_name,categoty_name,has_sub_category,s_id1,s_city_id1, s_country_id1, s_category_id1, s_description1, s_user_id1;

        getMyService(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SellerServiceSetActivity.this);
            pDialog.setMessage(getResources().getString(R.string.pdialog));
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            list = new ArrayList<HashMap<String, String>>();
            cat_list = new ArrayList<HashMap<String, String>>();
            String uri =  Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", "getMyService"));
            nameValuePairs.add(new BasicNameValuePair("s_user_id", s_user_id));
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
                        s_id1 = object.getString("s_id");
                        s_city_id1 = object.getString("s_city_id");
                        s_country_id1 = object.getString("s_country_id");
                        s_category_id1 = object.getString("s_category_id");
                        s_description1 = object.getString("s_description");
                        s_user_id1 = object.getString("s_user_id");

                        c_id = object.getString("c_id");
                        c_images = object.getString("c_images");
                        c_type = object.getString("c_type");
                        c_title = object.getString("c_title");
                        c_ar_title = object.getString("c_ar_title");
                        c_is_parent_id = object.getString("c_is_parent_id");
                        c_total_services = object.getString("c_total_services");
                        c_created = object.getString("c_created");
                        city_name = object.getString("city_name");
                        country_name = object.getString("country_name");
                        categoty_name = object.getString("categoty_name");

                        JSONArray sub_category = object.getJSONArray("sub_category");
                        if (sub_category.length()>0) {

                            for (int j = 0; j < sub_category.length(); j++) {
                                JSONObject obj = sub_category.getJSONObject(j);
                                String s_id1 = obj.getString("s_id");
                                String s_city_id1 = obj.getString("s_city_id");
                                String s_country_id1 = obj.getString("s_country_id");
                                String s_category_id1 = obj.getString("s_category_id");
                                String s_description1 = obj.getString("s_description");
                                String s_user_id1 = obj.getString("s_user_id");
                                String c_id = obj.getString("c_id");
                                String c_images = obj.getString("c_images");
                                String c_type = obj.getString("c_type");
                                String c_title = obj.getString("c_title");
                                String c_is_parent_id = obj.getString("c_is_parent_id");
                                String c_total_services = obj.getString("c_total_services");
                                String c_created = obj.getString("c_created");
                                String city_name = obj.getString("city_name");
                                String country_name = obj.getString("country_name");
                                String categoty_name = obj.getString("categoty_name");

                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("s_id", s_id1);
                                map.put("s_city_id", s_city_id1);
                                map.put("s_country_id", s_country_id1);
                                map.put("s_category_id", s_category_id1);
                                map.put("s_description", s_description1);
                                map.put("s_user_id", s_user_id1);
                                map.put("c_id", c_id);
                                map.put("c_images", c_images);
                                map.put("c_type", c_type);
                                map.put("c_title", c_title);
                                map.put("c_is_parent_id", c_is_parent_id);
                                map.put("c_total_services", c_total_services);
                                map.put("c_created", c_created);
                                map.put("city_name", city_name);
                                map.put("country_name", country_name);
                                map.put("categoty_name", categoty_name);
                                cat_list.add(map);

                            }
                        }
                        has_sub_category = object.getString("has_sub_category");


                        map = new HashMap<String, String>();
                        map.put("s_id",s_id1);
                        map.put("s_city_id",s_city_id1);
                        map.put("s_country_id",s_country_id1);
                        map.put("s_category_id",s_category_id1);
                        map.put("s_description",s_description1);
                        map.put("s_user_id",s_user_id1);
                        map.put("c_id",c_id);
                        map.put("c_images",c_images);
                        map.put("c_type",c_type);
                        map.put("c_title",c_title);
                        map.put("c_ar_title",c_ar_title);
                        map.put("c_is_parent_id",c_is_parent_id);
                        map.put("c_total_services",c_total_services);
                        map.put("c_created",c_created);
                        map.put("city_name",city_name);
                        map.put("country_name",country_name);
                        map.put("categoty_name",categoty_name);
                        map.put("has_sub_category",has_sub_category);
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

            SellerViewListAdapter cateListAdapter = new SellerViewListAdapter(SellerServiceSetActivity.this,list,cat_list);
            lvcate.setAdapter(cateListAdapter);
            cateListAdapter.notifyDataSetChanged();
            lvcate.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
           // recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            Log.e("c_is_parent_id",""+cat_list.size());

        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}
