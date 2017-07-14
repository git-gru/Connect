package com.jarrebnnee.connect.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jarrebnnee.connect.MyApplication;
import com.jarrebnnee.connect.R;

import java.util.ArrayList;
import java.util.HashMap;

public class BookCateListAdapter extends BaseAdapter{

	Context c;
	ArrayList<HashMap<String, String>> cityList;
	HashMap<String, String> resultp;
	LayoutInflater inflater;


	public BookCateListAdapter(Context activity,
							   ArrayList<HashMap<String, String>> stateList) {
		this.c = activity;
		this.cityList = stateList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cityList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tvAName;
		inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.custom_spblack, parent, false);
		
		resultp = new HashMap<String, String>();
		resultp=cityList.get(position);
		
		tvAName = (TextView)itemView.findViewById(R.id.tv);

		int temp = MyApplication.getDefaultLanguage();
		if (temp == 1) {
			tvAName.setText(resultp.get("c_ar_title"));
		} else {
			tvAName.setText(resultp.get("c_title"));
		}


		Typeface custom_font = Typeface.createFromAsset(c.getAssets(),  "fonts/16023_futuran_0.ttf");
		tvAName.setTypeface(custom_font);
		Log.d("city", ""+resultp.get("c_title"));
		return itemView;
	}

}
