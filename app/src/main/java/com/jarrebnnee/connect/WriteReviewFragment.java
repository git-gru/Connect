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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jarrebnnee.connect.Service.GetSet;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.ServiceHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WriteReviewFragment extends AppCompatActivity {

    Button btn_submit;
    TextView tv1,tv2,tv3;
    EditText etRating;
    RatingBar rat;
    ImageView ivBack;
    TextView tvTitle;
    Toolbar toolbar;
    GetSet getSet;
    String jsr_id,rating,comment;
    SaveSharedPrefrence prefrence;
    String lang;
    String strDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_write_review);
        getSet = GetSet.getInstance();
        prefrence = new SaveSharedPrefrence();
        lang = prefrence.getlang(getApplicationContext());
        jsr_id = getSet.getjsr_id();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "" + "</font>")));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        btn_submit = (Button)findViewById(R.id.btn_submit);
        tv1 = (TextView)findViewById(R.id.tv1);
        tv2 = (TextView)findViewById(R.id.tv2);
        tv3 = (TextView)findViewById(R.id.tv3);
        etRating = (EditText)findViewById(R.id.etRating);
        rat = (RatingBar)findViewById(R.id.rat);

        Typeface custom_fontbold = Typeface.createFromAsset(getAssets(),  "fonts/tt0144m.ttf");
        tv1.setTypeface(custom_fontbold);

        Typeface custom_fontbold1 = Typeface.createFromAsset(getAssets(),  "fonts/tt0142m.ttf");
        tv2.setTypeface(custom_fontbold1);
        tv3.setTypeface(custom_fontbold1);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/16023_futuran_0.ttf");
        btn_submit.setTypeface(custom_font);
        etRating.setTypeface(custom_font);
        tvTitle.setTypeface(custom_font);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
         strDate = sdf.format(c.getTime());
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment = etRating.getText().toString();
                rating= String.valueOf(rat.getRating());
                if( etRating.getText().toString().length() == 0 ) {
                    etRating.setError(getResources().getString(R.string.commentrequired));
                } else if (rating.equals("")){
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.starrequired),Toast.LENGTH_LONG).show();
                }else{
                    Log.d("rating", rating);
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//we are connected to a network
                        //  url=URLCollection.LoginUrl+"&u_email="+email+"&u_password="+password+"&device_type=0&device_token="+regId;
                        //   SignInUrlCall(url);
                        new AddReview(getApplicationContext()).execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });



    }
    class AddReview extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status,msg;

        AddReview(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(WriteReviewFragment.this);
            pDialog.setMessage(getResources().getString(R.string.pdialog));
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            String uri =  Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", "completeJob"));
            nameValuePairs.add(new BasicNameValuePair("jsr_id", jsr_id));
            nameValuePairs.add(new BasicNameValuePair("rating", rating));
            nameValuePairs.add(new BasicNameValuePair("comment", comment));
            nameValuePairs.add(new BasicNameValuePair("date", strDate));
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
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.addreviewsucess),Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
