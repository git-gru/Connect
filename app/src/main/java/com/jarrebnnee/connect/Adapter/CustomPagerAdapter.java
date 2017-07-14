package com.jarrebnnee.connect.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jarrebnnee.connect.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomPagerAdapter extends PagerAdapter{

	 Context mContext;
	    LayoutInflater mLayoutInflater;
	   // ArrayList<HashMap<String, String>> mResources;
	   // HashMap<String, String> map;
	   ArrayList<HashMap<String, String>> cat_list;
	HashMap<String, String> map;

	    public CustomPagerAdapter(Context context,ArrayList<HashMap<String, String>> cat_list) {
	        mContext = context;
	        this.cat_list=cat_list;

	    }

	    @Override
	    public int getCount() {
	        return cat_list.size();
	    }

	    @Override
	    public boolean isViewFromObject(View view, Object object) {
	        return view == ((LinearLayout) object);
	    }

	    @Override
	    public Object instantiateItem(ViewGroup container, int position) {
			mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

			map = new HashMap<String, String>();
			map = cat_list.get(position);

	        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

			String img = map.get("s_image");
			Picasso.with(mContext).load(img).into(imageView);

	        container.addView(itemView);

	        return itemView;
	    }

	    @Override
	    public void destroyItem(ViewGroup container, int position, Object object) {
	        container.removeView((LinearLayout) object);
	    }
	
}
