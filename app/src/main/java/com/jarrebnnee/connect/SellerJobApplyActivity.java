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
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jarrebnnee.connect.Adapter.SellerJobListAdapter;
import com.jarrebnnee.connect.Service.GetSet;
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

public class SellerJobApplyActivity extends AppCompatActivity {

    String comment,price,u_id,js_id,jsr_posted_user_id;
    SaveSharedPrefrence prefrence;
    GetSet getSet;
    EditText etPrice,etDesc;
    Button btn_submit;
    Toolbar toolbar;
    ImageView ivBack;
    TextView tvTitle;
    String lang, js_id1, js_user_id1;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_jon_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        setSupportActionBar(toolbar);

        bundle = getIntent().getExtras();
        js_id1 = bundle.getString("js_id");
        js_user_id1 = bundle.getString("js_user_id");


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "" + "</font>")));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSet = GetSet.getInstance();
        prefrence = new SaveSharedPrefrence();
        lang = prefrence.getlang(getApplicationContext());
        u_id = prefrence.getUserID(getApplicationContext());

        if (js_id1.length() == 0 || js_user_id1.length() == 0) {
            js_id = getSet.getjsr_id();
            jsr_posted_user_id = getSet.getjsr_posted_user_id();
        } else {
            js_id = js_id1;
            jsr_posted_user_id = js_user_id1;
        }



        etPrice = (EditText)findViewById(R.id.etPrice);
        etDesc = (EditText)findViewById(R.id.etDesc);
        btn_submit = (Button)findViewById(R.id.btn_submit);

        Typeface custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(),  "fonts/16023_futuran_0.ttf");
        tvTitle.setTypeface(custom_font);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price = etPrice.getText().toString();
                comment = etDesc.getText().toString();
                if( etPrice.getText().toString().length() == 0 ) {
                    etPrice.setError(getResources().getString(R.string.prequired));
                }else  if( etDesc.getText().toString().length() == 0 ) {
                    etDesc.setError(getResources().getString(R.string.contactrequired));
                }else {
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//we are connected to a network
                        //  url=URLCollection.LoginUrl+"&u_email="+email+"&u_password="+password+"&device_type=0&device_token="+regId;
                        //   SignInUrlCall(url);
                        new Apply(getApplicationContext()).execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SellerJobListActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),SellerJobListActivity.class);
        startActivity(i);
        finish();
    }

    class Apply extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg;

        Apply(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SellerJobApplyActivity.this);
            pDialog.setMessage(getResources().getString(R.string.pdialog));
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub

            String uri =  Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("action","applyJob"));
            nameValuePairs.add(new BasicNameValuePair("jsr_job_id",js_id));
            nameValuePairs.add(new BasicNameValuePair("jsr_posted_user_id",jsr_posted_user_id));
            nameValuePairs.add(new BasicNameValuePair("jsr_requested_user_id",u_id));
            nameValuePairs.add(new BasicNameValuePair("jsr_apply_price",price));
            nameValuePairs.add(new BasicNameValuePair("jsr_apply_text",comment));
            nameValuePairs.add(new BasicNameValuePair("lang",lang));
            // nameValuePairs.add(new BasicNameValuePair("radius",radius));



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
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.applysucess),Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(i);
                finish();
            }
        }
    }
}
