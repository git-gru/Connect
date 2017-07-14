package com.jarrebnnee.connect;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jarrebnnee.connect.Helper.Registration.Data;
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
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import com.facebook.login.widget.LoginButton;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    Toolbar toolbar;
    EditText input_email, input_psw;
    //TextInputLayout input_layout_email,input_layout_psw;
    Button btn_signin;
    LoginButton loginButton;
    private static final int RC_SIGN_IN = 007;
    TextView tvSignup, tvTitle, tvForgot, tvDont, tvoruse, tvShow;
    ImageView ivBack;
    boolean i = false;
    String url = "http://cp3767.veba.co/~shubantech/ebay_clone_api/";
    String action, mr_device_type, mr_device_token, u_email, u_password,lang;
    GetSet getSet;
    SaveSharedPrefrence sharedPreferences;
    TrackGPS gps;
    ProgressDialog progress_dialog;
    CallbackManager callbackManager;
    String action_social, sm_social_provider, sm_social_provider_id, u_type, u_first_name, u_gender,u_last_name, u_latitute, u_longitute;

    GoogleApiClient google_api_client;
    GoogleApiAvailability google_api_availability;
    SignInButton signIn_btn;
    private static final int PERMISSION_REQUEST_CODE=10;
    private static final int SIGN_IN_CODE = 0;
    private static final int PROFILE_PIC_SIZE = 120;
    private ConnectionResult connection_result;
    private boolean is_intent_inprogress;
    private boolean is_signInBtn_clicked;

    private int request_code;

    String[] permission = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        buidNewGoogleApiClient();
        setContentView(R.layout.activity_sign_in);
        callbackManager = CallbackManager.Factory.create();
        getSet = GetSet.getInstance();
        sharedPreferences = new SaveSharedPrefrence();
        lang = sharedPreferences.getlang(getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "" + "</font>")));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        input_email = (EditText) findViewById(R.id.input_email);
        input_psw = (EditText) findViewById(R.id.input_psw);
        tvSignup = (TextView) findViewById(R.id.tvSignup);
        btn_signin = (Button) findViewById(R.id.btn_signin);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvForgot = (TextView) findViewById(R.id.tvForgot);
        tvDont = (TextView) findViewById(R.id.tvDont);
        tvoruse = (TextView) findViewById(R.id.tvoruse);
        tvShow = (TextView) findViewById(R.id.tvShow);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/16023_futuran_0.ttf");
        input_email.setTypeface(custom_font);
        input_psw.setTypeface(custom_font);
        tvSignup.setTypeface(custom_font);
        btn_signin.setTypeface(custom_font);
        tvTitle.setTypeface(custom_font);
        tvForgot.setTypeface(custom_font);
        tvDont.setTypeface(custom_font);
        tvoruse.setTypeface(custom_font);
        tvShow.setTypeface(custom_font);

      /*  input_layout_email = (TextInputLayout)findViewById(R.id.input_layout_email);
        input_layout_psw = (TextInputLayout)findViewById(R.id.input_layout_psw);*/
        progress_dialog = new ProgressDialog(this);
        progress_dialog.setMessage(getResources().getString(R.string.showdialog));

        u_type = sharedPreferences.getUserType(getApplicationContext());
        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ForgetPwdActivity.class);
                startActivity(i);
            }
        });
        tvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == false) {
                    input_psw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    tvShow.setText(getResources().getString(R.string.hide));
                    input_psw.setSelection(input_psw.length());
                    i = true;
                } else {
                    input_psw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    tvShow.setText(getResources().getString(R.string.show));
                    input_psw.setSelection(input_psw.length());
                    i = false;
                }

            }
        });
        gps = new TrackGPS(SignInActivity.this);
        if (gps.canGetLocation()) {

            u_longitute = String.valueOf(gps.getLongitude());
            u_latitute = String.valueOf(gps.getLatitude());

         //   Toast.makeText(getApplicationContext(), "Longitude:" + u_longitute + "\nLatitude:" + u_latitute, Toast.LENGTH_SHORT).show();
            Log.e("latlong", "Longitude:" + u_longitute + "\nLatitude:" + u_latitute);
        } else {
            gps.showSettingsAlert();
        }
        signIn_btn = (SignInButton) findViewById(R.id.sign_in_button);
        signIn_btn.setSize(SignInButton.SIZE_STANDARD);


        google_api_client.connect();

        if (google_api_client.isConnected()) {
            google_api_client.disconnect();
        }


        /*input_email.addTextChangedListener(new MyTextWatcher(input_email));
        input_psw.addTextChangedListener(new MyTextWatcher(input_psw));*/
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //submitForm();
                if (input_email.getText().toString().length() == 0) {
                    input_email.setError(getResources().getString(R.string.emailrequired));
                } else if (input_psw.getText().toString().length() == 0) {
                    input_psw.setError(getResources().getString(R.string.pwdrequired));
                } else {
                    u_email = input_email.getText().toString();
                    u_password = input_psw.getText().toString();
                    action = "login";
                    mr_device_type = "0";
                    mr_device_token = sharedPreferences.getDeviceToken(getApplicationContext());
                    //UserLoginRetrofit();
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//we are connected to a network
                        //  url=URLCollection.LoginUrl+"&u_email="+email+"&u_password="+password+"&device_type=0&device_token="+regId;
                        //   SignInUrlCall(url);
                        new UserLoginRetrofit(getApplicationContext()).execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
                    }


                }
            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (isAllowed()) {
                        Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                        startActivity(i);
                    } else {
                        requestPermission();
                    }
                } else {
                    Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                    startActivity(i);
                }
              /*  Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(i);*/
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
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
/*
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
                    }*/
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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
    }

    private void buidNewGoogleApiClient() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();


        google_api_client = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();
    }
   /* void SocialLogin() {
        String url1 = "http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(url1).build();
        RestInterface restInterface = adapter.create(RestInterface.class);

        HashMap<String, String> prm = new HashMap<String, String>();


        prm.put("action", action_social);
        prm.put("sm_social_provider",sm_social_provider);
        prm.put("sm_social_provider_id",sm_social_provider_id);
        prm.put("u_email", u_email);
        prm.put("u_password", u_password);
        prm.put("mr_device_type", mr_device_type);
        prm.put("mr_device_token", mr_device_token);
        prm.put("u_type", u_type);
        prm.put("u_first_name", u_first_name);
        prm.put("u_last_name", u_last_name);
        prm.put("u_latitute", u_latitute);
        prm.put("u_longitute", u_longitute);

        Log.e("prm", "" + prm);

        restInterface.getRegisterResponce(prm, new Callback<Register>()

        {
            @Override
            public void success(Register model, Response response) {
                // progressDialog.dismiss();
                Log.e("get status", "" + model.getStatus());
                Log.e("get message", "" + model.getMessage());
                if (model.getStatus().equals("0")) {
                    Toast.makeText(getApplicationContext(),"Facebook Sign In Sucessfully",Toast.LENGTH_LONG).show();
                   // finish();
                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(),"You Already Registered Sucessfully",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {

                String merror = error.getMessage();
                Log.e("errer", "" + merror);
                Toast.makeText(getApplicationContext(), "I’m sorry, something has gone wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }*/

    public void RequestData() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                JSONObject json = response.getJSONObject();
                System.out.println("Json data :" + json);
                if (json != null) {
                    //String text = "<b>Id :</b> " +json.getString("id")+"<br><br><b>Name :</b> "+json.getString("name")+"<br><br><b>Email :</b> "+json.getString("email")+"<br><br><b>Profile link :</b> "+json.getString("link");
                    // details_txt.setText(Html.fromHtml(text));
                    // profile.setProfileId(json.getString("id"));
                    // System.out.println("text  :"+text);

//                    Log.e("email_id", email_id);
                    u_email = object.optString(getString(R.string.fbParamEmail));
                  //  sharedPreferences.saveUserEmail(getApplicationContext(),u_email);
                    Log.e("u_email", u_email);

                    try {
                        u_first_name = object.getString("first_name");
                        Log.e("u_first_name", u_first_name);
                    //    sharedPreferences.saveUserFName(getApplicationContext(),u_first_name);

                        sm_social_provider_id = json.getString("id");
                        Log.e("sm_social_provider_id", sm_social_provider_id);
                     //   sharedPreferences.saveSM_SOCIAL_PROVIDER_ID(getApplicationContext(),sm_social_provider_id);

                        u_last_name = object.getString("last_name");
                        Log.e("u_last_name", u_last_name);
                     //   sharedPreferences.saveUserLName(getApplicationContext(),u_last_name);

                        String gender1 = object.getString("gender");
                        if (gender1.equals("male")){
                            u_gender="M";
                        }else {
                            u_gender="F";
                        }
                     //   sharedPreferences.saveUserGender(getApplicationContext(),u_gender);

                        try {
                           /* String url = object.getString("link");
                            byte[] str = url.getBytes("UTF-8");
                            u_image = Base64.encodeToString(str, Base64.DEFAULT);
                            Log.e("base64",""+u_image);*/
                            String id=object.getString("id");
                            URL imageURL = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
                            Log.e("imageURL",""+imageURL);
                            String url = String.valueOf(imageURL);
                            byte[] str = url.getBytes("UTF-8");
                            //u_image = Base64.encodeToString(str, Base64.DEFAULT);
                            sharedPreferences.saveUserImage(getApplicationContext(),url);
                            Log.e("url",""+url);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    action_social = "sociallogin";
                    sm_social_provider = "facebook";
                    mr_device_type = "0";
                    mr_device_token = sharedPreferences.getDeviceToken(getApplicationContext());
                    u_password = "";

                  /*  sharedPreferences.saveSocialAction(getApplicationContext(),sm_social_provider);
                    Intent i = new Intent(getApplicationContext(),SocialSignUpActivity.class);
                    startActivity(i);
                    finish();*/
                    new SocialLogin(getApplicationContext()).execute();

                   /* Profile profile = Profile.getCurrentProfile();
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
                    }*/
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

    /*    if (requestCode == SIGN_IN_CODE) {
            request_code = requestCode;
            if (responseCode != RESULT_OK) {
                is_signInBtn_clicked = false;
                progress_dialog.dismiss();
            }
            is_intent_inprogress = false;

            if (!google_api_client.isConnecting()) {
                google_api_client.connect();
            }
        }*/

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }

    public void handleSignInResult(GoogleSignInResult result) {

            GoogleSignInAccount acct = result.getSignInAccount();
            String dname = acct.getDisplayName();
            Uri p= acct.getPhotoUrl();String photo = "";
        if (p == null) {

        } else {
            photo=p.toString();
        }
        String email = acct.getEmail();
            String fname = acct.getAccount().name;
            String family = acct.getFamilyName();
            String given = acct.getGivenName();
            Log.e("google", "dname: "+dname+"\n"+"photo: "+photo+"\n"+"email: "+email+"\n"+"fname: "+fname+"\n"+"family: "+family+"\n"+"given: "+given);

        String personName = dname;
        String id = acct.getId();
      //  String lastName = currentPerson.getName();
        String last_name = family;
        String lf_name = acct.getGivenName();
        String personPhotoUrl = photo;
        //int gender1 = person.getGender();

       /* if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }*/



       /* TextView   user_name = (TextView) findViewById(R.id.userName);
        user_name.setText("Name: "+personName);
        TextView gemail_id = (TextView)findViewById(R.id.emailId);
        gemail_id.setText("Email Id: " +email);*/
        // setProfilePic(personPhotoUrl);
        Log.e("email>>personName>",email+">>"+personName+">>"+id+">>"+lf_name+">>"+last_name);
        progress_dialog.dismiss();
        action_social = "sociallogin";
        sm_social_provider = "google";
        sm_social_provider_id = id;
        mr_device_type = "0";
        mr_device_token = sharedPreferences.getDeviceToken(getApplicationContext());
        u_password = "";
        u_email = email;
        u_type = sharedPreferences.getUserType(getApplicationContext());
        u_first_name = lf_name;
        u_last_name =last_name;

        try {
            byte[] str = personPhotoUrl.getBytes("UTF-8");
            String base64 = Base64.encodeToString(str, Base64.DEFAULT);
            Log.e("base64",""+base64);
            //u_image = base64;
        } catch (UnsupportedEncodingException e) {
            Log.e("exception", ""+e);
        }

        sharedPreferences.saveUserImage(getApplicationContext(),personPhotoUrl);

        new SocialLogin(getApplicationContext()).execute();

    }

    void UserLoginRetrofit() {
        String url1 =  Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(url1).build();
        RestInterface restInterface = adapter.create(RestInterface.class);

        HashMap<String, String> prm = new HashMap<String, String>();


        prm.put("action", action);
        prm.put("u_email", u_email);
        prm.put("u_password", u_password);
        prm.put("mr_device_type", mr_device_type);
        prm.put("mr_device_token", mr_device_token);

        Log.e("prm", "" + prm);

        restInterface.getRegisterResponce(prm, new Callback<Register>()

        {
            @Override
            public void success(Register model, Response response) {
                // progressDialog.dismiss();


                Log.e("get status", "" + model.getStatus());
                Log.e("get message", "" + model.getMessage());
                if (model.getMessage().equals("user data")) {
                    Log.e("get id", "" + model.getData().getUId());
                    Data data = model.getData();

                    String id = data.getUId();
                    Log.e("get idid", "" + id);
                    sharedPreferences.saveUserID(getApplicationContext(), model.getData().getUId());//getStatus().getUserId());
               //     Toast.makeText(getApplicationContext(), "Login In Sucessfully" + id, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(i);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Please Sign Up Today", Toast.LENGTH_SHORT).show();
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
   //     getProfileInfo();

        // Update the UI after signin
        changeUI(true);


    }

    @Override
    public void onConnectionSuspended(int i) {
        google_api_client.connect();
        changeUI(false);

    }


    private void gPlusSignIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(google_api_client);
        startActivityForResult(signInIntent, RC_SIGN_IN);

     /*   if (!google_api_client.isConnecting()) {
            Log.d("user connected", "connected");
            is_signInBtn_clicked = true;
            progress_dialog.show();
            resolveSignInError();

        }*/
    }


    /*
     Revoking access from Google+ account
     */

    private void gPlusRevokeAccess() {
        if (google_api_client.isConnected()) {
        /*    Plus.AccountApi.clearDefaultAccount(google_api_client);
            Plus.AccountApi.revokeAccessAndDisconnect(google_api_client)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.d("MainActivity", "User access revoked!");
                            buidNewGoogleApiClient();
                            google_api_client.connect();
                            changeUI(false);
                        }

                    });*/

            Auth.GoogleSignInApi.revokeAccess(google_api_client).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            changeUI(false);
                        }
                    });
        }
    }


    /*
      Method to resolve any signin errors
     */


    /*
      Sign-out from Google+ account
     */

    private void gPlusSignOut() {
        if (google_api_client.isConnected()) {
      /*      Plus.AccountApi.clearDefaultAccount(google_api_client);
            google_api_client.disconnect();
            google_api_client.connect();
            changeUI(false);*/
            Auth.GoogleSignInApi.signOut(google_api_client).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            changeUI(false);
                        }
                    });
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
        Log.e("email>>personName>",email+">>"+personName+">>"+id+">>"+lf_name+">>"+last_name);
        progress_dialog.dismiss();
        action_social = "sociallogin";
        sm_social_provider = "google";
        sm_social_provider_id = id;
        mr_device_type = "0";
        mr_device_token = sharedPreferences.getDeviceToken(getApplicationContext());
        u_password = "";
        u_email = email;
        u_type = sharedPreferences.getUserType(getApplicationContext());
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
            //u_image = base64;
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

             //   resolveSignInError();
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

    class UserLoginRetrofit extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg,u_gender, u_id, u_first_name, u_last_name, u_email1, u_password1, u_address, u_latitute, u_longitute, u_city, u_phone, u_postcode, u_country, u_status, u_type, u_is_notification_sound, u_created, u_modified,u_image;

        UserLoginRetrofit(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SignInActivity.this);
            pDialog.setMessage(getResources().getString(R.string.pdialog));
            pDialog.setCancelable(false);
            pDialog.show();

            u_type = sharedPreferences.getUserType(SignInActivity.this);

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
   //         nameValuePairs.add(new BasicNameValuePair("u_id", u_id));
            nameValuePairs.add(new BasicNameValuePair("u_type", u_type));
            nameValuePairs.add(new BasicNameValuePair("lang",lang));

            ServiceHandler handler = new ServiceHandler();
            String jsonSt = handler.makeServiceCall(
                    uri, ServiceHandler.POST, nameValuePairs);

            Log.d("Response: ", "> " + jsonSt);

            if (jsonSt != null) {

                try {
                    pDialog.dismiss();
                    JSONObject jsonObj = new JSONObject(jsonSt);
                    status = jsonObj.getString("status");
                    msg = jsonObj.getString("message");
                    Log.e("msg", msg);
                    //Toast.makeText(SignInActivity.this, msg, Toast.LENGTH_LONG).show();
                    if (status.equals("0")) {
                        JSONObject data = jsonObj.getJSONObject("data");
                        u_id = data.getString("u_id");
                        u_first_name = data.getString("u_first_name");
                        u_last_name = data.getString("u_last_name");
                        u_email1 = data.getString("u_email");
                        u_password1 = data.getString("u_password");
                        u_address = data.getString("u_address");
                        u_gender = data.getString("u_gender");
                        u_latitute = data.getString("u_latitute");
                        u_longitute = data.getString("u_longitute");
                        u_city = data.getString("u_city");
                        u_phone = data.getString("u_phone");
                        u_postcode = data.getString("u_postcode");
                        u_country = data.getString("u_country");
                        u_status = data.getString("u_status");
                        u_type = data.getString("u_type");
                        u_is_notification_sound = data.getString("u_is_notification_sound");
                        u_created = data.getString("u_created");
                        u_modified = data.getString("u_modified");
                        u_image = data.getString("u_img");

                    } else if (status.equals("1")) {

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
                Log.e("get id", "" + u_id);
                sharedPreferences.saveUserID(getApplicationContext(), u_id);
                sharedPreferences.saveUserFName(getApplicationContext(), u_first_name);
                sharedPreferences.saveUserLName(getApplicationContext(), u_last_name);
                sharedPreferences.saveUserPhone(getApplicationContext(), u_phone);
                sharedPreferences.saveUserEmail(getApplicationContext(), u_email1);
                sharedPreferences.saveUserCountry(getApplicationContext(), u_country);
                sharedPreferences.saveUserCity(getApplicationContext(), u_city);
                sharedPreferences.saveUserGender(getApplicationContext(), u_gender);
                sharedPreferences.saveUserImage(getApplicationContext(),u_image);
                sharedPreferences.saveUserType(getApplicationContext(),u_type);
                sharedPreferences.saveu_is_notification_sound(getApplicationContext(),u_is_notification_sound);
                sharedPreferences.saveUserLatitute(getApplicationContext(),u_latitute);
                sharedPreferences.saveUserLongitute(getApplicationContext(),u_longitute);//getStatus().getUserId());

                boolean check = sharedPreferences.getNewReg(SignInActivity.this);

                if (check) {
                    if (u_type.equals("2")) {
                        Intent i = new Intent(getApplicationContext(), SellerSubscribeActivity.class);
                        startActivity(i);
                        finish();
                        sharedPreferences.saveNewReg(SignInActivity.this, false);
                    } else {
                   //     Toast.makeText(getApplicationContext(),getResources().getString(R.string.lsucess), Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(i);
                        finish();
                    }

                } else {
                 //   Toast.makeText(getApplicationContext(),getResources().getString(R.string.lsucess), Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(i);
                    finish();
                }



            } else {
              //  if (msg.equals("Email already register")) {
                    //Toast.makeText(getApplicationContext(),getResources().getString(R.string.epswdmatch), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
                /*} else {
                    Toast.makeText(getApplicationContext(), "Please Sign Up Today", Toast.LENGTH_SHORT).show();
                }*/
            }
        }
    }
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

            pDialog = new ProgressDialog(SignInActivity.this);
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
                sharedPreferences.saveUserLongitute(getApplicationContext(),u_longitute1);
                //getStatus().getUserId());
                //Toast.makeText(getApplicationContext(), "Login In Sucessfully", Toast.LENGTH_LONG).show();
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
                   // Toast.makeText(getApplicationContext(), "Login In Sucessfully", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), SocialSignUpActivity.class);
                    startActivity(i);
                    finish();
                    gPlusSignOut();
               /* } else {
                  //  Toast.makeText(getApplicationContext(), "Please Sign Up Today", Toast.LENGTH_SHORT).show();
                }*/
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


    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(SignInActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(SignInActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(SignInActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }

    }

    private boolean isAllowed() {
        int result = ContextCompat.checkSelfPermission(SignInActivity.this,Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //     Log.e("value", "Permission Granted, Now you can save image .");
                    Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                    startActivity(i);
                } else {
                    //      Log.e("value", "Permission Denied, You cannot save image.");
                    Toast.makeText(SignInActivity.this, "Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
