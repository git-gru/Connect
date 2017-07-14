package com.jarrebnnee.connect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jarrebnnee.connect.Adapter.SubCateAdapter;
import com.jarrebnnee.connect.Service.GetSet;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.ServiceHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

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

public class SubCateActivity extends AppCompatActivity {
    Toolbar toolbar;
    GridView gvsubCate;
    SubCateAdapter adapter;
    ArrayList<HashMap<String,String>> cat_list;
    HashMap<String,String> map;
    String c_id,u_type,u_id;
    String[] cate_name={"Repairs & Home Service","Beauty and Health","Lessons and Hobbies","Events and Weddings","Business Services","Personal and More"};
    GetSet getSet;
    ImageView ivBack;
    TextView tvTitle;
    TextView tvno;
    SaveSharedPrefrence prefrence;
    String lang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_subcate);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvno =(TextView) findViewById(R.id.tvno);
        setSupportActionBar(toolbar);

        prefrence = new SaveSharedPrefrence();
        u_type = prefrence.getUserType(getApplicationContext());
        u_id = prefrence.getUserID(getApplicationContext());
        lang = prefrence.getlang(getApplicationContext());

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "" + "</font>")));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getSet = GetSet.getInstance();
        c_id = getSet.getsubc_id();
        tvTitle.setText(getSet.getsubc_name());
        gvsubCate = (GridView)findViewById(R.id.gvsubCate);

        Typeface custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(),  "fonts/16023_futuran_0.ttf");
        tvTitle.setTypeface(custom_font);
        tvno.setTypeface(custom_font);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gvsubCate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                map = cat_list.get(position);
                String c_id = map.get("c_id");
                String c_name = map.get("c_title");
                getSet.setsubc_id(c_id);
                getSet.setsubc_name(c_name);

                if (u_type.equals("2")){
                    Intent i = new Intent(getApplicationContext(), SellerJobListActivity.class);
                    startActivity(i);
                    finish();
                }else {

                    Intent i = new Intent(getApplicationContext(), ServiceListActivity.class);
                    startActivity(i);
                    finish();
                }
               // switchContent(serviceListActivity,c_name);
            }
        });
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//we are connected to a network
            //  url=URLCollection.LoginUrl+"&u_email="+email+"&u_password="+password+"&device_type=0&device_token="+regId;
            //   SignInUrlCall(url);
            new SubCategory(getApplicationContext()).execute();
        } else {
            Toast.makeText(getApplicationContext(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
        }


    }

    class SubCategory extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg,c_id1,c_images,c_type,c_title,c_is_parent_id,c_total_services,c_created,s_modified;// u_id, u_first_name, u_last_name, u_email1, u_password1, u_address, u_latitute, u_longitute, u_city, u_phone, u_postcode, u_country, u_status, u_type, u_is_notification_sound, u_created, u_modified;

        SubCategory(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SubCateActivity.this);
            pDialog.setMessage(getResources().getString(R.string.pdialog));
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            cat_list = new ArrayList<HashMap<String, String>>();
            String uri =  Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", "getSubCategoryList"));
            nameValuePairs.add(new BasicNameValuePair("c_id", c_id));
            nameValuePairs.add(new BasicNameValuePair("lang",lang));
            if (u_type.equals("2")) {
                nameValuePairs.add(new BasicNameValuePair("u_id", u_id));
            }

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
                        c_id1 = object.getString("c_id");
                        c_images = object.getString("c_images");
                        c_type = object.getString("c_type");
                        c_title = object.getString("c_title");
                        c_is_parent_id = object.getString("c_is_parent_id");
                        c_total_services = object.getString("c_total_services");
                        c_created = object.getString("c_created");
                        s_modified = object.getString("s_modified");

                        map = new HashMap<String, String>();
                        map.put("c_id",c_id1);
                        map.put("c_images",c_images);
                        map.put("c_type",c_type);
                        map.put("c_title",c_title);
                        map.put("c_is_parent_id",c_is_parent_id);
                        map.put("c_total_services",c_total_services);
                        map.put("c_created",c_created);
                        map.put("s_modified",s_modified);
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

            pDialog.dismiss();
            if (status.equals("0")) {
                tvno.setVisibility(View.GONE);
                gvsubCate.setVisibility(View.VISIBLE);
                adapter = new SubCateAdapter(getApplicationContext(), cat_list);
                gvsubCate.setAdapter(adapter);
            }else {
                tvno.setVisibility(View.VISIBLE);
                gvsubCate.setVisibility(View.GONE);
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageLoader.getInstance().destroy();

    }

}
