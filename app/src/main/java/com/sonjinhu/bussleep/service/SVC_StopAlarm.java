package com.sonjinhu.bussleep.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

/**
 * Created by sonjh on 2017-02-26.
 */

// CustomNotification in com.sonjinhu.bussleep.util
public class SVC_StopAlarm extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationManager mMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mMgr.cancel(12345);

        stopService(new Intent(this, SVC_StartAlarm.class));
        stopSelf();

        return super.onStartCommand(intent, flags, startId);
    }
}