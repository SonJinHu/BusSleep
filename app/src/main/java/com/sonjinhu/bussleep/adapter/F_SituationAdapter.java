package com.sonjinhu.bussleep.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sonjinhu.bussleep.R;

import java.util.ArrayList;

public class F_SituationAdapter extends RecyclerView.Adapter<F_SituationAdapter.mViewHolder> {

    private ArrayList<F_SituationItem> items = null;
    private String stOrd, stopFlag;

    public F_SituationAdapter(ArrayList<F_SituationItem> items, String stOrd, String stopFlag) {
        this.items = items;
        this.stOrd = stOrd;
        this.stopFlag = stopFlag;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = mInflater.inflate(R.layout.itme_f_situation, parent, false);
        return new mViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(mViewHolder vHolder, int position) {
        vHolder.view1.setBackgroundResource(items.get(position).getImgWay());
        vHolder.view2.setText(items.get(position).getStaNm());

//        int remainStop = Integer.parseInt(items.get(getItemCount() - 1).getStaOrd()) - Integer.parseInt(items.get(position).getStaOrd());
//        String remainStopStr = String.valueOf(remainStop) + "정류장\n남음";

        if (items.get(position).getStaSeq().equals(stOrd)) {

            if (stopFlag.equals("0")) {
                vHolder.view3.setBackgroundResource(R.drawable.bus);
            }

            if (stopFlag.equals("1")) {
                vHolder.view4.setBackgroundResource(R.drawable.bus);
            }

        } else {
            vHolder.view3.setBackground(null);
            vHolder.view4.setBackground(null);
        }
    }

    class mViewHolder extends RecyclerView.ViewHolder {
        ImageView view1;
        TextView view2;
        ImageView view3;
        ImageView view4;

        mViewHolder(View itemView) {
            super(itemView);
            view1 = (ImageView) itemView.findViewById(R.id.item_f_way);
            view2 = (TextView) itemView.findViewById(R.id.item_f_staNm);
            view3 = (ImageView) itemView.findViewById(R.id.item_f_bus_0);
            view4 = (ImageView) itemView.findViewById(R.id.item_f_bus_1);
        }
    }
}
