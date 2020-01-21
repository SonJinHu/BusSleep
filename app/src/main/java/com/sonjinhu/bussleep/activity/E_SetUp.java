package com.sonjinhu.bussleep.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sonjinhu.bussleep.R;
import com.sonjinhu.bussleep.fragment.E_FRG_Pref_SetUp;
import com.sonjinhu.bussleep.util.Config;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

//'D_getPosAdapter'에서 넘어왔습니다.
public class E_SetUp extends Activity implements View.OnClickListener {

    public static Activity setBusActivity;
    String TAG = getClass().getSimpleName();

    String routeNo, routeTp, plainNo, vehId, remainNo, rideStaNm, stopStaNm;
    int posStop;
    TextView tv1;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.e_btn:
                Util util = new Util();
                if (util.isNetwork(this)) {
                    util.saveSharedPre(this, "switchSound", "true");
                    nextByCheck();
                } else {
                    Snackbar snackbar = util.getSnack(v);
                    snackbar.show();
                }
                break;
        }
    }

    @SuppressLint("BatteryLife")
    void nextByCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(getPackageName())) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            } else {
                new checkPosByVehId().execute();
            }
        } else {
            new checkPosByVehId().execute();
        }
    }

    private class checkPosByVehId extends AsyncTask<Void, Void, String> {
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
                stOrd = items1.item(0).getFirstChild().getNodeValue();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return stOrd;
        }

        @Override
        protected void onPostExecute(String stOrd) {
            super.onPostExecute(stOrd);
            SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(E_SetUp.this);
            String conditionValueStr = mPref.getString("pref_condition_list", "1");
            int conditionValue = Integer.parseInt(conditionValueStr);

            int ordAlarm = (posStop + 1) - conditionValue;
            int ordBus = Integer.parseInt(stOrd);

            if (ordAlarm <= ordBus) {
                warningDialog();
            } else {
                startActivity(new Intent(E_SetUp.this, F_Situation.class));
            }
        }

        void warningDialog() {
            Dialog dialog = new Dialog(E_SetUp.this);
            if (dialog.getWindow() != null)
                dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
            dialog.setContentView(R.layout.dialog_warning);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    finish();
                }
            });
            dialog.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_e_setup);
        setBusActivity = this;

        getFragmentManager().beginTransaction().replace(R.id.e_frame, new E_FRG_Pref_SetUp()).commit();

        initPref();
        initText();
        initByTp();

        Button btn = (Button) findViewById(R.id.e_btn);
        btn.setOnClickListener(this);
    }

    void initPref() {
        Util util = new Util();
        routeNo = util.getSharedPre(this, "routeNo");
        routeTp = util.getSharedPre(this, "routeTp");
        plainNo = util.getSharedPre(this, "plainNo");
        vehId = util.getSharedPre(this, "vehId");
        remainNo = util.getSharedPre(this, "remainNo");
        rideStaNm = util.getSharedPre(this, "rideStaNm");
        stopStaNm = util.getSharedPre(this, "stopStaNm");

        String posStopStr = util.getSharedPre(this, "posStop");
        posStop = Integer.parseInt(posStopStr);
    }

    void initText() {
        View include = findViewById(R.id.e_include_main);
        tv1 = (TextView) include.findViewById(R.id.e_include_tv_routeNo);
        TextView tv2 = (TextView) include.findViewById(R.id.e_include_tv_plainNo);
        TextView tv3 = (TextView) include.findViewById(R.id.e_include_tv_remainNo);
        TextView tv4 = (TextView) include.findViewById(R.id.e_include_tv_rideStaNm);
        TextView tv5 = (TextView) include.findViewById(R.id.e_include_tv_stopStaNm);
        tv1.setText(routeNo);
        tv2.setText(plainNo);
        tv3.setText(remainNo);
        tv4.setText(rideStaNm);
        tv5.setText(stopStaNm);
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
}