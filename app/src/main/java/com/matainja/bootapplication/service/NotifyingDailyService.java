package com.matainja.bootapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.matainja.bootapplication.activity.MainActivity;

public class NotifyingDailyService extends Service {

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent pIntent, int flags, int startId) {
        // TODO Auto-generated method stub
        Intent newIntent= new Intent(this, MainActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        this.startActivity(newIntent);
        Log.i("tag","NotifyingDailyService");
    
        return super.onStartCommand(pIntent, flags, startId);
    }
}