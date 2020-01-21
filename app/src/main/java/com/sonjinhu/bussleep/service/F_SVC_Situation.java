package com.sonjinhu.bussleep.service;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.sonjinhu.bussleep.activity.C_Search;
import com.sonjinhu.bussleep.activity.D_Station;
import com.sonjinhu.bussleep.activity.E_SetUp;
import com.sonjinhu.bussleep.activity.F_Situation;
import com.sonjinhu.bussleep.activity.G_AlarmByService;
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
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class F_SVC_Situation extends Service {

    String TAG = getClass().getSimpleName();

    String vehId, staSeqArr, staNmArr;
    int posStop, conditionValue;

    PowerManager.WakeLock mWakeLock;
    Timer mTimer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initPref();

        PowerManager mPowerMgr = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = mPowerMgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "cpuWakeLock");
        mWakeLock.acquire();
    }

    void initPref() {
        Util util = new Util();
        vehId = util.getSharedPre(this, "vehId");
        staSeqArr = util.getSharedPre(this, "staSeqArr");
        staNmArr = util.getSharedPre(this, "staNmArr");

        String posStopStr = util.getSharedPre(this, "posStop");
        posStop = Integer.parseInt(posStopStr);

        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
        String conditionValueStr = mPref.getString("pref_condition_list", "1");
        conditionValue = Integer.parseInt(conditionValueStr);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (new Util().isNetwork(F_SVC_Situation.this)) {

                    new getPosByVehId().execute();

                } else {

                    CustomNotification noti = new CustomNotification(F_SVC_Situation.this);
                    PowerManager mPowerMgr = (PowerManager) getSystemService(POWER_SERVICE);

                    stopSelf();
                    noti.cancelNotification();
                    endActivity();

                    if (mPowerMgr.isScreenOn()) {
                        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
                        boolean isKeyLocked = km.inKeyguardRestrictedInputMode();

                        if (isKeyLocked) {
                            Intent mIntent = new Intent(F_SVC_Situation.this, G_AlarmByService.class);
                            mIntent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            mIntent.putExtra("ordRemain", 999);
                            startActivity(mIntent);
                        } else {
                            noti.setHeadUpNotiNetwork();
                        }

                    } else {
                        Intent mIntent = new Intent(F_SVC_Situation.this, G_AlarmByService.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        mIntent.putExtra("ordRemain", 999);
                        startActivity(mIntent);
                    }

                }
            }
        }, 15000, 15000);
        return super.onStartCommand(intent, flags, startId);
    }

    private class getPosByVehId extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String xmlStr = null;
            String stOrd = null;
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
                stOrd = items1.item(0).getFirstChild().getNodeValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stOrd;
        }

        @Override
        protected void onPostExecute(String stOrd) {
            super.onPostExecute(stOrd);
            int ordAlarm = (posStop + 1) - conditionValue;
            int ordBus = Integer.parseInt(stOrd);
            int ordRemain = (posStop + 1) - ordBus;

            Log.e(TAG, "busPos / alrPos : " + ordBus + " / " + ordAlarm);

            CustomNotification noti = new CustomNotification(F_SVC_Situation.this);
            if (ordBus < ordAlarm) {
                noti.setNotification(false, ordRemain, nowStaNm(stOrd));
            } else {
                stopSelf();
                noti.cancelNotification();
                endActivity();
                setAlarmByIsScreenOn(ordRemain, nowStaNm(stOrd));
            }
        }

        String nowStaNm(String stOrd) {
            String[] staSeqArr = F_SVC_Situation.this.staSeqArr.split(",");
            String[] staNmArr = F_SVC_Situation.this.staNmArr.split(",");

            String nowStaNm = null;
            for (int i = 0; i < staNmArr.length; i++) {
                if (stOrd.equals(staSeqArr[i])) {
                    nowStaNm = staNmArr[i];
                    break;
                }
            }
            return nowStaNm;
        }

        void setAlarmByIsScreenOn(int ordRemain, String nowStaNm) {
            PowerManager mPowerMgr = (PowerManager) getSystemService(POWER_SERVICE);

            if (mPowerMgr.isScreenOn()) {
                KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
                boolean isKeyLocked = km.inKeyguardRestrictedInputMode();

                if (isKeyLocked) {
                    Intent mIntent = new Intent(F_SVC_Situation.this, G_AlarmByService.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    mIntent.putExtra("ordRemain", ordRemain);
                    startActivity(mIntent);
                } else {

                    String contentText;
                    if (ordRemain == 0) {
                        contentText = "하차정류장 근처에요!!!";
                    } else if (ordRemain < 0) {
                        int abs = Math.abs(ordRemain);
                        contentText = "죄송해요... " + abs + "정류장 지나쳤어요...";
                    } else {
                        contentText = ordRemain + "정류장 전이에요!";
                    }

                    new CustomNotification(F_SVC_Situation.this).setHeadUpNoti(contentText, nowStaNm);

                }

            } else {
                Intent mIntent = new Intent(F_SVC_Situation.this, G_AlarmByService.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                mIntent.putExtra("ordRemain", ordRemain);
                startActivity(mIntent);
            }
        }
    }

    void endActivity() {
        if (C_Search.searchActivity != null) C_Search.searchActivity.finish();
        if (D_Station.getStationActivity != null) D_Station.getStationActivity.finish();
        if (E_SetUp.setBusActivity != null) E_SetUp.setBusActivity.finish();
        if (F_Situation.alarmActivity != null) F_Situation.alarmActivity.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWakeLock.release();
        mTimer.cancel();
    }
}