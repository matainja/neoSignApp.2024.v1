package com.matainja.bootapplication.reciever;

import static com.matainja.bootapplication.session.SessionManagement.IS_AUTOSTART;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.matainja.bootapplication.activity.MainActivity;
import com.matainja.bootapplication.activity.SplashActivity;
import com.matainja.bootapplication.session.SessionManagement;

import java.util.HashMap;


public class BootReceiver extends BroadcastReceiver {
    SessionManagement sessionManagement;
    SharedPreferences sharedPreferences;
    boolean isAutoStart;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
        {
            Log.e("tag","recieved");
            sessionManagement = new SessionManagement(context);
            HashMap<String, String> autoStartDetails = new HashMap<String, String>();
            autoStartDetails = sessionManagement.getAutoStartDetails();
            isAutoStart = Boolean.parseBoolean(autoStartDetails.get(IS_AUTOSTART));
            Log.e("Tag","isAutoStart1>>>"+isAutoStart);
            if (isAutoStart){
                Intent newIntent= new Intent(context, SplashActivity.class);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    context.startActivity(newIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{

            }


            Log.e("tag","recieved1");
        }
        else{
            Log.e("tag","not recieved");
        }

    }
}
