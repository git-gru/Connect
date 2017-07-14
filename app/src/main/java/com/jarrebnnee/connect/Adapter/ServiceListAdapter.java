package com.jarrebnnee.connect.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jarrebnnee.connect.FullImageActivity;
import com.jarrebnnee.connect.R;
import com.jarrebnnee.connect.SellerDetailNotificationActivity;
import com.jarrebnnee.connect.Service.GetSet;
import com.jarrebnnee.connect.ServiceItemDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jigna on 13/10/16.
 */
public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.MyViewHolder>{
    ArrayList<HashMap<String,String>> list;
    HashMap<String,String> map;
    private  Context c;
    GetSet getSet;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtView,tvLoc,tvRating,tvDist;
        Button btnCall,btnBook;
        public ImageView imgView;
        LinearLayout layout;
        RatingBar rat;

        public MyViewHolder(View view) {
            super(view);
            txtView = (TextView) view.findViewById(R.id.tvSName);
            tvLoc = (TextView) view.findViewById(R.id.tvLoc);
            tvRating = (TextView) view.findViewById(R.id.tvRating);
            tvDist = (TextView) view.findViewById(R.id.tvDist);
            btnCall = (Button) view.findViewById(R.id.btnCall);
            btnBook = (Button) view.findViewById(R.id.btnHire);
            imgView = (ImageView) view.findViewById(R.id.iv1);
            layout = (LinearLayout)view.findViewById(R.id.layout);
            rat = (RatingBar)view.findViewById(R.id.rat);

            Typeface custom_font = Typeface.createFromAsset(c.getAssets(),  "fonts/16023_futuran_0.ttf");
            tvLoc.setTypeface(custom_font);
            tvRating.setTypeface(custom_font);
            btnCall.setTypeface(custom_font);
            btnBook.setTypeface(custom_font);
            tvDist.setTypeface(custom_font);

            Typeface custom_fontbold = Typeface.createFromAsset(c.getAssets(),  "fonts/tt0144m.ttf");
            txtView.setTypeface(custom_fontbold);




        }
    }


    public ServiceListAdapter(Context context, ArrayList<HashMap<String,String>> list) {
        this.c = context;
        this.list = list;
        getSet = GetSet.getInstance();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_srlist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        map = new HashMap<String, String>();
        map = list.get(position);

         String u_first_name = map.get("u_first_name");
         String u_last_name = map.get("u_last_name");
        final String image =map.get("u_img");
        String distance = map.get("distance");
        String location = map.get("u_city_name");
        final String number = map.get("u_phone");

        Picasso.with(c).load(image).into(holder.imgView);
        holder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           /*     imgView.setDrawingCacheEnabled(true);
                imgView.buildDrawingCache();
                Bitmap bitmap = Bitmap.createBitmap(imgView.getDrawingCache());
                    *//*imgView.buildDrawingCache();
                    Bitmap bitmap=imgView.getDrawingCache();*/
                Intent in=new Intent(c,FullImageActivity.class);
                in.putExtra("Image",image);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(in);
            }
        });
        holder.txtView.setText(u_first_name+" "+u_last_name);
        holder.tvDist.setText(distance+" Km");
        holder.tvLoc.setText(location);

        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (number.length() > 3) {
                //    Toast.makeText(c, "Dial: " + number, Toast.LENGTH_SHORT).show();
                    try {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + number));
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        c.startActivity(callIntent);
                    } catch (Exception e) {
                        Log.e("ssssss", "dialer exception: " + e);
                    }

                 /*   if (ActivityCompat.checkSelfPermission(c,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    c.startActivity(callIntent);*/
                } else {
                    Toast.makeText(c, "Sorry, we do not have contact details for this seller", Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.length() > 3) {
                    c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    Toast.makeText(c, "Sorry, we do not have contact details for this seller", Toast.LENGTH_SHORT).show();
                }

            }
        });

        String rating = map.get("avg_rating");
        holder.rat.setRating(Float.parseFloat(rating));

        String review = map.get("total_review");
        holder.tvRating.setText(review + "Reviews");

        holder.layout.setTag(position);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i1 = (int) v.getTag();
                map = list.get(i1);

                String u_first_name = map.get("u_first_name");
                String u_last_name = map.get("u_last_name");

                String sp_id = map.get("u_id");
                getSet.setc_id(sp_id);
                getSet.setC_name(u_first_name +" "+u_last_name);
                Intent i = new Intent(c,ServiceItemDetailActivity.class);
                i.putExtra("sp_id", sp_id);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
