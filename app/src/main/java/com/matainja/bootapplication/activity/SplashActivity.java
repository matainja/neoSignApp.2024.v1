package com.matainja.bootapplication.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.matainja.bootapplication.R;


public class SplashActivity extends AppCompatActivity {
    Handler handler;
    ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
}