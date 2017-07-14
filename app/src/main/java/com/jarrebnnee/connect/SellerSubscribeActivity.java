package com.jarrebnnee.connect;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class SellerSubscribeActivity extends AppCompatActivity {

    RecyclerView rv_subscribe;
    Toolbar toolbar;
    ImageView ivBack;
    TextView tvTitle;
    SaveSharedPrefrence saveSharedPrefrence;
    String lang = "", u_id, obj;
    ArrayList<HashMap<String, String>> list;
    SellerSubscribePackageAdaper packageAdaper;

    String check;

    public static final int PAYPAL_REQUEST_CODE = 123;
    public static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_subscribe);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        setSupportActionBar(toolbar);

      //  Log.e("check",check);
        saveSharedPrefrence = new SaveSharedPrefrence();
        lang = saveSharedPrefrence.getlang(getApplicationContext());
        u_id = saveSharedPrefrence.getUserID(getApplicationContext());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "" + "</font>")));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/16023_futuran_0.ttf");
        tvTitle.setTypeface(custom_font);

        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);


        list = new ArrayList<HashMap<String, String>>();
        rv_subscribe = (RecyclerView) findViewById(R.id.rv_subscribe);
        int numberOfColumns = 2;
        rv_subscribe.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        packageAdaper = new SellerSubscribePackageAdaper(list, SellerSubscribeActivity.this);
        rv_subscribe.setAdapter(packageAdaper);

        callPackageListApi();

    }




    void callPackageListApi() {
        String url = Urlcollection.url + "action=getPackageList&lang=" + lang;
        Log.e("url", url);

        final ProgressDialog pDialog = new ProgressDialog(SellerSubscribeActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equals("0")) {

                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);

                            String pkg_id = object.getString("pkg_id");
                            String pkg_radios = object.getString("pkg_radios");
                            String pkg_min_advertise = object.getString("pkg_min_advertise");
                            String pkg_price = object.getString("pkg_price");
                            String pkg_desc = object.getString("pkg_desc");
                            String pkg_title = object.getString("pkg_title");

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("pkg_id", pkg_id);
                            map.put("pkg_radios", pkg_radios);
                            map.put("pkg_min_advertise", pkg_min_advertise);
                            map.put("pkg_price", pkg_price);
                            map.put("pkg_desc", pkg_desc);
                            map.put("pkg_title", pkg_title);

                            list.add(map);
                            packageAdaper.notifyDataSetChanged();
                        }


                    } else {
                        Toast.makeText(SellerSubscribeActivity.this, message, Toast.LENGTH_SHORT).show();
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


    private void getPayment(String paymentAmount, String packageName, String pkg_id) {
        //Getting the amount from editText


        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "USD", packageName,
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
      //  intent.putExtra("pkg_id1", pkg_id);
        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);


                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);

                        JSONObject object= confirm.toJSONObject();
                        JSONObject response = object.getJSONObject("response");
                        String transaction_id = response.getString("id");
                        String state = response.getString("state");
                        Log.e("paymentExample", "\n\nresponse : "+response);
                        Log.e("paymentExample", "\ntransaction_id : "+transaction_id);
                        Log.e("paymentExample", "\nstate : "+state);

                        CallSubscribePlanApi(obj, transaction_id);
                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("paymentExample", "The user canceled.");
                Toast.makeText(SellerSubscribeActivity.this,"The user canceled." ,Toast.LENGTH_SHORT).show();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.e("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
                Toast.makeText(SellerSubscribeActivity.this,"An invalid Payment or PayPalConfiguration was submitted. Please see the docs." ,Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void CallSubscribePlanApi(final String obj, String transaction_id) {
        String pkg_id = saveSharedPrefrence.getpkg_id(SellerSubscribeActivity.this);
        String no_of_advertise = saveSharedPrefrence.getno_of_advertise(SellerSubscribeActivity.this);
        String paymentAmount = saveSharedPrefrence.getpaymentAmount(SellerSubscribeActivity.this);
        String packageName = saveSharedPrefrence.getpackageName(SellerSubscribeActivity.this);
        String packageRadius = saveSharedPrefrence.getpackageRadius(SellerSubscribeActivity.this);
        String url = Urlcollection.url+"action=subscribePlan&lang="+lang+"&transaction_id="+transaction_id+"&u_id="+u_id+"&pkg_id="+pkg_id+"&no_of_advertise="+no_of_advertise+"&amount="+paymentAmount+"&subscribe_status=1&payment_type=1";
        Log.e("url", url);

        final ProgressDialog pDialog = new ProgressDialog(SellerSubscribeActivity.this);
        pDialog.setMessage("Subscribing your plan...");
        pDialog.show();
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pDialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String message = object.getString("message");
                    Toast.makeText(SellerSubscribeActivity.this, message, Toast.LENGTH_SHORT).show();

                    String pkg_id = saveSharedPrefrence.getpkg_id(SellerSubscribeActivity.this);
                    String no_of_advertise = saveSharedPrefrence.getno_of_advertise(SellerSubscribeActivity.this);
                    String paymentAmount = saveSharedPrefrence.getpaymentAmount(SellerSubscribeActivity.this);
                    String packageName = saveSharedPrefrence.getpackageName(SellerSubscribeActivity.this);

                    saveSharedPrefrence.savepkg_id(SellerSubscribeActivity.this, pkg_id);
                    saveSharedPrefrence.savepaymentAmount(SellerSubscribeActivity.this, paymentAmount);
                    saveSharedPrefrence.savepackageName(SellerSubscribeActivity.this,packageName);
                    saveSharedPrefrence.saveno_of_advertise(SellerSubscribeActivity.this,no_of_advertise);
                    String packageRadius = saveSharedPrefrence.getpackageRadius(SellerSubscribeActivity.this);
                    Intent intent= new Intent(SellerSubscribeActivity.this, SellerSubscribedPlans.class);
                    startActivity(intent);
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
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }


   // void CallSubscribePlanApi()


    class SellerSubscribePackageAdaper extends RecyclerView.Adapter<SellerSubscribePackageAdaper.MyViewHolder2>{


        ArrayList<HashMap<String, String>> list;
        Context context;
        HashMap<String, String> map;

        public SellerSubscribePackageAdaper(ArrayList<HashMap<String, String>> list, Context context) {
            this.list = list;
            this.context = context;
        }

        public class MyViewHolder2 extends RecyclerView.ViewHolder {
            TextView tv_package_name, tv_package_desc, tv_package_radius, tv_package_minimum, tv_package_price;
            Button btn_package_subscribe;

            public MyViewHolder2(View itemView) {
                super(itemView);
                tv_package_name = (TextView) itemView.findViewById(R.id.tv_package_name);
                tv_package_desc = (TextView) itemView.findViewById(R.id.tv_package_desc);
                tv_package_radius = (TextView) itemView.findViewById(R.id.tv_package_radius);
                tv_package_minimum = (TextView) itemView.findViewById(R.id.tv_package_minimum);
                tv_package_price = (TextView) itemView.findViewById(R.id.tv_package_price);

                btn_package_subscribe = (Button) itemView.findViewById(R.id.btn_package_subscribe);
            }
        }



        @Override
        public MyViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_subscribe, parent, false);

            return new SellerSubscribePackageAdaper.MyViewHolder2(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder2 holder, final int position) {
            map = list.get(position);
            Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "fonts/16023_futuran_0.ttf");
            holder.tv_package_name.setTypeface(custom_font);
            holder.tv_package_desc.setTypeface(custom_font);
            holder.tv_package_radius.setTypeface(custom_font);
            holder.tv_package_minimum.setTypeface(custom_font);
            holder.tv_package_price.setTypeface(custom_font);
            holder.btn_package_subscribe.setTypeface(custom_font);


            holder.tv_package_name.setText(map.get("pkg_title"));
            holder.tv_package_desc.setText(map.get("pkg_desc"));
            holder.tv_package_radius.setText(map.get("pkg_radios")+" Radius");
            holder.tv_package_minimum.setText(map.get("pkg_min_advertise")+" Minimum Advertise");
            if (map.get("pkg_price").equals("0")) {
                holder.tv_package_price.setText("Free");
                holder.tv_package_price.setTextColor(Color.parseColor("#32CD32"));
                holder.tv_package_price.setTypeface(custom_font, Typeface.BOLD);

            } else {
                holder.tv_package_price.setText("$ "+map.get("pkg_price"));
            }




            final String pkg_id = map.get("pkg_id");
            final String paymentAmount = map.get("pkg_price");
            final String packageName = map.get("pkg_title");
            final String no_of_advertise=map.get("pkg_min_advertise");
            final String package_radius=map.get("pkg_radios");

            holder.btn_package_subscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   Toast.makeText(context, "pkg_ig : "+pkg_id+"\nAmount : "+paymentAmount,Toast.LENGTH_SHORT).show();
                    CallUserSubscribedApi(pkg_id, paymentAmount,packageName);
                 //   getPayment(paymentAmount, packageName, pkg_id);
                    saveSharedPrefrence.savepkg_id(SellerSubscribeActivity.this, pkg_id);
                    saveSharedPrefrence.savepaymentAmount(SellerSubscribeActivity.this, paymentAmount);
                    saveSharedPrefrence.savepackageName(SellerSubscribeActivity.this,packageName);
                    saveSharedPrefrence.saveno_of_advertise(SellerSubscribeActivity.this,no_of_advertise);
                    saveSharedPrefrence.savepackageRadius(SellerSubscribeActivity.this, package_radius);
                }
            });
        }



        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private void CallUserSubscribedApi(final String pkg, final String paymentAmount , final String packageName) {
        String url = Urlcollection.url+"action=getSubscribeUserPlanList&lang="+lang+"&u_id="+u_id;
        Log.e("url", url);

        final ProgressDialog pDialog = new ProgressDialog(SellerSubscribeActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();


        StringRequest request= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pDialog.dismiss();
                try {
                    Log.e("res", response);
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    String message = object.getString("message");
                    if (status.equals("0")) {
                        boolean check = true;
                        JSONArray PackData = object.getJSONArray("PackData");
                        int kk = PackData.length();

                        if (kk > 0) {
                            for (int i = 0; i < PackData.length(); i++) {
                                JSONObject jsonObject = PackData.getJSONObject(i);
                                String pkg_id = jsonObject.getString("pkg_id");
                                check = true;
                                if (pkg_id.equals(pkg)) {
                                    Toast.makeText(SellerSubscribeActivity.this, "You have already subscribed this advertise plan", Toast.LENGTH_LONG).show();
                                    check = false;
                                }
                            }
                            if (check) {
                                if (paymentAmount.equals("0")) {
                                    Toast.makeText(SellerSubscribeActivity.this, "Free plan", Toast.LENGTH_SHORT).show();
                                    CallSubscribePlanApi("", "null");
                                } else {
                                    getPayment(paymentAmount, packageName, pkg);

                                }
                            }
                        } else if (kk <= 0) {
                            if (paymentAmount.equals("0")) {
                                Toast.makeText(SellerSubscribeActivity.this, "Free plan", Toast.LENGTH_SHORT).show();
                                CallSubscribePlanApi("", "null");
                            } else {
                                getPayment(paymentAmount, packageName, pkg);

                            }
                        }


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

    @Override
    public void onBackPressed() {
        if (SellerSubscribeActivity.this.isTaskRoot()) {
            Intent intent = new Intent(SellerSubscribeActivity.this, HomeActivity.class);
            startActivity(intent);
        } else {
            super.onBackPressed();
        }

    }
}







