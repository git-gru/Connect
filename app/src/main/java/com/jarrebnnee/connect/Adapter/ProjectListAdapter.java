package com.jarrebnnee.connect.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jarrebnnee.connect.FullImageActivity;
import com.jarrebnnee.connect.ProjectDetailActivity;
import com.jarrebnnee.connect.R;
import com.jarrebnnee.connect.SellerJobListActivity;
import com.jarrebnnee.connect.Service.GetSet;
import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.ServiceHandler;
import com.jarrebnnee.connect.Urlcollection;
import com.jarrebnnee.connect.WriteReviewFragment;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jigna on 13/10/16.
 */
public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.MyViewHolder>{
    private ArrayList<HashMap<String,String>> list;
    private Context c;
    WriteReviewFragment reviewFragment;
    Bundle mBundle;
    GetSet getSet;
    SaveSharedPrefrence prefrence;
    String u_type;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtView,tvCategory,tvdes,tvStatus;
        LinearLayout layout;
        public ImageView imgView;
        Button btnReview;
       // Button btnReview;

        public MyViewHolder(View view) {
            super(view);
            txtView = (TextView) view.findViewById(R.id.tvSName);
            tvCategory = (TextView) view.findViewById(R.id.tvCategory);
            tvStatus = (TextView) view.findViewById(R.id.tvStatus);
            tvdes = (TextView) view.findViewById(R.id.tvdes);
            layout = (LinearLayout)view.findViewById(R.id.layout);
            imgView = (ImageView) view.findViewById(R.id.iv1);
            btnReview = (Button) view.findViewById(R.id.btnReview);
            //btnReview = (Button) view.findViewById(R.id.btnReview);
            Typeface custom_font = Typeface.createFromAsset(c.getAssets(),  "fonts/tt0142m.ttf");
            tvStatus.setTypeface(custom_font);
            tvCategory.setTypeface(custom_font);
            Typeface custom_font1 = Typeface.createFromAsset(c.getAssets(),  "fonts/16023_futuran_0.ttf");
           // btnReview.setTypeface(custom_font1);

            Typeface custom_fontbold = Typeface.createFromAsset(c.getAssets(),  "fonts/tt0144m.ttf");
            txtView.setTypeface(custom_fontbold);



        }

    }


    public ProjectListAdapter(Context context, ArrayList<HashMap<String,String>> list) {
        this.c = context;
        this.list = list;
        getSet = GetSet.getInstance();
        prefrence=new SaveSharedPrefrence();

        u_type = prefrence.getUserType(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_project_list, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        HashMap<String,String> resultp = list.get(position);

        holder.txtView.setText(resultp.get("js_title"));
        holder.tvCategory.setText("Category: "+resultp.get("js_category_name"));
        holder.tvdes.setText(resultp.get("js_description"));
        final String img = resultp.get("js_image");
        Picasso.with(c).load(img).into(holder.imgView);
        holder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*imgView.setDrawingCacheEnabled(true);
                imgView.buildDrawingCache();
                Bitmap bitmap = Bitmap.createBitmap(imgView.getDrawingCache());
                    *//*imgView.buildDrawingCache();
                    Bitmap bitmap=imgView.getDrawingCache();*/
                Intent in=new Intent(c,FullImageActivity.class);
                in.putExtra("Image",img);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(in);
            }
        });
        if (u_type.equals("2")) {
            holder.btnReview.setVisibility(View.GONE);
        } else if (u_type.equals("1")) {
            holder.btnReview.setVisibility(View.VISIBLE);
        }
        //holder.imgView.setImageResource(icons[position]);
       /* if (sts[position].equals("Pending")){
            holder.tvStatus.setTextColor(Color.parseColor("#FF6600"));
        }else {
            holder.tvStatus.setTextColor(Color.parseColor("#228B22"));
        }*/
        String jsr_status = resultp.get("js_status");

        if (jsr_status.equals("3")){
            holder.tvStatus.setText("Status: Completed");
            holder.btnReview.setText("Completed");
        }else if (jsr_status.equals("2")){
            holder.tvStatus.setText("Status: Running");
            holder.btnReview.setText("Running");
        }else if (jsr_status.equals("1")){
            holder.tvStatus.setText("Status: Award Now");
            holder.btnReview.setText("Award Now");
        }else if (jsr_status.equals("4")){
            holder.tvStatus.setText("Status: Awarded");
            holder.btnReview.setText("Awardeded");

        }else if (jsr_status.equals("5")){
            holder.tvStatus.setText("Status: Rejected");
            holder.btnReview.setText("Rejected");
        }else if (jsr_status.equals("0")){
            holder.tvStatus.setText("Status: Pending");
            holder.btnReview.setText("Pending");
        }

        holder.layout.setTag(position);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = (int) v.getTag();
                HashMap<String,String> resultp = list.get(i);
                getSet.setjs_id(resultp.get("js_id"));
                getSet.setjs_title(resultp.get("js_title"));
              //  Toast.makeText(c,holder.txtView.getText().toString(), Toast.LENGTH_SHORT).show();
                Intent i1 = new Intent(c, ProjectDetailActivity.class);
                i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(i1);
            }
        });
       /* holder.btnReview.setTag(position);
        holder.btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i1 = (int) v.getTag();
                HashMap<String,String> resultp = list.get(i1);
                String jsr_id=resultp.get("js_id");
                getSet.setjsr_id(jsr_id);
                Intent i = new Intent(c,WriteReviewFragment.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(i);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
