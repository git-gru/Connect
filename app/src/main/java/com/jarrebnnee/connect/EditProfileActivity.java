package com.jarrebnnee.connect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jarrebnnee.connect.Adapter.EditCityListAdapter;
import com.jarrebnnee.connect.Adapter.EditCountryListAdapter;
import com.jarrebnnee.connect.Service.GetSet;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.ServiceHandler;
import com.jarrebnnee.connect.Service.TrackGPS;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Vardhman Infonet 4 on 22-Feb-17.
 */

public class EditProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView ivBack;
    EditText edFName,edLName,edCNo,edEmail;
    Button btn_submit;
    SaveSharedPrefrence saveSharedPrefrence;
    String u_first_name,u_last_name,u_phone,u_email,u_longitute,u_latitute,country_id,city_id,u_gender,u_image;
    TrackGPS gps;
    GetSet getSet;
    String mr_device_token,u_id,u_address,u_city,u_postcode,u_country,u_is_notification_sound;
    Spinner spCountry,spCity;
    ArrayList<HashMap<String,String>> countrylist;
    HashMap<String,String> cmap;
    ArrayList<HashMap<String,String>> citylist;
    HashMap<String,String> citymap;
    EditCountryListAdapter countryListAdapter;
    EditCityListAdapter cityListAdapter;
    TextView tvTitle,tv, tvTap;
    RadioButton rbMale,rbFemale;
    ImageView iv1;
    private static int RESULT_LOAD_IMG = 1;
    private String imagepath=null;
    private int serverResponseCode = 0;
    String lang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragement_edit_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "" + "</font>")));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        saveSharedPrefrence = new SaveSharedPrefrence();
        getSet = GetSet.getInstance();
        lang = saveSharedPrefrence.getlang(getApplicationContext());
        mr_device_token = saveSharedPrefrence.getDeviceToken(getApplicationContext());
        u_id = saveSharedPrefrence.getUserID(getApplicationContext());
        u_first_name = saveSharedPrefrence.getUserFName(getApplicationContext());
        u_last_name = saveSharedPrefrence.getUserLName(getApplicationContext());
        u_phone = saveSharedPrefrence.getUserPhone(getApplicationContext());
        u_email = saveSharedPrefrence.getUserEmail(getApplicationContext());
        country_id = saveSharedPrefrence.getUserCountry(getApplicationContext());
        city_id = saveSharedPrefrence.getUserCity(getApplicationContext());
        u_gender = saveSharedPrefrence.getUserGender(getApplicationContext());
        u_image = saveSharedPrefrence.getUserImage(getApplicationContext());

        btn_submit = (Button)findViewById(R.id.btn_submit);
        edFName = (EditText)findViewById(R.id.edFName);
        edLName = (EditText)findViewById(R.id.edLName);
        edCNo = (EditText)findViewById(R.id.edCNo);
        edEmail = (EditText)findViewById(R.id.edEmail);
        spCountry = (Spinner) findViewById(R.id.spCountry);
        spCity = (Spinner) findViewById(R.id.spCity);
        rbMale = (RadioButton)findViewById(R.id.rbMale);
        rbFemale = (RadioButton)findViewById(R.id.rbFemale);
        tv = (TextView)findViewById(R.id.tv);
        tvTap = (TextView)findViewById(R.id.tvTap);
        iv1 = (ImageView)findViewById(R.id.iv1);

        Typeface custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(),  "fonts/16023_futuran_0.ttf");
        btn_submit.setTypeface(custom_font);
        edFName.setTypeface(custom_font);
        edLName.setTypeface(custom_font);
        edCNo.setTypeface(custom_font);
        edEmail.setTypeface(custom_font);
        tvTitle.setTypeface(custom_font);
        tv.setTypeface(custom_font);
        tvTap.setTypeface(custom_font);
        rbMale.setTypeface(custom_font);
        rbFemale.setTypeface(custom_font);

        edEmail.setText(u_email);
        edCNo.setText(u_phone);
        edFName.setText(u_first_name);
        edLName.setText(u_last_name);
        Picasso.with(getApplicationContext()).load(u_image).into(iv1);

        if (u_gender.equals("M")){
            rbMale.setChecked(true);
            rbFemale.setChecked(false);
        }else{
            rbFemale.setChecked(true);
            rbMale.setChecked(false);
        }

        gps = new TrackGPS(getApplicationContext());
        if (gps.canGetLocation()) {

            u_longitute = String.valueOf(gps.getLongitude());
            u_latitute = String.valueOf(gps.getLatitude());

          //  Toast.makeText(getApplicationContext(), "Longitude:" + u_longitute + "\nLatitude:" + u_latitute, Toast.LENGTH_SHORT).show();
            Log.e("latlong", "Longitude:" + u_longitute + "\nLatitude:" + u_latitute);
        } else {
            gps.showSettingsAlert();
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rbMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rbMale.isChecked()){
                    rbMale.setChecked(true);
                    rbFemale.setChecked(false);
                }else{
                    rbFemale.setChecked(true);
                    rbMale.setChecked(false);
                }
            }
        });
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
// Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });
        rbFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rbFemale.isChecked()){
                    rbFemale.setChecked(true);
                    rbMale.setChecked(false);
                }else{
                    rbMale.setChecked(true);
                    rbFemale.setChecked(false);
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( edFName.getText().toString().length() == 0 ) {
                    edFName.setError(getResources().getString(R.string.fnamerequired));
                }else if( edLName.getText().toString().length() == 0 ) {
                    edLName.setError(getResources().getString(R.string.lnamerequired));
                }else if( edCNo.getText().toString().length() == 0 ) {
                    edCNo.setError(getResources().getString(R.string.contactrequired));
                }else if( edEmail.getText().toString().length() == 0 ) {
                    edEmail.setError(getResources().getString(R.string.emailrequired));
                }else if( country_id.equals("0")) {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.countryrequired),Toast.LENGTH_LONG).show();
                }else if( city_id.equals("0")) {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.cityrequired),Toast.LENGTH_LONG).show();
                }else {
                    if (rbMale.isChecked()){
                        u_gender = "M";
                    }else{
                        u_gender = "F";
                    }
                    u_email = edEmail.getText().toString();
                    u_phone = edCNo.getText().toString();
                    u_first_name = edFName.getText().toString();
                    u_last_name = edLName.getText().toString();
                    new UserProfile(getApplicationContext()).execute();

                }
            }
        });

        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> map = countrylist.get(position);
                country_id = map.get("id");

                new City(getApplicationContext()).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> map = citylist.get(position);
                city_id = map.get("city_id");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//we are connected to a network
            //  url=URLCollection.LoginUrl+"&u_email="+email+"&u_password="+password+"&device_type=0&device_token="+regId;
            //   SignInUrlCall(url);
            new Country(getApplicationContext()).execute();
        } else {
            Toast.makeText(getApplicationContext(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        if (requestCode == RESULT_LOAD_IMG && responseCode == RESULT_OK
                && null != data) {
            // Get the Image from data

            Uri selectedImage = data.getData();
            imagepath = getPath(selectedImage);
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            // Get the cursor
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgDecodableString = cursor.getString(columnIndex);
            cursor.close();
            Log.e("imgDecodableString",""+imgDecodableString);
            String fileName = imgDecodableString.substring(imgDecodableString.lastIndexOf('/') + 1);
           // tvProfile.setText(fileName);

            // new UploadFileAsync(imgDecodableString).execute();


        /*    try {
                byte[] str = imgDecodableString.getBytes("UTF-8");
                u_img = Base64.encodeToString(str, Base64.DEFAULT);
                Log.e("base64",""+u_img);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/

            //ImageView imgView = (ImageView) findViewById(R.id.imgView);
            // Set the Image in ImageView after decoding the String
             iv1.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
            new Thread(new Runnable() {
                public void run() {

                    uploadFile(imagepath,u_id,u_first_name,u_last_name,u_latitute,u_longitute,mr_device_token);

                }
            }).start();


        } else {
           /* Toast.makeText(this, "You haven't picked Image",
                    Toast.LENGTH_LONG).show();*/
        }
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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
            cmap.put("id",id1);
            cmap.put("iso",iso);
            cmap.put("iso3",iso3);
            cmap.put("fips",fips);
            cmap.put("country",country1);
            cmap.put("continent",continent);
            cmap.put("currency_code",currency_code);
            cmap.put("currency_name",currency_name);
            cmap.put("phone_prefix",phone_prefix);
            cmap.put("postal_code",postal_code);
            cmap.put("languages",languages);
            cmap.put("geonameid",geonameid);
            cmap.put("is_display",is_display);
            countrylist.add(cmap);

            String uri =  Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action","country_list"));
            nameValuePairs.add(new BasicNameValuePair("lang",lang));

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
                    for (int i = 0;i<data.length();i++) {
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
                        cmap.put("id",id);
                        cmap.put("iso",iso);
                        cmap.put("iso3",iso3);
                        cmap.put("fips",fips);
                        cmap.put("country",country);
                        cmap.put("continent",continent);
                        cmap.put("currency_code",currency_code);
                        cmap.put("currency_name",currency_name);
                        cmap.put("phone_prefix",phone_prefix);
                        cmap.put("postal_code",postal_code);
                        cmap.put("languages",languages);
                        cmap.put("geonameid",geonameid);
                        cmap.put("is_display",is_display);
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
                countryListAdapter = new EditCountryListAdapter(getApplicationContext(),countrylist);
                spCountry.setAdapter(countryListAdapter);

                for (int i=0;i<countrylist.size();i++){
                    cmap = countrylist.get(i);
                    String id = cmap.get("id");

                    if (id.equals(country_id)){
                        spCountry.setSelection(i);
                    }
                }

            } else {

            }
        }
    }


    class City extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg, city_id1, city_country_id, city_name;

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
            String city_id11 = "0";
            String city_name1 = "City";

            citymap.put("city_id",city_id11);
            citymap.put("city_country_id",city_country_id);
            citymap.put("city_name",city_name1);
            citylist.add(citymap);
            String uri =  Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action","city_list"));
            nameValuePairs.add(new BasicNameValuePair("country_id",country_id));
            nameValuePairs.add(new BasicNameValuePair("lang",lang));

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
                    for (int i = 0;i<data.length();i++) {
                        JSONObject da = data.getJSONObject(i);
                        city_id1 = da.getString("city_id");
                        city_country_id = da.getString("city_country_id");
                        city_name = da.getString("city_name");

                        citymap = new HashMap<String, String>();
                        citymap.put("city_id",city_id1);
                        citymap.put("city_country_id",city_country_id);
                        citymap.put("city_name",city_name);


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
                Log.e("get id", "" + city_id1);
                cityListAdapter = new EditCityListAdapter(getApplicationContext(),citylist);
                spCity.setAdapter(cityListAdapter);
                for (int i=0;i<citylist.size();i++){
                    citymap = citylist.get(i);
                    String id = citymap.get("city_id");

                    if (id.equals(city_id)){
                        spCity.setSelection(i);
                    }
                }

            } else {
                cityListAdapter = new EditCityListAdapter(getApplicationContext(),citylist);
                spCity.setAdapter(cityListAdapter);

                for (int i=0;i<citylist.size();i++){
                    citymap = citylist.get(i);
                    String id = citymap.get("city_id");

                    if (id.equals(city_id)){
                        spCity.setSelection(i);
                    }
                }
            }
        }
    }
    class UserProfile extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg,u_id1, u_first_name1, u_last_name1, u_email1, u_password1, u_address, u_latitute1, u_longitute1, u_city1, u_phone1, u_postcode, u_country1, u_status, u_type1, u_is_notification_sound1, u_created, u_modified,u_gender1,u_img;

        UserProfile(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /*pDialog = new ProgressDialog(EditProfileActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();*/

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub

            String uri =  Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", "user_profile"));
            nameValuePairs.add(new BasicNameValuePair("mr_device_token", mr_device_token));
            nameValuePairs.add(new BasicNameValuePair("u_id", u_id));
            nameValuePairs.add(new BasicNameValuePair("u_address", u_address));
            nameValuePairs.add(new BasicNameValuePair("u_email", u_email));
            nameValuePairs.add(new BasicNameValuePair("u_first_name", u_first_name));
            nameValuePairs.add(new BasicNameValuePair("u_last_name", u_last_name));
            nameValuePairs.add(new BasicNameValuePair("u_city", city_id));
            nameValuePairs.add(new BasicNameValuePair("u_phone", u_phone));
            nameValuePairs.add(new BasicNameValuePair("u_postcode", u_postcode));
            nameValuePairs.add(new BasicNameValuePair("u_country", country_id));
            nameValuePairs.add(new BasicNameValuePair("u_is_notification_sound", u_is_notification_sound));
            nameValuePairs.add(new BasicNameValuePair("u_latitute", u_latitute));
            nameValuePairs.add(new BasicNameValuePair("u_longitute", u_longitute));
            nameValuePairs.add(new BasicNameValuePair("u_gender",u_gender));
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
                   JSONObject data = jsonObj.getJSONObject("data");
                   u_id1 = data.getString("u_id");
                    u_first_name1 = data.getString("u_first_name");
                    u_last_name1 = data.getString("u_last_name");
                    u_email1 = data.getString("u_email");
                    u_password1 = data.getString("u_password");
                    u_address = data.getString("u_address");
                    u_latitute1 = data.getString("u_latitute");
                    u_longitute1 = data.getString("u_longitute");
                    u_city1 = data.getString("u_city");
                    u_phone1 = data.getString("u_phone");
                    u_postcode = data.getString("u_postcode");
                    u_country1 = data.getString("u_country");
                    u_status = data.getString("u_status");
                    u_type1 = data.getString("u_type");
                    u_is_notification_sound1 = data.getString("u_is_notification_sound");
                    u_created = data.getString("u_created");
                    u_modified = data.getString("u_modified");
                    u_gender1 = data.getString("u_gender");
                    u_img = data.getString("u_img");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

          //  Log.e("imagepath",imagepath);
          //  pDialog.dismiss();
            if (status.equals("0")) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.pupdate), Toast.LENGTH_LONG).show();
                saveSharedPrefrence.saveUserFName(getApplicationContext(), u_first_name1);
                saveSharedPrefrence.saveUserLName(getApplicationContext(), u_last_name1);
                saveSharedPrefrence.saveUserPhone(getApplicationContext(), u_phone1);
                saveSharedPrefrence.saveUserEmail(getApplicationContext(), u_email1);
                saveSharedPrefrence.saveUserCountry(getApplicationContext(), u_country1);
                saveSharedPrefrence.saveUserCity(getApplicationContext(), u_city1);
                saveSharedPrefrence.saveUserGender(getApplicationContext(), u_gender1);
                saveSharedPrefrence.saveUserImage(getApplicationContext(), u_img);

                finish();
            }else {
                Toast.makeText(getApplicationContext(), ""+msg, Toast.LENGTH_LONG).show();
            }

        }
    }
    public void uploadFile(String sourceFileUri,String u_id,String u_first_name1,String u_last_name1,String u_latitute1,String u_longitute1,String mr_device_token) {


        String fileName = sourceFileUri;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);
        Log.e("u_id", "u_id:" + u_id);

        if (!sourceFile.isFile()) {

          /*  if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }*/

            Log.e("uploadFile", "Source File not exist :" + sourceFileUri);


        } else {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL("http://jarbni.com/connect/ebay_clone_api/?action=image_upload&u_id=" + u_id);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                Log.e("Content-Type", boundary);
                conn.setRequestProperty("uploaded_file", sourceFile.getAbsolutePath());

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + sourceFile.getAbsolutePath() + "\"" + lineEnd);

                dos.writeBytes(lineEnd);
                Log.e("url", String.valueOf(url));

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)

                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();


                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                StringBuilder sb = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    sb.append(output);
                }
                Log.i("conn.getHeaderField()", "" + sb.toString());

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                //close the streams //
                fileInputStream.close();

                dos.flush();
                dos.close();


           /* } catch (MalformedURLException ex1) {

               *//* if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }*//*
                ex1.printStackTrace();

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e1) {

               *//* if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }*//*
                e1.printStackTrace();


            }*/

            /*if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();

            }*/


            } // End else block
            catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        saveSharedPrefrence = new SaveSharedPrefrence();
        getSet = GetSet.getInstance();
        mr_device_token = saveSharedPrefrence.getDeviceToken(getApplicationContext());
        u_id = saveSharedPrefrence.getUserID(getApplicationContext());
        u_first_name = saveSharedPrefrence.getUserFName(getApplicationContext());
        u_last_name = saveSharedPrefrence.getUserLName(getApplicationContext());
        u_phone = saveSharedPrefrence.getUserPhone(getApplicationContext());
        u_email = saveSharedPrefrence.getUserEmail(getApplicationContext());
        country_id = saveSharedPrefrence.getUserCountry(getApplicationContext());
        city_id = saveSharedPrefrence.getUserCity(getApplicationContext());
    }

    public void switchContent(Fragment fragment, String mItemSelected) {
        if (getApplicationContext() == null)
            return;
        if (getApplicationContext() instanceof HomeActivity) {
            HomeActivity mainActivity = (HomeActivity) getApplicationContext();
            Fragment frag = fragment;
            mainActivity.switchContent(fragment,mItemSelected);
        }

    }

}
