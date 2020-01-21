package com.sonjinhu.bussleep.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;

/**
 * Created by sonjh on 2017-02-26.
 */

// CustomNotification in com.sonjinhu.bussleep.util
public class SVC_StartAlarm extends Service {

    String wayValue, ringtoneValue;

    Vibrator vibrator;
    Ringtone ringtone;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initPref();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startAlarm();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAlarm();
    }

    void initPref() {
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
        wayValue = mPref.getString("pref_way_list", "1");
        ringtoneValue = mPref.getString("pref_sound_ringtone", "content://settings/system/alarm_alert");
    }

    void startAlarm() {
        long[] pattern = {1000, 1000};
        switch (wayValue) {
            case "1": // 소리
                ringtone = RingtoneManager.getRingtone(this, Uri.parse(ringtoneValue));
                ringtone.setStreamType(AudioManager.STREAM_ALARM);
                ringtone.play();
                break;
            case "0": // 진동
                vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(pattern, 0);
                break;
            case "-1": // 진동 및 소리
                vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(pattern, 0);
                ringtone = RingtoneManager.getRingtone(this, Uri.parse(ringtoneValue));
                ringtone.setStreamType(AudioManager.STREAM_ALARM);
                ringtone.play();
                break;
        }
    }

    void stopAlarm() {
        switch (wayValue) {
            case "1": // 소리
                ringtone.stop();
                break;
            case "0": // 진동
                vibrator.cancel();
                break;
            case "-1": // 진동 및 소리
                vibrator.cancel();
                ringtone.stop();
                break;
        }
    }
}
