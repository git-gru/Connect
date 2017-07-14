package com.jarrebnnee.connect.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jarrebnnee.connect.DisplayIncomingAdvertise;
import com.jarrebnnee.connect.FullImageActivity;
import com.jarrebnnee.connect.R;
import com.jarrebnnee.connect.Service.GetSet;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.ServiceHandler;
import com.jarrebnnee.connect.ServiceItemDetailActivity;
import com.jarrebnnee.connect.Urlcollection;
import com.jarrebnnee.connect.WriteReviewFragment;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jigna on 13/10/16.
 */
public class ApplyerListAdapter extends RecyclerView.Adapter<ApplyerListAdapter.MyViewHolder>{
    private ArrayList<HashMap<String,String>> list;
    private Activity c;
    WriteReviewFragment reviewFragment;
    Bundle mBundle;
    GetSet getSet;
    String jsr_id;
    SaveSharedPrefrence prefrence;
    String user_id,lang;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtView,tvDesc,tvStatus, tv_proposed_price;
        Button btnAward,btnReject,btnDetails;
        public ImageView imgView;
        RatingBar rat;

        public MyViewHolder(View view) {
            super(view);
            txtView = (TextView) view.findViewById(R.id.tvSName);
            tvDesc = (TextView) view.findViewById(R.id.tvDesc);
            tvStatus = (TextView) view.findViewById(R.id.tvStatus);
            tv_proposed_price = (TextView) view.findViewById(R.id.tv_proposed_price);
            rat = (RatingBar)view.findViewById(R.id.rat);
            imgView = (ImageView) view.findViewById(R.id.iv1);
            btnAward = (Button) view.findViewById(R.id.btnReview);
            btnReject = (Button) view.findViewById(R.id.btnReject);
            btnDetails = (Button) view.findViewById(R.id.btnDetails);

            Typeface custom_font = Typeface.createFromAsset(c.getAssets(),  "fonts/tt0142m.ttf");
            tvDesc.setTypeface(custom_font);
            btnAward.setTypeface(custom_font);
            tvStatus.setTypeface(custom_font);
            btnReject.setTypeface(custom_font);

            Typeface custom_fontbold = Typeface.createFromAsset(c.getAssets(),  "fonts/tt0144m.ttf");
            txtView.setTypeface(custom_fontbold);




        }

    }


    public ApplyerListAdapter(Activity context, ArrayList<HashMap<String,String>> list) {
        this.c = context;
        this.list = list;
        getSet = GetSet.getInstance();
        prefrence = new SaveSharedPrefrence();
        user_id = prefrence.getUserID(c);
        lang = prefrence.getlang(c);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_applyrc, parent, false);

        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final HashMap<String,String> resultp = list.get(position);

        holder.txtView.setText(resultp.get("u_first_name")+" "+resultp.get("u_last_name"));
        holder.tvDesc.setText(resultp.get("jsr_apply_text"));
        final String img = resultp.get("u_img");
        Picasso.with(c).load(img).into(holder.imgView);
        holder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  imgView.setDrawingCacheEnabled(true);
                imgView.buildDrawingCache();
                Bitmap bitmap = Bitmap.createBitmap(imgView.getDrawingCache());
                *//*    imgView.buildDrawingCache();
                    Bitmap bitmap=imgView.getDrawingCache();*/
                Intent in=new Intent(c,FullImageActivity.class);
                in.putExtra("Image",img);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(in);
            }
        });

        /*String jsr_rating = resultp.get("jsr_rating");
        if (jsr_rating.equals("")){
            jsr_rating = "0";
        }else {

        }*/
        String jsr_status = resultp.get("jsr_status");
        if (jsr_status.equals("3")){
            holder.tvStatus.setText("Status: Completed");
            holder.btnAward.setText("Completed");
            holder.btnReject.setVisibility(View.INVISIBLE);
            holder.btnDetails.setVisibility(View.INVISIBLE);
 //           holder.btnAward.setVisibility(View.GONE);
        }else if (jsr_status.equals("2")){
            holder.tvStatus.setText("Status: Running");
            holder.btnAward.setText("Finish Now");
 //           holder.btnAward.setVisibility(View.VISIBLE);
        }else if (jsr_status.equals("1")){
            holder.tvStatus.setText("Status: Pending");
            holder.btnAward.setText("Award Now");
            holder.btnAward.setBackgroundColor(Color.parseColor("#008000"));
            holder.btnReject.setVisibility(View.VISIBLE);
//            holder.btnAward.setVisibility(View.VISIBLE);
        }else if (jsr_status.equals("4")){
            holder.tvStatus.setText("Status: Awarded");
            holder.btnAward.setText("Reject Now");
//            holder.btnAward.setVisibility(View.GONE);
        }else if (jsr_status.equals("5")){
            holder.tvStatus.setText("Status: Rejected");
            holder.btnAward.setText("Rejected");
            holder.btnReject.setVisibility(View.GONE);
        }
        holder.rat.setRating((float)4.0);

        holder.btnAward.setTag(position);
        holder.btnAward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = (int) v.getTag();
                HashMap<String,String> resultp = list.get(i);
                jsr_id=resultp.get("jsr_id");
                String jsr_status = resultp.get("jsr_status");
                if (jsr_status.equals("3")){
                   holder.btnAward.setVisibility(View.GONE);
                }else if (jsr_status.equals("2")){
                    holder.btnAward.setVisibility(View.VISIBLE);
                    getSet.setjsr_id(jsr_id);
                    Intent i1 = new Intent(c,WriteReviewFragment.class);
                    i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    c.startActivity(i1);
                    c.finish();
                }else if (jsr_status.equals("1")){
                    holder.btnAward.setVisibility(View.VISIBLE);
                    new AwardJob(c).execute();
                }else if (jsr_status.equals("4")){
                    holder.btnAward.setVisibility(View.GONE);
                  //  new RejectJob(c).execute();
                  //  holder.btnAward.setText("Awarded");
                }else if (jsr_status.equals("5")){
                    holder.btnAward.setVisibility(View.GONE);
                  //  holder.btnAward.setText("Rejected");
                }

            }
        });
        holder.tv_proposed_price.setText("Proposed price: $"+resultp.get("jsr_apply_price"));
        final String proposed_price = resultp.get("jsr_apply_price");
        final String comment = resultp.get("jsr_apply_text");
        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog(proposed_price, comment);

            }
        });
        holder.btnReject.setTag(position);
        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = (int) v.getTag();
                HashMap<String,String> resultp = list.get(i);
                jsr_id=resultp.get("jsr_id");
                new RejectJob(c).execute();
            }
        });
        holder.txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, ServiceItemDetailActivity.class);
                intent.putExtra("sp_id", resultp.get("u_id"));
                c.startActivity(intent);
            }
        });


    }

    private void ShowDialog(String proposed_price, String comment) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
        alertDialogBuilder.setTitle(c.getString(R.string.sellerdbid));
        alertDialogBuilder.setMessage("Seller Bid Price = $"+proposed_price+"\n\nComment : "+comment);
        alertDialogBuilder.setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class AwardJob extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status,msg,u_id,u_first_name, u_last_name, u_email, u_password, u_address, u_latitute, u_longitute, u_city, u_phone, u_postcode, u_country,u_description,u_img, u_status, u_type, u_is_notification_sound,u_seller_services, u_created, u_modified,
                s_id,s_city_id,s_country_id,s_category_id,s_user_id,s_created,s_modified,city_name,avg_rating,total_review,country_name,categoty_name;

        AwardJob(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            String uri =  Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", "awardSellerForJob"));
            nameValuePairs.add(new BasicNameValuePair("jsr_id", jsr_id));
            nameValuePairs.add(new BasicNameValuePair("u_id", user_id));
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

            if (status.equals("0")) {
                Toast.makeText(c, c.getResources().getString(R.string.awardsucess), Toast.LENGTH_SHORT).show();
                c.finish();
            }
        }
    }
    class RejectJob extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status,msg,u_id,u_first_name, u_last_name, u_email, u_password, u_address, u_latitute, u_longitute, u_city, u_phone, u_postcode, u_country,u_description,u_img, u_status, u_type, u_is_notification_sound,u_seller_services, u_created, u_modified,
                s_id,s_city_id,s_country_id,s_category_id,s_user_id,s_created,s_modified,city_name,avg_rating,total_review,country_name,categoty_name;

        RejectJob(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            String uri =  Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", "rejectJob"));
            nameValuePairs.add(new BasicNameValuePair("jsr_id", jsr_id));
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

            if (status.equals("0")) {
                Toast.makeText(c, c.getResources().getString(R.string.rejectsucess), Toast.LENGTH_SHORT).show();
                c.finish();
            }
        }
    }

}
