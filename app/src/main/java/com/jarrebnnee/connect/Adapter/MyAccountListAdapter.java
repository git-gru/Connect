package com.jarrebnnee.connect.Adapter;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jarrebnnee.connect.HomeActivity;
import com.jarrebnnee.connect.LocationUpdaterService;
import com.jarrebnnee.connect.MainActivity;
import com.jarrebnnee.connect.NotiSettingsActivity;
import com.jarrebnnee.connect.R;
import com.jarrebnnee.connect.SellerSetServicesActivity;
import com.jarrebnnee.connect.SellerSubscribeActivity;
import com.jarrebnnee.connect.SellerSubscribedPlans;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.ServiceHandler;
import com.jarrebnnee.connect.Urlcollection;
import com.jarrebnnee.connect.ChagePasswordFragement;
import com.jarrebnnee.connect.EditProfileActivity;
import com.jarrebnnee.connect.UserInterestsActivity;
import com.jarrebnnee.connect.fragement.Projects_Fragement;
import com.facebook.login.LoginManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by jigna on 13/10/16.
 */
public class MyAccountListAdapter extends RecyclerView.Adapter<MyAccountListAdapter.MyViewHolder>{
    private String[] cat_name;
    private Activity c;
    Projects_Fragement projects_fragement;
    ChagePasswordFragement chagePasswordFragement;
    Bundle mBundle;
    SaveSharedPrefrence sharedPreferences;
    String  u_is_notification_sound;
    String u_id,lang1,u_type;
    ArrayList<String> lang;
    ArrayAdapter<String> arrad;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtView;
        public ImageView imgView;
        public MyViewHolder(View view) {
            super(view);
            lang = new ArrayList<String>();
            txtView = (TextView) view.findViewById(R.id.textView);
            imgView = (ImageView) view.findViewById(R.id.img);

            Typeface custom_font1 = Typeface.createFromAsset(c.getAssets(),  "fonts/tt0142m.ttf");
            txtView.setTypeface(custom_font1);

            lang.add("English");
            lang.add("Arabic");

            arrad = new ArrayAdapter<String>(c, android.R.layout.simple_list_item_1, lang);

        }

    }


    public MyAccountListAdapter(Activity context, String[] cat_name) {
        this.c = context;
        this.cat_name = cat_name;
        sharedPreferences = new SaveSharedPrefrence();
        u_id = sharedPreferences.getUserID(c);
        u_type = sharedPreferences.getUserType(c);
        lang1 = sharedPreferences.getlang(c);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_aclist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Log.e("MyAccountListAdapter", "u_type="+u_type);
        holder.txtView.setText(cat_name[position]);
        holder.imgView.setTag(position);
        u_is_notification_sound = sharedPreferences.getu_is_notification_sound(c);
        if (position==1) {
            if (u_is_notification_sound.equals("0")) {
                holder.imgView.setImageResource(R.drawable.cross_icon);
            } else {
                holder.imgView.setImageResource(R.drawable.icon_right);
            }
        }
       // holder.tvStatus.setText(sts[position]);
        //holder.imgView.setImageResource(icons[position]);

        holder.txtView.setTag(position);
        holder.txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = (int) v.getTag();
                String item = cat_name[i];
                if (i == 0){
                    projects_fragement = new Projects_Fragement();
                    switchContent(projects_fragement,item);
                    HomeActivity.layout_home.setBackgroundResource(R.color.black);
                    HomeActivity.layout_project.setBackgroundResource(R.color.colorPrimary);
                    HomeActivity.layout_search.setBackgroundResource(R.color.black);
                    HomeActivity.layout_book.setBackgroundResource(R.color.black);
                    HomeActivity.layout_account.setBackgroundResource(R.color.black);
                }else if (i == 1){
                    //CustomNotificationDialog(u_is_notification_sound);
                    Button dialogButtonno,dialogButtonyes;
                    TextView tv;
                    final Dialog dialog = new Dialog(c);
                    dialog.setContentView(R.layout.custom_layout_notifi);
                    dialog.getWindow().setTitleColor(c.getResources().getColor(R.color.colorPrimary));
                    dialog.setTitle(c.getResources().getString(R.string.notification));

                    dialogButtonno  = (Button) dialog.findViewById(R.id.btnno);
                    dialogButtonyes = (Button) dialog.findViewById(R.id.btnyes);
                    tv = (TextView)dialog.findViewById(R.id.tv);
                    if (u_is_notification_sound.equals("0")) {
                        tv.setText(c.getResources().getString(R.string.onnotification));
                    } else {
                        tv.setText(c.getResources().getString(R.string.offnotification));
                    }
                    dialog.show();

                    dialogButtonyes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Close dialog
                            if (u_is_notification_sound.equals("0")) {
                                u_is_notification_sound = "1";
                            } else {
                                u_is_notification_sound = "0";
                            }
                            new Notification(c).execute();
                            if (u_is_notification_sound.equals("0")) {
                                holder.imgView.setImageResource(R.drawable.cross_icon);
                            } else {
                                holder.imgView.setImageResource(R.drawable.icon_right);
                            }
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
                     /*   AlertDialog.Builder builder1 = new AlertDialog.Builder(c);
                        builder1.setTitle("Notification");

                        if (u_is_notification_sound.equals("0")) {
                            builder1.setMessage("Are you sure you want to ON Notification?");
                        } else {
                            builder1.setMessage("Are you sure you want to OFF Notification?");
                        }
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (u_is_notification_sound.equals("0")) {
                                            u_is_notification_sound = "1";
                                        } else {
                                            u_is_notification_sound = "0";
                                        }
                                        new Notification(c).execute();
                                        if (u_is_notification_sound.equals("0")) {
                                            holder.imgView.setImageResource(R.drawable.cross_icon);
                                        } else {
                                            holder.imgView.setImageResource(R.drawable.icon_right);
                                        }
                                        dialog.cancel();
                                    }
                                });

                        builder1.setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        builder1.show();*/

                }else if (i == 2){
                    Intent i1 = new Intent(c,ChagePasswordFragement.class);
                    c.startActivity(i1);
                }else if (i == 3){
                    Intent intent = new Intent(c,EditProfileActivity.class);
                    c.startActivity(intent);

                }else if (i == 4){
                    if (u_type.equals("1")) {
                        Intent intent = new Intent(c, UserInterestsActivity.class);
                        c.startActivity(intent);
                    } else if (u_type.equals("2")) {
                        ChangeLangugeDialog();
                    }

                } else if (i == 5) {
                    if (u_type.equals("1")) {
                        ChangeLangugeDialog();
                    } else if (u_type.equals("2")) {
                        Intent intent = new Intent(c, SellerSubscribeActivity.class);
                        c.startActivity(intent);
                    }
                } else if (i == 6) {
                    if (u_type.equals("1")) {
                        Intent intent = new Intent(c, NotiSettingsActivity.class);
                        c.startActivity(intent);
                    }else if (u_type.equals("2")) {
                        Intent intent = new Intent(c, SellerSubscribedPlans.class);
                        c.startActivity(intent);
                    }

                } else if (i == 7) {
                    if (u_type.equals("1")) {
                        CustomDialog();
                    }
                    if (u_type.equals("2")) {
                        Intent intent = new Intent(c, SellerSetServicesActivity.class);
                        c.startActivity(intent);
                    }

                } else if (i == 8) {
                    if (u_type.equals("2")) {
                        CustomDialog();
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return cat_name.length;
    }

    public void switchContent(Fragment fragment,String mItemSelected) {
        if (c == null)
            return;
        if (c instanceof HomeActivity) {
            HomeActivity mainActivity = (HomeActivity) c;
            Fragment frag = fragment;
            mainActivity.switchContent(fragment,mItemSelected);
        }

    }

    private void CustomNotificationDialog(final String u_is_notification) {

        Button dialogButtonno,dialogButtonyes;
        TextView tv;
        final Dialog dialog = new Dialog(c);
        dialog.setContentView(R.layout.custom_layout_notifi);
        dialog.getWindow().setTitleColor(c.getResources().getColor(R.color.colorPrimary));
        dialog.setTitle("Notification");

        dialogButtonno  = (Button) dialog.findViewById(R.id.btnno);
        dialogButtonyes = (Button) dialog.findViewById(R.id.btnyes);
        tv = (TextView)dialog.findViewById(R.id.tv);
        if (u_is_notification.equals("0")) {
            tv.setText("Are you sure you want to ON Notification?");
        } else {
            tv.setText("Are you sure you want to OFF Notification?");
        }
        dialog.show();

        dialogButtonyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                if (u_is_notification.equals("0")) {
                    u_is_notification_sound = "1";
                } else {
                    u_is_notification_sound = "0";
                }
                new Notification(c).execute();

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
    public void  ChangeLangugeDialog(){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(c);
            dialogBuilder.setAdapter(arrad, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String s = lang.get(which);
                    Log.e("language",s);
                    if (s.equals("English")) {
                        setLocale("en");
                        Urlcollection.lang = "english";
                        sharedPreferences.savelang(c,"english");
                    }else if (s.equals("Arabic")) {
                        setLocale("ar");
                        Urlcollection.lang = "arabic";
                        sharedPreferences.savelang(c,"arabic");
                    }
                    dialog.dismiss();
                }
            });

            dialogBuilder.setTitle(c.getResources().getString(R.string.clange));
            AlertDialog b = dialogBuilder.create();
            b.show();

    }
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = c.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(c, HomeActivity.class);
        c.startActivity(refresh);
        c.finish();
    }
    private void CustomDialog() {

        Button dialogButtonno,dialogButtonyes;
        final Dialog dialog = new Dialog(c);
        dialog.setContentView(R.layout.custom_layout_logout);
        dialog.getWindow().setTitleColor(c.getResources().getColor(R.color.colorPrimary));
        dialog.setTitle(c.getResources().getString(R.string.logout));

        dialogButtonno  = (Button) dialog.findViewById(R.id.btnno);
        dialogButtonyes = (Button) dialog.findViewById(R.id.btnyes);

        dialog.show();

        dialogButtonyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                sharedPreferences.saveUserID(c, "");

                String id = sharedPreferences.getUserID(c);
               /* SharedPreferences settings = context.getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
                settings.edit().remove("KeyName").commit();*/
                LoginManager.getInstance().logOut();

                Intent i = new Intent(c , MainActivity.class);
                c.startActivity(i);
                if (isMyServiceRunning(LocationUpdaterService.class)) {
                    Log.e("service", "stopping");
                    Intent intent1 = new Intent(c, LocationUpdaterService.class);
                    c.stopService(intent1);
                }

                Toast.makeText(c, c.getResources().getString(R.string.logoutsucess), Toast.LENGTH_SHORT).show();
                c.finish();
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

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    class Notification extends AsyncTask<Void, Void, Void> {
        boolean st = false;
        String result;
        Context context;
        ProgressDialog pDialog;
        String status, msg,u_is_notification_sound1;

        Notification(Context context) {

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
            nameValuePairs.add(new BasicNameValuePair("action","setNotificationFlag"));
            nameValuePairs.add(new BasicNameValuePair("u_is_notification_sound",u_is_notification_sound));
            nameValuePairs.add(new BasicNameValuePair("u_id",u_id));//1-completed,2-other
            nameValuePairs.add(new BasicNameValuePair("lang",lang1));


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
                    u_is_notification_sound1 = jsonObj.getString("u_is_notification_sound");
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

//            pDialog.dismiss();
            if (status.equals("0")) {
                if (u_is_notification_sound1.equals("1")){
                    Toast.makeText(c,c.getResources().getString(R.string.onsucess),Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(c,c.getResources().getString(R.string.offsucess),Toast.LENGTH_SHORT).show();
                }
                sharedPreferences.saveu_is_notification_sound(c,u_is_notification_sound1);

            }
        }
    }

}
