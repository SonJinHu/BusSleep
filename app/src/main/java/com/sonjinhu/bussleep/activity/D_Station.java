package com.sonjinhu.bussleep.activity;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sonjinhu.bussleep.R;
import com.sonjinhu.bussleep.adapter.D_getPosAdapter;
import com.sonjinhu.bussleep.adapter.D_getPosItem;
import com.sonjinhu.bussleep.adapter.D_getStationAdapter;
import com.sonjinhu.bussleep.adapter.D_getStationItem;
import com.sonjinhu.bussleep.util.Config;
import com.sonjinhu.bussleep.util.ScrollListener;
import com.sonjinhu.bussleep.util.Util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

//'C_FRG_Route'에서 넘어왔습니다.
public class D_Station extends AppCompatActivity implements View.OnClickListener {

    public static Activity getStationActivity;
    String TAG = getClass().getSimpleName();

    String routeId, routeTp, routeNo;
    TextView tv2;
    FloatingActionButton fab;
    RecyclerView recycler1, recycler2;

    ArrayList<D_getStationItem> staItems;
    int staLength;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.d_compat_back:
                finish();
                break;
            case R.id.d_compat_star:
                Toast.makeText(this, "준비 중 입니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.d_fab:
                Util util = new Util();
                if (util.isNetwork(this)) {
                    new getPosByRoute().execute();
                } else {
                    Snackbar snackbar = util.getSnack(getCurrentFocus());
                    snackbar.show();
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_d_station);
        getStationActivity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.d_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        initText();
        initByType();
        initRecycler();

        new getStationByRoute().execute();
        new getPosByRoute().execute();
    }

    void initText() {
        Bundle mBundle = getIntent().getBundleExtra("BUNDLE");
        routeId = mBundle.getString("routeId");
        routeTp = mBundle.getString("routeTp");
        routeNo = mBundle.getString("routeNo");

        View include = findViewById(R.id.d_include_title);
        TextView tv1 = (TextView) include.findViewById(R.id.d_include_tv_routeTp);
        tv2 = (TextView) include.findViewById(R.id.d_include_tv_routeNo);
        TextView tv3 = (TextView) include.findViewById(R.id.d_include_tv_staNm);
        TextView tv4 = (TextView) include.findViewById(R.id.d_include_tv_endNm);
        tv1.setText(mBundle.getString("routeTp"));
        tv2.setText(mBundle.getString("routeNo"));
        tv3.setText(mBundle.getString("staNodeNm"));
        tv4.setText(mBundle.getString("endNodeNm"));
    }

    void initByType() {
        AppCompatImageView back = (AppCompatImageView) findViewById(R.id.d_compat_back);
        back.setOnClickListener(this);

        AppCompatImageView star = (AppCompatImageView) findViewById(R.id.d_compat_star);
        star.setOnClickListener(this);

        fab = (FloatingActionButton) findViewById(R.id.d_fab);
        fab.setOnClickListener(this);

        ColorStateList color;
        switch (routeTp) {
            case "공항버스":
                back.setImageResource(R.drawable.ic_keyboard_arrow_left_sky_24dp);
                star.setImageResource(R.drawable.ic_star_border_sky_24dp);

                color = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.sky));
                tv2.setTextColor(color);
                fab.setBackgroundTintList(color);
                break;

            case "마을버스":
                back.setImageResource(R.drawable.ic_keyboard_arrow_left_village_24dp);
                star.setImageResource(R.drawable.ic_star_border_village_24dp);

