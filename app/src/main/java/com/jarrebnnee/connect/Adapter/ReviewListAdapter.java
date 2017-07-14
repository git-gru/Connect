package com.jarrebnnee.connect.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jarrebnnee.connect.R;
import com.jarrebnnee.connect.Service.GetSet;
import com.jarrebnnee.connect.WriteReviewFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jigna on 13/10/16.
 */
public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.MyViewHolder>{
    private ArrayList<HashMap<String,String>> list;
    private Context c;
    WriteReviewFragment reviewFragment;
    Bundle mBundle;
    GetSet getSet;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtView,tvDesc,tvTime;
       // Button btnReview;
        public ImageView imgView;
        RatingBar rat;

        public MyViewHolder(View view) {
            super(view);
            txtView = (TextView) view.findViewById(R.id.tvSName);
            tvDesc = (TextView) view.findViewById(R.id.tvDesc);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            rat = (RatingBar)view.findViewById(R.id.rat);
            imgView = (ImageView) view.findViewById(R.id.iv1);

            Typeface custom_font = Typeface.createFromAsset(c.getAssets(),  "fonts/tt0142m.ttf");
            tvDesc.setTypeface(custom_font);
            tvTime.setTypeface(custom_font);

            Typeface custom_fontbold = Typeface.createFromAsset(c.getAssets(),  "fonts/tt0144m.ttf");
            txtView.setTypeface(custom_fontbold);

        }

    }


    public ReviewListAdapter(Context context, ArrayList<HashMap<String,String>> list) {
        this.c = context;
        this.list = list;
        getSet = GetSet.getInstance();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_reviewrc, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        HashMap<String,String> resultp = list.get(position);

        holder.txtView.setText(resultp.get("u_first_name")+" "+resultp.get("u_last_name"));
        holder.tvDesc.setText(resultp.get("jsr_comment_text"));
        String img = resultp.get("u_img");
        Picasso.with(c).load(img).into(holder.imgView);

        String jsr_rating = resultp.get("jsr_rating");
        if (jsr_rating.equals("")){

        }else {
            holder.rat.setRating(Float.parseFloat(jsr_rating));
        }
        Log.e("zzzzzzz",""+resultp.get("date"));
        holder.tvTime.setText(resultp.get("date"));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
