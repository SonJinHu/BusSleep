package com.sonjinhu.bussleep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sonjinhu.bussleep.R;

import java.util.ArrayList;

public class C_getRouteAdapter extends BaseAdapter {

    public ArrayList<C_getRouteItem> items = null;

    public C_getRouteAdapter(ArrayList<C_getRouteItem> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context mContext = parent.getContext();
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder vHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_c_getroute, parent, false);
            vHolder = new ViewHolder();
            vHolder.view1 = (TextView) convertView.findViewById(R.id.item_c_number);
            vHolder.view2 = (TextView) convertView.findViewById(R.id.item_c_type);
            convertView.setTag(vHolder);
        } else {
            vHolder = (ViewHolder) convertView.getTag();
        }

        vHolder.view1.setText(items.get(position).getRouteNm());
        vHolder.view1.setTextColor(items.get(position).getRouteTpColor());
        vHolder.view2.setText(items.get(position).getRouteTp());

        return convertView;
    }

    private class ViewHolder {
        TextView view1;
        TextView view2;
    }
}

