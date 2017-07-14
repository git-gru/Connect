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

import com.jarrebnnee.connect.CatListGS;
import com.jarrebnnee.connect.MyApplication;
import com.jarrebnnee.connect.R;
import com.jarrebnnee.connect.SellerSetServicesActivity;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.ServiceHandler;
import com.jarrebnnee.connect.Urlcollection;
import com.jarrebnnee.connect.fragement.SetServiceForSellerFragement;

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
public class CateListAdapterTry extends RecyclerView.Adapter<CateListAdapterTry.MyViewHolder> {
    private ArrayList<HashMap<String, String>> cat_name;
    private Context c;
    CatListGS map;
    String sc_email, sc_category_id, sc_user_id, lang;
    SaveSharedPrefrence saveSharedPrefrence;
    SubCateListAdapter subCateListAdapter;
    ArrayList<CatListGS> cat_list;
    boolean flag;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox ck;
        RecyclerView lvcate;


        public MyViewHolder(View view) {
            super(view);
            ck = (CheckBox) view.findViewById(R.id.cbCate1);
            lvcate = (RecyclerView) view.findViewById(R.id.lvcate);
            //  lvcate.setNestedScrollingEnabled(false);

            Typeface custom_font = Typeface.createFromAsset(c.getAssets(), "fonts/16023_futuran_0.ttf");
            ck.setTypeface(custom_font);

           // ck.setChecked(map.getetCheckedBox());
            ck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final CatListGS map = cat_list.get(getAdapterPosition());
                    CheckBox checkBox = (CheckBox) view;
                    if (checkBox.isChecked()) {
                        map.setCheckedBox(true);
                        SellerSetServicesActivity.cate_id.add(sc_category_id);
                        Log.e("add", sc_category_id + "<<" + SellerSetServicesActivity.cate_id.size());
                    } else {
                        map.setCheckedBox(false);
                        SellerSetServicesActivity.cate_id.remove(sc_category_id);
                        Log.e("remove", sc_category_id + ">>" + SellerSetServicesActivity.cate_id.size());
                    }
                    notifyDataSetChanged();
                }
            });


        }

    }

    public CateListAdapterTry(Context context, ArrayList<CatListGS> cat_name) {
        this.c = context;
        this.cat_list = cat_name;
        saveSharedPrefrence = new SaveSharedPrefrence();
        lang = saveSharedPrefrence.getlang(c);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_checkbox, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        map = cat_list.get(position);
        String iv = map.getC_images();
        sc_email = saveSharedPrefrence.getUserEmail(c);
        sc_user_id = saveSharedPrefrence.getUserID(c);
        flag = false;



        int temp = MyApplication.getDefaultLanguage();
        if (temp == 1) {
            String item = map.getC_ar_title();
            holder.ck.setText(item);
        } else {
            String item = map.getC_title();
            holder.ck.setText(map.getC_title());
        }

        holder.ck.setChecked(map.getetCheckedBox());

        holder.ck.setTag(position);
        holder.ck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                 @Override
                                                 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                     int i = (int) holder.ck.getTag();
                                                     map = cat_list.get(i);
                                                     sc_category_id = map.getC_id();
                                                     String c_is_sub_category = map.getC_is_sub_category();
                                                     if (holder.ck.isChecked()) {
                                                         // new Subscribe(c).execute();
                                                         if (map.getetCheckedBox()) {
                                                             /*SetServiceForSellerFragement.cate_id.add(sc_category_id);
                                                             Log.e("add", sc_category_id + "<<" + SetServiceForSellerFragement.cate_id.size());*/
                                                         }

                                                         if (c_is_sub_category.equals("1")) {
                                                         //    new SubCategory(c, holder).execute();
                                                             holder.lvcate.setVisibility(View.VISIBLE);
                                                         } else {
                                                             holder.lvcate.setVisibility(View.GONE);
                                                         }
                                                    //     map.setCheckedBox(true);
                                                     } else {
                                                         if (!map.getetCheckedBox()) {
                                                            /* SetServiceForSellerFragement.cate_id.remove(sc_category_id);
                                                             Log.e("remove", sc_category_id + ">>" + SetServiceForSellerFragement.cate_id.size());*/
                                                         }

                                                         holder.lvcate.setVisibility(View.GONE);
                                                      //   map.setCheckedBox(false);
                                                     }
                                                 }
                                             }

        );

    }

    @Override
    public int getItemCount() {
        return cat_list.size();
    }

   /* class Subscribe extends AsyncTask<Void, Void, Void> {
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

    class SubCategory extends AsyncTask<Void, Void, Void> {
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
            nameValuePairs.add(new BasicNameValuePair("lang", lang));

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
                subCateListAdapter = new SubCateListAdapter(c, cat_list);
                holder.lvcate.setAdapter(subCateListAdapter);
            }
        }
    }*/
}
