package com.jarrebnnee.connect;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.ServiceHandler;
import com.jarrebnnee.connect.fragement.BookNow_Fragement;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vardhman Infonet 4 on 22-Feb-17.
 */

public class ChagePasswordFragement extends AppCompatActivity {

    EditText edNewPwd,edOldPwd,edConfPwd;
    Button btn_submit;
    String u_id,u_oldpassword,u_newpassword,u_confirm;
    SaveSharedPrefrence saveSharedPrefrence;
    Toolbar toolbar;
    ImageView ivBack;
    TextView tvTitle;
    String lang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chage_pwd);
        saveSharedPrefrence = new SaveSharedPrefrence();
        u_id = saveSharedPrefrence.getUserID(getApplicationContext());
        lang = saveSharedPrefrence.getlang(getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView)findViewById(R.id.tvTitle);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "" + "</font>")));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        edOldPwd = (EditText)findViewById(R.id.edOldPwd);
        edNewPwd = (EditText)findViewById(R.id.edNewPwd);
        edConfPwd = (EditText)findViewById(R.id.edConfPwd);
        btn_submit = (Button)findViewById(R.id.btn_submit);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/16023_futuran_0.ttf");
        edOldPwd.setTypeface(custom_font);
        edNewPwd.setTypeface(custom_font);
        edConfPwd.setTypeface(custom_font);
        btn_submit.setTypeface(custom_font);
        tvTitle.setTypeface(custom_font);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u_newpassword = edNewPwd.getText().toString();
                u_oldpassword = edOldPwd.getText().toString();
                u_confirm = edConfPwd.getText().toString();

                if (!u_newpassword.equals(u_confirm)){
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.npsame),Toast.LENGTH_LONG).show();
                }else if( edOldPwd.getText().toString().length() == 0 ) {
                    edOldPwd.setError(getResources().getString(R.string.oprequired));
                }else if( edNewPwd.getText().toString().length() == 0 ) {
                    edNewPwd.setError(getResources().getString(R.string.nprequired));
                }else {
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//we are connected to a network
                        //  url=URLCollection.LoginUrl+"&u_email="+email+"&u_password="+password+"&device_type=0&device_token="+regId;
                        //   SignInUrlCall(url);
                        new ChangePwd(getApplicationContext()).execute();

                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

    }
    class ChangePwd extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status,msg;

        ChangePwd(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ChagePasswordFragement.this);
            pDialog.setMessage(getResources().getString(R.string.pdialog));
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            String uri =  Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", "changePassword"));
            nameValuePairs.add(new BasicNameValuePair("u_id", u_id));
            nameValuePairs.add(new BasicNameValuePair("u_oldpassword", u_oldpassword));
            nameValuePairs.add(new BasicNameValuePair("u_newpassword", u_newpassword));
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
                Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_LONG).show();
                finish();
            }else{
                Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_LONG).show();
            }
        }
    }

}
