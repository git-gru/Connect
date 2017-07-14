package com.jarrebnnee.connect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.jarrebnnee.connect.Adapter.CityListAdapter;
import com.jarrebnnee.connect.Adapter.CountryListAdapter;
import com.jarrebnnee.connect.Service.FilePath;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.ServiceHandler;
import com.jarrebnnee.connect.Service.TrackGPS;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SocialSignUpActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView ivBack;
    Button btn_signup;
    EditText edFName,edLName,edCNo,edEmail;
    Spinner spCountry,spCity;

    ArrayList<HashMap<String,String>> countrylist;
    HashMap<String,String> cmap;
    ArrayList<HashMap<String,String>> citylist;
    HashMap<String,String> citymap;

    CountryListAdapter countryListAdapter;
    CityListAdapter cityListAdapter;
    SaveSharedPrefrence sharedPreferences;
    TrackGPS gps;
    String country_id,city_id;
    String u_id,action, mr_device_type, mr_device_token,sm_social_provider,sm_social_provider_id, u_email, u_password="", u_type, u_first_name,u_gender,u_phone,u_last_name, u_latitute, u_longitute,u_image;
    private int serverResponseCode = 0;
    TextView tvTitle;
    String lang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_social_sign_up);
        countrylist = new ArrayList<HashMap<String,String>>();
        citylist = new ArrayList<HashMap<String,String>>();
        sharedPreferences = new SaveSharedPrefrence();

        lang = sharedPreferences.getlang(getApplicationContext());

        u_id = sharedPreferences.getUserID(getApplicationContext());
        u_email = sharedPreferences.getUserEmail(getApplicationContext());
        u_first_name = sharedPreferences.getUserFName(getApplicationContext());
        u_last_name = sharedPreferences.getUserLName(getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "" + "</font>")));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        btn_signup = (Button)findViewById(R.id.btn_signup);
        edFName = (EditText)findViewById(R.id.edFName);
        edLName = (EditText)findViewById(R.id.edLName);
        edCNo = (EditText)findViewById(R.id.edCNo);
        edEmail = (EditText)findViewById(R.id.edEmail);
        spCity = (Spinner)findViewById(R.id.spCity);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        spCountry = (Spinner)findViewById(R.id.spCountry);

        edFName.setText(u_first_name);
        edLName.setText(u_last_name);
        edEmail.setText(u_email);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/16023_futuran_0.ttf");
        edFName.setTypeface(custom_font);
        edLName.setTypeface(custom_font);
        edCNo.setTypeface(custom_font);
        edEmail.setTypeface(custom_font);
        tvTitle.setTypeface(custom_font);
        gps = new TrackGPS(SocialSignUpActivity.this);
        if (gps.canGetLocation()) {

            u_longitute = String.valueOf(gps.getLongitude());
            u_latitute = String.valueOf(gps.getLatitude());

          //  Toast.makeText(getApplicationContext(), "Longitude:" + u_longitute + "\nLatitude:" + u_latitute, Toast.LENGTH_SHORT).show();
            Log.e("latlong", "Longitude:" + u_longitute + "\nLatitude:" + u_latitute);
        } else {
            gps.showSettingsAlert();
        }

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

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( edEmail.getText().toString().length() == 0 ) {
                    edEmail.setError(getResources().getString(R.string.emailrequired));
                }else if( edFName.getText().toString().length() == 0 ) {
                    edFName.setError(getResources().getString(R.string.fnamerequired));
                }else if( edLName.getText().toString().length() == 0 ) {
                    edLName.setError(getResources().getString(R.string.lnamerequired));
                }else if( edCNo.getText().toString().length() == 0 ) {
                    edCNo.setError(getResources().getString(R.string.contactrequired));
                }else if( country_id.equals("0")) {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.countryrequired),Toast.LENGTH_LONG).show();
                }else if( city_id.equals("0")) {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.cityrequired),Toast.LENGTH_LONG).show();
                }else {
                    action = "sociallogin";
                    mr_device_type = "0";
                    mr_device_token = sharedPreferences.getDeviceToken(getApplicationContext());
                    u_email = edEmail.getText().toString();
                    sm_social_provider = sharedPreferences.getSocialAction(getApplicationContext());
                    u_type = sharedPreferences.getUserType(getApplicationContext());
                    sm_social_provider_id = sharedPreferences.getSM_SOCIAL_PROVIDER_ID(getApplicationContext());
                    u_first_name =  edFName.getText().toString();
                    u_last_name =  edLName.getText().toString();
                    u_image = sharedPreferences.getUserImage(getApplicationContext());
                    u_gender = sharedPreferences.getUserGender(getApplicationContext());
                    u_phone =  edCNo.getText().toString();
                    Log.e("uu_image",u_image);
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//we are connected to a network
                        //  url=URLCollection.LoginUrl+"&u_email="+email+"&u_password="+password+"&device_type=0&device_token="+regId;
                        //   SignInUrlCall(url);
                        new SocialLogin(getApplicationContext()).execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
                    }




                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SignInActivity.class);
                startActivity(i);
                finish();
            }
        });


        new Country(getApplicationContext()).execute();
    }
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
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

        String selectedFilePath = FilePath.getPath(this,Uri.parse(sourceFileUri));
        File sourceFile = new File(selectedFilePath);
        if (!sourceFile.isFile()) {

          /*  if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }*/

            Log.e("uploadFile", "Source File not exist :"+sourceFileUri);

        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL("http://jarbni.com/connect/ebay_clone_api/?action=image_upload&u_id="+u_id);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", sourceFile.getAbsolutePath());

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + sourceFile.getAbsolutePath() + "\"" + lineEnd);

                dos.writeBytes(lineEnd);
                Log.e("conn", String.valueOf(conn.getOutputStream()));

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

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);


                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();


            } catch (MalformedURLException ex) {

               /* if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }*/
                ex.printStackTrace();

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

               /* if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }*/
                e.printStackTrace();


            }

            /*if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();

            }*/


        } // End else block
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

            pDialog = new ProgressDialog(SocialSignUpActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            // pDialog.show();

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
                countryListAdapter = new CountryListAdapter(getApplicationContext(),countrylist);
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

            pDialog = new ProgressDialog(SocialSignUpActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            //pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            citylist = new ArrayList<HashMap<String, String>>();
            citymap = new HashMap<String, String>();
            String city_id1 = "0";
            String city_name1 = "City";

            citymap.put("city_id",city_id1);
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
                        city_id = da.getString("city_id");
                        city_country_id = da.getString("city_country_id");
                        city_name = da.getString("city_name");

                        citymap = new HashMap<String, String>();
                        citymap.put("city_id",city_id);
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
                Log.e("get id", "" + city_id);
                cityListAdapter = new CityListAdapter(getApplicationContext(),citylist);
                spCity.setAdapter(cityListAdapter);

            } else {
                cityListAdapter = new CityListAdapter(getApplicationContext(),citylist);
                spCity.setAdapter(cityListAdapter);
            }
        }
    }
    class SocialLogin extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg, u_id1, u_first_name1, u_last_name1, u_email1, u_password1, u_address, u_latitute1, u_longitute1, u_city1, u_gender1,u_phone1, u_postcode, u_country1, u_status, u_type1, u_is_notification_sound, u_created, u_modified,u_img;

        SocialLogin(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SocialSignUpActivity.this);
            pDialog.setMessage(getResources().getString(R.string.pdialog));
            pDialog.setCancelable(false);
            pDialog.show();

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
            nameValuePairs.add(new BasicNameValuePair("u_img",u_image));
            nameValuePairs.add(new BasicNameValuePair("u_type",u_type));
            nameValuePairs.add(new BasicNameValuePair("lang",lang));



            ServiceHandler handler = new ServiceHandler();
            String jsonSt = handler.makeServiceCall(
                    uri, ServiceHandler.POST, nameValuePairs);
            Log.e("nameValuePairs",""+nameValuePairs);
            Log.e("Response: ", "> " + jsonSt);

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
                    u_is_notification_sound = data.getString("u_is_notification_sound");
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

            pDialog.dismiss();
            if (status.equals("0")) {
                Log.e("get id", "" + u_id);
               /* new Thread(new Runnable() {
                    public void run() {

                        uploadFile(u_image,u_id,u_first_name,u_last_name,u_latitute,u_longitute,mr_device_token);

                    }
                }).start();*/
                sharedPreferences.saveUserFName(getApplicationContext(), u_first_name1);
                sharedPreferences.saveUserLName(getApplicationContext(), u_last_name1);
                sharedPreferences.saveUserPhone(getApplicationContext(), u_phone1);
                sharedPreferences.saveUserEmail(getApplicationContext(), u_email1);
                sharedPreferences.saveUserCountry(getApplicationContext(), u_country1);
                sharedPreferences.saveUserCity(getApplicationContext(), u_city1);
                sharedPreferences.saveUserGender(getApplicationContext(), u_gender1);
                sharedPreferences.saveUserID(getApplicationContext(), u_id);
                sharedPreferences.saveUserImage(getApplicationContext(), u_img);
                sharedPreferences.saveUserType(getApplicationContext(),u_type1);//getStatus().getUserId());
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.lsucess), Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
                finish();

                //SignUpActivity.gPlusSignOut();

            } else {
               // if (msg.equals("Email already register")) {
                    /*new Thread(new Runnable() {
                        public void run() {

                            uploadFile(u_image,u_id,u_first_name,u_last_name,u_latitute,u_longitute,mr_device_token);

                        }
                    }).start();*/
                    sharedPreferences.saveUserFName(getApplicationContext(), u_first_name1);
                    sharedPreferences.saveUserLName(getApplicationContext(), u_last_name1);
                    sharedPreferences.saveUserPhone(getApplicationContext(), u_phone1);
                    sharedPreferences.saveUserEmail(getApplicationContext(), u_email1);
                    sharedPreferences.saveUserCountry(getApplicationContext(), u_country1);
                    sharedPreferences.saveUserCity(getApplicationContext(), u_city1);
                    sharedPreferences.saveUserGender(getApplicationContext(), u_gender1);
                    sharedPreferences.saveUserID(getApplicationContext(), u_id);
                    sharedPreferences.saveUserImage(getApplicationContext(), u_img);
                    sharedPreferences.saveUserType(getApplicationContext(),u_type1);//getStatus().getUserId());
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.lsucess), Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(i);
                    finish();
                   // SignUpActivity.gPlusSignOut();
                /*} else {
                    //  Toast.makeText(getApplicationContext(), "Please Sign Up Today", Toast.LENGTH_SHORT).show();
                }*/
            }
        }
    }
}
