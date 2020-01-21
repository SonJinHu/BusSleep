package com.sonjinhu.bussleep.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sonjh on 2017-04-20.
 */

public class Util {
    public void saveSharedPre(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("BusSleep", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getSharedPre(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("BusSleep", MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public boolean isNetwork(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            if (activeNetwork != null && activeNetwork.isConnected())
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Snackbar getSnack(View v) {
        return Snackbar.make(v, "인터넷에 연결되지 않았어요.", Snackbar.LENGTH_LONG);
    }
}