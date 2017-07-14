package com.jarrebnnee.connect.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jarrebnnee.connect.R;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.ServiceHandler;
import com.jarrebnnee.connect.Urlcollection;

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
public class SellerViewSubListAdapter extends RecyclerView.Adapter<SellerViewSubListAdapter.MyViewHolder> {
    private ArrayList<HashMap<String, String>> cat_name;
    private Context c;
    HashMap<String, String> map;
    String sc_email, sc_category_id, sc_user_id;
    SaveSharedPrefrence saveSharedPrefrence;
    SubCateListAdapter subCateListAdapter;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ck,tvserv,tvContry,tvCity;
        RecyclerView lvcate;

        public MyViewHolder(View view) {
            super(view);
            ck = (TextView) view.findViewById(R.id.tvCate1);
            lvcate = (RecyclerView) view.findViewById(R.id.lvcate);
            tvserv = (TextView) view.findViewById(R.id.tvserv);
            tvContry = (TextView) view.findViewById(R.id.tvContry);
            tvCity = (TextView) view.findViewById(R.id.tvCity);
            lvcate.setNestedScrollingEnabled(false);

            Typeface custom_font = Typeface.createFromAsset(c.getAssets(), "fonts/16023_futuran_0.ttf");
            ck.setTypeface(custom_font);
            tvContry.setTypeface(custom_font);
            tvCity.setTypeface(custom_font);

            Typeface custom_fontbold = Typeface.createFromAsset(c.getAssets(),  "fonts/tt0144m.ttf");
            tvserv.setTypeface(custom_fontbold);

        }

    }

    public SellerViewSubListAdapter(Context context, ArrayList<HashMap<String, String>> cat_name) {
        this.c = context;
        this.cat_name = cat_name;
        saveSharedPrefrence = new SaveSharedPrefrence();
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

        holder.ck.setText(map.get("c_title"));
        holder.ck.setTag(position);

        holder.tvContry.setText("Country: "+map.get("country_name"));
        holder.tvCity.setText("City: "+map.get("city_name"));

    }

    @Override
    public int getItemCount() {
        return cat_name.size();
    }

    class Subscribe extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;

        String status, msg, c_id, c_images, c_type, c_title, c_is_parent_id, c_total_services, c_created, s_modified;// u_id, u_first_name, u_last_name, u_email1, u_password1, u_address, u_latitute, u_longitute, u_city, u_phone, u_postcode, u_country, u_status, u_type, u_is_notification_sound, u_created, u_modified;

        Subscribe(Context context) {

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
            nameValuePairs.add(new BasicNameValuePair("action", "subscribe_category"));
            nameValuePairs.add(new BasicNameValuePair("sc_email", sc_email));
            nameValuePairs.add(new BasicNameValuePair("sc_category_id", sc_category_id));
            nameValuePairs.add(new BasicNameValuePair("sc_user_id", sc_user_id));


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

            // pDialog.dismiss();
        }

    }
}
