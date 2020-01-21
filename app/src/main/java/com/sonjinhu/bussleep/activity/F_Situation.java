package com.sonjinhu.bussleep.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sonjinhu.bussleep.R;
import com.sonjinhu.bussleep.adapter.F_SituationAdapter;
import com.sonjinhu.bussleep.adapter.F_SituationItem;
import com.sonjinhu.bussleep.service.F_SVC_Situation;
import com.sonjinhu.bussleep.util.Config;
import com.sonjinhu.bussleep.util.CustomNotification;
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
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class F_Situation extends Activity implements View.OnClickListener {

    public static Activity alarmActivity;
    String TAG = getClass().getSimpleName();

    Intent sIntent;

    String routeNo, routeTp, plainNo, vehId, staSeqArr, staNmArr;
    int posStop, conditionValue;

    TextView tv1, tv3;
    RecyclerView recycler;
    ArrayList<F_SituationItem> items;

    BroadcastReceiver br;
    Timer mTimer;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.f_btn_end:
                new CustomNotification(F_Situation.this).cancelNotification();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        stopService(sIntent);
                        Log.e(TAG, "onClick f_btn_end : stopService()");
                    }
                }, 1500);

                finish();
                Intent mIntent = new Intent(this, B_Main.class);
                startActivity(mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate() : stopService()");
        setContentView(R.layout.act_f_situation);
        alarmActivity = this;

        sIntent = new Intent(this, F_SVC_Situation.class);
        stopService(sIntent);

        initPref();
        initVariable();
        initByTp();
        initStation();
    }

    void initPref() {
        Util util = new Util();
        routeNo = util.getSharedPre(this, "routeNo");
        routeTp = util.getSharedPre(this, "routeTp");
        plainNo = util.getSharedPre(this, "plainNo");
        vehId = util.getSharedPre(this, "vehId");
        staSeqArr = util.getSharedPre(this, "staSeqArr");
        staNmArr = util.getSharedPre(this, "staNmArr");

        String posStopStr = util.getSharedPre(this, "posStop");
        posStop = Integer.parseInt(posStopStr);

        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(F_Situation.this);
        String conditionValueStr = mPref.getString("pref_condition_list", "1");
        conditionValue = Integer.parseInt(conditionValueStr);
    }

    void initVariable() {
        tv1 = (TextView) findViewById(R.id.f_tv_routeNo);
        TextView tv2 = (TextView) findViewById(R.id.f_tv_plainNo);
        tv3 = (TextView) findViewById(R.id.f_tv_remainNo);
        tv1.setText(routeNo);
        tv2.setText(plainNo);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler = (RecyclerView) findViewById(R.id.f_recycler);
        recycler.setLayoutManager(mLayoutManager);

        Button btn = (Button) findViewById(R.id.f_btn_end);
        btn.setOnClickListener(this);
    }

    void initByTp() {
        ColorStateList color;
        switch (routeTp) {
            case "공항버스":
                color = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.sky));
                tv1.setTextColor(color);
                break;

            case "마을버스":
                color = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.village));
                tv1.setTextColor(color);
                break;

            case "간선버스":
                color = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.blue));
                tv1.setTextColor(color);
                break;

            case "지선버스":
                color = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green));
                tv1.setTextColor(color);
                break;

            case "순환버스":
                color = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.yellow));
                tv1.setTextColor(color);
                break;

            case "광역버스":
                color = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red));
                tv1.setTextColor(color);
                break;
        }
    }

    void initStation() {
        items = new ArrayList<>();

        String[] staSeqArr = this.staSeqArr.split(",");
        String[] staNmArr = this.staNmArr.split(",");

        for (int i = 0; i < staNmArr.length; i++) {
            F_SituationItem item = new F_SituationItem();
            if (i == 0) {
                item.setImgWay(R.drawable.direct_f_head);
            } else if (i + 1 == staNmArr.length - conditionValue) {
                item.setImgWay(R.drawable.direct_f_alarm);
            } else if (i + 1 == staNmArr.length) {
                item.setImgWay(R.drawable.direct_f_stop);
            } else {
                item.setImgWay(R.drawable.direct_f_progress);
            }
            item.setStaSeq(staSeqArr[i]);
            item.setStaNm(staNmArr[i]);
            items.add(item);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart() : stopService()");
        stopService(sIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart() : registerBR()");
        registerBR();
    }

    void registerBR() {
        final Util util = new Util();
        final Snackbar snackbar = getSnack();

        if (util.isNetwork(this)) {
            snackbar.dismiss();
        } else {
            snackbar.show();
        }

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                    if (util.isNetwork(context)) {
                        snackbar.dismiss();
                        new getPosByVehId().execute();
                    } else {
                        snackbar.show();
                        new CustomNotification(F_Situation.this).cancelNotification();
                    }

                }
            }
        };

        registerReceiver(br, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    Snackbar getSnack() {
        return Snackbar.make(recycler, "인터넷에 연결되지 않았어요.", Snackbar.LENGTH_INDEFINITE)
                .setAction("알람해제", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                stopService(sIntent);
                                Log.e(TAG, "onClick snackBar : stopService()");
                            }
                        }, 1500);

                        finish();
                        Intent mIntent = new Intent(F_Situation.this, B_Main.class);
                        startActivity(mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume() : mTimer.schedule()");
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e(TAG, "Timer() : I'm still alive.");
                if (new Util().isNetwork(F_Situation.this))
                    new getPosByVehId().execute();
            }
        }, 15000, 15000);
    }

    private class getPosByVehId extends AsyncTask<Void, Void, String[]> {

        Parcelable recyclerState;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            recyclerState = recycler.getLayoutManager().onSaveInstanceState();
        }

        @Override
        protected String[] doInBackground(Void... voids) {
            String xmlStr = null;
            String[] arrPos = new String[2];
            try {
                URL url = new URL(new Config().POS_URL_VEHID + vehId);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");

                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }

                rd.close();
                conn.disconnect();
                Log.e(TAG, "POS_URL_VEHID : " + sb.toString());

                xmlStr = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                assert xmlStr != null;
                InputStream is = new ByteArrayInputStream(xmlStr.getBytes());
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = factory.newDocumentBuilder();
                Document doc = documentBuilder.parse(is);
                Element element = doc.getDocumentElement();

                NodeList items1 = element.getElementsByTagName("stOrd");
                NodeList items2 = element.getElementsByTagName("stopFlag");

                String stOrd = items1.item(0).getFirstChild().getNodeValue();
                String stopFlag = items2.item(0).getFirstChild().getNodeValue();

                arrPos[0] = stOrd;
                arrPos[1] = stopFlag;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return arrPos;
        }

        @Override
        protected void onPostExecute(String[] arrPos) {
            super.onPostExecute(arrPos);
            String stOrd = arrPos[0];
            String stopFlag = arrPos[1];
            F_SituationAdapter adapter = new F_SituationAdapter(items, stOrd, stopFlag);
            recycler.setAdapter(adapter);
            recycler.getLayoutManager().onRestoreInstanceState(recyclerState);

            int ordAlarm = (posStop + 1) - conditionValue;
            int ordBus = Integer.parseInt(stOrd);
            int ordRemain = (posStop + 1) - ordBus;

            CustomNotification noti = new CustomNotification(F_Situation.this);
            if (ordBus < ordAlarm) {
                tv3.setText(String.valueOf(ordRemain));
                Util util = new Util();
                if (util.getSharedPre(F_Situation.this, "switchSound").equals("true")) {
                    noti.setNotification(true, ordRemain, nowStaNm(stOrd));
                    util.saveSharedPre(F_Situation.this, "switchSound", "false");
                } else {
                    noti.setNotification(false, ordRemain, nowStaNm(stOrd));
                }

            } else {
                noti.cancelNotification();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        stopService(sIntent);
                        Log.e(TAG, "before G_AlarmByResume.class : stopService()");
                    }
                }, 1500);

                finish();
                Intent mIntent = new Intent(F_Situation.this, G_AlarmByResume.class);
                startActivity(mIntent.putExtra("ordRemain", ordRemain));
            }
        }

        String nowStaNm(String stOrd) {
            String[] staSeqArr = F_Situation.this.staSeqArr.split(",");
            String[] staNmArr = F_Situation.this.staNmArr.split(",");

            String nowStaNm = null;
            for (int i = 0; i < staNmArr.length; i++) {
                if (stOrd.equals(staSeqArr[i])) {
                    nowStaNm = staNmArr[i];
                    break;
                }
            }
            return nowStaNm;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause() : mTimer.cancel()");
        mTimer.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop() : startService(), unregisterBR()");
        startService(sIntent);
        unregisterBR();
    }

    void unregisterBR() {
        try {
            unregisterReceiver(br);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy() : mTimer.cancel()");
        mTimer.cancel();
    }
}