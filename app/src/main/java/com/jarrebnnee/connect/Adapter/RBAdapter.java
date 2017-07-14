package com.jarrebnnee.connect.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jarrebnnee.connect.CatListGS;
import com.jarrebnnee.connect.MyApplication;
import com.jarrebnnee.connect.R;
import com.jarrebnnee.connect.SellerSetServicesActivity;

import java.util.ArrayList;

/**
 * Created by vi6 on 21-Jun-17.
 */

public class RBAdapter extends RecyclerView.Adapter<RBAdapter.ViewHolder>{
    ArrayList<CatListGS> list;
    private int lastCheckedPosition = -1;
    Context context;

    public RBAdapter(ArrayList<CatListGS> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RBAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.custom_radio, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RBAdapter.ViewHolder holder, int position) {
        CatListGS listGS = list.get(position);
        String s = listGS.getC_title();
        String sa = listGS.getC_ar_title();

        int temp = MyApplication.getDefaultLanguage();
        if (temp == 1) {
         //   String item = map.getC_ar_title();
            holder.myTv.setText(sa);
        } else {
            holder.myTv.setText(s);
        }
        holder.myRb.setChecked(position == lastCheckedPosition);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton myRb;
        TextView myTv;



        public ViewHolder(View itemView) {
            super(itemView);
            myRb = (RadioButton) itemView.findViewById(R.id.myRb);
            myTv = (TextView) itemView.findViewById(R.id.myTv);
            myRb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastCheckedPosition = getAdapterPosition();
                    CatListGS gs = list.get(getAdapterPosition());
                    String s = gs.getC_id();
                    Log.e("xxxxxx", s);
                    SellerSetServicesActivity.s_category_id = s;
                    notifyItemRangeChanged(0, list.size());
                }
            });
        }
    }
}
