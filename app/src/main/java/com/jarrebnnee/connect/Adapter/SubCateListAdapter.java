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
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.jarrebnnee.connect.R;
import com.jarrebnnee.connect.SellerServiceSetActivity;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.ServiceHandler;
import com.jarrebnnee.connect.Urlcollection;
import com.jarrebnnee.connect.fragement.SetServiceForSellerFragement;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Vardhman Infonet 4 on 03-Mar-17.
 */
public class SubCateListAdapter extends RecyclerView.Adapter<SubCateListAdapter.MyViewHolder>{
    private ArrayList<HashMap<String,String>> cat_name;
    private Context c;
    HashMap<String,String> map;
    String sc_email,sc_category_id,sc_user_id;
    SaveSharedPrefrence saveSharedPrefrence;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox ck;
        RecyclerView lvcate;

        public MyViewHolder(View view) {
            super(view);
            ck = (CheckBox)view.findViewById(R.id.cbCate1);
            lvcate = (RecyclerView)view.findViewById(R.id.lvcate);

            Typeface custom_font = Typeface.createFromAsset(c.getAssets(),  "fonts/16023_futuran_0.ttf");
            ck.setTypeface(custom_font);

        }

    }

    public SubCateListAdapter(Context context, ArrayList<HashMap<String,String>> cat_name) {
        this.c = context;
        this.cat_name = cat_name;
        saveSharedPrefrence = new SaveSharedPrefrence();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_checkbox, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        map = cat_name.get(position);
        String iv = map.get("c_images");
        sc_email = saveSharedPrefrence.getUserEmail(c);
        sc_user_id = saveSharedPrefrence.getUserID(c);

        holder.ck.setText(map.get("c_title"));
        holder.ck.setTag(position);
        holder.ck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int i = (int) holder.ck.getTag();
                map = cat_name.get(i);
                sc_category_id = map.get("c_id");
                if (holder.ck.isChecked()){
                    SetServiceForSellerFragement.cate_id.add(sc_category_id);
                    Log.e("add",sc_category_id +"<<"+ SetServiceForSellerFragement.cate_id.size());
                }else{
                    SetServiceForSellerFragement.cate_id.remove(sc_category_id);
                    Log.e("remove",sc_category_id +">>"+SetServiceForSellerFragement.cate_id.size());
                  //  holder.lvcate.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return cat_name.size();
    }
   /* class Subscribe  extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg,c_id,c_images,c_type,c_title,c_is_parent_id,c_total_services,c_created,s_modified;// u_id, u_first_name, u_last_name, u_email1, u_password1, u_address, u_latitute, u_longitute, u_city, u_phone, u_postcode, u_country, u_status, u_type, u_is_notification_sound, u_created, u_modified;

        Subscribe (Context context) {

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
    }*/
}
