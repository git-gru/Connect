package com.jarrebnnee.connect.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jarrebnnee.connect.ProjectDetailActivity;
import com.jarrebnnee.connect.R;
import com.jarrebnnee.connect.SellerJobApplyActivity;
import com.jarrebnnee.connect.SellerJobListActivity;
import com.jarrebnnee.connect.Service.GetSet;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.ServiceHandler;
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
public class SellerJobListAdapter extends RecyclerView.Adapter<SellerJobListAdapter.MyViewHolder>{
    private ArrayList<HashMap<String,String>> list;
    private Activity c;
    WriteReviewFragment reviewFragment;
    Bundle mBundle;
    GetSet getSet;
    String jsr_id,u_id,lang;
    SaveSharedPrefrence prefrence;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtView,tvCategory,tvdes,tvStatus;
        LinearLayout layout;
        public ImageView imgView;
        Button btnReview;

        public MyViewHolder(View view) {
            super(view);
            txtView = (TextView) view.findViewById(R.id.tvSName);
            tvCategory = (TextView) view.findViewById(R.id.tvCategory);
            tvStatus = (TextView) view.findViewById(R.id.tvStatus);
            tvdes = (TextView) view.findViewById(R.id.tvdes);
            layout = (LinearLayout)view.findViewById(R.id.layout);
            imgView = (ImageView) view.findViewById(R.id.iv1);
            btnReview = (Button) view.findViewById(R.id.btnReview);

            Typeface custom_font = Typeface.createFromAsset(c.getAssets(),  "fonts/tt0142m.ttf");
            tvStatus.setTypeface(custom_font);
            tvCategory.setTypeface(custom_font);
            Typeface custom_font1 = Typeface.createFromAsset(c.getAssets(),  "fonts/16023_futuran_0.ttf");
            btnReview.setTypeface(custom_font1);


            Typeface custom_fontbold = Typeface.createFromAsset(c.getAssets(),  "fonts/tt0144m.ttf");
            txtView.setTypeface(custom_fontbold);

        }

    }


    public SellerJobListAdapter(Activity context, ArrayList<HashMap<String,String>> list) {
        this.c = context;
        this.list = list;
        getSet = GetSet.getInstance();
        prefrence = new SaveSharedPrefrence();
        u_id = prefrence.getUserID(c);
        lang = prefrence.getlang(c);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_project_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        HashMap<String,String> resultp = list.get(position);

        holder.txtView.setText(resultp.get("js_title"));
        holder.tvCategory.setText("Category: "+resultp.get("c_title"));
        holder.tvdes.setText(resultp.get("js_description"));
        String img = resultp.get("js_image");
        Picasso.with(c).load(img).into(holder.imgView);
        //holder.imgView.setImageResource(icons[position]);
       /* if (sts[position].equals("Pending")){
            holder.tvStatus.setTextColor(Color.parseColor("#FF6600"));
        }else {
            holder.tvStatus.setTextColor(Color.parseColor("#228B22"));
        }*/
        String jsr_status = resultp.get("js_status");

        if (jsr_status.equals("3")){
            holder.tvStatus.setText("Status: Completed");
            holder.btnReview.setText("Completed");
         //   holder.btnReview.setVisibility(View.GONE);
            holder.btnReview.setEnabled(false);
        }else if (jsr_status.equals("2")){
            holder.tvStatus.setText("Status: Awarded");
            holder.btnReview.setText("Awarded");
           // holder.btnReview.setVisibility(View.GONE);
            holder.btnReview.setEnabled(false);
        }else if (jsr_status.equals("1")){
            holder.tvStatus.setText("Status: Applied");
            holder.btnReview.setText("Applied");
            //holder.btnReview.setVisibility(View.GONE);
            holder.btnReview.setEnabled(false);
        }else if (jsr_status.equals("4")){
            holder.tvStatus.setText("Status: Accept");
            holder.btnReview.setText("Accept");
            holder.btnReview.setBackgroundColor(Color.parseColor("#008000"));
            holder.btnReview.setVisibility(View.VISIBLE);
            holder.btnReview.setEnabled(true);
        }else if (jsr_status.equals("5")){
            holder.tvStatus.setText("Status: Rejected");
            holder.btnReview.setText("Rejected");
            //holder.btnReview.setVisibility(View.GONE);
            holder.btnReview.setEnabled(false);
        }else if (jsr_status.equals("0")){
            holder.tvStatus.setText("Status: Pending");
            holder.btnReview.setText("Apply");
            //holder.btnReview.setVisibility(View.VISIBLE);
            holder.btnReview.setEnabled(true);
        }

        holder.layout.setTag(position);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = (int) v.getTag();
                HashMap<String,String> resultp = list.get(i);
                getSet.setjs_id(resultp.get("js_id"));
                getSet.setjs_title(resultp.get("js_title"));
                getSet.setjsr_posted_user_id(resultp.get("js_user_id"));
           //     Toast.makeText(c,holder.txtView.getText().toString(), Toast.LENGTH_SHORT).show();
                Intent i1 = new Intent(c, ProjectDetailActivity.class);
                i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(i1);
            }
        });
        holder.btnReview.setTag(position);
        holder.btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i1 = (int) v.getTag();
                HashMap<String,String> resultp = list.get(i1);
                jsr_id=resultp.get("jsr_id");
                getSet.setjsr_id(resultp.get("js_id"));
                getSet.setjsr_posted_user_id(resultp.get("js_user_id"));
                String jsr_status = resultp.get("js_status");
                if (jsr_status.equals("3")){
                    /*holder.tvStatus.setText("Status: Completed");
                    holder.btnReview.setText("Completed");
                    holder.btnReview.setVisibility(View.GONE);*/
                }else if (jsr_status.equals("2")){
                    /*holder.tvStatus.setText("Status: Awarded");
                    holder.btnReview.setText("Awarded");
                    holder.btnReview.setVisibility(View.GONE);*/
                }else if (jsr_status.equals("1")){

                }else if (jsr_status.equals("4")){
                    new acceptJob(c).execute();
                }else if (jsr_status.equals("5")){
                }else if (jsr_status.equals("0")){
                    Intent i = new Intent(c,SellerJobApplyActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("js_id", "");
                    i.putExtra("js_user_id","");
                    c.startActivity(i);
                    c.finish();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class acceptJob extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg;

        acceptJob(Context context) {

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

            nameValuePairs.add(new BasicNameValuePair("action","acceptJob"));
            nameValuePairs.add(new BasicNameValuePair("jsr_id",jsr_id));
            nameValuePairs.add(new BasicNameValuePair("u_id",u_id));
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //pDialog.dismiss();
            if (status.equals("0")) {
                c.finish();
                Toast.makeText(c,c.getResources().getString(R.string.aceeptsucess),Toast.LENGTH_LONG).show();
                Intent i = new Intent(c,SellerJobListActivity.class);
                c.startActivity(i);

            }
        }
    }


}
