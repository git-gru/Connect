package com.jarrebnnee.connect.fragement;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jarrebnnee.connect.Adapter.ProjectListAdapter;
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
 * Created by Vardhman Infonet 4 on 22-Feb-17.
 */

public class Projects_Fragement extends Fragment {

    RecyclerView lvProject;
    String[] cate_name={"Service provider name","Service provider name","Service provider name"};
    String[] cate_status={"Pending","Completed","Pending"};
    ProjectListAdapter projectListAdapter;
    int[] cat_icon={R.drawable.icon_repair,R.drawable.icon_health,R.drawable.icon_lesson,R.drawable.icon_event,R.drawable.icon_business,R.drawable.icon_personal};
    ArrayList<HashMap<String,String>> plist;
    HashMap<String,String> map;
    String user_id,js_status1,u_type;
    TextView tvno;
    SaveSharedPrefrence saveSharedPrefrence;
    Button btnRun,btnComplted;
    String lang;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_project, container, false);
        lvProject = (RecyclerView)rootView.findViewById(R.id.lvProject);
        tvno = (TextView)rootView.findViewById(R.id.tvno);
        btnRun = (Button)rootView.findViewById(R.id.btnRun);
        btnComplted = (Button)rootView.findViewById(R.id.btnComplted);

        saveSharedPrefrence = new SaveSharedPrefrence();
        user_id = saveSharedPrefrence.getUserID(getActivity());
        lang = saveSharedPrefrence.getlang(getActivity());
        js_status1 = "2";
        u_type = saveSharedPrefrence.getUserType(getActivity());

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/16023_futuran_0.ttf");
        tvno.setTypeface(custom_font);
        btnComplted.setTypeface(custom_font);
        btnRun.setTypeface(custom_font);

        btnComplted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnComplted.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnRun.setBackgroundColor(getResources().getColor(R.color.blackk));
                js_status1 = "1";
                if (u_type.equals("2")){
                    new SellerProjectList(getActivity()).execute();
                }else {
                    new ProjectList(getActivity()).execute();
                }

            }
        });
        btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnComplted.setBackgroundColor(getResources().getColor(R.color.blackk));
                btnRun.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                js_status1 = "2";
                if (u_type.equals("2")){
                    new SellerProjectList(getActivity()).execute();
                }else {
                    new ProjectList(getActivity()).execute();
                }
            }
        });
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//we are connected to a network
            //  url=URLCollection.LoginUrl+"&u_email="+email+"&u_password="+password+"&device_type=0&device_token="+regId;
            //   SignInUrlCall(url);

            if (u_type.equals("2")){
                new SellerProjectList(getActivity()).execute();
            }else {
                new ProjectList(getActivity()).execute();
            }

        } else {
            Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
        }

        return rootView;
    }
    class ProjectList extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg,js_id,js_user_id,js_category_name,js_category_id,js_title,js_appointment_date,js_price,js_description,js_status,js_radius,js_created,js_modified,js_image,c_id,
                totalRecord,c_images,c_type,c_is_parent_id,c_total_services;

        ProjectList(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getResources().getString(R.string.pdialog));
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            plist = new ArrayList<HashMap<String, String>>();


            String uri =  Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action","getMyPostedProject"));
            nameValuePairs.add(new BasicNameValuePair("u_id",user_id));
            nameValuePairs.add(new BasicNameValuePair("js_status",js_status1));//1-completed,2-other
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
                    JSONArray data = jsonObj.getJSONArray("data");
                    for (int i = 0;i<data.length();i++) {
                        JSONObject obj = data.getJSONObject(i);
                        js_id = obj.getString("js_id");
                        js_user_id = obj.getString("js_user_id");
                        js_category_id = obj.getString("js_category_id");
                        js_title = obj.getString("js_title");
                        js_description = obj.getString("js_description");
                        js_price = obj.getString("js_price");
                        js_appointment_date = obj.getString("js_appointment_date");
                        js_image = obj.getString("js_image");
                        js_radius =obj.getString("js_radius");
                        js_status =obj.getString("js_status");
                        js_created = obj.getString("js_created");
                        js_modified = obj.getString("js_modified");

                        c_id = obj.getString("c_id");
                        c_images = obj.getString("c_images");
                        c_type = obj.getString("c_type");
                        js_category_name = obj.getString("c_title");
                        c_is_parent_id = obj.getString("c_is_parent_id");
                        c_total_services = obj.getString("c_total_services");

                        map = new HashMap<String,String>();
                        map.put("js_id",js_id);
                        map.put("js_user_id",js_user_id);
                        map.put("js_category_id",js_category_id);
                        map.put("js_category_name",js_category_name);
                        map.put("js_title",js_title);
                        map.put("js_description",js_description);
                        map.put("js_price",js_price);
                        map.put("js_appointment_date",js_appointment_date);
                        map.put("js_image",js_image);
                        map.put("js_radius",js_radius);
                        map.put("js_status",js_status);
                        map.put("js_created",js_created);
                        map.put("js_modified",js_modified);

                        map.put("c_id",c_id);
                        map.put("c_images",c_images);
                        map.put("c_type",c_type);
                        map.put("c_is_parent_id",c_is_parent_id);
                        map.put("c_total_services",c_total_services);

                        plist.add(map);

                    }
                    totalRecord = jsonObj.getString("totalRecord");


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
                tvno.setVisibility(View.GONE);
                lvProject.setVisibility(View.VISIBLE);
                projectListAdapter = new ProjectListAdapter(getActivity(),plist);
                lvProject.setAdapter(projectListAdapter);
            } else {
                tvno.setVisibility(View.VISIBLE);
                lvProject.setVisibility(View.GONE);
            }
        }
    }
    class SellerProjectList extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg,js_id,js_user_id,js_category_name,js_category_id,js_title,js_appointment_date,js_price,js_description,js_status,js_radius,js_created,js_modified,js_image,c_id,
                totalRecord,c_images,c_type,c_is_parent_id,c_total_services,jsr_id,jsr_job_id,jsr_posted_user_id,jsr_requested_user_id,
                jsr_status,jsr_comment_text,jsr_rating,jsr_apply_text,jsr_apply_price,jsr_created,jsr_modified;

        SellerProjectList(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getResources().getString(R.string.pdialog));
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            plist = new ArrayList<HashMap<String, String>>();


            String uri =  Urlcollection.url;//"http://cp3767.veba.co/~shubantech/Ebay_clone/ebay_clone_api/?";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("action","getMyProjectSeller"));
            nameValuePairs.add(new BasicNameValuePair("u_id",user_id));
            nameValuePairs.add(new BasicNameValuePair("js_status",js_status1));//1-completed,2-other
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
                    JSONArray data = jsonObj.getJSONArray("data");
                    for (int i = 0;i<data.length();i++) {
                        JSONObject obj = data.getJSONObject(i);
                        jsr_id = obj.getString("jsr_id");
                        jsr_job_id = obj.getString("jsr_job_id");
                        jsr_posted_user_id = obj.getString("jsr_posted_user_id");
                        jsr_requested_user_id = obj.getString("jsr_requested_user_id");
                        jsr_status = obj.getString("jsr_status");
                        jsr_comment_text = obj.getString("jsr_comment_text");
                        jsr_rating = obj.getString("jsr_rating");
                        jsr_apply_text = obj.getString("jsr_apply_text");
                        jsr_apply_price = obj.getString("jsr_apply_price");
                        jsr_created = obj.getString("jsr_created");
                        jsr_modified = obj.getString("jsr_modified");

                        js_id = obj.getString("js_id");
                        js_user_id = obj.getString("js_user_id");
                        js_category_id = obj.getString("js_category_id");
                        js_title = obj.getString("js_title");
                        js_description = obj.getString("js_description");
                        js_price = obj.getString("js_price");
                        js_appointment_date = obj.getString("js_appointment_date");
                        js_image = obj.getString("js_image");
                        js_radius =obj.getString("js_radius");
                        js_status =obj.getString("js_status");
                        js_created = obj.getString("js_created");
                        js_modified = obj.getString("js_modified");

                        c_id = obj.getString("c_id");
                        c_images = obj.getString("c_images");
                        c_type = obj.getString("c_type");
                        js_category_name = obj.getString("c_title");
                        c_is_parent_id = obj.getString("c_is_parent_id");
                        c_total_services = obj.getString("c_total_services");

                        map = new HashMap<String,String>();
                        map.put("js_id",js_id);
                        map.put("js_user_id",js_user_id);
                        map.put("js_category_id",js_category_id);
                        map.put("js_category_name",js_category_name);
                        map.put("js_title",js_title);
                        map.put("js_description",js_description);
                        map.put("js_price",js_price);
                        map.put("js_appointment_date",js_appointment_date);
                        map.put("js_image",js_image);
                        map.put("js_radius",js_radius);
                        map.put("js_status",js_status);
                        map.put("js_created",js_created);
                        map.put("js_modified",js_modified);

                        map.put("c_id",c_id);
                        map.put("c_images",c_images);
                        map.put("c_type",c_type);
                        map.put("c_is_parent_id",c_is_parent_id);
                        map.put("c_total_services",c_total_services);

                        plist.add(map);

                    }
                    totalRecord = jsonObj.getString("totalRecord");


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
                tvno.setVisibility(View.GONE);
                lvProject.setVisibility(View.VISIBLE);
                projectListAdapter = new ProjectListAdapter(getActivity(),plist);
                lvProject.setAdapter(projectListAdapter);
                projectListAdapter.notifyDataSetChanged();
            } else {
                tvno.setVisibility(View.VISIBLE);
                lvProject.setVisibility(View.GONE);
            }
        }
    }

}
