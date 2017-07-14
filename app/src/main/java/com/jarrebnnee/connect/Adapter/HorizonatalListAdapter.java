package com.jarrebnnee.connect.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jarrebnnee.connect.MyApplication;
import com.jarrebnnee.connect.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jigna on 13/10/16.
 */
public class HorizonatalListAdapter extends BaseAdapter {
    private Context c;
    ArrayList<HashMap<String, String>> cat_list;
    HashMap<String, String> map;

    public HorizonatalListAdapter(Context context,ArrayList<HashMap<String, String>> cat_list) {
        this.c = context;
        this.cat_list = cat_list;
    }
    @Override
    public int getCount() {
        return cat_list.size();
    }


    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TextView txtView;
        ImageView imgView;
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_home_list, parent, false);
        map = new HashMap<String, String>();
        map = cat_list.get(position);

        txtView = (TextView) itemView.findViewById(R.id.tv);
        imgView = (ImageView) itemView.findViewById(R.id.iv);

        Typeface custom_font = Typeface.createFromAsset(c.getAssets(),  "fonts/16023_futuran_0.ttf");
        txtView.setTypeface(custom_font);

        int temp = MyApplication.getDefaultLanguage();
        if (temp == 1) {
            String item = map.get("c_ar_title");
            txtView.setText(item);
        } else {
            String item = map.get("c_title");
            txtView.setText(item);
        }


        String img = map.get("c_images");

        Picasso.with(c).load(img).into(imgView);

       /* txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c,txtView.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
*/
        return itemView;
    }



}
