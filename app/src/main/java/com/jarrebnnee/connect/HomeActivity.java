package com.jarrebnnee.connect;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jarrebnnee.connect.Service.GetSet;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.ServiceHandler;
import com.jarrebnnee.connect.Service.TrackGPS;
import com.jarrebnnee.connect.fragement.BookNow_Fragement;
import com.jarrebnnee.connect.fragement.FragmentDrawer;
import com.jarrebnnee.connect.fragement.Home_Fragement;
import com.jarrebnnee.connect.fragement.MyAccountFragement;
import com.jarrebnnee.connect.fragement.Projects_Fragement;
import com.jarrebnnee.connect.fragement.Search_Fragement;
import com.jarrebnnee.connect.fragement.Search_Job_Fragement;
import com.jarrebnnee.connect.fragement.Search_Seller_Fragement;
import com.jarrebnnee.connect.fragement.SetServiceForSellerFragement;
import com.facebook.login.LoginManager;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    Toolbar toolbar;
    private FragmentDrawer drawerFragment;
    public static ImageView ivBack, ivTitle, ivBookNow, ivNotify,ivSearch;
    EditText etSearch;
    LinearLayout lySearch;
    GetSet getSet;
    Fragment fragment;
    TrackGPS gps;
    SaveSharedPrefrence saveSharedPrefrence;
    boolean isSellerHasServices = false;
    public static TextView tvHome, tvSearch, tvProject, tvBookNow, tvMyAccount, tvTitle;
    public static LinearLayout layout_home, layout_project, layout_search, layout_book, layout_account;
    String action, mr_device_token, u_id, u_address, u_first_name, u_last_name, u_city, u_phone, u_postcode, u_country, u_is_notification_sound, u_latitute, u_longitute, u_gender, u_type, deviceID;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    TextView tvNotify;
    String lang;
    int s=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSet = GetSet.getInstance();
        saveSharedPrefrence = new SaveSharedPrefrence();
        action = "user_profile";
        lang = saveSharedPrefrence.getlang(getApplicationContext());
        mr_device_token = saveSharedPrefrence.getDeviceToken(getApplicationContext());
        u_id = saveSharedPrefrence.getUserID(getApplicationContext());
        u_first_name = saveSharedPrefrence.getUserFName(getApplicationContext());
        u_last_name = saveSharedPrefrence.getUserLName(getApplicationContext());
        u_gender = saveSharedPrefrence.getUserGender(getApplicationContext());
        u_type = saveSharedPrefrence.getUserType(getApplicationContext());
        deviceID = saveSharedPrefrence.getDeviceToken(getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivTitle = (ImageView) findViewById(R.id.ivTitle);
        ivSearch = (ImageView) findViewById(R.id.ivSearch);
        ivBookNow = (ImageView) findViewById(R.id.ivBookNow);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvNotify = (TextView) findViewById(R.id.tvNotify);
        ivNotify = (ImageView) findViewById(R.id.ivNotify);
        etSearch = (EditText) findViewById(R.id.etSearch);
        lySearch = (LinearLayout) findViewById(R.id.lySearch);
        ivBack.setVisibility(View.GONE);
        ivNotify.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "" + "</font>")));

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(new FragmentDrawer.FragmentDrawerListener() {
            @Override
            public void onDrawerItemSelected(View view, int position) {
                displayView(position);
            }
        });
        Log.e("device", deviceID);

        String loc = Locale.getDefault().getISO3Language();
        Log.e("444444", loc);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
      /*  if (u_type.equals("2")) {
            ivBack.setVisibility(View.VISIBLE);
            ivNotify.setVisibility(View.GONE);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "" + "</font>")));

        } else {
            ivBack.setVisibility(View.GONE);
            ivNotify.setVisibility(View.VISIBLE);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "" + "</font>")));

            drawerFragment = (FragmentDrawer)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
            drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
            drawerFragment.setDrawerListener(new FragmentDrawer.FragmentDrawerListener() {
                @Override
                public void onDrawerItemSelected(View view, int position) {
                    displayView(position);
                }
            });
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        }*/



        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s==0) {
                    ivTitle.setVisibility(View.GONE);
                    ivNotify.setVisibility(View.GONE);
                    tvNotify.setVisibility(View.GONE);
                    tvTitle.setVisibility(View.GONE);
                    lySearch.setVisibility(View.VISIBLE);
                    s=1;
                }else{
                    ivTitle.setVisibility(View.VISIBLE);
                    ivNotify.setVisibility(View.VISIBLE);
                    tvNotify.setVisibility(View.VISIBLE);
                    tvTitle.setVisibility(View.GONE);
                    lySearch.setVisibility(View.GONE);
                    s=0;
                }
            }
        });

        getUserServices();

        tvHome = (TextView) findViewById(R.id.tvHome);
        tvSearch = (TextView) findViewById(R.id.tvSearch);
        tvProject = (TextView) findViewById(R.id.tvProject);
        tvBookNow = (TextView) findViewById(R.id.tvBookNow);
        tvMyAccount = (TextView) findViewById(R.id.tvMyAccount);

        layout_home = (LinearLayout) findViewById(R.id.layout_home);
        layout_search = (LinearLayout) findViewById(R.id.layout_search);
        layout_project = (LinearLayout) findViewById(R.id.layout_project);
        layout_book = (LinearLayout) findViewById(R.id.layout_book);
        layout_account = (LinearLayout) findViewById(R.id.layout_account);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/16023_futuran_0.ttf");
        tvHome.setTypeface(custom_font);
        tvSearch.setTypeface(custom_font);
        tvProject.setTypeface(custom_font);
        tvBookNow.setTypeface(custom_font);
        tvMyAccount.setTypeface(custom_font);
        tvTitle.setTypeface(custom_font);
        tvNotify.setTypeface(custom_font);
        etSearch.setTypeface(custom_font);
        if (u_type.equals("2")) {
            ivBookNow.setImageResource(R.drawable.set_service);
            tvBookNow.setText(getResources().getString(R.string.setservice));
        } else {
            ivBookNow.setImageResource(R.drawable.book_now);
            tvBookNow.setText(getResources().getString(R.string.booknow));
        }
        gps = new TrackGPS(HomeActivity.this);
        if (gps.canGetLocation()) {

            u_longitute = String.valueOf(gps.getLongitude());
            u_latitute = String.valueOf(gps.getLatitude());

        //    Toast.makeText(getApplicationContext(), "Longitude:" + u_longitute + "\nLatitude:" + u_latitute, Toast.LENGTH_SHORT).show();
         //   Log.e("latlong", "Longitude:" + u_longitute + "\nLatitude:" + u_latitute);

            UpdateMyLocation(u_id, u_latitute, u_longitute);
        } else {
            gps.showSettingsAlert();
        }


        displayView(0);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//we are connected to a network
            //  url=URLCollection.LoginUrl+"&u_email="+email+"&u_password="+password+"&device_type=0&device_token="+regId;
            //   SignInUrlCall(url);

            new getAllNotification(getApplicationContext()).execute();

        } else {
            Toast.makeText(getApplicationContext(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
        }

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>=3) {
                    getSet.setSearch("");
                    String str = etSearch.getText().toString();
                    getSet.setSearch(str);
                    InputMethodManager inputManager =
                            (InputMethodManager) getApplicationContext().
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    if (u_type.equals("2")){
                        Search_Job_Fragement fragemebt_search = new Search_Job_Fragement();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragemebt_search);
                        fragmentTransaction.commit();
                    }else {
                        Search_Seller_Fragement fragemebt_search = new Search_Seller_Fragement();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragemebt_search);
                        fragmentTransaction.commit();
                    }

                    layout_home.setBackgroundResource(R.color.black);
                    layout_project.setBackgroundResource(R.color.black);
                    layout_search.setBackgroundResource(R.color.black);
                    layout_book.setBackgroundResource(R.color.black);
                    layout_account.setBackgroundResource(R.color.black);

              //      Toast.makeText(getApplicationContext(), "afterTextChanged", Toast.LENGTH_LONG).show();
               }
            }
        });

        layout_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Home_Fragement fragemebt_home = new Home_Fragement();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragemebt_home);
                fragmentTransaction.commit();

                layout_home.setBackgroundResource(R.color.colorPrimary);
                layout_project.setBackgroundResource(R.color.black);
                layout_search.setBackgroundResource(R.color.black);
                layout_book.setBackgroundResource(R.color.black);
                layout_account.setBackgroundResource(R.color.black);
                ivTitle.setVisibility(View.VISIBLE);
                ivNotify.setVisibility(View.VISIBLE);
                tvNotify.setVisibility(View.VISIBLE);
                lySearch.setVisibility(View.GONE);
                etSearch.getText().clear();
            }
        });
        layout_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Projects_Fragement fragemebt_project = new Projects_Fragement();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragemebt_project);
                // fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                ivTitle.setVisibility(View.GONE);
                tvTitle.setVisibility(View.VISIBLE);
                lySearch.setVisibility(View.GONE);
                tvTitle.setText(getResources().getString(R.string.my_project));
                layout_home.setBackgroundResource(R.color.black);
                layout_project.setBackgroundResource(R.color.colorPrimary);
                layout_search.setBackgroundResource(R.color.black);
                layout_book.setBackgroundResource(R.color.black);
                layout_account.setBackgroundResource(R.color.black);
                etSearch.getText().clear();
            }
        });
        layout_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Search_Fragement fragemebt_search = new Search_Fragement();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragemebt_search);
                //  fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                ivTitle.setVisibility(View.GONE);
                lySearch.setVisibility(View.GONE);
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(getResources().getString(R.string.ylocation));
                layout_home.setBackgroundResource(R.color.black);
                layout_project.setBackgroundResource(R.color.black);
                layout_search.setBackgroundResource(R.color.colorPrimary);
                layout_book.setBackgroundResource(R.color.black);
                layout_account.setBackgroundResource(R.color.black);
                etSearch.getText().clear();


            }
        });
        layout_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (u_type.equals("2")) {
                    ivBookNow.setImageResource(R.drawable.set_service);
                    tvTitle.setText(getResources().getString(R.string.setservice));
                    if (isSellerHasServices) {
                        Intent intent = new Intent(HomeActivity.this, SellerServiceSetActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(HomeActivity.this, SellerSetServicesActivity.class);
                        startActivity(intent);
                        /*SetServiceForSellerFragement fragemebt_seller = new SetServiceForSellerFragement();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragemebt_seller);
                        // fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        ivTitle.setVisibility(View.GONE);
                        lySearch.setVisibility(View.GONE);
                        tvTitle.setVisibility(View.VISIBLE);
                        etSearch.getText().clear();*/
                    }

                } else {
                    ivBookNow.setImageResource(R.drawable.book_now);
                    tvTitle.setText(getResources().getString(R.string.booknow));
                    BookNow_Fragement fragemebt_book = new BookNow_Fragement();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragemebt_book);
                    // fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    ivTitle.setVisibility(View.GONE);
                    tvTitle.setVisibility(View.VISIBLE);
                    etSearch.getText().clear();
                }
                layout_home.setBackgroundResource(R.color.black);
                layout_project.setBackgroundResource(R.color.black);
                layout_search.setBackgroundResource(R.color.black);
                layout_book.setBackgroundResource(R.color.colorPrimary);
                layout_account.setBackgroundResource(R.color.black);
            }
        });
        layout_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAccountFragement fragemebt_account = new MyAccountFragement();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragemebt_account);
                // fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                ivTitle.setVisibility(View.GONE);
                tvTitle.setVisibility(View.VISIBLE);
                lySearch.setVisibility(View.GONE);
                tvTitle.setText(getResources().getString(R.string.account));
                layout_home.setBackgroundResource(R.color.black);
                layout_project.setBackgroundResource(R.color.black);
                layout_search.setBackgroundResource(R.color.black);
                layout_book.setBackgroundResource(R.color.black);
                layout_account.setBackgroundResource(R.color.colorPrimary);
                etSearch.getText().clear();
            }
        });
        ivNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(i);
            }
        });

        //  new UserProfile(getApplicationContext()).execute();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void getUserServices() {
        if (u_type.equals("2")) {
            String url = Urlcollection.url + "action=getMyService&s_user_id=" + u_id;
            Log.d("url", url);
            final ProgressDialog pDialog = new ProgressDialog(HomeActivity.this);
            pDialog.setMessage(getResources().getString(R.string.pdialog));
            pDialog.setCancelable(false);
            pDialog.show();

            StringRequest request= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {pDialog.dismiss();
                        JSONObject jsonObject= new JSONObject(response);
                        JSONArray data = jsonObject.getJSONArray("data");
                        if (data.length() > 0) {
                            saveSharedPrefrence.saveSellerHasService(HomeActivity.this, true);
                            isSellerHasServices = true;
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
    }

    private void UpdateMyLocation(String u_id, String u_latitute, String u_longitute) {
        String url = Urlcollection.url + "action=updateUserLocation&lang=" + lang + "&u_id=" + u_id+"&u_latitute="+u_latitute+"&u_longitute="+u_longitute;
        Log.e("url", url);


        StringRequest request= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("response", response);
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    String message = object.getString("message");
                    Toast.makeText(HomeActivity.this, message, Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.container_body);
        if (frag instanceof Home_Fragement) {
            if (frag.isVisible()) {
                Log.d("visible", "true");

                CustomDialog();

            }
        } else {
            Home_Fragement fragemebt_home = new Home_Fragement();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragemebt_home);
            fragmentTransaction.commit();
        }
    }
    private void CustomDialog() {

        Button dialogButtonno,dialogButtonyes;
        TextView tv;
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.custom_layout_logout);
        dialog.getWindow().setTitleColor(getResources().getColor(R.color.colorPrimary));
        dialog.setTitle(getResources().getString(R.string.exit));

        dialogButtonno  = (Button) dialog.findViewById(R.id.btnno);
        dialogButtonyes = (Button) dialog.findViewById(R.id.btnyes);
        tv = (TextView) dialog.findViewById(R.id.tv);
        tv.setText("Are Sure You want to Exit?");
        dialog.show();

        dialogButtonyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
               /* SharedPreferences settings = context.getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
                settings.edit().remove("KeyName").commit();*/
               // gps.stopUsingGPS();

                HomeActivity.this.finishAffinity();



            }
        });

        dialogButtonno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog

                dialog.dismiss();

            }
        });

    }


    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new Home_Fragement();
                title = "Home";
                break;
            default:
                fragment = new Home_Fragement();
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            //  fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            // set the toolbar title
            // getSupportActionBar().setTitle(title);
            // getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#F06322\">" + title + "</font>")));

        }
    }

    public void switchContent(Fragment fragment, String mItemSelected) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_body, fragment);
        ft.commit();
        /*layout_home.setBackgroundResource(R.color.colorPrimary);
        layout_project.setBackgroundResource(R.color.black);
        layout_search.setBackgroundResource(R.color.black);
        layout_book.setBackgroundResource(R.color.black);
        layout_account.setBackgroundResource(R.color.black);*/
        ivTitle.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(mItemSelected);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Home Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    class getAllNotification extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg, cn_id, cn_device_id, cn_user_id, cn_title, cn_description, cn_device_type, cn_status, cn_noti_type, cn_noti_type_id,
                cn_created, cn_modified, totalRecord;

        getAllNotification(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
/*
            pDialog = new ProgressDialog(HomeActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();*/

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub

            String uri = Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", "getAllNotification"));
            nameValuePairs.add(new BasicNameValuePair("u_id", u_id));
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
                        cn_id = object.getString("cn_id");
                        cn_device_id = object.getString("cn_device_id");
                        cn_user_id = object.getString("cn_user_id");
                        cn_title = object.getString("cn_title");
                        cn_description = object.getString("cn_description");
                        cn_device_type = object.getString("cn_device_type");
                        cn_status = object.getString("cn_status");
                        cn_noti_type = object.getString("cn_noti_type");
                        cn_noti_type_id = object.getString("cn_noti_type_id");
                        cn_created = object.getString("cn_created");
                        cn_modified = object.getString("cn_modified");
                    }
                    totalRecord = jsonObj.getString("totalRecord");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (status.equals("1")) {
                tvNotify.setText("");
                ivNotify.setImageResource(R.drawable.notification_icon);
            } else {
                tvNotify.setText(totalRecord);
            }

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        gps = new TrackGPS(HomeActivity.this);
        Intent intent = new Intent(HomeActivity.this, LocationUpdaterService.class);
        if (u_type.equals("1")) {
            if (!isMyServiceRunning(LocationUpdaterService.class)) {
                Log.e("service", "starting");
                startService(intent);
            }

        } else if (u_type.equals("2")) {
            if (isMyServiceRunning(LocationUpdaterService.class)) {
                stopService(intent);
                Log.e("service", "not starting as it is seller");
            }
        }
        if (gps.canGetLocation()) {

        } else {
            gps.showSettingsAlert();
        }

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
