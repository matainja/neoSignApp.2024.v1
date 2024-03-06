package com.matainja.bootapplication.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.matainja.bootapplication.Model.ContentModel;
import com.matainja.bootapplication.Model.RSSModel;

import java.util.HashMap;
import java.util.List;


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
    public static final String PAIRING_CODE = "ParingCode";
    public static final String PAIRING_STATUS = "PairingStatus";
    public static final String ORIENTATION = "orientation";
    public static final String STRECH = "strech";
    // Constructor

    public SessionManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void createStrechSession(String strech ){
        editor.putString(STRECH, strech);
        // commit changes
        editor.commit();
    }
    public void createOrientationSession(String orientation ){
        editor.putString(ORIENTATION, orientation);
        // commit changes
        editor.commit();
    }
    public void createPairingSession(String paringCode ){
        editor.putString(PAIRING_CODE, paringCode);
        // commit changes
        editor.commit();
    }
    public void createWakeupSession(boolean wakeStatus ){
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
        editor.putBoolean(IS_KEEPONTOP, keepOnStatus);
        // commit changes
        editor.commit();
    }
    public void createPairingSession(boolean pairingStatus ){
        editor.putBoolean(PAIRING_STATUS, pairingStatus);
        // commit changes
        editor.commit();
    }

    public void createContentDataSession( List<ContentModel> slideItems ){
        editor.putString("slideItem", new Gson().toJson(slideItems)).apply();
    }

    public void createContentRssFeedDataSession(List<RSSModel> rssContent, String newString){
        Log.e("Tag","sesionSave>>>"+newString);

        editor.putString("rssfeed"+newString, new Gson().toJson(rssContent)).apply();
    }
    public void createdisplayContentRssFeedDataSession(List<RSSModel> rssContent, String newString){
        editor.putString("displayrssfeed", new Gson().toJson(rssContent)).apply();
    }

    /**
     * Get stored session data
     **/
    public HashMap<String, String> getWakeupDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(IS_WAKEUP, String.valueOf(pref.getBoolean(IS_WAKEUP, true)));
        // return user
        return user;
    }
    public HashMap<String, String> getAutoStartDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(IS_AUTOSTART, String.valueOf(pref.getBoolean(IS_AUTOSTART, true)));
        // return user
        return user;
    }
    public HashMap<String, String> getKeepOnTopDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(IS_KEEPONTOP, String.valueOf(pref.getBoolean(IS_KEEPONTOP, true)));
        // return user
        return user;
    }
    public HashMap<String, String> getParingDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(PAIRING_CODE, pref.getString(PAIRING_CODE,""));
        // return user
        return user;
    }
    public HashMap<String, String> getOrientDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(ORIENTATION, pref.getString(ORIENTATION,"Landscape"));
        // return user
        return user;
    }
    public HashMap<String, String> getStrechDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(STRECH, pref.getString(STRECH,"off"));
        // return user
        return user;
    }
    public HashMap<String, String> getPairingStatusDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(PAIRING_STATUS, String.valueOf(pref.getBoolean(PAIRING_STATUS, false)));
        // return user
        return user;
    }
    public HashMap<String, String> getContentItemDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put("slideItem", pref.getString("slideItem",""));
        // return user
        return user;
    }
    public HashMap<String, String> getContentRssFeedItemDetails(String newString){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put("rssfeed"+newString, pref.getString("rssfeed"+newString,""));
        // return user
        return user;
    }
    public HashMap<String, String> getdisplayContentRssFeedItemDetails(String newString){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put("displayrssfeed", pref.getString("displayrssfeed",""));
        // return user
        return user;
    }

    //Session clear
    public void clearSession(){
        Log.e("Tag","remove>>>2");
        editor.remove(ORIENTATION);
        editor.remove(STRECH);
        editor.remove("slideItem");
        editor.remove("displayrssfeed");
    // Apply the changes
        editor.apply();
    }
    public void clearRssFeedOverlaysSession(String laysId){
        Log.e("Tag","remove>>>1");
        editor.remove("rssfeed"+laysId);
        // Apply the changes
        editor.apply();
    }
}