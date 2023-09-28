package com.matainja.bootapplication.session;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashMap;



public class SessionManagement {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "MyPref";

    // All Shared Preferences Keys

    public static final String IS_WAKEUP = "IsWakeUp";
    public static final String IS_AUTOSTART = "IsAutoStart";
    public static final String IS_KEEPONTOP = "IsKeepOnTop";

    // Constructor

    public SessionManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createWakeupSession(boolean wakeStatus ){
        // Storing login value as TRUE
        editor.putBoolean(IS_WAKEUP, wakeStatus);


        // commit changes
        editor.commit();
    }
    public void createAutoStartSession(boolean autoStatus ){
        // Storing login value as TRUE
        editor.putBoolean(IS_AUTOSTART, autoStatus);


        // commit changes
        editor.commit();
    }
    public void createKeepOnTopSession(boolean keepOnStatus ){
        // Storing login value as TRUE
        editor.putBoolean(IS_KEEPONTOP, keepOnStatus);
        // commit changes
        editor.commit();
    }
    /**
     * Get stored session data
     **/
    public HashMap<String, String> getWakeupDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(IS_WAKEUP, String.valueOf(pref.getBoolean(IS_WAKEUP, false)));
        // return user
        return user;
    }
    public HashMap<String, String> getAutoStartDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(IS_AUTOSTART, String.valueOf(pref.getBoolean(IS_AUTOSTART, false)));
        // return user
        return user;
    }
    public HashMap<String, String> getKeepOnTopDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(IS_KEEPONTOP, String.valueOf(pref.getBoolean(IS_KEEPONTOP, false)));
        // return user
        return user;
    }
}