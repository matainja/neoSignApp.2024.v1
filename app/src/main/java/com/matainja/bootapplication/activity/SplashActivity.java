package com.matainja.bootapplication.activity;

import static com.matainja.bootapplication.session.SessionManagement.IS_AUTOSTART;
import static com.matainja.bootapplication.session.SessionManagement.IS_KEEPONTOP;
import static com.matainja.bootapplication.session.SessionManagement.IS_WAKEUP;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.matainja.bootapplication.R;
import com.matainja.bootapplication.helper.DefaultExceptionHandler;
import com.matainja.bootapplication.session.SessionManagement;

import java.util.HashMap;


public class SplashActivity extends AppCompatActivity {
    Handler handler;
    ProgressBar progress;
    SessionManagement sessionManagement;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    boolean isWakeUP,isAutoStart,isKeepOnTop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initSession();

        progress=(ProgressBar)findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        handler = new Handler();
        Runnable r = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                i.putExtra("callfrom","splash");
                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // Staring Main Activity
                SplashActivity.this.startActivity(i);
                Log.e("SessionManagement"," Inside checkLogin IFFFF");

                SplashActivity.this.finish();
            }
        };
        handler.postDelayed(r, 1000);
    }
    private void initSession() {
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sessionManagement = new SessionManagement(SplashActivity.this);

        HashMap<String, String> getUserDetails = new HashMap<String, String>();
        getUserDetails = sessionManagement.getWakeupDetails();
        isWakeUP = Boolean.parseBoolean(getUserDetails.get(IS_WAKEUP));

        HashMap<String, String> getAutoStartDetails = new HashMap<String, String>();
        getAutoStartDetails = sessionManagement.getAutoStartDetails();
        isAutoStart = Boolean.parseBoolean(getAutoStartDetails.get(IS_AUTOSTART));
        Log.e("TAG", "isWakeUP>>> " + isWakeUP);

        HashMap<String, String> getKeepOnTopDetails = new HashMap<String, String>();
        getKeepOnTopDetails = sessionManagement.getKeepOnTopDetails();
        isKeepOnTop = Boolean.parseBoolean(getKeepOnTopDetails.get(IS_KEEPONTOP));

        if (isKeepOnTop){
            Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        }

    }
}