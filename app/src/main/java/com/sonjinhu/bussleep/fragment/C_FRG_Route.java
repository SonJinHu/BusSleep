package com.sonjinhu.bussleep.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sonjinhu.bussleep.R;
import com.sonjinhu.bussleep.activity.D_Station;
import com.sonjinhu.bussleep.util.Util;

import static com.sonjinhu.bussleep.activity.C_Search.adapter;

public class C_FRG_Route extends Fragment implements AdapterView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putString("routeId", adapter.items.get(position).getRouteId());
        bundle.putString("routeTp", adapter.items.get(position).getRouteTp());
        bundle.putString("routeNo", adapter.items.get(position).getRouteNm());
        bundle.putString("staNodeNm", adapter.items.get(position).getStartNodeNm());
        bundle.putString("endNodeNm", adapter.items.get(position).getEndNodeNm());

        Log.e("", adapter.items.get(position).getRouteId());

        Util util = new Util();
        if (util.isNetwork(getActivity())) {
            Intent intent = new Intent(getActivity(), D_Station.class);
            intent.putExtra("BUNDLE", bundle);
            startActivity(intent);
        } else {
            Snackbar snackbar = util.getSnack(view);
            snackbar.show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_c_route, container, false);

        ListView mListView = (ListView) view.findViewById(R.id.ca_routelist_listview);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

        return view;
    }
}