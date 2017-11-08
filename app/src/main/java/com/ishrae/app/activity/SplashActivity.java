package com.ishrae.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ishrae.app.R;
import com.ishrae.app.model.SharedPref;
import com.ishrae.app.notification.FireBaseInstanceIDService;
import com.ishrae.app.utilities.Util;

public class SplashActivity extends BaseAppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    Util.setDeviceSreenH(SplashActivity.this);
                    if (SharedPref.isLogin(SplashActivity.this)) {
                        Util.startActivityWithFinish(SplashActivity.this, DashboardActivity.class);

                    } else {

                        Util.startActivityWithFinish(SplashActivity.this, LoginActivity.class);
                    }

                    overridePendingTransition(R.anim.feed_in, R.anim.feed_out);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }
}
