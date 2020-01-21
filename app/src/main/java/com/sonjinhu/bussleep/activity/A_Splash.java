package com.sonjinhu.bussleep.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sonjinhu.bussleep.R;

/**
 * Created by sonjh on 2016-05-26.
 */

public class A_Splash extends Activity {

    int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_a_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(A_Splash.this, B_Main.class));
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
