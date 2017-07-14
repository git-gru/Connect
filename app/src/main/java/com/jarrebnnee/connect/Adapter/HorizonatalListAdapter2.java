package com.jarrebnnee.connect.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jarrebnnee.connect.R;

/**
 * Created by jigna on 13/10/16.
 */
public class HorizonatalListAdapter2 extends RecyclerView.Adapter<HorizonatalListAdapter2.MyViewHolder>{
    private String[] cat_name;
    private int[] icons;
    private  Context c;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtView;
        public ImageView imgView;

        public MyViewHolder(View view) {
            super(view);
            txtView = (TextView) view.findViewById(R.id.tv);
            imgView = (ImageView) view.findViewById(R.id.iv);
            Typeface custom_font = Typeface.createFromAsset(c.getAssets(),  "fonts/16023_futuran_0.ttf");
            txtView.setTypeface(custom_font);

        }
    }


    public HorizonatalListAdapter2(Context context, String[] cat_name, int[] icons) {
        this.c = context;
        this.cat_name = cat_name;
        this.icons = icons;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_home_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.txtView.setText(cat_name[position]);
        holder.imgView.setImageResource(icons[position]);

        holder.txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c,holder.txtView.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cat_name.length;
    }
}
