package com.sonjinhu.bussleep.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.preference.PreferenceManager;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.sonjinhu.bussleep.R;
import com.sonjinhu.bussleep.activity.B_Main;
import com.sonjinhu.bussleep.activity.F_Situation;
import com.sonjinhu.bussleep.service.SVC_StartAlarm;
import com.sonjinhu.bussleep.service.SVC_StopAlarm;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by sonjh on 2017-04-24.
 */

public class CustomNotification {

    private Context context;
    private String stopStaNm, conditionValue;

    public CustomNotification(Context context) {
        this.context = context;
        stopStaNm = new Util().getSharedPre(context, "stopStaNm");
        conditionValue = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString("pref_condition_list", "1");
    }

    public void setNotification(boolean isFirst, int ordRemain, String nowStaNm) {
        PendingIntent pIntent =
                PendingIntent.getActivity(context, 0, new Intent(context, F_Situation.class), 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.app_notification)
                .setContentTitle("하차지 : " + stopStaNm)
                .setContentText(ordRemain + "정류장 남았어요. " + conditionValue + "정류장 전에 알려드릴게요!")
                .setColor(ContextCompat.getColor(context, R.color.identity))

                .setOngoing(true)
                .setWhen(System.currentTimeMillis())
                .setTicker("BUS SLEEP 알람등록")
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pIntent);

        if (isFirst) {
            mBuilder.setDefaults(android.app.Notification.DEFAULT_SOUND | android.app.Notification.DEFAULT_VIBRATE);
        }

        if (Build.VERSION.SDK_INT > 23) {
            mBuilder.setSubText("현위치 : " + nowStaNm);
        }

        NotificationManager mMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mMgr.notify(170302, mBuilder.build());
    }

    public void cancelNotification() {
        NotificationManager mMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mMgr.cancel(170302);
    }

    public void setHeadUpNoti(String contentText, String nowStaNm) {
        PendingIntent alrOffIntent =
                PendingIntent.getService(context, 0, new Intent(context, SVC_StopAlarm.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.app_notification)
                .setContentTitle("하차지 : " + stopStaNm)
                .setContentText(contentText)
                .setColor(ContextCompat.getColor(context, R.color.identity))
                .addAction(0, "알람해제", alrOffIntent)

                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDeleteIntent(alrOffIntent)
                .setFullScreenIntent(alrOffIntent, true);

        if (23 < Build.VERSION.SDK_INT) {
            mBuilder.setSubText("현위치 : " + nowStaNm);
        }

        if (Build.VERSION.SDK_INT < 24) {
            mBuilder.setOngoing(true);
            mBuilder.setContentIntent(alrOffIntent);
        }

        NotificationManager mMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mMgr.notify(12345, mBuilder.build());

        Intent alrOnIntent = new Intent(context, SVC_StartAlarm.class);
        context.startService(alrOnIntent);
    }

    public void setHeadUpNotiNetwork() {
        PendingIntent pIntent =
                PendingIntent.getActivity(context, 0, new Intent(context, B_Main.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.app_notification)
                .setContentTitle("알람 자동종료")
                .setContentText("인터넷 연결이 안되요. ( T-T)")
                .setColor(ContextCompat.getColor(context, R.color.identity))

                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setFullScreenIntent(pIntent, true)
                .setDeleteIntent(pIntent)
                .setDefaults(android.app.Notification.DEFAULT_SOUND | android.app.Notification.DEFAULT_VIBRATE);

        if (Build.VERSION.SDK_INT < 24) {
            mBuilder.setOngoing(true);
            mBuilder.setContentIntent(pIntent);
        }

        NotificationManager mMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mMgr.notify(12345, mBuilder.build());
    }
}
