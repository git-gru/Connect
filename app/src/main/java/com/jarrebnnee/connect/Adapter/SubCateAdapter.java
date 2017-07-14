package com.jarrebnnee.connect.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jarrebnnee.connect.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vardhman Infonet 4 on 03-Mar-17.
 */

public class SubCateAdapter extends BaseAdapter{
    private ArrayList<HashMap<String,String>> cat_name;
    private Context c;
    HashMap<String,String> map;
    ImageLoader imageLoader;
    DisplayImageOptions options;


    public SubCateAdapter(Context context, ArrayList<HashMap<String,String>> cat_name) {
        this.c = context;
        this.cat_name = cat_name;

        try
        {
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.ic_launcher)
                    .showImageForEmptyUri(R.mipmap.ic_launcher)
                    .showImageOnFail(R.mipmap.ic_launcher)
                    .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                    .displayer(new RoundedBitmapDisplayer(2)).build();
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public int getCount() {
        return cat_name.size();
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
        TextView tvcname;
        ImageView ivcname;
        map = cat_name.get(position);
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_subcate, parent, false);

        tvcname = (TextView)itemView.findViewById(R.id.tv);
        ivcname = (ImageView)itemView.findViewById(R.id.iv);

        String iv = map.get("c_images");
        imageLoader.displayImage(iv, ivcname, options);
        tvcname.setText(map.get("c_title"));
        Typeface custom_font = Typeface.createFromAsset(c.getAssets(),  "fonts/16023_futuran_0.ttf");
        tvcname.setTypeface(custom_font);

        return itemView;
    }

}
