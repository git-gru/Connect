package com.jarrebnnee.connect.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jarrebnnee.connect.MyApplication;
import com.jarrebnnee.connect.R;
import com.jarrebnnee.connect.SellerServiceSetActivity;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.ServiceHandler;
import com.jarrebnnee.connect.SimpleDividerItemDecoration;
import com.jarrebnnee.connect.Urlcollection;
import com.jarrebnnee.connect.fragement.SetServiceForSellerFragement;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Vardhman Infonet 4 on 03-Mar-17.
 */
public class SellerViewListAdapter extends RecyclerView.Adapter<SellerViewListAdapter.MyViewHolder> {
    private ArrayList<HashMap<String, String>> cat_name;
    private Activity c;
    HashMap<String, String> map;
    String sc_email, sc_category_id, sc_user_id;
    SaveSharedPrefrence saveSharedPrefrence;
    SellerViewSubListAdapter subCateListAdapter;
    ArrayList<HashMap<String, String>>  cat_list = new ArrayList<HashMap<String, String>>();
    String s_id,lang;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ck,tvContry,tvCity, tvv1, tvv2;

        ImageView ivDelete, ivPic;

        public MyViewHolder(View view) {
            super(view);
            ck = (TextView) view.findViewById(R.id.tvCate1);
            tvContry = (TextView) view.findViewById(R.id.tvContry);
            tvCity = (TextView) view.findViewById(R.id.tvCity);
            tvv1 = (TextView) view.findViewById(R.id.tvv1);
            tvv2 = (TextView) view.findViewById(R.id.tvv2);
            ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
            ivPic = (ImageView) view.findViewById(R.id.ivPic);
//            lvcate.setNestedScrollingEnabled(false);

            Typeface custom_font = Typeface.createFromAsset(c.getAssets(), "fonts/16023_futuran_0.ttf");
            ck.setTypeface(custom_font);
            tvContry.setTypeface(custom_font);
            tvCity.setTypeface(custom_font);
            tvv1.setTypeface(custom_font);
            tvv2.setTypeface(custom_font);

            Typeface custom_fontbold = Typeface.createFromAsset(c.getAssets(),  "fonts/tt0144m.ttf");


        }

    }

    public SellerViewListAdapter(Activity context, ArrayList<HashMap<String, String>> cat_name, ArrayList<HashMap<String, String>> cat_list1) {
        this.c = context;
        this.cat_name = cat_name;

        this.cat_list = cat_list1;
        saveSharedPrefrence = new SaveSharedPrefrence();
        lang = saveSharedPrefrence.getlang(c);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_sellercatelist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        map = cat_name.get(position);

        sc_email = saveSharedPrefrence.getUserEmail(c);
        sc_user_id = saveSharedPrefrence.getUserID(c);

        int temp = MyApplication.getDefaultLanguage();
        if (temp == 1) {
            //   String item = map.getC_ar_title();
            holder.ck.setText(map.get("c_ar_title"));
        } else {
            holder.ck.setText(map.get("c_title"));
        }
        holder.ck.setTag(position);
        String c_is_parent_id = map.get("has_sub_category");
        Log.e("c_is_parent_id",c_is_parent_id);
        holder.tvContry.setText(map.get("country_name"));
        String img = map.get("c_images");
        Log.e("image", img);
        Picasso.with(c).load(img).into(holder.ivPic);
        holder.tvCity.setText(map.get("city_name"));
      /*  if (c_is_parent_id.equals("1")){
            holder.tvserv.setVisibility(View.VISIBLE);
            holder.lvcate.setVisibility(View.VISIBLE);
            subCateListAdapter = new SellerViewSubListAdapter(c, cat_list);
            holder.lvcate.setAdapter(subCateListAdapter);
            holder.lvcate.addItemDecoration(new SimpleDividerItemDecoration(c));
            Log.e("c_is_parent_id",""+cat_list.size());

        }else{
            holder.lvcate.setVisibility(View.GONE);
            holder.tvserv.setVisibility(View.GONE);
        }*/
        holder.ivDelete.setTag(position);
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = (int) v.getTag();
                map = cat_name.get(i);
                s_id = map.get("s_id");
                String c_is_parent_id = map.get("has_sub_category");
                Log.e("c_is_parent_id",c_is_parent_id);
                Button dialogButtonno,dialogButtonyes;
                TextView tv;
                final Dialog dialog = new Dialog(c);
                dialog.setContentView(R.layout.custom_layout_notifi);
                dialog.getWindow().setTitleColor(c.getResources().getColor(R.color.colorPrimary));
                dialog.setTitle(c.getResources().getString(R.string.dservice));

                dialogButtonno  = (Button) dialog.findViewById(R.id.btnno);
                dialogButtonyes = (Button) dialog.findViewById(R.id.btnyes);
                tv = (TextView)dialog.findViewById(R.id.tv);
                if (c_is_parent_id.equals("1")){
                    tv.setText(c.getResources().getString(R.string.suredelete));
                }else {
                    tv.setText(c.getResources().getString(R.string.thissdelete));
                }
                dialog.show();
                dialogButtonyes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close dialog
                        new deleteMyService(c).execute();
                        dialog.dismiss();

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
        });


    }

    @Override
    public int getItemCount() {
        return cat_name.size();
    }

    class deleteMyService extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;

        String status, msg;

        deleteMyService(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            String uri = Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", "deleteMyService"));
            nameValuePairs.add(new BasicNameValuePair("s_id", s_id));
            nameValuePairs.add(new BasicNameValuePair("s_user_id", sc_user_id));
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
                c.finish();
                Intent i = new Intent(c,SellerServiceSetActivity.class);
                c.startActivity(i);
                Toast.makeText(c,c.getResources().getString(R.string.dsucess),Toast.LENGTH_LONG).show();
            }
            // pDialog.dismiss();
        }
    }

    /*class SubCategory extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg, c_id1, c_images, c_type, c_title, c_is_parent_id, c_total_services, c_created, s_modified;// u_id, u_first_name, u_last_name, u_email1, u_password1, u_address, u_latitute, u_longitute, u_city, u_phone, u_postcode, u_country, u_status, u_type, u_is_notification_sound, u_created, u_modified;
        MyViewHolder holder;
        SubCategory(Context context, MyViewHolder holder) {

            this.context = context;
            this.holder = holder;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            cat_list = new ArrayList<HashMap<String, String>>();
            String uri = Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action", "getSubCategoryList"));
            nameValuePairs.add(new BasicNameValuePair("c_id", sc_category_id));

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
                        c_id1 = object.getString("c_id");
                        c_images = object.getString("c_images");
                        c_type = object.getString("c_type");
                        c_title = object.getString("c_title");
                        c_is_parent_id = object.getString("c_is_parent_id");
                        c_total_services = object.getString("c_total_services");
                        c_created = object.getString("c_created");
                        s_modified = object.getString("s_modified");

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("c_id", c_id1);
                        map.put("c_images", c_images);
                        map.put("c_type", c_type);
                        map.put("c_title", c_title);
                        map.put("c_is_parent_id", c_is_parent_id);
                        map.put("c_total_services", c_total_services);
                        map.put("c_created", c_created);
                        map.put("s_modified", s_modified);
                        cat_list.add(map);

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

            if (status.equals("0")) {

            }
        }
    }*/
}
