package com.cikoapps.rigatransport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


/**
 * Creation date 1/27/2015
 * -------------------------
 * Modified 2/1/2015 by Arvis code formatting
 */
public class SplashScreenActivity extends Activity {

    private final static int SPLASH_SCREEN_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        }, SPLASH_SCREEN_TIME);
    }
}
