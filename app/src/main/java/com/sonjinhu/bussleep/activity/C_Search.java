package com.sonjinhu.bussleep.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sonjinhu.bussleep.R;
import com.sonjinhu.bussleep.adapter.C_getRouteAdapter;
import com.sonjinhu.bussleep.adapter.C_getRouteItem;
import com.sonjinhu.bussleep.fragment.C_FRG_NoNetwork;
import com.sonjinhu.bussleep.fragment.C_FRG_NoResult;
import com.sonjinhu.bussleep.fragment.C_FRG_Route;
import com.sonjinhu.bussleep.fragment.C_FRG_AskSearch;
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
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class C_Search extends AppCompatActivity implements View.OnClickListener {

    public static Activity searchActivity;
    public static C_getRouteAdapter adapter;
    String TAG = getClass().getSimpleName();

    EditText edit;
    Timer timer;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.c_compat_cancel:
                edit.setText("");
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_c_search);
        searchActivity = this;

        getFragmentManager().beginTransaction().replace(R.id.c_frame, new C_FRG_AskSearch()).commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.c_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_identity_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initEdit();
    }

    void initEdit() {
        final AppCompatImageView cancel = (AppCompatImageView) findViewById(R.id.c_compat_cancel);
        cancel.setOnClickListener(this);

        edit = (EditText) findViewById(R.id.c_edit);
        edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (edit.length() != 0) {
                    searchBus();
                } else {
                    getFragmentManager().beginTransaction().replace(R.id.c_frame, new C_FRG_AskSearch()).commit();
                }
                return true;
            }
        });
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (timer != null) timer.cancel();
                if (edit.length() != 0) {
                    cancel.setVisibility(View.VISIBLE);
                } else {
                    cancel.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edit.length() != 0) {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            searchBus();
                        }
                    }, 500);
                } else {
                    getFragmentManager().beginTransaction().replace(R.id.c_frame, new C_FRG_AskSearch()).commit();
                }
            }
        });
    }

    void searchBus() {
        if (new Util().isNetwork(this)) {
            new getRouteList().execute();
        } else {
            getFragmentManager().beginTransaction().replace(R.id.c_frame, new C_FRG_NoNetwork()).commit();
        }
    }

    private class getRouteList extends AsyncTask<Void, Void, C_getRouteAdapter> {

        String searchNo;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            searchNo = edit.getText().toString();
        }

        @Override
        protected C_getRouteAdapter doInBackground(Void... voids) {
            try {
                URL url = new URL(new Config().ROUTE_URL + searchNo);

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
                Log.e(TAG, "ROUTE_URL : " + sb.toString());

                ArrayList<C_getRouteItem> items = parseXml(sb.toString());
                adapter = new C_getRouteAdapter(items);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return adapter;
        }

        @Override
        protected void onPostExecute(C_getRouteAdapter adapter) {
            super.onPostExecute(adapter);
            if (adapter.getCount() != 0) {
                getFragmentManager().beginTransaction().replace(R.id.c_frame, new C_FRG_Route()).commit();
            } else {
                getFragmentManager().beginTransaction().replace(R.id.c_frame, new C_FRG_NoResult()).commit();
            }
        }

        ArrayList<C_getRouteItem> parseXml(String xmlStr) {

            ArrayList<C_getRouteItem> items = new ArrayList<>();

            try {
                InputStream is = new ByteArrayInputStream(xmlStr.getBytes());

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = factory.newDocumentBuilder();
                Document doc = docBuilder.parse(is);
                Element element = doc.getDocumentElement();

                NodeList nodeList1 = element.getElementsByTagName("itemList");
                NodeList nodeList2 = element.getElementsByTagName("busRouteId");
                NodeList nodeList3 = element.getElementsByTagName("busRouteNm");
                NodeList nodeList4 = element.getElementsByTagName("corpNm");
                NodeList nodeList5 = element.getElementsByTagName("routeType");
                NodeList nodeList6 = element.getElementsByTagName("stStationNm");
                NodeList nodeList7 = element.getElementsByTagName("edStationNm");

                for (int i = 0; i < nodeList1.getLength(); i++) {
                    String busRouteId = nodeList2.item(i).getFirstChild().getNodeValue();
                    String busRouteNm = nodeList3.item(i).getFirstChild().getNodeValue();
                    String corpNm = nodeList4.item(i).getFirstChild().getNodeValue();
                    String routeType = nodeList5.item(i).getFirstChild().getNodeValue();
                    String stStationNm = nodeList6.item(i).getFirstChild().getNodeValue();
                    String edStationNm = nodeList7.item(i).getFirstChild().getNodeValue();

                    if (!routeType.equals("0") && !routeType.equals("7") && !routeType.equals("8") && !routeType.equals("9")) {
                        if (!corpNm.substring(0, 2).equals("경기")) {
                            if (filterNm(busRouteNm)) {
                                C_getRouteItem item = new C_getRouteItem();

                                item.setRouteId(busRouteId);
                                item.setRouteNm(busRouteNm);
                                item.setRouteTp(convertType(routeType));
                                item.setRouteTpColor(convertColor(routeType));
                                item.setStartNodeNm(stStationNm);
                                item.setEndNodeNm(edStationNm);

                                items.add(item);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return items;
        }

        boolean filterNm(String routeName) {
            boolean mSwitch = true;
            String busRouteNm = "";

            for (int i = 0; i < routeName.length() && mSwitch; i++) {
                if (routeName.charAt(i) <= '9') {
                    if (routeName.charAt(i) != '-') {
                        busRouteNm += routeName.charAt(i);
                    } else {
                        mSwitch = false;
                    }
                }
            }
            return searchNo.equals(busRouteNm.substring(0, searchNo.length())) || Integer.parseInt(searchNo) == Integer.parseInt(busRouteNm);
        }

        String convertType(String routeType) {
            switch (routeType) {
                case "0":
                    routeType = "공용버스";
                    break;
                case "1":
                    routeType = "공항버스";
                    break;
                case "2":
                    routeType = "마을버스";
                    break;
                case "3":
                    routeType = "간선버스";
                    break;
                case "4":
                    routeType = "지선버스";
                    break;
                case "5":
                    routeType = "순환버스";
                    break;
                case "6":
                    routeType = "광역버스";
                    break;
                case "7":
                    routeType = "인천버스";
                    break;
                case "8":
                    routeType = "경기버스";
                    break;
                case "9":
                    routeType = "폐지노선";
                    break;
            }
            return routeType;
        }

        int convertColor(String routeType) {
            int textColor = 0;
            switch (routeType) {
                case "1":
                    textColor = ContextCompat.getColor(getBaseContext(), R.color.sky);
                    break;
                case "2":
                    textColor = ContextCompat.getColor(getBaseContext(), R.color.village);
                    break;
                case "3":
                    textColor = ContextCompat.getColor(getBaseContext(), R.color.blue);
                    break;
                case "4":
                    textColor = ContextCompat.getColor(getBaseContext(), R.color.green);
                    break;
                case "5":
                    textColor = ContextCompat.getColor(getBaseContext(), R.color.yellow);
                    break;
                case "6":
                    textColor = ContextCompat.getColor(getBaseContext(), R.color.red);
                    break;
            }
            return textColor;
        }
    }
}