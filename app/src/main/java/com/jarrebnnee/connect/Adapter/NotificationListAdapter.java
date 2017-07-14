package com.jarrebnnee.connect.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jarrebnnee.connect.DisplayIncomingAdvertise;
import com.jarrebnnee.connect.JobDetailNotificationActivity;
import com.jarrebnnee.connect.ProjectDetailActivity;
import com.jarrebnnee.connect.R;
import com.jarrebnnee.connect.SellerDetailNotificationActivity;
import com.jarrebnnee.connect.SellerJobApplyActivity;
import com.jarrebnnee.connect.Service.GetSet;
import com.jarrebnnee.connect.WriteReviewFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jigna on 13/10/16.
 */
public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.MyViewHolder>{
    private ArrayList<HashMap<String,String>> list;
    private Activity c;
    WriteReviewFragment reviewFragment;
    Bundle mBundle;
    GetSet getSet;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtView,tvdes;
        LinearLayout layout;

        public MyViewHolder(View view) {
            super(view);
            txtView = (TextView) view.findViewById(R.id.tvSName);
            tvdes = (TextView) view.findViewById(R.id.tvdes);
            layout = (LinearLayout)view.findViewById(R.id.noti_layout);

            Typeface custom_font1 = Typeface.createFromAsset(c.getAssets(),  "fonts/16023_futuran_0.ttf");
            tvdes.setTypeface(custom_font1);

            Typeface custom_fontbold = Typeface.createFromAsset(c.getAssets(),  "fonts/tt0144m.ttf");
            txtView.setTypeface(custom_fontbold);

        }

    }


    public NotificationListAdapter(Activity context, ArrayList<HashMap<String,String>> list) {
        this.c = context;
        this.list = list;
        getSet = GetSet.getInstance();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_notify_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        HashMap<String,String> resultp = list.get(position);

        holder.txtView.setText(resultp.get("cn_title"));
        holder.tvdes.setText(resultp.get("cn_description"));

        holder.layout.setTag(position);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = (int) v.getTag();
                HashMap<String,String> resultp = list.get(i);

                String cn_noti_type_id = resultp.get("cn_noti_type_id");
                String cn_noti_type = resultp.get("cn_noti_type");

                getSet.setjs_id(cn_noti_type_id);
                getSet.setc_id(cn_noti_type_id);

                if (cn_noti_type.equals("6")) {
                    Intent resultIntent = new Intent(c, DisplayIncomingAdvertise.class);
                    resultIntent.putExtra("type_id", cn_noti_type_id);
                    //  resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    c.startActivity(resultIntent);
                } else {

                    // if (cn_noti_type.equals("0") || cn_noti_type.equals("2")) {
                    Intent resultIntent = new Intent(c, JobDetailNotificationActivity.class);
                    c.startActivity(resultIntent);
                }
                  //  resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                /*}else{
                    Intent resultIntent = new Intent(c, SellerDetailNotificationActivity.class);
                    resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    c.startActivity(resultIntent);
                }*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
