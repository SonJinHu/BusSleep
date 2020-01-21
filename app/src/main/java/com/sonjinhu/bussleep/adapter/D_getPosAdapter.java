package com.sonjinhu.bussleep.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sonjinhu.bussleep.R;
import com.sonjinhu.bussleep.activity.D_Station;
import com.sonjinhu.bussleep.activity.E_SetUp;
import com.sonjinhu.bussleep.util.Util;

import java.util.ArrayList;

import static com.sonjinhu.bussleep.R.id.item_d_tv_btnStop;

public class D_getPosAdapter extends RecyclerView.Adapter<D_getPosAdapter.mViewHolder> {

    private boolean selectSwitch1 = false;
    private boolean selectSwitch2 = false;
    private boolean selectSwitch3 = false;

    private int posRide;
    private int posStop;
    private String routeNo;
    private String routeTp;
    private String plainNo;
    private String vehId;

    private Context context;
    private View recycler;
    private ArrayList<D_getPosItem> items = null;
    private ArrayList<D_getStationItem> staItems;

    public D_getPosAdapter(Context context, View recycler, ArrayList<D_getPosItem> items, ArrayList<D_getStationItem> staItems, String routeNo, String routeTp) {
        this.context = context;
        this.recycler = recycler;
        this.items = items;
        this.staItems = staItems;
        this.routeNo = routeNo;
        this.routeTp = routeTp;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = mInflater.inflate(R.layout.item_d_getpos, parent, false);
        return new mViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(final mViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (items.get(position).isBus1()) {
            holder.view1.setVisibility(View.VISIBLE);
            holder.view1.setPadding(0, items.get(position).getSite1(), 0, 0);
            holder.plainNo1.setText(items.get(position).getPlainNo1());

            if (!selectSwitch1) {
                holder.btnSelect1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnSelect("1", position);
                        posRide = position;
                        plainNo = items.get(position).getPlainNo1();
                        vehId = items.get(position).getVehId1();
                    }
                });
            } else {
                holder.btnSelect1.setVisibility(View.GONE);
                holder.btnCancel1.setVisibility(View.VISIBLE);
                holder.btnCancel1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnCancel();
                    }
                });
            }
        } else {
            holder.view1.setVisibility(View.GONE);
        }

        if (items.get(position).isBus2()) {
            holder.view2.setVisibility(View.VISIBLE);
            holder.view2.setPadding(0, items.get(position).getSite2(), 0, 0);
            holder.plainNo2.setText(items.get(position).getPlainNo2());

            if (!selectSwitch2) {
                holder.btnSelect2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnSelect("2", position);
                        posRide = position;
                        plainNo = items.get(position).getPlainNo2();
                        vehId = items.get(position).getVehId2();
                    }
                });
            } else {
                holder.btnSelect2.setVisibility(View.GONE);
                holder.btnCancel2.setVisibility(View.VISIBLE);
                holder.btnCancel2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnCancel();
                    }
                });
            }
        } else {
            holder.view2.setVisibility(View.GONE);
        }

        if (items.get(position).isBus3()) {
            holder.view3.setVisibility(View.VISIBLE);
            holder.view3.setPadding(0, items.get(position).getSite3(), 0, 0);
            holder.plainNo3.setText(items.get(position).getPlainNo3());

            if (!selectSwitch3) {
                holder.btnSelect3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnSelect("3", position);
                        posRide = position;
                        plainNo = items.get(position).getPlainNo3();
                        vehId = items.get(position).getVehId3();
                    }
                });
            } else {
                holder.btnSelect3.setVisibility(View.GONE);
                holder.btnCancel3.setVisibility(View.VISIBLE);
                holder.btnCancel3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnCancel();
                    }
                });
            }
        } else {
            holder.view3.setVisibility(View.GONE);
        }

        if (items.get(position).isBtnStop()) {
            holder.btnStop.setVisibility(View.VISIBLE);
            holder.btnStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    posStop = position;
                    btnStop();
                }
            });
        } else {
            holder.btnStop.setVisibility(View.GONE);
        }
    }

    private void btnSelect(String division, int position) {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setBus1(false);
            items.get(i).setBus2(false);
            items.get(i).setBus3(false);
        }

        switch (division) {
            case "1":
                selectSwitch1 = true;
                items.get(position).setBus1(true);

                for (int i = 0; i < position + 2; i++)
                    items.get(i).setBtnStop(false);
                for (int i = position + 2; i < items.size(); i++)
                    items.get(i).setBtnStop(true);

                notifyDataSetChanged();
                break;

            case "2":
                selectSwitch2 = true;
                items.get(position).setBus2(true);

                for (int i = 0; i < position + 2; i++)
                    items.get(i).setBtnStop(false);
                for (int i = position + 2; i < items.size(); i++)
                    items.get(i).setBtnStop(true);

                notifyDataSetChanged();
                break;

            case "3":
                selectSwitch3 = true;
                items.get(position).setBus3(true);

                for (int i = 0; i < position + 2; i++)
                    items.get(i).setBtnStop(false);
                for (int i = position + 2; i < items.size(); i++)
                    items.get(i).setBtnStop(true);

                notifyDataSetChanged();
                break;
        }
    }

    private void btnCancel() {
        Util util = new Util();
        if (util.isNetwork(context)) {

            selectSwitch1 = false;
            selectSwitch2 = false;
            selectSwitch3 = false;

            if (context instanceof D_Station) {
                ((D_Station) context).getPosResume();
            }

        } else {
            Snackbar snackbar = util.getSnack(recycler);
            snackbar.show();
        }
    }

    private void btnStop() {
        Util util = new Util();
        if (util.isNetwork(context)) {

            String staSeqArr = "";
            for (int i = posRide; i < posStop + 1; i++)
                staSeqArr += staItems.get(i).getSeq() + ",";

            String staNmArr = "";
            for (int i = posRide; i < posStop + 1; i++)
                staNmArr += staItems.get(i).getStaNm() + ",";

            util.saveSharedPre(context, "routeNo", routeNo);
            util.saveSharedPre(context, "routeTp", routeTp);
            util.saveSharedPre(context, "plainNo", plainNo);
            util.saveSharedPre(context, "vehId", vehId);
            util.saveSharedPre(context, "posRide", String.valueOf(posRide));
            util.saveSharedPre(context, "posStop", String.valueOf(posStop));
            util.saveSharedPre(context, "remainNo", String.valueOf(posStop - posRide));
            util.saveSharedPre(context, "rideStaNm", staItems.get(posRide).getStaNm());
            util.saveSharedPre(context, "stopStaNm", staItems.get(posStop).getStaNm());
            util.saveSharedPre(context, "staSeqArr", staSeqArr);
            util.saveSharedPre(context, "staNmArr", staNmArr);

            context.startActivity(new Intent(context, E_SetUp.class));

        } else {
            Snackbar snackbar = util.getSnack(recycler);
            snackbar.show();
        }
    }

    class mViewHolder extends RecyclerView.ViewHolder {
        View view1;
        View view2;
        View view3;
        TextView btnStop;
        TextView plainNo1;
        TextView plainNo2;
        TextView plainNo3;
        TextView btnSelect1;
        TextView btnSelect2;
        TextView btnSelect3;
        TextView btnCancel1;
        TextView btnCancel2;
        TextView btnCancel3;

        mViewHolder(View itemView) {
            super(itemView);
            view1 = itemView.findViewById(R.id.item_d_view1);
            view2 = itemView.findViewById(R.id.item_d_view2);
            view3 = itemView.findViewById(R.id.item_d_view3);
            btnStop = (TextView) itemView.findViewById(item_d_tv_btnStop);

            View include1 = itemView.findViewById(R.id.item_d_include1);
            View include2 = itemView.findViewById(R.id.item_d_include2);
            View include3 = itemView.findViewById(R.id.item_d_include3);
            plainNo1 = (TextView) include1.findViewById(R.id.item_d_include_tv_plainNo);
            plainNo2 = (TextView) include2.findViewById(R.id.item_d_include_tv_plainNo);
            plainNo3 = (TextView) include3.findViewById(R.id.item_d_include_tv_plainNo);
            btnSelect1 = (TextView) include1.findViewById(R.id.item_d_include_tv_btnSelect);
            btnSelect2 = (TextView) include2.findViewById(R.id.item_d_include_tv_btnSelect);
            btnSelect3 = (TextView) include3.findViewById(R.id.item_d_include_tv_btnSelect);
            btnCancel1 = (TextView) include1.findViewById(R.id.item_d_include_tv_btnCancel);
            btnCancel2 = (TextView) include2.findViewById(R.id.item_d_include_tv_btnCancel);
            btnCancel3 = (TextView) include3.findViewById(R.id.item_d_include_tv_btnCancel);
        }
    }
}