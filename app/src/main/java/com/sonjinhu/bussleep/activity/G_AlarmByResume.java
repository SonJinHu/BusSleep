package com.sonjinhu.bussleep.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.sonjinhu.bussleep.R;

public class G_AlarmByResume extends Activity implements View.OnClickListener {

    String wayValue, ringtoneValue;
    int ordRemain;

    Vibrator vibrator;
    Ringtone ringtone;

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.g_btn_close:
                finish();
                Intent mIntent = new Intent(this, B_Main.class);
                startActivity(mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_g_alarm);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initPref();
        initText();

        Button btn = (Button) findViewById(R.id.g_btn_close);
        btn.setOnClickListener(this);

        startAlarm();
    }

    void initPref() {
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
        wayValue = mPref.getString("pref_way_list", "1");
        ringtoneValue = mPref.getString("pref_sound_ringtone", "content://settings/system/alarm_alert");

        ordRemain = getIntent().getIntExtra("ordRemain", 0);
    }

    void initText() {
        TextView tv = (TextView) findViewById(R.id.g_tv_result);

        if (ordRemain == 0) {
            tv.setText("하차정류장 근처에요!!!");
        } else if (ordRemain < 0) {
            int abs = Math.abs(ordRemain);
            tv.setText("죄송해요... " + abs + "정류장 지나쳤어요...");
        } else {
            tv.setText("" + ordRemain + "정류장 전이에요!");
        }
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

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        Intent mIntent = new Intent(this, B_Main.class);
        startActivity(mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopAlarm();
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