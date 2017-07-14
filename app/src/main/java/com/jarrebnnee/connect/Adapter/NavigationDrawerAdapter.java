package com.jarrebnnee.connect.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.jarrebnnee.connect.NavDrawerItem;
import com.jarrebnnee.connect.R;
import com.jarrebnnee.connect.SellerSubscribeActivity;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.ServiceHandler;
import com.jarrebnnee.connect.Service.TrackGPS;
import com.jarrebnnee.connect.Urlcollection;
import com.jarrebnnee.connect.UserInterestsActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by jigna on 13/10/16.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    String lang_loginaccount, lang_btnsignin, lang_edemail, lang_edpwd;
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Activity context;
    TrackGPS gps;
    Geocoder geocoder;
    List<Address> addresses;
    // GetSet getset;
    SaveSharedPrefrence sharedPreferences;
    CateListAdapter adapter;
    ArrayList<HashMap<String, String>> list;
    HashMap<String, String> map;
    MyViewHolder holder;
    String u_type;

    public NavigationDrawerAdapter(Activity context, List<NavDrawerItem> data, ArrayList<HashMap<String, String>> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.list = list;
        sharedPreferences = new SaveSharedPrefrence();
        gps = new TrackGPS(context);
        geocoder = new Geocoder(context, Locale.getDefault());

    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        NavDrawerItem current = data.get(position);
        Log.e("posi", "" + current);
        gps = new TrackGPS(context);

        u_type = sharedPreferences.getUserType(context);
        if (u_type.equals("2")) {
            holder.tv_subscribe.setVisibility(View.VISIBLE);
        } else {
            holder.tv_subscribe.setVisibility(View.GONE);
        }

        holder.tv_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SellerSubscribeActivity.class);
                context.startActivity(intent);
            }
        });


        if (position == 0) {
            if (u_type.equals("2")) {
                holder.tv_subscribe.setVisibility(View.GONE);
            }
            if (gps.canGetLocation()) {
                geocoder = new Geocoder(context, Locale.getDefault());
                double u_longitute = gps.getLongitude();
                double u_latitute = gps.getLatitude();
                try {
                    List<Address> addresses = new ArrayList<>();
                    addresses = geocoder.getFromLocation(u_latitute, u_longitute, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    if (addresses.size() > 0) {
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName();
                        holder.title.setText(city + "," + state + "," + country);
                        Log.e("latlong", "Longitude:" + u_longitute + "\nLatitude:" + u_latitute);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                //String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            } else {
                gps.showSettingsAlert();
                context.finish();
            }


        } else if (position == 1) {

            holder.iv_map.setVisibility(View.GONE);
            holder.ly_rd.setVisibility(View.VISIBLE);
            holder.tv_subscribe.setVisibility(View.GONE);
            holder.title.setText(current.getTitle());
            Log.e("hhh", "pos ; " + position + "\ntitle : " + current.getTitle());

        } else if (position == 2) {
            if (u_type.equals("2")) {
                holder.iv_map.setVisibility(View.GONE);
                holder.ly_rd.setVisibility(View.GONE);
                holder.ly_cb.setVisibility(View.GONE);
                holder.tv_subscribe.setVisibility(View.VISIBLE);
                holder.title.setVisibility(View.GONE);
                holder.title.setText(current.getTitle());
            } else {
                holder.iv_map.setVisibility(View.GONE);
                holder.ly_rd.setVisibility(View.GONE);
                holder.ly_cb.setVisibility(View.VISIBLE);
                LinearLayoutManager llm = new LinearLayoutManager(context);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                holder.lv.setLayoutManager(llm);

                adapter = new CateListAdapter(context, list);
                holder.lv.setAdapter(adapter);
                holder.title.setVisibility(View.VISIBLE);
                holder.title.setText(current.getTitle());
                holder.title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, UserInterestsActivity.class);
                        context.startActivity(intent);
                    }
                });
            }
        } else if (position == 3) {
            if (u_type.equals("2")) {
                holder.iv_map.setVisibility(View.GONE);
                holder.ly_rd.setVisibility(View.GONE);
                holder.ly_cb.setVisibility(View.GONE);
                holder.tv_subscribe.setVisibility(View.GONE);
                holder.title.setVisibility(View.VISIBLE);
                holder.title.setText(current.getTitle());
            } else {
                holder.iv_map.setVisibility(View.GONE);
                holder.ly_rd.setVisibility(View.GONE);
                holder.ly_cb.setVisibility(View.GONE);
                holder.title.setVisibility(View.GONE);
                holder.title.setText(current.getTitle());
                Log.e("hhh", "pos ; " + position + "\ntitle : " + current.getTitle());
            }
        } else if (position == 4) {
            holder.iv_map.setVisibility(View.GONE);
            holder.ly_rd.setVisibility(View.GONE);
            holder.tv_subscribe.setVisibility(View.GONE);
            holder.ly_cb.setVisibility(View.GONE);
            holder.title.setText(current.getTitle());
            Log.e("hhh", "pos ; " + position + "\ntitle : " + current.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView iv_map;
        LinearLayout ly_rd, ly_cb;
        RadioButton rbMale, rbFemale;
        //CheckBox cbCate1, cbCate2, cbCate3, cbCate4, cbCate5;
        RecyclerView lv;
        TextView tv_subscribe;


        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title1);

           /* getset = GetSet.getInstance();
            sharedPreferences = new SaveSharedPrefrence();*/
            iv_map = (ImageView) itemView.findViewById(R.id.iv_map);
            //title = (TextView) itemView.findViewById(R.id.title);
            ly_cb = (LinearLayout) itemView.findViewById(R.id.ly_cb);
            ly_rd = (LinearLayout) itemView.findViewById(R.id.ly_rd);
            tv_subscribe = (TextView) itemView.findViewById(R.id.tv_subscribe);
            rbMale = (RadioButton) itemView.findViewById(R.id.rbMale);
            rbFemale = (RadioButton) itemView.findViewById(R.id.rbFemale);
            lv = (RecyclerView) itemView.findViewById(R.id.lv);
          /*  cbCate1 = (CheckBox) itemView.findViewById(R.id.cbCate1);
            cbCate2 = (CheckBox) itemView.findViewById(R.id.cbCate2);
            cbCate3 = (CheckBox) itemView.findViewById(R.id.cbCate3);
            cbCate4 = (CheckBox) itemView.findViewById(R.id.cbCate4);
            cbCate5 = (CheckBox) itemView.findViewById(R.id.cbCate5);*/

            Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/16023_futuran_0.ttf");
            title.setTypeface(custom_font);
            tv_subscribe.setTypeface(custom_font);
            rbMale.setTypeface(custom_font);
            rbFemale.setTypeface(custom_font);
            /*cbCate1.setTypeface(custom_font);
            cbCate2.setTypeface(custom_font);
            cbCate3.setTypeface(custom_font);
            cbCate4.setTypeface(custom_font);
            cbCate5.setTypeface(custom_font);*/


            String u_gender = sharedPreferences.getUserGender(context);
            if (u_gender.equals("F")) {
                rbFemale.setChecked(true);
                rbMale.setChecked(false);
            } else {
                rbMale.setChecked(true);
                rbFemale.setChecked(false);
            }

        }
    }


}
