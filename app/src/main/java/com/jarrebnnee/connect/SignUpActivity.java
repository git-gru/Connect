package com.jarrebnnee.connect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.util.Base64;
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

import com.jarrebnnee.connect.Adapter.CityListAdapter;
import com.jarrebnnee.connect.Adapter.CountryListAdapter;
import com.jarrebnnee.connect.Helper.Registration.Register;
import com.jarrebnnee.connect.Service.GetSet;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.ServiceHandler;
import com.jarrebnnee.connect.Service.TrackGPS;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SignUpActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    Toolbar toolbar;
    ImageView ivBack;
    TextView tvTitle, tvoruse, tvShow,tvProfile;
    EditText edFName, edLName, edCNo, edEmail, edPsw;
    Button btn_signup;
    boolean i = false;
    String action = "userRagister", mr_device_type = "0", mr_device_token, u_email, u_password, u_type, u_first_name,u_gender,u_phone,u_last_name, u_latitute, u_longitute;
    TrackGPS gps;
    ProgressDialog progress_dialog;
    CallbackManager callbackManager;
    String action_social, sm_social_provider, sm_social_provider_id;
    GetSet getSet;
    LoginButton loginButton;
    String url;

    GoogleApiClient google_api_client;
    GoogleApiAvailability google_api_availability;
    SignInButton signIn_btn;
    SaveSharedPrefrence sharedPreferences;
    private int serverResponseCode = 0;

    private static final int SIGN_IN_CODE = 0;
    private static final int PROFILE_PIC_SIZE = 120;
    private ConnectionResult connection_result;
    private boolean is_intent_inprogress;
    private boolean is_signInBtn_clicked;

    private int request_code;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString,country_id,city_id;
    Spinner spCountry,spCity;
    ArrayList<HashMap<String,String>> countrylist;
    HashMap<String,String> cmap;
    ArrayList<HashMap<String,String>> citylist;
    HashMap<String,String> citymap;

    CountryListAdapter countryListAdapter;
    CityListAdapter cityListAdapter;
    RadioButton rbMale,rbFemale;
    private String imagepath=null;
    TextView tv;
    String lang, facebook_id, f_name, m_name, l_name, full_name, profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        buidNewGoogleApiClient();
        setContentView(R.layout.activity_sign_up);
        callbackManager = CallbackManager.Factory.create();
        sharedPreferences = new SaveSharedPrefrence();
        countrylist = new ArrayList<HashMap<String,String>>();
        citylist = new ArrayList<HashMap<String,String>>();
        lang = sharedPreferences.getlang(getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "" + "</font>")));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getSet = GetSet.getInstance();
        loginButton = (LoginButton) findViewById(R.id.login_button);

        btn_signup = (Button) findViewById(R.id.btn_signup);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvoruse = (TextView) findViewById(R.id.tvoruse);
        edFName = (EditText) findViewById(R.id.edFName);
        edLName = (EditText) findViewById(R.id.edLName);
        edCNo = (EditText) findViewById(R.id.edCNo);
        edEmail = (EditText) findViewById(R.id.edEmail);
        edPsw = (EditText) findViewById(R.id.edPsw);
        tvShow = (TextView) findViewById(R.id.tvShow);
        tvProfile = (TextView) findViewById(R.id.tvProfile);
        spCountry = (Spinner) findViewById(R.id.spCountry);
        spCity = (Spinner) findViewById(R.id.spCity);
        rbMale = (RadioButton)findViewById(R.id.rbMale);
        rbFemale = (RadioButton)findViewById(R.id.rbFemale);
        tv = (TextView)findViewById(R.id.tv);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/16023_futuran_0.ttf");
        btn_signup.setTypeface(custom_font);
        tvTitle.setTypeface(custom_font);
        tvoruse.setTypeface(custom_font);
        edFName.setTypeface(custom_font);
        edLName.setTypeface(custom_font);
        edCNo.setTypeface(custom_font);
        edEmail.setTypeface(custom_font);
        edPsw.setTypeface(custom_font);
        tvShow.setTypeface(custom_font);
        tvProfile.setTypeface(custom_font);
        tv.setTypeface(custom_font);
        rbMale.setTypeface(custom_font);
        rbFemale.setTypeface(custom_font);

        progress_dialog = new ProgressDialog(this);
        progress_dialog.setMessage(getResources().getString(R.string.showdialog));

        tvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == false) {
                    edPsw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    tvShow.setText(getResources().getString(R.string.hide));
                    edPsw.setSelection(edPsw.length());
                    i = true;
                } else {
                    edPsw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    tvShow.setText(getResources().getString(R.string.show));
                    edPsw.setSelection(edPsw.length());
                    i = false;
                }

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
        tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
// Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });
        gps = new TrackGPS(SignUpActivity.this);
        if(gps.canGetLocation()){

            u_longitute = String.valueOf(gps.getLongitude());
            u_latitute = String.valueOf(gps .getLatitude());

          //  Toast.makeText(getApplicationContext(),"Longitude:"+u_longitute+"\nLatitude:"+u_latitute,Toast.LENGTH_SHORT).show();
            Log.e("latlong","Longitude:"+u_longitute+"\nLatitude:"+u_latitute);
        }
        else
        {
            gps.showSettingsAlert();
        }

        signIn_btn = (SignInButton) findViewById(R.id.sign_in_button);
        signIn_btn.setSize(SignInButton.SIZE_STANDARD);
        signIn_btn.setScopes(new Scope[]{Plus.SCOPE_PLUS_LOGIN});

        google_api_client.connect();

        if (google_api_client.isConnected()) {
            google_api_client.disconnect();
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
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
                }else if( edPsw.getText().toString().length() == 0 ) {
                    edPsw.setError(getResources().getString(R.string.pwdrequired));
                }/*else if( imagepath.equals("")) {
                     Toast.makeText(getApplicationContext(),"Profile Picture is required!",Toast.LENGTH_LONG).show();
                }*/else if( country_id.equals("0")) {
                  Toast.makeText(getApplicationContext(),getResources().getString(R.string.countryrequired),Toast.LENGTH_LONG).show();
              }else if( city_id.equals("0")) {
                  Toast.makeText(getApplicationContext(),getResources().getString(R.string.cityrequired),Toast.LENGTH_LONG).show();
              }else {
                    action = "userRagister";
                    mr_device_type = "0";
                    mr_device_token = sharedPreferences.getDeviceToken(getApplicationContext());
                    u_type = sharedPreferences.getUserType(getApplicationContext());
                    u_email = edEmail.getText().toString();
                    u_password = edPsw.getText().toString();
                    u_first_name = edFName.getText().toString();
                    u_last_name = edLName.getText().toString();
                    u_phone = edCNo.getText().toString();
                    if (rbMale.isChecked()){
                        u_gender = "M";
                    }else{
                        u_gender = "F";
                    }
                  //UserRegisterRetrofit();
                  new UserLoginRetrofit(getApplicationContext()).execute();

                }

            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    // share.setVisibility(View.INVISIBLE);
                    // details.setVisibility(View.INVISIBLE);
                    // profile.setProfileId(null);
                }
            }
        });
        loginButton.setReadPermissions(Arrays.asList("email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                // facebook_id=f_name= m_name= l_name= gender= profile_image= full_name= email_id="";

                if (AccessToken.getCurrentAccessToken() != null) {
                    action_social = "sociallogin";
                    sm_social_provider = "facebook";
                    mr_device_type = "0";
                    mr_device_token = sharedPreferences.getDeviceToken(getApplicationContext());
                    u_password = "";
                    u_type = sharedPreferences.getUserType(getApplicationContext());
                    RequestData();

                    Profile profile = Profile.getCurrentProfile();
                    if (profile != null) {
                        Log.e("abc","User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken());
                        facebook_id=profile.getId();
                        Log.e("facebook_id", facebook_id);
                        f_name=profile.getFirstName();
                        Log.e("f_name", f_name);
                        m_name=profile.getMiddleName();
                        Log.e("m_name", m_name);
                        l_name=profile.getLastName();
                        Log.e("l_name", l_name);
                        full_name=profile.getName();
                        Log.e("full_name", full_name);
                        profile_image=profile.getProfilePictureUri(400, 400).toString();
                        Log.e("profile_image", profile_image);
                    }
                    // share.setVisibility(View.VISIBLE);
                    //  details.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {
            }
        });
        signIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gPlusSignIn();

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


        new Country(getApplicationContext()).execute();

    }

    private void buidNewGoogleApiClient() {



        google_api_client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();
    }
    public void RequestData() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                JSONObject json = response.getJSONObject();
                System.out.println("Json data :" + json);
                if (json != null) {


//                    Log.e("email_id", email_id);
                    u_email = object.optString(getString(R.string.fbParamEmail));
                    u_phone = object.optString(getString(R.string.fbParamPhone));
                    Log.e("u_email", u_email);
                    Log.e("u_phone", u_phone);
                    try {
                      //  String text = "<b>Id :</b> " +json.getString("id")+"<br><br><b>Name :</b> "+json.getString("name")+"<br><br><b>Email :</b> "+json.getString("email")+"<br><br><b>Profile link :</b> "+json.getString("link")+"<br><br><b>Profile link :</b> "+json.getString("phone");
                        // details_txt.setText(Html.fromHtml(text));
                        // profile.setProfileId(json.getString("id"));
                        //System.out.println("text  :"+text);
                        u_first_name = object.getString("first_name");
                        Log.e("u_first_name", u_first_name);
                        sm_social_provider_id = json.getString("id");
                        Log.e("sm_social_provider_id", sm_social_provider_id);
                        u_last_name = object.getString("last_name");
                        Log.e("u_last_name", u_last_name);

                        String gender1 = object.getString("gender");
                        if (gender1.equals("male")){
                            u_gender="M";
                        }else {
                            u_gender="F";
                        }
                        try {
                           /* String url = object.getString("link");
                            byte[] str = url.getBytes("UTF-8");
                            u_image = Base64.encodeToString(str, Base64.DEFAULT);
                            Log.e("base64",""+u_image);*/
                            String id=object.getString("id");
                            URL imageURL = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
                            Log.e("imageURL",""+imageURL);
                            url = String.valueOf(imageURL);
                            byte[] str = url.getBytes("UTF-8");
                            //u_img = Base64.encodeToString(str, Base64.DEFAULT);
                          //  Log.e("base64",""+u_img);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        //u_image = object.getString("link");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    action_social = "sociallogin";
                    sm_social_provider = "facebook";
                    mr_device_type = "0";
                    mr_device_token = sharedPreferences.getDeviceToken(getApplicationContext());
                    u_password = "";
                    u_type =sharedPreferences.getUserType(getApplicationContext());

                    sharedPreferences.saveUserImage(getApplicationContext(),url);

                   new SocialLogin(getApplicationContext()).execute();

                    Profile profile = Profile.getCurrentProfile();
                    if (profile != null) {

                        facebook_id=profile.getId();
                        Log.e("facebook_id", facebook_id);
                        m_name=profile.getMiddleName();
                        Log.e("m_name", m_name);
                        l_name=profile.getLastName();
                        Log.e("l_name", l_name);
                        full_name=profile.getName();
                        Log.e("full_name", full_name);
                        profile_image=profile.getProfilePictureUri(400, 400).toString();
                        Log.e("profile_image", profile_image);
                    }
                }

            }


        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email,gender,birthday,location"); // Parámetros que pedimos a facebook
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);

        if (requestCode == SIGN_IN_CODE) {
            request_code = requestCode;
            if (responseCode != RESULT_OK) {
                is_signInBtn_clicked = false;
                progress_dialog.dismiss();
            }
            is_intent_inprogress = false;

            if (!google_api_client.isConnecting()) {
                google_api_client.connect();
            }
        }else if (requestCode == RESULT_LOAD_IMG && responseCode == RESULT_OK
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
            imgDecodableString = cursor.getString(columnIndex);
            cursor.close();
            Log.e("imgDecodableString",""+imgDecodableString);
            String fileName = imgDecodableString.substring(imgDecodableString.lastIndexOf('/') + 1);
            tvProfile.setText(fileName);

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
           // imgView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));

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
    class UserLoginRetrofit extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg,u_gender1, u_id1, u_first_name1, u_last_name1, u_email1, u_password1, u_address1, u_latitute1, u_longitute1, u_city1, u_phone1, u_postcode1, u_country1, u_status1, u_type1, u_is_notification_sound1, u_created1, u_modified1;

        UserLoginRetrofit(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SignUpActivity.this);
            pDialog.setMessage(getResources().getString(R.string.pdialog));
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub

            String uri =  Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", action));
            nameValuePairs.add(new BasicNameValuePair("u_email", u_email));
            nameValuePairs.add(new BasicNameValuePair("u_password", u_password));
            nameValuePairs.add(new BasicNameValuePair("mr_device_type", mr_device_type));
            nameValuePairs.add(new BasicNameValuePair("mr_device_token", mr_device_token));
            nameValuePairs.add(new BasicNameValuePair("u_type", u_type));
            nameValuePairs.add(new BasicNameValuePair("u_first_name", u_first_name));
            nameValuePairs.add(new BasicNameValuePair("u_last_name", u_last_name));
            nameValuePairs.add(new BasicNameValuePair("u_latitute", u_latitute));
            nameValuePairs.add(new BasicNameValuePair("u_longitute", u_longitute));
            nameValuePairs.add(new BasicNameValuePair("u_phone", u_phone));
            nameValuePairs.add(new BasicNameValuePair("u_country", country_id));
            nameValuePairs.add(new BasicNameValuePair("u_city", city_id));
            nameValuePairs.add(new BasicNameValuePair("u_gender", u_gender));
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
                    u_address1 = data.getString("u_address");
                    u_gender1 = data.getString("u_gender");
                    u_latitute1 = data.getString("u_latitute");
                    u_longitute1 = data.getString("u_longitute");
                    u_city1 = data.getString("u_city");
                    u_phone1 = data.getString("u_phone");
                    u_postcode1 = data.getString("u_postcode");
                    u_country1 = data.getString("u_country");
                    u_status1 = data.getString("u_status");
                    u_type1 = data.getString("u_type");
                    u_is_notification_sound1 = data.getString("u_is_notification_sound");
                    u_created1 = data.getString("u_created");
                    u_modified1 = data.getString("u_modified");
                    sharedPreferences.saveNewReg(SignUpActivity.this, true);



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
                Log.e("imagepath","imagepath");
                Log.e("get id", "" + u_id1);
               //getStatus().getUserId());
                if (imagepath == null){

                }else {
                    new Thread(new Runnable() {
                        public void run() {

                            uploadFile(imagepath, u_id1, u_first_name1, u_last_name1, u_latitute1, u_longitute1, mr_device_token);

                        }
                    }).start();
                }
                //Toast.makeText(getApplicationContext(),getResources().getString(R.string.supsucess),Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                sharedPreferences.saveNewReg(SignUpActivity.this, true);
                finish();
            } else {
                //Toast.makeText(getApplicationContext(),getResources().getString(R.string.alreadyregister),Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            }
        }
    }

    void UserRegisterRetrofit() {
        String url1 =  Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(url1).build();
        RestInterface restInterface = adapter.create(RestInterface.class);

        HashMap<String, String> prm = new HashMap<String, String>();


        prm.put("action", action);
        prm.put("u_email", u_email);
        prm.put("u_password", u_password);
        prm.put("mr_device_type", mr_device_type);
        prm.put("mr_device_token", mr_device_token);
        prm.put("u_type", u_type);
        prm.put("u_first_name", u_first_name);
        prm.put("u_last_name", u_last_name);
        prm.put("u_latitute", u_latitute);
        prm.put("u_longitute", u_longitute);
        prm.put("u_phone", u_phone);
        prm.put("u_country", country_id);
        prm.put("u_city", city_id);
        prm.put("u_gender",u_gender);

        Log.e("prm", "" + prm);

        restInterface.getRegisterResponce(prm, new Callback<Register>()

        {
            @Override
            public void success(Register model, Response response) {
                // progressDialog.dismiss();
                Log.e("get status", "" + model.getStatus());
                Log.e("get message", "" + model.getMessage());
                if (model.getStatus().equals("0")) {


                } else {
                    Toast.makeText(getApplicationContext(),"You Already Registered",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {

                String merror = error.getMessage();
                Log.e("errer", "" + merror);
                Toast.makeText(getApplicationContext(), "I’m sorry, something has gone wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        is_signInBtn_clicked = false;
        // Get user's information and set it into the layout
        getProfileInfo();

        // Update the UI after signin
        changeUI(true);
    }

    @Override
    public void onConnectionSuspended(int i) {
        google_api_client.connect();
        changeUI(false);
    }

    private void gPlusSignIn() {
        if (!google_api_client.isConnecting()) {
            Log.d("user connected", "connected");
            is_signInBtn_clicked = true;
            progress_dialog.show();
            resolveSignInError();

        }
    }
/*
     Revoking access from Google+ account
     */

    private void gPlusRevokeAccess() {
        if (google_api_client.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(google_api_client);
            Plus.AccountApi.revokeAccessAndDisconnect(google_api_client)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.d("MainActivity", "User access revoked!");
                            buidNewGoogleApiClient();
                            google_api_client.connect();
                            changeUI(false);
                        }

                    });
        }
    }


    /*
      Method to resolve any signin errors
     */

    private void resolveSignInError() {
        if (connection_result.hasResolution()) {
            try {
                is_intent_inprogress = true;
                connection_result.startResolutionForResult(this, SIGN_IN_CODE);
                Log.d("resolve error", "sign in error resolved");
            } catch (IntentSender.SendIntentException e) {
                is_intent_inprogress = false;
                google_api_client.connect();
            }
        }
    }

    /*
      Sign-out from Google+ account
     */

    public void gPlusSignOut() {
        if (google_api_client.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(google_api_client);
            google_api_client.disconnect();
            google_api_client.connect();
            changeUI(false);
        }
    }



    /*
     get user's information name, email, profile pic,Date of birth,tag line and about me
     */

    private void getProfileInfo() {

        try {

            if (Plus.PeopleApi.getCurrentPerson(google_api_client) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(google_api_client);
                setPersonalInfo(currentPerson);

            } else {
                Toast.makeText(getApplicationContext(),
                        "No Personal info mention", Toast.LENGTH_LONG).show();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     set the User information into the views defined in the layout
     */

    private void setPersonalInfo(Person currentPerson) {
        String email = null;
        String personName = currentPerson.getDisplayName();
        String id = currentPerson.getId();
        Person.Name lastName = currentPerson.getName();
        String last_name = lastName.getFamilyName();
        String lf_name = lastName.getGivenName();
        String personPhotoUrl = currentPerson.getImage().getUrl();
        int gender1 = currentPerson.getGender();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        email = Plus.AccountApi.getAccountName(google_api_client);


       /* TextView   user_name = (TextView) findViewById(R.id.userName);
        user_name.setText("Name: "+personName);
        TextView gemail_id = (TextView)findViewById(R.id.emailId);
        gemail_id.setText("Email Id: " +email);*/
        // setProfilePic(personPhotoUrl);
        Log.e("email>>personName>",email+">>"+personName+">>"+id+">>"+lf_name+">>"+last_name+">"+gender1);
        progress_dialog.dismiss();
        action_social = "sociallogin";
        sm_social_provider = "google";
        sm_social_provider_id = id;
        mr_device_type = "0";
        mr_device_token = sharedPreferences.getDeviceToken(getApplicationContext());
        u_password = "";
        u_email = email;
        u_type =sharedPreferences.getUserType(getApplicationContext());
        u_first_name = lf_name;
        u_last_name =last_name;
        if (gender1 == 0){
            u_gender="M";
        }else {
            u_gender="F";
        }
        try {
            byte[] str = personPhotoUrl.getBytes("UTF-8");
            String base64 = Base64.encodeToString(str, Base64.DEFAULT);
            Log.e("base64",""+base64);
          //  u_img = base64;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        sharedPreferences.saveUserImage(getApplicationContext(),personPhotoUrl);


        new SocialLogin(getApplicationContext()).execute();

        //Toast.makeText(this, "Person information is shown!", Toast.LENGTH_LONG).show();
    }

    /*
     By default the profile pic url gives 50x50 px image.
     If you need a bigger image we have to change the query parameter value from 50 to the size you want
    */

   /* private void setProfilePic(String profile_pic){
        profile_pic = profile_pic.substring(0,
                profile_pic.length() - 2)
                + PROFILE_PIC_SIZE;
        ImageView    user_picture = (ImageView)findViewById(R.id.profile_pic);
        new LoadProfilePic(user_picture).execute(profile_pic);
    }*/

    /*
     Show and hide of the Views according to the user login status
     */

    private void changeUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            //findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {

            //findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            //  findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }




    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {

        if (!result.hasResolution()) {
            google_api_availability.getErrorDialog(this, result.getErrorCode(),request_code).show();
            return;
        }

        if (!is_intent_inprogress) {

            connection_result = result;

            if (is_signInBtn_clicked) {

                resolveSignInError();
            }
        }

    }

   /* @Override
    public void onResult(@NonNull People.LoadPeopleResult peopleData) {
        if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
            PersonBuffer personBuffer = peopleData.getPersonBuffer();
            ArrayList<String> list = new ArrayList<String>();
            ArrayList<String> img_list= new ArrayList<String>();
            try {
                int count = personBuffer.getCount();

                for (int i = 0; i < count; i++) {
                    list.add(personBuffer.get(i).getDisplayName());
                    img_list.add(personBuffer.get(i).getImage().getUrl());
                }
               *//* Intent intent = new Intent(MainActivity.this,FriendActivity.class);
                intent.putStringArrayListExtra("friendsName",list);
                intent.putStringArrayListExtra("friendsPic",img_list);
                startActivity(intent);*//*
            } finally {
                personBuffer.release();
            }
        } else {
            Log.e("circle error", "Error requesting visible circles: " + peopleData.getStatus());
        }
    }*/

    class SocialLogin extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg, u_id, u_first_name1, u_last_name1, u_email1,u_gender1, u_password1, u_address, u_latitute1, u_longitute1, u_city1, u_phone, u_postcode, u_country1, u_status, u_type1, u_is_notification_sound, u_created, u_modified;

        SocialLogin(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SignUpActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub

            String uri =  Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", action_social));
            nameValuePairs.add(new BasicNameValuePair("sm_social_provider", sm_social_provider));
            nameValuePairs.add(new BasicNameValuePair("sm_social_provider_id", sm_social_provider_id));
            nameValuePairs.add(new BasicNameValuePair("u_email", u_email));
            nameValuePairs.add(new BasicNameValuePair("u_password", u_password));
            nameValuePairs.add(new BasicNameValuePair("mr_device_type", mr_device_type));
            nameValuePairs.add(new BasicNameValuePair("mr_device_token", mr_device_token));
            nameValuePairs.add(new BasicNameValuePair("u_type", u_type));
            nameValuePairs.add(new BasicNameValuePair("u_first_name", u_first_name));
            nameValuePairs.add(new BasicNameValuePair("u_last_name", u_last_name));
            nameValuePairs.add(new BasicNameValuePair("u_latitute", u_latitute));
            nameValuePairs.add(new BasicNameValuePair("u_longitute", u_longitute));
            nameValuePairs.add(new BasicNameValuePair("u_gender",u_gender));
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
                    u_id = data.getString("u_id");
                    u_first_name1 = data.getString("u_first_name");
                    u_last_name1 = data.getString("u_last_name");
                    u_email1 = data.getString("u_email");
                    u_password1 = data.getString("u_password");
                    u_address = data.getString("u_address");
                    u_latitute1 = data.getString("u_latitute");
                    u_longitute1 = data.getString("u_longitute");
                    u_city1 = data.getString("u_city");
                    u_gender1 = data.getString("u_gender");
                    u_phone = data.getString("u_phone");
                    u_postcode = data.getString("u_postcode");
                    u_country1 = data.getString("u_country");
                    u_status = data.getString("u_status");
                    u_type1 = data.getString("u_type");
                    u_is_notification_sound = data.getString("u_is_notification_sound");
                    u_created = data.getString("u_created");
                    u_modified = data.getString("u_modified");
                    sharedPreferences.saveNewReg(SignUpActivity.this, true);

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
                sharedPreferences.saveUserFName(getApplicationContext(), u_first_name1);
                sharedPreferences.saveUserLName(getApplicationContext(), u_last_name1);
                sharedPreferences.saveUserPhone(getApplicationContext(), u_phone);
                sharedPreferences.saveUserEmail(getApplicationContext(), u_email1);
                sharedPreferences.saveUserID(getApplicationContext(), u_id);
                sharedPreferences.saveUserCountry(getApplicationContext(), u_country1);
                sharedPreferences.saveUserCity(getApplicationContext(), u_city1);
                sharedPreferences.saveUserGender(getApplicationContext(), u_gender1);
                sharedPreferences.saveUserType(getApplicationContext(),u_type1);
                sharedPreferences.saveu_is_notification_sound(getApplicationContext(),u_is_notification_sound);
                sharedPreferences.saveUserLatitute(getApplicationContext(),u_latitute1);
                sharedPreferences.saveUserLongitute(getApplicationContext(),u_longitute1);//getStatus().getUserId());
                Intent i = new Intent(getApplicationContext(), SocialSignUpActivity.class);
                startActivity(i);
                finish();
                gPlusSignOut();

            } else {
               // if (msg.equals("Email already register")) {
                    sharedPreferences.saveUserFName(getApplicationContext(), u_first_name1);
                    sharedPreferences.saveUserLName(getApplicationContext(), u_last_name1);
                    sharedPreferences.saveUserPhone(getApplicationContext(), u_phone);
                    sharedPreferences.saveUserEmail(getApplicationContext(), u_email1);
                    sharedPreferences.saveUserID(getApplicationContext(), u_id);
                    sharedPreferences.saveUserCountry(getApplicationContext(), u_country1);
                    sharedPreferences.saveUserCity(getApplicationContext(), u_city1);
                    sharedPreferences.saveUserGender(getApplicationContext(), u_gender1);
                    sharedPreferences.saveUserType(getApplicationContext(),u_type1);
                    sharedPreferences.saveu_is_notification_sound(getApplicationContext(),u_is_notification_sound);
                sharedPreferences.saveUserLatitute(getApplicationContext(),u_latitute1);
                sharedPreferences.saveUserLongitute(getApplicationContext(),u_longitute1);//getStatus().getUserId());

                    Intent i = new Intent(getApplicationContext(), SocialSignUpActivity.class);
                    startActivity(i);
                    finish();
                    gPlusSignOut();
              /*  } else {
                    //  Toast.makeText(getApplicationContext(), "Please Sign Up Today", Toast.LENGTH_SHORT).show();
                }*/
            }
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

            pDialog = new ProgressDialog(SignUpActivity.this);
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

            pDialog = new ProgressDialog(SignUpActivity.this);
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
    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);

        if (google_api_client.isConnected()) {
            google_api_client.connect();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
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
                Log.e("Content-Type",boundary);
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
}
