package com.jarrebnnee.connect.fragement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jarrebnnee.connect.Adapter.MyAccountListAdapter;
import com.jarrebnnee.connect.FullImageActivity;
import com.jarrebnnee.connect.JobDetailNotificationActivity;
import com.jarrebnnee.connect.R;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.squareup.picasso.Picasso;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Vardhman Infonet 4 on 22-Feb-17.
 */

public class MyAccountFragement extends Fragment {

    RecyclerView lvAccount;
    TextView tv_welusername;
    MyAccountListAdapter myAccountListAdapter;
    private String[] name;
    SaveSharedPrefrence sharedPreferences;
    String u_first_name,u_last_name,u_image,u_is_notification_sound,u_id,u_type;
    ImageView iv1;
    String lang;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_myaccount, container, false);
        sharedPreferences = new SaveSharedPrefrence();

        u_first_name = sharedPreferences.getUserFName(getApplicationContext());
        u_image = sharedPreferences.getUserImage(getApplicationContext());
        u_last_name = sharedPreferences.getUserLName(getApplicationContext());
        u_is_notification_sound = sharedPreferences.getu_is_notification_sound(getApplicationContext());
        u_id = sharedPreferences.getUserID(getApplicationContext());
        u_type=sharedPreferences.getUserType(getApplicationContext());
        lang = sharedPreferences.getlang(getApplicationContext());

        if (u_type.equals("1")) {
            name= new String[]{getString(R.string.my_project), getString(R.string.notification), getString(R.string.change_pwd), getString(R.string.editp), getString(R.string.userint),getString(R.string.clange), getString(R.string.action_settings), getString(R.string.logout)};
        } else if (u_type.equals("2")) {
            name= new String[]{getString(R.string.my_project), getString(R.string.notification), getString(R.string.change_pwd), getString(R.string.editp),getString(R.string.clange), getString(R.string.subs_new),getString(R.string.subs_man), getString(R.string.manageserv), getString(R.string.logout)};
        }

        lvAccount = (RecyclerView)rootView.findViewById(R.id.lvAccount);
        tv_welusername = (TextView)rootView.findViewById(R.id.tv_welusername);
        iv1 = (ImageView)rootView.findViewById(R.id.iv1);

        tv_welusername.setText(getResources().getString(R.string.welcome)+" "+u_first_name.toString()+" "+u_last_name.toString());
        Picasso.with(getApplicationContext()).load(u_image).into(iv1);
        myAccountListAdapter = new MyAccountListAdapter(getActivity(),name);
        lvAccount.setAdapter(myAccountListAdapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        u_first_name = sharedPreferences.getUserFName(getApplicationContext());
        u_last_name = sharedPreferences.getUserLName(getApplicationContext());
        u_image = sharedPreferences.getUserImage(getApplicationContext());
        u_is_notification_sound = sharedPreferences.getu_is_notification_sound(getApplicationContext());
        tv_welusername.setText(getResources().getString(R.string.welcome)+" "+u_first_name.toString()+" "+u_last_name.toString());
        Picasso.with(getApplicationContext()).load(u_image).into(iv1);
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                       /* iv1.setDrawingCacheEnabled(true);
                        iv1.buildDrawingCache();
                        Bitmap bitmap = Bitmap.createBitmap(iv1.getDrawingCache());
                        *//*iv1.buildDrawingCache();
                        Bitmap bitmap=iv1.getDrawingCache();*/
                Intent in=new Intent(getActivity(),FullImageActivity.class);
                in.putExtra("Image",u_image);
                startActivity(in);
            }
        });
        myAccountListAdapter = new MyAccountListAdapter(getActivity(),name);
        lvAccount.setAdapter(myAccountListAdapter);
        myAccountListAdapter.notifyDataSetChanged();
    }
}