                color = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.village));
                tv2.setTextColor(color);
                fab.setBackgroundTintList(color);
                break;

            case "간선버스":
                back.setImageResource(R.drawable.ic_keyboard_arrow_left_blue_24dp);
                star.setImageResource(R.drawable.ic_star_border_blue_24dp);

                color = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.blue));
                tv2.setTextColor(color);
                fab.setBackgroundTintList(color);
                break;

            case "지선버스":
                back.setImageResource(R.drawable.ic_keyboard_arrow_left_green_24dp);
                star.setImageResource(R.drawable.ic_star_border_green_24dp);

                color = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green));
                tv2.setTextColor(color);
                fab.setBackgroundTintList(color);
                break;

            case "순환버스":
                back.setImageResource(R.drawable.ic_keyboard_arrow_left_yellow_24dp);
                star.setImageResource(R.drawable.ic_star_border_yellow_24dp);

                color = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.yellow));
                tv2.setTextColor(color);
                fab.setBackgroundTintList(color);
                break;

            case "광역버스":
                back.setImageResource(R.drawable.ic_keyboard_arrow_left_red_24dp);
                star.setImageResource(R.drawable.ic_star_border_red_24dp);

                color = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red));
                tv2.setTextColor(color);
                fab.setBackgroundTintList(color);
                break;
        }
    }

    void initRecycler() {
        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler1 = (RecyclerView) findViewById(R.id.d_recycler1);
        recycler1.setLayoutManager(mLayoutManager1);

        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler2 = (RecyclerView) findViewById(R.id.d_recycler2);
        recycler2.setLayoutManager(mLayoutManager2);

        RecyclerView.OnScrollListener listener = new ScrollListener(fab, recycler1, recycler2);
        recycler1.addOnScrollListener(listener);
        recycler2.addOnScrollListener(listener);
    }

    private class getStationByRoute extends AsyncTask<Void, Void, D_getStationAdapter> {
        @Override
        protected D_getStationAdapter doInBackground(Void... voids) {

            D_getStationAdapter adapter = null;

            try {
                URL url = new URL(new Config().STATION_URL + routeId);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");

                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null)
                    sb.append(line);

                conn.disconnect();
                rd.close();
                Log.e(TAG, "STATION_URL : " + sb.toString());

                staItems = parseXml(sb.toString());
                adapter = new D_getStationAdapter(staItems);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return adapter;
        }

        @Override
        protected void onPostExecute(D_getStationAdapter adapter) {
            super.onPostExecute(adapter);
            recycler1.setAdapter(adapter);
        }

        ArrayList<D_getStationItem> parseXml(String stationXml) {

            ArrayList<D_getStationItem> items = new ArrayList<>();

            try {
                InputStream is = new ByteArrayInputStream(stationXml.getBytes());
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = factory.newDocumentBuilder();
                Document doc = documentBuilder.parse(is);
                Element element = doc.getDocumentElement();

                NodeList nodeList1 = element.getElementsByTagName("itemList");
                NodeList nodeList2 = element.getElementsByTagName("seq");
                NodeList nodeList3 = element.getElementsByTagName("stationNm");
                NodeList nodeList4 = element.getElementsByTagName("transYn");

                staLength = nodeList1.getLength();
                int turnOrd = 0;
                for (int i = 0; i < staLength; i++) {
                    String transYn = nodeList4.item(i).getFirstChild().getNodeValue();
                    if (transYn.equals("Y")) {
                        turnOrd = i + 1;
                        break;
                    }
                }

                Log.e(TAG, "" + turnOrd);
                for (int j = 0; j < staLength; j++) {
                    String seq = nodeList2.item(j).getFirstChild().getNodeValue();
                    String staNm = nodeList3.item(j).getFirstChild().getNodeValue();

                    D_getStationItem item = new D_getStationItem();

                    item.setSeq(seq);
                    item.setStaNm(staNm);
                    item.setWayImg(imgWayByOrd(j + 1, turnOrd));

                    items.add(item);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return items;
        }

        int imgWayByOrd(int staOrd, int turnOrd) {
            if (turnOrd != 1) {
                if (staOrd == 1)
                    return R.drawable.direct_d_head;
                else if (staOrd < turnOrd)
                    return R.drawable.direct_d_pre;
                else if (staOrd == turnOrd)
                    return R.drawable.direct_d_trans;
                else if (staOrd == staLength)
                    return R.drawable.direct_d_foot;
                else
                    return R.drawable.direct_d_post;
            } else {
                if (staOrd == 1)
                    return R.drawable.direct_d_head;
                else if (staOrd == staLength)
                    return R.drawable.direct_d_foot_noturn;
                else
                    return R.drawable.direct_d_pre;
            }
        }
    }

    private class getPosByRoute extends AsyncTask<Void, Void, D_getPosAdapter> {

        Parcelable recyclerState;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            recyclerState = recycler2.getLayoutManager().onSaveInstanceState();
        }

        @Override
        protected D_getPosAdapter doInBackground(Void... voids) {

            D_getPosAdapter adapter = null;

            try {
                URL url = new URL(new Config().POS_URL_ROUTEID + routeId);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");

                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null)
                    sb.append(line);

                conn.disconnect();
                rd.close();
                Log.e(TAG, "POS_URL_ROUTEID : " + sb.toString());

                ArrayList<D_getPosItem> posItems = parseXml(sb.toString());
                adapter = new D_getPosAdapter(D_Station.this, recycler2, posItems, staItems, routeNo, routeTp);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return adapter;
        }

        @Override
        protected void onPostExecute(D_getPosAdapter adapter) {
            super.onPostExecute(adapter);
            recycler2.setAdapter(adapter);
            recycler2.getLayoutManager().onRestoreInstanceState(recyclerState);
        }

        ArrayList<D_getPosItem> parseXml(String stationXml) {

            ArrayList<D_getPosItem> items = new ArrayList<>();

            try {
                InputStream is = new ByteArrayInputStream(stationXml.getBytes());
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = factory.newDocumentBuilder();
                Document doc = documentBuilder.parse(is);
                Element element = doc.getDocumentElement();

                NodeList nodeList1 = element.getElementsByTagName("itemList");
                NodeList nodeList2 = element.getElementsByTagName("sectOrd");
                NodeList nodeList3 = element.getElementsByTagName("sectDist");
                NodeList nodeList4 = element.getElementsByTagName("fullSectDist");
                NodeList nodeList5 = element.getElementsByTagName("plainNo");
                NodeList nodeList6 = element.getElementsByTagName("vehId");

                int posLength = nodeList1.getLength();
                int[] sectOrdArr = new int[posLength];
                int[] siteDpArr = new int[posLength];
                String[] plainNoArr = new String[posLength];
                String[] vehIdArr = new String[posLength];

                for (int i = 0; i < posLength; i++) {
                    int sectOrd = Integer.parseInt(nodeList2.item(i).getFirstChild().getNodeValue());
                    float sectDist = Float.parseFloat(nodeList3.item(i).getFirstChild().getNodeValue());
                    float fullDist = Float.parseFloat(nodeList4.item(i).getFirstChild().getNodeValue()); // 간혹 'fullSectDist' 에서 '실수'(소수점 두 자리)가 나옴..
                    String plainNo = nodeList5.item(i).getFirstChild().getNodeValue();
                    String vehId = nodeList6.item(i).getFirstChild().getNodeValue();

                    if (fullDist == Float.parseFloat("0")) // 간혹 'fullSectDist' 에서 '0'이 나옴..
                        fullDist = Float.parseFloat("300");
                    float ratio = (sectDist / fullDist - (int) (sectDist / fullDist)) * 60; // 간혹 'sectDist'가 'fullDist'보다 높게 나옴..
                    float scale = getResources().getDisplayMetrics().density;
                    int siteDp = (int) (ratio * scale + 0.5f);

                    sectOrdArr[i] = sectOrd;
                    siteDpArr[i] = siteDp;
                    plainNoArr[i] = plainNo;
                    vehIdArr[i] = vehId;

                    Log.e(TAG, sectOrdArr[i] + " / " + plainNoArr[i] + " / " + sectDist + " / " + fullDist + " / " + siteDpArr[i] + " / " + vehId);
                }

                int k = 0;
                for (int j = 0; j < staLength; j++) {

                    D_getPosItem item = new D_getPosItem();

                    if (nodeList1.getLength() != 0) {

                        if (j + 1 == sectOrdArr[k]) {
                            item.setBus1(true);
                            item.setPlainNo1(plainNoArr[k]);
                            item.setSite1(siteDpArr[k]);
                            item.setVehId1(vehIdArr[k]);

                            item.setBus2(false);
                            item.setPlainNo2("");
                            item.setSite2(0);
                            item.setVehId2("");

                            item.setBus3(false);
                            item.setPlainNo3("");
                            item.setSite3(0);
                            item.setVehId3("");
                            if (k < sectOrdArr.length - 1) k++;
                            if (j + 1 == sectOrdArr[k]) {
                                item.setBus2(true);
                                item.setPlainNo2(plainNoArr[k]);
                                item.setSite2(siteDpArr[k]);
                                item.setVehId2(vehIdArr[k]);

                                item.setPlainNo3("");
                                item.setBus3(false);
                                item.setSite3(0);
                                item.setVehId3("");
                                if (k < sectOrdArr.length - 1) k++;
                                if (j + 1 == sectOrdArr[k]) {
                                    item.setBus3(true);
                                    item.setPlainNo3(plainNoArr[k]);
                                    item.setSite3(siteDpArr[k]);
                                    item.setVehId3(vehIdArr[k]);
                                    if (k < sectOrdArr.length - 1) k++;
                                }
                            }
                        } else {
                            item.setBus1(false);
                            item.setBus2(false);
                            item.setBus3(false);
                            item.setPlainNo1("");
                            item.setPlainNo2("");
                            item.setPlainNo3("");
                            item.setSite1(0);
                            item.setSite2(0);
                            item.setSite3(0);
                            item.setVehId1("");
                            item.setVehId2("");
                            item.setVehId3("");
                        }

                    } else {

                        item.setBus1(false);
                        item.setBus2(false);
                        item.setBus3(false);
                        item.setPlainNo1("");
                        item.setPlainNo2("");
                        item.setPlainNo3("");
                        item.setSite1(0);
                        item.setSite2(0);
                        item.setSite3(0);
                        item.setVehId1("");
                        item.setVehId2("");
                        item.setVehId3("");

                    }
                    items.add(item);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return items;
        }
    }

    // 'D_getPosAdapter'에서 쓰임.
    public void getPosResume() {
        new getPosByRoute().execute();
    }
}