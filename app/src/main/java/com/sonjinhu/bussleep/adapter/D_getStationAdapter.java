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

public class D_getStationAdapter extends RecyclerView.Adapter<D_getStationAdapter.mViewHolder> {

    private ArrayList<D_getStationItem> items = null;

    public D_getStationAdapter(ArrayList<D_getStationItem> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = mInflater.inflate(R.layout.item_d_getstation, parent, false);
        return new mViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, int position) {
        holder.staNm.setText(items.get(position).getStaNm());
        holder.wayImg.setImageResource(items.get(position).getWayImg());
    }

    class mViewHolder extends RecyclerView.ViewHolder {
        TextView staNm;
        ImageView wayImg;

        mViewHolder(View itemView) {
            super(itemView);
            staNm = (TextView) itemView.findViewById(R.id.item_d_staNm);
            wayImg = (ImageView) itemView.findViewById(R.id.item_d_way);
        }
    }
}