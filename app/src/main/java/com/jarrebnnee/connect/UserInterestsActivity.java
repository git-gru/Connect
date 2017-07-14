package com.jarrebnnee.connect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jarrebnnee.connect.Adapter.CateListAdapterBuyer;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class UserInterestsActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView ivBack;
    TextView tvTitle;
    String lang, u_id;
    RecyclerView rvInterest;
    SaveSharedPrefrence saveSharedPrefrence;
    ArrayList<CatListGS> list;
    //SellerAddAdvAdapter advAdapter;
    CateListAdapterBuyer advAdapter;
    Button btn_submit, btn_cancel;
    ArrayList<String> cat_list;
    ArrayList<String> InterestList;
    public static ArrayList<String> cate_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interests);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        btn_submit = (Button) findViewById(R.id.btn_inte_submit);
        btn_cancel = (Button) findViewById(R.id.btn_inte_cancel);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "" + "</font>")));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        list= new ArrayList<CatListGS>();
        cat_list= new ArrayList<>();
        saveSharedPrefrence= new SaveSharedPrefrence();
        cate_id= new ArrayList<>();
        lang = saveSharedPrefrence.getlang(UserInterestsActivity.this);
        u_id = saveSharedPrefrence.getUserID(UserInterestsActivity.this);
        rvInterest = (RecyclerView) findViewById(R.id.rvInterest);
        rvInterest.setLayoutManager(new LinearLayoutManager(UserInterestsActivity.this, LinearLayoutManager.VERTICAL,false));
        advAdapter= new CateListAdapterBuyer(UserInterestsActivity.this, list);
        rvInterest.setAdapter(advAdapter);

        CallGetMyBuyerSettings();




        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category="";
                Log.e("cate_id.size", "" + cate_id.size());
                for (int i = 0; i < cate_id.size(); i++) {
                    category += cate_id.get(i);
                    if (i == (cate_id.size()-1)) {
                    } else {
                        category += ",";
                    }
                    Log.e("s_category_id", "" + category);
                }
                if (category.equals("")) {
                    Toast.makeText(UserInterestsActivity.this, getResources().getString(R.string.pservice), Toast.LENGTH_LONG).show();
                } else {
                    CallUpdateMyCate(category);
                }
             /*



                for (int i=0;i<cat_list.size();i++) {
                    category = category.concat(cat_list.get(i)+",");
                }
                if (category != null && category.length() > 0) {
                    category = category.substring(0, category.length()-1);
                }
                Log.e("finalized", category);
                CallUpdateMyCate(category);*/
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cat_list.size() > 0) {
                    cat_list.clear();
                }
                finish();
            }
        });



    }

    private void CallGetMyBuyerSettings() {

        String u_id = saveSharedPrefrence.getUserID(UserInterestsActivity.this);
        String url=Urlcollection.url+"action=getMyBuyerSettings&u_id="+u_id;
        Log.e("url", url);
        final ProgressDialog pDialog = new ProgressDialog(UserInterestsActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String message = object.getString("message");
                    Toast.makeText(UserInterestsActivity.this, message, Toast.LENGTH_SHORT).show();
                    String interested_category = object.getString("interested_category");
                    Log.e("interested_category", interested_category);
                    InterestList = new  ArrayList<String>(Arrays.asList(interested_category.split(",")));;
                    for (int i=0; i<InterestList.size();i++) {
                        Log.e("InterestList", InterestList.get(i));
                    }
                    //CallGetCatApi();
                    CallGetCategories();
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

    private void CallGetCategories() {
        String url=Urlcollection.url+"action=getMainCategoryList";
        Log.e("url", url);
        final ProgressDialog pDialog = new ProgressDialog(UserInterestsActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest request= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    String status = jsonObj.getString("status");
                    String msg = jsonObj.getString("message");
                    Log.e("msg", msg);
                    JSONArray data = jsonObj.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        String c_id = object.getString("c_id");
                        String c_images = object.getString("c_images");
                        String c_type = object.getString("c_type");
                        String c_title = object.getString("c_title");
                        String c_is_parent_id = object.getString("c_is_parent_id");
                        String c_total_services = object.getString("c_total_services");
                        String c_created = object.getString("c_created");
                        String c_ar_title = object.getString("c_ar_title");
                        String s_modified = object.getString("s_modified");
                        String c_is_sub_category = object.getString("c_is_sub_category");


                        boolean isChecked = false;
                        for (int k=0;k<InterestList.size();k++) {
                            String temp = InterestList.get(k);
                            if (temp.equals(c_id)) {
                                isChecked = true;
                            }
                        }

                        CatListGS listGS = new CatListGS(c_ar_title, c_id, c_images, c_type, c_title, c_is_parent_id, c_total_services, c_created, s_modified, c_is_sub_category, isChecked);

                        list.add(listGS);
                        advAdapter.notifyDataSetChanged();

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


    private void CallUpdateMyCate(String category) {

        String u_id = saveSharedPrefrence.getUserID(UserInterestsActivity.this);
        String url=Urlcollection.url+"action=updateMyIntrestCategory&lang="+lang+"&u_id="+u_id+"&c_id="+category;
        Log.e("url", url);
        final ProgressDialog pDialog = new ProgressDialog(UserInterestsActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest request= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    String message = object.getString("message");
                    Toast.makeText(UserInterestsActivity.this, message, Toast.LENGTH_LONG).show();
                    finish();
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


/*    void CallGetCatApi(*//*final LinearLayout layout*//*) {

        String url=Urlcollection.url+"action=getCatSubCat&lang="+lang;
        final ProgressDialog pDialog = new ProgressDialog(UserInterestsActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest request= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    Log.e("response",response);
                    JSONObject object= new JSONObject(response);
                    String status = object.getString("status");
                    String message = object.getString("message");
             //       LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) layout.getLayoutParams();
                    Toast.makeText(UserInterestsActivity.this, message, Toast.LENGTH_SHORT).show();
                    JSONArray data = object.getJSONArray("data");

                    for (int i=0; i<data.length();i++) {

                        String tag = "false";
                        JSONObject jsonObject = data.getJSONObject(i);
                        String c_id = jsonObject.getString("c_id");
                        String c_title = jsonObject.getString("c_title");
                        if (InterestList.size()>0||InterestList!=null) {

                            for (int j = 0; j < InterestList.size(); j++) {
                                if (c_id.equals(InterestList.get(j))) {
                                    tag = "true";
                                }
                            }
                        }
                        HashMap<String,String> map= new HashMap<String, String>();
                        map.put("c_id",c_id );
                        map.put("c_title",c_title );
                        map.put("tag", tag);
                       // Log.e("added", c_title);
                        list.add(map);
                        advAdapter.notifyDataSetChanged();
                       *//* params.setMargins(30, 20, 30, 0);*//*

                        JSONArray subcategory = jsonObject.getJSONArray("subcategory");
                        int kk = subcategory.length();

                        if (kk>0) {

                            for (int j=0; j<subcategory.length();j++) {
                                JSONObject object1 = subcategory.getJSONObject(j);
                                String c_id_s = object1.getString("c_id");
                                String c_title_s = object1.getString("c_title");
                                HashMap<String,String> map1= new HashMap<String, String>();
                                map1.put("c_id",c_id_s );
                                map1.put("c_title",c_title_s );
                      //          Log.e("harshad", c_title_s);
                                list.add(map1);
                                advAdapter.notifyDataSetChanged();
                            }
                        }

                        else {
                          *//*  params.setMargins(30, 20, 30, 0);
                            layout.setLayoutParams(params);*//*


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
    }*/







    class SellerAddAdvAdapter extends RecyclerView.Adapter<UserInterestsActivity.SellerAddAdvAdapter.MyViewHolder2>{


        ArrayList<HashMap<String, String>> list;
        Context context;
        HashMap<String, String> map;

        public SellerAddAdvAdapter(ArrayList<HashMap<String, String>> list, Context context) {
            this.list = list;
            this.context = context;
        }

        public class MyViewHolder2 extends RecyclerView.ViewHolder {
            TextView tv_my_inte;
            CheckBox cb_my_inte;
            LinearLayout layout;

            public MyViewHolder2(View itemView) {
                super(itemView);
                tv_my_inte = (TextView) itemView.findViewById(R.id.tv_my_inte);
                cb_my_inte = (CheckBox) itemView.findViewById(R.id.cb_my_inte);
                layout = (LinearLayout) itemView.findViewById(R.id.layout_user_interest);
                this.setIsRecyclable(false);
           //     CallGetCatApi(layout);
            }
        }



        @Override
        public UserInterestsActivity.SellerAddAdvAdapter.MyViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_my_interest, parent, false);

            return new UserInterestsActivity.SellerAddAdvAdapter.MyViewHolder2(itemView);
        }

        @Override
        public void onBindViewHolder(final UserInterestsActivity.SellerAddAdvAdapter.MyViewHolder2 holder, final int position) {
            map = list.get(position);
           /* for (int i=0; i<list.size();i++) {
                HashMap<String,String> m= list.get(i);*/
           /* for (int i = 0; i < list.size(); i++) {
                holder.cb_my_inte.setChecked(false);
            }*/
//in some cases, it will prevent unwanted situations
            holder.cb_my_inte.setOnCheckedChangeListener(null);

            //if true, your checkbox will be selected, else unselected
          //  holder.cb_my_inte.setChecked(list.get(position).isSelected());
                /*for (int j=0;j<InterestList.size();j++) {
                    String c = map.get("c_id");
                    if (c.equals(InterestList.get(j))) {
                        holder.cb_my_inte.setChecked(true);
                    }
                }*/
           // }
            Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "fonts/16023_futuran_0.ttf");
            holder.tv_my_inte.setTypeface(custom_font);

            holder.tv_my_inte.setText(map.get("c_title"));
            final String c_id =map.get("c_id");




            holder.cb_my_inte.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (holder.cb_my_inte.isChecked()) {
                        cat_list.add(c_id);
                        Log.e("added",c_id);
                    } else if(!holder.cb_my_inte.isChecked()){
                        if (cat_list.contains(c_id)) {
                            cat_list.remove(c_id);
                            Log.e("removed",c_id);
                        }
                    }

                }
            });


            String tag1 = map.get("tag");
            try {
                if (tag1.equals("true")) {
                    holder.cb_my_inte.setChecked(true);
                    if (!cat_list.contains(c_id)) {
                        cat_list.add(c_id);
                    }
                } else{
                    holder.cb_my_inte.setChecked(false);
                    if (cat_list.contains(c_id)) {
                        cat_list.remove(c_id);
                    }
                }
            } catch (Exception e) {
                Log.e("exception", "UserInterest : "+e);
            }




        }



        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}
