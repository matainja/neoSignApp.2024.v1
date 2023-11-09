package com.matainja.bootapplication.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.matainja.bootapplication.session.SessionManagement.IS_AUTOSTART;
import static com.matainja.bootapplication.session.SessionManagement.IS_KEEPONTOP;
import static com.matainja.bootapplication.session.SessionManagement.IS_WAKEUP;
import static com.matainja.bootapplication.session.SessionManagement.PAIRING_CODE;
import static com.matainja.bootapplication.session.SessionManagement.PAIRING_STATUS;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matainja.bootapplication.Model.ClockParams;
import com.matainja.bootapplication.Model.ContentModel;
import com.matainja.bootapplication.Model.RSSModel;
import com.matainja.bootapplication.R;
import com.matainja.bootapplication.session.SessionManagement;
import com.matainja.bootapplication.util.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity {
    CoordinatorLayout contentLay;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch autoStartSwitch;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch keepAwakeSwitch;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch keepOnTopSwitch;
    TextView auto_start,auto_start_title;
    PowerManager.WakeLock powerLatch;
    SessionManagement sessionManagement;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    boolean isWakeUP,isAutoStart,isKeepOnTop,isOptimizationPopUP=true,isAutoPopUP=true;
    boolean pairingStatus;
    private  String[] AndroidBatteryPermission=new String[] {Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS};
    public static final int PERMISSION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS =  10;
    DrawerLayout drawer;
    NavigationView navigationView;
    private MainActivity.MyReceiver MyReceiver=null;
    RelativeLayout parentPairing,parentVideoView,parentContentImage;
    private static final String Videos_URL = "https://app.neosign.tv/storage/app/public/content/147/videos/BXS0VN61JxLgDjtWbhqMgEb9nCEobaKDRdL1J1YI.mp4";
    ProgressBar progress_bar;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;
    private int position = 0;
    TextView pairingCode;
    String pairCode;
    String countdownText;
    String orientation="Landscape";
    String translationString;
    private Switch rootView1;
    private Switch rootView2;
    private Switch rootView3;
    private ViewGroup rootView4,rootView5;
    View nextView1,nextView2,nextView3,nextView4, nextView5, nextView6, nextView7, nextView8, nextView9, nextView10;
    TextView keep_awake,exit,keep_reload;
    ImageButton keepExit,keepReload;
    LinearLayout parentInternetLay;
    RelativeLayout parent_auto_start,parent_keep_awake,parent_keep_on_top,parent_reload,parent_exit;
    RelativeLayout webView_lay,parentContentRssFeed;
    TextView rssTitle,rssDescription,rssDate;
    ImageView rssImageView,rssQR;
    WebView myWebView;
    long newDuration=0;
    Handler handler;
    Runnable myRunnable;
    int currentIndex;
    int contentCurrentIndex=0;
    int slideShowCallCount=0;
    private int currentPage = 0;
    int firstdataCount=0;
    int count=0;
    String mUrl;
    private static final int FILECHOOSER_RESULTCODE   = 1;
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private String folderName="com.matainja";
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    ImageView content_image;
    VideoView videoView;
    ProgressBar progressBar,video_progress;
    List<ContentModel> slideItems = new ArrayList<>();
    List<ContentModel> newSlideItems = new ArrayList<>();
    @SuppressLint({"CutPasteId", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        initSession();





        //accessAllPermission();

        handler = new Handler();
        parentInternetLay=(LinearLayout) findViewById(R.id.parentInternetLay);
        contentLay=(CoordinatorLayout) findViewById(R.id.contentLay);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView =(NavigationView)findViewById(R.id.nav_view);


        parentPairing= findViewById(R.id.parentPairing);
        pairingCode = findViewById(R.id.pairingCode);
        parentContentRssFeed=(RelativeLayout)findViewById(R.id.parentContentRssFeed);
        rssImageView = findViewById(R.id.rssImageView);
        rssTitle = findViewById(R.id.rssTitle);
        rssDescription = findViewById(R.id.rssDescription);
        rssDate = findViewById(R.id.rssDate);
        rssQR = findViewById(R.id.rssQR);


        content_image = findViewById(R.id.content_image);
        videoView = findViewById(R.id.videoView);
        parentContentImage = findViewById(R.id.parentContentImage);
        parentVideoView = findViewById(R.id.parentVideoView);
        webView_lay = findViewById(R.id.webView_lay);
        myWebView=(WebView)findViewById(R.id.webview);
        progressBar=(ProgressBar)findViewById(R.id.progress_Bar);
        video_progress=(ProgressBar)findViewById(R.id.video_progress);
        View header = navigationView.getHeaderView(0);
        rootView1 = (Switch)header.findViewById(R.id.autoStartSwitch);
        rootView2 =  (Switch)header.findViewById(R.id.keepAwakeSwitch);
        rootView3 =  (Switch)header.findViewById(R.id.keepOnTopSwitch);
        rootView4 =  header.findViewById(R.id.parent_reload);
        rootView5 =  header.findViewById(R.id.parent_exit);

        autoStartSwitch=(Switch) header.findViewById(R.id.autoStartSwitch);
        keepAwakeSwitch=(Switch) header.findViewById(R.id.keepAwakeSwitch);
        keepOnTopSwitch=(Switch) header.findViewById(R.id.keepOnTopSwitch);
        keep_awake = (TextView) header.findViewById(R.id.keep_awake);
        exit = (TextView) header.findViewById(R.id.keep_exit);
        keep_reload = (TextView) header.findViewById(R.id.keep_reload);
        auto_start=(TextView)header.findViewById(R.id.auto_start);
        auto_start_title=(TextView)header.findViewById(R.id.auto_start_title);
        keepExit = (ImageButton) header.findViewById(R.id.keepExit);
        keepReload = (ImageButton) header.findViewById(R.id.keepReload);
        parent_auto_start=(RelativeLayout)header.findViewById(R.id.parent_auto_start);
        parent_keep_awake=(RelativeLayout)header.findViewById(R.id.parent_keep_awake);
        parent_keep_on_top=(RelativeLayout)header.findViewById(R.id.parent_keep_on_top);
        parent_reload=(RelativeLayout)header.findViewById(R.id.parent_reload);
        parent_exit=(RelativeLayout)header.findViewById(R.id.parent_exit);


        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sessionManagement = new SessionManagement(MainActivity.this);
        HashMap<String, String> getPairDetails = new HashMap<String, String>();
        getPairDetails = sessionManagement.getParingDetails();
        pairCode = getPairDetails.get(PAIRING_CODE);
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("Tag","deviceId>>>"+deviceId);


        if (pairCode.equals("")){
            // Generate a random number
            Random random = new Random();
            pairCode = UUID.randomUUID().toString().substring(0,5);
            pairCode=pairCode.toUpperCase();
            // Update the TextView with the random number and text
            pairingCode.setText(pairCode);
            sessionManagement.createPairingSession(pairCode);
        }else{
            // Update the TextView with the random number and text
            pairingCode.setText(pairCode);

        }
        if(isNetworkAvailable()){
            parentInternetLay.setVisibility(GONE);
            // Initialize the Timer
            Timer timer = new Timer();
            // Schedule the TimerTask to make API calls every X milliseconds
            long delay = 0;  // Initial delay before the first API call
            long period = 3000;  // Repeat the API call every 5 seconds (adjust as needed)
            timer.schedule(new MyTask(), delay, period);


        }
        else{
            parentInternetLay.setVisibility(VISIBLE);
            showSnack();
        }


        parent_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.app_name);
                builder.setIcon(R.mipmap.neo_app_icon);
                builder.setMessage("Do you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // on below line we are finishing activity.
                                MainActivity.this.finish();

                                // on below line we are exiting our activity
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        parent_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.app_name);
                builder.setIcon(R.mipmap.neo_app_icon);
                builder.setMessage("Do you want to reload this page?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if (drawer.isDrawerOpen(GravityCompat.START)) {
                                    parent_auto_start.setBackgroundColor(Color.TRANSPARENT);
                                    parent_keep_awake.setBackgroundColor(Color.TRANSPARENT);
                                    parent_keep_on_top.setBackgroundColor(Color.TRANSPARENT);
                                    parent_reload.setBackgroundColor(Color.TRANSPARENT);
                                    parent_exit.setBackgroundColor(Color.TRANSPARENT);
                                    autoStartSwitch.setFocusable(false);
                                    keepAwakeSwitch.setFocusable(false);
                                    keepOnTopSwitch.setFocusable(false);
                                    parent_exit.setFocusable(false);
                                    parent_reload.setFocusable(false);
                                    videoView.setZOrderOnTop(false);
                                    drawer.closeDrawer(GravityCompat.START);
                                }
                                if(isNetworkAvailable()){
                                    parentInternetLay.setVisibility(GONE);
                                    slideShowCallCount=0;
                                    initPairing(pairCode);
                                }
                                else{
                                    parentInternetLay.setVisibility(VISIBLE);
                                    showSnack();
                                }

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });




        MyReceiver = new MyReceiver();
        broadcastIntent();

    }
    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(flags);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (Settings.canDrawOverlays(MainActivity.this)) {
                autoStartSwitch.setChecked(true);
                sessionManagement.createAutoStartSession(true);
            } else {
                autoStartSwitch.setChecked(false);
                sessionManagement.createAutoStartSession(false);
            }
        }
        else {
            Log.v("App", "OS Version Less than S");
            //No need for Permission as less then M OS.
            if (isAutoStart){
                autoStartSwitch.setChecked(true);
                sessionManagement.createAutoStartSession(true);
            }else{
                autoStartSwitch.setChecked(false);
                sessionManagement.createAutoStartSession(false);

            }
        }
        parent_auto_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                sessionManagement = new SessionManagement(MainActivity.this);
                HashMap<String, String> getAutoStartDetails = new HashMap<String, String>();
                getAutoStartDetails = sessionManagement.getAutoStartDetails();
                isAutoStart = Boolean.parseBoolean(getAutoStartDetails.get(IS_AUTOSTART));


                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.app_name);
                builder.setIcon(R.mipmap.neo_app_icon);
                builder.setMessage("Do you want to change Auto Start status?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if (isAutoStart){
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                        if (Settings.canDrawOverlays(MainActivity.this)) {
                                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                                            startActivityForResult(intent, 0);
                                        }
                                    }else{
                                        autoStartSwitch.setChecked(false);
                                        sessionManagement.createAutoStartSession(false);
                                    }
                                }
                                else{
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                        if (!Settings.canDrawOverlays(MainActivity.this)) {
                                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                                            startActivityForResult(intent, 0);
                                        }
                                    }else{
                                        autoStartSwitch.setChecked(true);
                                        sessionManagement.createAutoStartSession(true);
                                    }

                                }

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sessionManagement = new SessionManagement(MainActivity.this);
        HashMap<String, String> getAutoStartDetails = new HashMap<String, String>();
        getAutoStartDetails = sessionManagement.getAutoStartDetails();
        isAutoStart = Boolean.parseBoolean(getAutoStartDetails.get(IS_AUTOSTART));
        Log.e("TAG", "isAutoStart>>> " + isAutoStart);

        //accessAllPermission();
        autoStartSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoStartSwitch.isChecked()){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        if (!Settings.canDrawOverlays(MainActivity.this)) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intent, 0);
                        }
                    }else{
                        sessionManagement.createAutoStartSession(true);
                    }
                }else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        if (Settings.canDrawOverlays(MainActivity.this)) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intent, 0);
                        }
                    }else{
                        sessionManagement.createAutoStartSession(false);
                    }

                }
            }
        });







        PowerManager powerManager =(PowerManager)getSystemService(POWER_SERVICE);
        powerLatch = powerManager.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP, "Lock");
        if (isWakeUP){
            keepAwakeSwitch.setChecked(true);
            //This code holds the CPU
            //powerLatch.acquire(24*60*60*1000L);
            powerLatch.acquire();
            sessionManagement.createWakeupSession(true);
        }
        else{
            keepAwakeSwitch.setChecked(false);
            //This code holds the CPU
            if (powerLatch.isHeld()){
                powerLatch.release();
                sessionManagement.createWakeupSession(false);
            }
        }
        keepAwakeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keepAwakeSwitch.isChecked()){
                    //This code holds the CPU
                    powerLatch.acquire();
                    sessionManagement.createWakeupSession(true);
                }else{
                    //This code holds the CPU
                    if (powerLatch.isHeld()){
                        powerLatch.release();
                        sessionManagement.createWakeupSession(false);
                    }

                }
            }
        });

        parent_keep_awake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                sessionManagement = new SessionManagement(MainActivity.this);

                HashMap<String, String> getUserDetails = new HashMap<String, String>();
                getUserDetails = sessionManagement.getWakeupDetails();
                isWakeUP = Boolean.parseBoolean(getUserDetails.get(IS_WAKEUP));


                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.app_name);
                builder.setIcon(R.mipmap.neo_app_icon);
                builder.setMessage("Do you want to change Keep Awake status?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                                if (isWakeUP){
                                    keepAwakeSwitch.setChecked(false);
                                    //This code holds the CPU
                                    if (powerLatch.isHeld()){
                                        powerLatch.release();
                                        sessionManagement.createWakeupSession(false);
                                    }

                                }
                                else{
                                    keepAwakeSwitch.setChecked(true);
                                    //This code holds the CPU
                                    //powerLatch.acquire(24*60*60*1000L);
                                    powerLatch.acquire();
                                    sessionManagement.createWakeupSession(true);
                                }



                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });






        if (isKeepOnTop){
            keepOnTopSwitch.setChecked(true);
            sessionManagement.createKeepOnTopSession(true);
        }
        else{
            keepOnTopSwitch.setChecked(false);
            sessionManagement.createKeepOnTopSession(false);
        }
        keepOnTopSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keepOnTopSwitch.isChecked()){
                    sessionManagement.createKeepOnTopSession(true);
                }else{
                    sessionManagement.createKeepOnTopSession(false);

                }
            }
        });
        parent_keep_on_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                sessionManagement = new SessionManagement(MainActivity.this);
                HashMap<String, String> getKeepOnTopDetails = new HashMap<String, String>();
                getKeepOnTopDetails = sessionManagement.getKeepOnTopDetails();
                isKeepOnTop = Boolean.parseBoolean(getKeepOnTopDetails.get(IS_KEEPONTOP));

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.app_name);
                builder.setIcon(R.mipmap.neo_app_icon);
                builder.setMessage("Do you want to change Keep On Top status?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if (isKeepOnTop){
                                    keepOnTopSwitch.setChecked(false);
                                    sessionManagement.createKeepOnTopSession(false);
                                }
                                else{
                                    keepOnTopSwitch.setChecked(true);
                                    sessionManagement.createKeepOnTopSession(true);
                                }

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
    public void broadcastIntent() {
        registerReceiver(MyReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
    private void initPairing(String pairCode){
        //pairCode=pairCode1.toUpperCase();
        HashMap<String, String> getPairingStatusDetail = new HashMap<String, String>();
        getPairingStatusDetail = sessionManagement.getPairingStatusDetails();
        pairingStatus = Boolean.parseBoolean(getPairingStatusDetail.get(PAIRING_STATUS));
        HashMap<String, String> getcontentDetails = new HashMap<String, String>();
        getcontentDetails = sessionManagement.getContentItemDetails();
        List<ContentModel> list = new ArrayList<>();
        list=new Gson().fromJson(getcontentDetails.get("slideItem"), new TypeToken<List<ContentModel>>(){}.getType());
        if(list==null){
            if (pairingStatus){
                parentPairing.setVisibility(VISIBLE);
            }
            else{
                parentVideoView.setVisibility(GONE);
                parentContentImage.setVisibility(GONE);
                webView_lay.setVisibility(GONE);
                parentContentRssFeed.setVisibility(GONE);
                parentPairing.setVisibility(VISIBLE);
            }
        }else{
            if (pairingStatus && Objects.requireNonNull(list).size()>0){
                parentPairing.setVisibility(GONE);
            }
            else{
                parentVideoView.setVisibility(GONE);
                parentContentImage.setVisibility(GONE);
                webView_lay.setVisibility(GONE);
                parentContentRssFeed.setVisibility(GONE);
                parentPairing.setVisibility(VISIBLE);
            }
        }

        StringRequest getRequest = new StringRequest(Request.Method.GET,
                "https://app.neosign.tv/api/pair-screen/"+ pairCode +"?browser=Mozilla%20Firefox&deviceTimezone=Europe/Berlin",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAG","----API-response--------"+response);
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            Boolean status = jsonObject.getBoolean("success");
                            if(status){
                                JSONObject data = jsonObject.getJSONObject("data") ;
                                Boolean status1 = data.getBoolean("status");
                                if(status1){
                                    orientation = data.getString("orientation");
                                }

                                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                                View yourChildView = findViewById(R.id.contentLay);

                                if (orientation.equals("90 degrees")) {
                                    // Rotate the DrawerLayout
                                    drawerLayout.setRotation(90);
                                    // Adjust the layout parameters of yourChildView
                                    DrawerLayout.LayoutParams layoutParams = (DrawerLayout.LayoutParams) yourChildView.getLayoutParams();
                                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                                    // Set any other necessary parameters
                                    yourChildView.setLayoutParams(layoutParams);

                                    // Repeat similar adjustments for other child views
                                }
                                else if (orientation.equals("180 degrees")) {
                                    // Rotate the DrawerLayout
                                    drawerLayout.setRotation(180);
                                    // Adjust the layout parameters of yourChildView
                                    DrawerLayout.LayoutParams layoutParams = (DrawerLayout.LayoutParams) yourChildView.getLayoutParams();
                                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                                    // Set any other necessary parameters
                                    yourChildView.setLayoutParams(layoutParams);
                                }
                                else if (orientation.equals("270 degrees")) {
                                    // Rotate the DrawerLayout
                                    drawerLayout.setRotation(270);
                                    // Adjust the layout parameters of yourChildView
                                    DrawerLayout.LayoutParams layoutParams = (DrawerLayout.LayoutParams) yourChildView.getLayoutParams();
                                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                                    // Set any other necessary parameters
                                    yourChildView.setLayoutParams(layoutParams);
                                }
                                else {
                                    // Rotate the DrawerLayout
                                    drawerLayout.setRotation(0);
                                    // Adjust the layout parameters of yourChildView
                                    DrawerLayout.LayoutParams layoutParams = (DrawerLayout.LayoutParams) yourChildView.getLayoutParams();
                                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                                    // Set any other necessary parameters
                                    yourChildView.setLayoutParams(layoutParams);
                                }






                                Boolean dataStatus = data.getBoolean("status");
                                if(dataStatus){
                                    sessionManagement.createPairingSession(true);
                                    pairingCode.setText("Paired");
                                    JSONArray dataArray = data.getJSONArray("data");
                                    if(dataArray.length()>0){
                                        parentPairing.setVisibility(GONE);
                                        newSlideItems.clear();
                                        for(currentIndex =0; currentIndex<dataArray.length();currentIndex++){
                                            JSONObject dataObject = dataArray.getJSONObject(currentIndex);
                                            String type = dataObject.getString("type");
                                            String url = dataObject.getString("url");
                                            String duration = dataObject.getString("duration");
                                            String extention = dataObject.getString("extention");
                                            String app_clock_hands_color= dataObject.getString("app_clock_hands_color");
                                            String app_clock_text= dataObject.getString("app_clock_text");
                                            String app_clock_timezone= dataObject.getString("app_clock_timezone");
                                            String app_clock_size= dataObject.getString("app_clock_size");
                                            String app_clock_minor_indicator_color= dataObject.getString("app_clock_minor_indicator_color ");
                                            String app_clock_major_indicator_color= dataObject.getString("app_clock_major_indicator_color");
                                            String app_clock_innerdot_size= dataObject.getString("app_clock_innerdot_size");
                                            String app_clock_innerdot_color= dataObject.getString("app_clock_innerdot_color");

                                            String cdtime= dataObject.getString("cdtime");
                                            String cdtranslation= dataObject.getString("cdtranslation");
                                            String app_cd_text= dataObject.getString("app_cd_text");

                                            String rssinfo= dataObject.getString("rssinfo");



                                            newSlideItems.add(new ContentModel(type, url, duration, extention,app_clock_hands_color,
                                                    app_clock_text,app_clock_timezone,app_clock_size,app_clock_minor_indicator_color,
                                                    app_clock_major_indicator_color,app_clock_innerdot_size,app_clock_innerdot_color,
                                                    cdtime,cdtranslation,app_cd_text,rssinfo
                                            ));
                                            sessionManagement.createContentDataSession(newSlideItems);
                                        }

                                        if (slideShowCallCount==0){
                                            clearTimeout();
                                            contentCurrentIndex=0;
                                            contentLay(newSlideItems);

                                            firstdataCount= newSlideItems.size();
                                        }
                                        int newDataCount=newSlideItems.size();
                                        if(firstdataCount != newDataCount){
                                            Log.e("time>>","Time>>");
                                            slideItems.clear();
                                            slideItems.addAll(newSlideItems);
                                            slideShowCallCount=0;

                                        }
                                    }

                                }
                            }
                            else {
                                sessionManagement.createPairingSession(false);
                            }
                        }catch (JSONException ex){
                            ex.printStackTrace();
                            Log.e("Error", "-----Json Array----: "+ex.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("Error", "-----VollyError----: "+error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(getRequest);
        getRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void contentLay(List<ContentModel> list) {

        slideShowCallCount++;
        long duration = Long.parseLong(list.get(contentCurrentIndex).getDuration()); // Set the duration in milliseconds

        ContentModel item = list.get(contentCurrentIndex);
        if (item.getType().equals("image")){
            parentVideoView.setVisibility(GONE);
            parentContentRssFeed.setVisibility(GONE);
            parentContentImage.setVisibility(VISIBLE);
            webView_lay.setVisibility(GONE);
            content_image.setImageBitmap(null);
            content_image.destroyDrawingCache();
            Glide.with(MainActivity.this)
                    .load(item.getUrl())
                    .error(R.drawable.neo_logo)
                    .into(content_image);

            myRunnable = new Runnable() {
                @Override
                public void run() {
                    contentLay(list);
                }
            };
            handler.postDelayed(myRunnable, duration);
        }
        else if(item.getType().equals("video")){
            webView_lay.setVisibility(GONE);
            parentContentRssFeed.setVisibility(GONE);
            parentContentImage.setVisibility(GONE);
            parentVideoView.setVisibility(VISIBLE);
            video_progress.setVisibility(VISIBLE);

            /*if (mediaControls==null){
                mediaControls  = new MediaController(MainActivity.this);
                mediaControls.setAnchorView(videoView);
                videoView.setMediaController(mediaControls);
            }*/

            try {
                Uri video = Uri.parse(item.getUrl());
                videoView.setVideoURI(video);


            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            //myVideoView.requestFocus();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                // Close the progress bar and play the video
                public void onPrepared(MediaPlayer mp) {
                    videoView.seekTo(position);
                    if (position == 0) {
                        video_progress.setVisibility(GONE);
                        //mp.setLooping(true);
                        mp.setVolume(0f, 0f);


                        videoView.start();
                       /* myRunnable = new Runnable() {
                            @Override
                            public void run() {
                                contentLay(list);
                            }
                        };
                        handler.postDelayed(myRunnable, duration);*/


                    } else {
                        videoView.pause();
                    }
                }
            });
            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // Handle errors here
                    Log.e("error>>>","VideoError");
                    return true;
                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                @Override
                public void onCompletion(MediaPlayer mp)
                {   mp.reset();
                    contentLay(list);
                }
            }); // video finish listener
        }
        else if(item.getType().equals("app")&&item.getExtention().equals("Youtube")){
            parentContentImage.setVisibility(GONE);
            parentVideoView.setVisibility(GONE);
            parentContentRssFeed.setVisibility(GONE);
            webView_lay.setVisibility(VISIBLE);
            iFrameLay(item.getUrl(),list);
            myRunnable = new Runnable() {
                @Override
                public void run() {
                    contentLay(list);
                }
            };
            handler.postDelayed(myRunnable, duration);
        }
        else if(item.getType().equals("app")&&item.getExtention().equals("Clock")){
            parentContentImage.setVisibility(GONE);
            parentContentRssFeed.setVisibility(GONE);
            parentVideoView.setVisibility(GONE);
            webView_lay.setVisibility(VISIBLE);
            clockiFrameLay(item.getUrl(),list,item);
            myRunnable = new Runnable() {
                @Override
                public void run() {
                    contentLay(list);
                }
            };
            handler.postDelayed(myRunnable, duration);
        }
        else if(item.getType().equals("app")&&item.getExtention().equals("Countdown")){
            parentContentImage.setVisibility(GONE);
            parentContentRssFeed.setVisibility(GONE);
            parentVideoView.setVisibility(GONE);
            webView_lay.setVisibility(VISIBLE);
            countDowniFrameLay(item.getUrl(),list,item);
            myRunnable = new Runnable() {
                @Override
                public void run() {
                    contentLay(list);
                }
            };
            handler.postDelayed(myRunnable, duration);
        }
        else if(item.getType().equals("app")&&item.getExtention().equals("WebUrl")){
            parentContentImage.setVisibility(GONE);
            parentContentRssFeed.setVisibility(GONE);
            parentVideoView.setVisibility(GONE);
            webView_lay.setVisibility(VISIBLE);
            webUriiFrameLay(item.getUrl(),list,item);
            myRunnable = new Runnable() {
                @Override
                public void run() {
                    contentLay(list);
                }
            };
            handler.postDelayed(myRunnable, duration);
        }
        else if(item.getType().equals("app")&&item.getExtention().equals("Vimeo")){
            parentContentImage.setVisibility(GONE);
            parentVideoView.setVisibility(GONE);
            parentContentRssFeed.setVisibility(GONE);
            webView_lay.setVisibility(VISIBLE);
            vimeoiFrameLay(item.getUrl(),list,item);
            myRunnable = new Runnable() {
                @Override
                public void run() {
                    contentLay(list);
                }
            };
            handler.postDelayed(myRunnable, duration);
        }
        else if(item.getType().equals("app")&&item.getExtention().equals("RSS FEED")){
            parentContentImage.setVisibility(GONE);
            parentVideoView.setVisibility(GONE);
            webView_lay.setVisibility(GONE);
            parentContentRssFeed.setVisibility(VISIBLE);
            String rssFeedUrl = item.getUrl();
            rssFeediFrameLay(item.getUrl(),list,item);
            myRunnable = new Runnable() {
                @Override
                public void run() {
                    contentLay(list);
                }
            };
            handler.postDelayed(myRunnable, duration);
        }
        contentCurrentIndex++;
        if (contentCurrentIndex >= list.size()) {
            contentCurrentIndex = 0;
        }
        Log.e("newDuration","duration>>>"+duration);
        Log.e("newDuration","newDuration>>>"+newDuration);



    }

    private void rssFeediFrameLay(String url, List<ContentModel> list, ContentModel item) {
        List<RSSModel> rsslist = new ArrayList<>();
        String originalString = item.getUrl();
        String newString = originalString.replace("https://app.neosign.tv/", "");
        String rssFeedUrl = newString;
        String[] rssinfoArray;

        if (item.getRssinfo() != null) {
            String rssinfo = item.getRssinfo();
            rssinfoArray = rssinfo.split(",");
        }
        String apiUrl = "https://app.neosign.tv/api/rss-feed";
        String apiUrlWithParams="";
        try {
            apiUrlWithParams = apiUrl + "?url=" + URLEncoder.encode(rssFeedUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.e("TAG","apiUrlWithParams>>>"+apiUrlWithParams);
        StringRequest getRequest = new StringRequest(Request.Method.GET,
                apiUrlWithParams,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAG","response>>>"+response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length()>0){
                                rsslist.clear();
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject dataObject = jsonArray.getJSONObject(i);
                                    String title = dataObject.getString("title");
                                    String description = dataObject.getString("description");

                                    String date = dataObject.getString("date");
                                    String qr_code = dataObject.getString("qr_code");
                                    String photo= dataObject.getString("photo");
                                    RSSModel rssModel=new RSSModel(title,description,date,qr_code,photo);
                                    rsslist.add(rssModel);
                                }
                            }
                            if (rsslist.size()>0){
                                Log.e("TAG","rsslist>>>"+rsslist);
                                for(int i=0;i<1;i++){
                                    RSSModel item = rsslist.get(i);
                                    Log.e("TAG","rsslist>>>"+item.getPhoto());
                                    Glide.with(MainActivity.this)
                                            .load(item.getPhoto())
                                            .error(R.drawable.neo_logo)
                                            .into(rssImageView);
                                    rssTitle.setText(item.getTitle());
                                    rssDescription.setText(item.getDescription());
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e("Error", "-----VollyError----: "+error.getMessage());
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(getRequest);
        getRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );

    }

    private void vimeoiFrameLay(String url, List<ContentModel> list, ContentModel item) {
        String originalString = item.getUrl();
        String newString = originalString.replace("https://app.neosign.tv/", "");
        String webUrl = newString;
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            myWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else if(Build.VERSION.SDK_INT >=17 && Build.VERSION.SDK_INT < 19) {
            myWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webSettings.setDomStorageEnabled(true);
        // Other webview options
        myWebView.getSettings().setLoadWithOverviewMode(true);
        //Other webview settings
        myWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        myWebView.setScrollbarFadingEnabled(false);
        myWebView.getSettings().setBuiltInZoomControls(false);
        myWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        myWebView.getSettings().setAllowFileAccess(true);
        myWebView.getSettings().setSupportZoom(false);
        myWebView.setWebViewClient(new BrowserPage(list));
        myWebView.setWebChromeClient(new Browser());
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        myWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        myWebView.getSettings().setUseWideViewPort(true);

        if(isNetworkAvailable()){
            progressBar.setVisibility(View.VISIBLE);
            parentInternetLay.setVisibility(GONE);
            String dataUrl = "<iframe src='" + newString+"?autoplay=1"+"' frameborder='0' allowfullscreen='true' autoplay='true' muted='true' style='width:100%; height:100%;'></iframe>";
            //String dataUrl = "<iframe width=\"100%\" height=\"100%\"  src=\" + docUrl + \" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay=true; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
            myWebView.loadData(dataUrl, "text/html", "utf-8");
            myWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Log.e("Tag","webviewError"+failingUrl);
                }
            });
            //myWebView.loadUrl(newurl);
        }
        else{
            parentInternetLay.setVisibility(VISIBLE);
            progressBar.setVisibility(View.GONE);

        }
    }

    private void webUriiFrameLay(String url, List<ContentModel> list, ContentModel item) {
        String originalString = item.getUrl();
        String newString = originalString.replace("https://app.neosign.tv/", "");
        String webUrl = newString;
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            myWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else if(Build.VERSION.SDK_INT >=17 && Build.VERSION.SDK_INT < 19) {
            myWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webSettings.setDomStorageEnabled(true);
        // Other webview options
        myWebView.getSettings().setLoadWithOverviewMode(true);
        //Other webview settings
        myWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        myWebView.setScrollbarFadingEnabled(false);
        myWebView.getSettings().setBuiltInZoomControls(false);
        myWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        myWebView.getSettings().setAllowFileAccess(true);
        myWebView.getSettings().setSupportZoom(false);
        myWebView.setWebViewClient(new BrowserPage(list));
        myWebView.setWebChromeClient(new Browser());
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        myWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        myWebView.getSettings().setUseWideViewPort(true);

        if(isNetworkAvailable()){
            progressBar.setVisibility(View.VISIBLE);
            parentInternetLay.setVisibility(GONE);
            String dataUrl ="<iframe width=\"100%\" height=\"100%\" src=" + webUrl + " ></iframe>";
            //String dataUrl = "<iframe width=\"100%\" height=\"100%\"  src=\" + docUrl + \" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay=true; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
            myWebView.loadData(dataUrl, "text/html", "utf-8");
            myWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Log.e("Tag","webviewError"+failingUrl);
                }
            });
            //myWebView.loadUrl(newurl);
        }
        else{
            parentInternetLay.setVisibility(VISIBLE);
            progressBar.setVisibility(View.GONE);

        }

    }

    private void countDowniFrameLay(String url, List<ContentModel> list, ContentModel item) {
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            myWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else if(Build.VERSION.SDK_INT >=17 && Build.VERSION.SDK_INT < 19) {
            myWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webSettings.setDomStorageEnabled(true);
        // Other webview options
        myWebView.getSettings().setLoadWithOverviewMode(true);
        //Other webview settings
        myWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        myWebView.setScrollbarFadingEnabled(false);
        myWebView.getSettings().setBuiltInZoomControls(false);
        myWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        myWebView.getSettings().setAllowFileAccess(true);
        myWebView.getSettings().setSupportZoom(false);
        myWebView.setWebViewClient(new BrowserPage(list));
        myWebView.setWebChromeClient(new Browser());
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        myWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        myWebView.getSettings().setUseWideViewPort(true);

        if(isNetworkAvailable()){
            progressBar.setVisibility(View.VISIBLE);
            parentInternetLay.setVisibility(GONE);
            String timeString = convertTimeFormat(item.getCdtime());
            if (item.getApp_cd_text()==null){
                countdownText = "";
            }else{
                countdownText = item.getApp_cd_text();
            }
            if (item.getCdtranslation()==null){
                translationString = ",,,";
            }else{
                translationString = item.getCdtranslation();
            }
            String[] translation = translationString.split(",");

            String day,hour,minute,second;
            if(translation[0]==null){
                day = "Days";
            }else{
                day = translation[0];
            }
            if(translation[1]==null){
                hour = "Hours";
            }else{
                hour = translation[1];
            }
            if(translation[2]==null){
                minute ="Minutes";
            }else{
                minute = translation[2];
            }
            if(translation[3]==null){
                second = "Seconds";
            }else{
                second = translation[3];
            }
            Log.e("translation>>","translation"+translation[2]);
            // Construct the URL using the parameters
            String baseUrl = "https://webplayer.neosign.tv/countdown.php?";
            Uri.Builder builder = Uri.parse(baseUrl).buildUpon();
            builder.appendQueryParameter("dayText", day);
            builder.appendQueryParameter("hourText", hour);
            builder.appendQueryParameter("minText", minute);
            builder.appendQueryParameter("secText", second);
            builder.appendQueryParameter("date", timeString);
            builder.appendQueryParameter("countdownText",countdownText);


            String clockurl = builder.build().toString();

            String dataUrl ="<iframe width=\"100%\" height=\"100%\" src=" + clockurl + " ></iframe>";
            //String dataUrl = "<iframe width=\"100%\" height=\"100%\"  src=\" + docUrl + \" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay=true; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
            myWebView.loadData(dataUrl, "text/html", "utf-8");
            myWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Log.e("Tag","webviewError"+failingUrl);
                }
            });
            //myWebView.loadUrl(newurl);
        }
        else{
            parentInternetLay.setVisibility(VISIBLE);
            progressBar.setVisibility(View.GONE);

        }
    }

    private String convertTimeFormat(String cdtime) {
        // Splitting the original time into hours and minutes
        String[] timeParts = cdtime.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);

        // Creating a new Date object with the current date
        Date currentDate = new Date();

        // Setting the hours and minutes of the Date object
        currentDate.setHours(hours);
        currentDate.setMinutes(minutes);

        // Formatting the date in the desired format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss", Locale.getDefault());
        String formattedTime = sdf.format(currentDate);

        return formattedTime;
    }

    private void clockiFrameLay(String url, List<ContentModel> list, ContentModel item) {
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            myWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else if(Build.VERSION.SDK_INT >=17 && Build.VERSION.SDK_INT < 19) {
            myWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webSettings.setDomStorageEnabled(true);
        // Other webview options
        myWebView.getSettings().setLoadWithOverviewMode(true);
        //Other webview settings
        myWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        myWebView.setScrollbarFadingEnabled(false);
        myWebView.getSettings().setBuiltInZoomControls(false);
        myWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        myWebView.getSettings().setAllowFileAccess(true);
        myWebView.getSettings().setSupportZoom(false);
        myWebView.setWebViewClient(new BrowserPage(list));
        myWebView.setWebChromeClient(new Browser());
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        myWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        myWebView.getSettings().setUseWideViewPort(true);

        if(isNetworkAvailable()){
            progressBar.setVisibility(View.VISIBLE);
            parentInternetLay.setVisibility(GONE);
            String appClockHandsColor = item.getApp_clock_hands_color();
            Log.e("appClockHandsColor>>","appClockHandsColor"+appClockHandsColor);
            String[] appClockHandsColorArray = appClockHandsColor.split(",");
            // Construct the URL using the parameters
            String baseUrl = "https://webplayer.neosign.tv/clock.php?";
            Uri.Builder builder = Uri.parse(baseUrl).buildUpon();
            builder.appendQueryParameter("text", item.getApp_clock_text());
            builder.appendQueryParameter("size", item.getApp_clock_size());
            builder.appendQueryParameter("hourColor", appClockHandsColorArray[0]);
            builder.appendQueryParameter("minuteColor", appClockHandsColorArray[1]);
            builder.appendQueryParameter("secondColor", appClockHandsColorArray[2]);
            builder.appendQueryParameter("minorIndicator",item.getApp_clock_minor_indicator_color());
            builder.appendQueryParameter("majorIndicator", item.getApp_clock_major_indicator_color());
            builder.appendQueryParameter("innerDotSize",  item.getApp_clock_innerdot_size());
            builder.appendQueryParameter("innerDotColor", item.getApp_clock_innerdot_color());

            String clockurl = builder.build().toString();

            String dataUrl ="<iframe width=\"100%\" height=\"100%\" src=" + clockurl + " ></iframe>";
            //String dataUrl = "<iframe width=\"100%\" height=\"100%\"  src=\" + docUrl + \" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay=true; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
            myWebView.loadData(dataUrl, "text/html", "utf-8");
            myWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Log.e("Tag","webviewError"+failingUrl);
                }
            });
            //myWebView.loadUrl(newurl);
        }
        else{
            parentInternetLay.setVisibility(VISIBLE);
            progressBar.setVisibility(View.GONE);

        }
    }

    private void iFrameLay(String newurl, List<ContentModel> list) {
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            myWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else if(Build.VERSION.SDK_INT >=17 && Build.VERSION.SDK_INT < 19) {
            myWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webSettings.setDomStorageEnabled(true);
        // Other webview options
        myWebView.getSettings().setLoadWithOverviewMode(true);
        //Other webview settings
        myWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        myWebView.setScrollbarFadingEnabled(false);
        myWebView.getSettings().setBuiltInZoomControls(false);
        myWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        myWebView.getSettings().setAllowFileAccess(true);
        myWebView.getSettings().setSupportZoom(false);
        myWebView.setWebViewClient(new BrowserPage(list));
        myWebView.setWebChromeClient(new Browser());
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        myWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        myWebView.getSettings().setUseWideViewPort(true);

        if(isNetworkAvailable()){
            progressBar.setVisibility(View.VISIBLE);
            parentInternetLay.setVisibility(GONE);
            String myYouTubeVideoUrl = newurl;
            // YouTube video ID
            String videoId = null;
            if (myYouTubeVideoUrl != null && myYouTubeVideoUrl.contains("youtube.com")) {
                int startIndex = myYouTubeVideoUrl.indexOf("v=");
                if (startIndex != -1) {
                    startIndex += 2; // Move past the "v="
                    int endIndex = myYouTubeVideoUrl.indexOf('&', startIndex);
                    if (endIndex == -1) {
                        endIndex = myYouTubeVideoUrl.length();
                    }
                    videoId = myYouTubeVideoUrl.substring(startIndex, endIndex);
                }
            }

            // Create the embedded YouTube link
            String embeddedLink = "https://www.youtube.com/embed/" + videoId + "?autoplay=1&mute=1";
            String dataUrl ="<iframe width=\"100%\" height=\"100%\" src=" + embeddedLink + " ></iframe>";
            //String dataUrl = "<iframe width=\"100%\" height=\"100%\"  src=\" + docUrl + \" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay=true; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
            myWebView.loadData(dataUrl, "text/html", "utf-8");
            myWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Log.e("Tag","webviewError"+failingUrl);
                }
            });
            //myWebView.loadUrl(newurl);
        }
        else{
            parentInternetLay.setVisibility(VISIBLE);
            progressBar.setVisibility(View.GONE);

        }
    }


    private void initSession() {
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sessionManagement = new SessionManagement(MainActivity.this);

        HashMap<String, String> getUserDetails = new HashMap<String, String>();
        getUserDetails = sessionManagement.getWakeupDetails();
        isWakeUP = Boolean.parseBoolean(getUserDetails.get(IS_WAKEUP));

        HashMap<String, String> getAutoStartDetails = new HashMap<String, String>();
        getAutoStartDetails = sessionManagement.getAutoStartDetails();
        isAutoStart = Boolean.parseBoolean(getAutoStartDetails.get(IS_AUTOSTART));


        HashMap<String, String> getKeepOnTopDetails = new HashMap<String, String>();
        getKeepOnTopDetails = sessionManagement.getKeepOnTopDetails();
        isKeepOnTop = Boolean.parseBoolean(getKeepOnTopDetails.get(IS_KEEPONTOP));

        HashMap<String, String> getPairingStatusDetail = new HashMap<String, String>();
        getPairingStatusDetail = sessionManagement.getPairingStatusDetails();
        pairingStatus = Boolean.parseBoolean(getPairingStatusDetail.get(PAIRING_STATUS));

    }
    private void accessAllPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && isAutoStart && isAutoPopUP) {
            requestAutoStart();
        }
        else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.S && isAutoStart){
            sessionManagement.createAutoStartSession(true);
        }
        if(ActivityCompat.checkSelfPermission(MainActivity.this,AndroidBatteryPermission[0])==
                PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !isAutoPopUP && isOptimizationPopUP){
            requestBatteryOptimization();
        }


    }
    public void requestAutoStart()
    {
        if (!Settings.canDrawOverlays(MainActivity.this)) {

            ViewGroup viewGroup=findViewById(android.R.id.content);
            TextView btn;
            TextView tittle,statement;
            ImageView icon;
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.all_alert_dialog,viewGroup,false);
            builder.setCancelable(false);
            builder.setView(view);
            icon=(ImageView)view.findViewById(R.id.aleart_img);
            icon.setImageResource(R.drawable.ic_baseline_autorenew_24);
            btn=view.findViewById(R.id.ok_btn);
            //btn.setText("Turn Off Now");
            btn.setText(getResources().getString(R.string.turn_on));
            tittle=view.findViewById(R.id.tittle);
            statement=view.findViewById(R.id.statements);
            tittle.setText(R.string.auto_start_tittle);
            statement.setText(R.string.auto_start_statement);
            AlertDialog alertDialog=builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isAutoPopUP = false;
                    alertDialog.dismiss();
                    alertDialog.cancel();
                    alertDialog.hide();
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, 0);
                }
            });
            alertDialog.show();
        }
    }
    public void requestBatteryOptimization()
    {
        String packageName = MainActivity.this.getPackageName();
        PowerManager pm = (PowerManager) MainActivity.this.getSystemService(POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                ViewGroup viewGroup=findViewById(android.R.id.content);
                TextView btn;
                TextView tittle,statement;
                ImageView icon;
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.all_alert_dialog,viewGroup,false);
                builder.setCancelable(false);
                builder.setView(view);
                icon=(ImageView)view.findViewById(R.id.aleart_img);
                icon.setImageResource(R.drawable.battery_optimi);
                btn=view.findViewById(R.id.ok_btn);
                //btn.setText("Turn Off Now");
                btn.setText(getResources().getString(R.string.turn_off_battry));
                tittle=view.findViewById(R.id.tittle);
                statement=view.findViewById(R.id.statements);
                tittle.setText(R.string.battery_optimization_tittle);
                statement.setText(R.string.battery_optimization_statement);
                AlertDialog alertDialog=builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        alertDialog.cancel();
                        alertDialog.hide();
                        isOptimizationPopUP=false;
                        Log.e("TAG","battryOptimisationCalled");
                       /* intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                        intent.setData(Uri.parse("package:" + packageName));
                        startActivity(intent);*/
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[] {
                                    Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                            }, PERMISSION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                        }
                    }
                });
                alertDialog.show();
            }
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // Handle remote control key events here
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        if (parent_exit.isFocusable()){
                            nextView5 = rootView5.focusSearch(View.FOCUS_UP);
                            if (nextView5 !=null && parent_exit.isFocusable()){
                                autoStartSwitch.setFocusable(false);
                                keepAwakeSwitch.setFocusable(false);
                                keepOnTopSwitch.setFocusable(false);
                                parent_exit.setFocusable(false);
                                parent_reload.setFocusable(true);
                                parent_reload.requestFocus();
                                Log.e("Test","nextView>>5"+nextView5);
                                if (parent_reload.isFocusable()){
                                    Log.e("Test","parent_reload>>");
                                    parent_exit.setBackgroundColor(Color.TRANSPARENT);
                                    parent_reload.setBackgroundColor(R.drawable.menu_selection);
                                }
                            }
                        }
                        else if (parent_reload.isFocusable()){
                            nextView6 = rootView5.focusSearch(View.FOCUS_UP);
                            if (nextView6 !=null && parent_reload.isFocusable()){
                                autoStartSwitch.setFocusable(false);
                                keepAwakeSwitch.setFocusable(false);
                                parent_reload.setFocusable(false);
                                parent_exit.setFocusable(false);
                                keepOnTopSwitch.setFocusable(true);
                                keepOnTopSwitch.requestFocus();
                                Log.e("Test","nextView>>6"+nextView3);
                                if (keepOnTopSwitch.isFocusable()){
                                    Log.e("Test","parent_reload>>");
                                    parent_reload.setBackgroundColor(Color.TRANSPARENT);
                                    parent_keep_on_top.setBackgroundColor(R.drawable.menu_selection);
                                }
                            }
                        }
                        else if (keepOnTopSwitch.isFocusable()){
                            nextView7 = rootView5.focusSearch(View.FOCUS_UP);
                            if (nextView7 !=null && keepOnTopSwitch.isFocusable()){
                                autoStartSwitch.setFocusable(false);
                                keepOnTopSwitch.setFocusable(false);
                                parent_reload.setFocusable(false);
                                parent_exit.setFocusable(false);
                                Log.e("Test","nextView>>7"+nextView2);
                                keepAwakeSwitch.setFocusable(true);
                                keepAwakeSwitch.requestFocus();
                                if (keepAwakeSwitch.isFocusable()){
                                    Log.e("Test","parent_keep_on_top>>");
                                    parent_keep_on_top.setBackgroundColor(Color.TRANSPARENT);
                                    parent_keep_awake.setBackgroundColor(R.drawable.menu_selection);
                                }


                            }
                        }
                        else if (keepAwakeSwitch.isFocusable()){
                            nextView8 = rootView5.focusSearch(View.FOCUS_UP);
                            if (nextView8 !=null && keepAwakeSwitch.isFocusable()){
                                keepAwakeSwitch.setFocusable(false);
                                keepOnTopSwitch.setFocusable(false);
                                parent_reload.setFocusable(false);
                                parent_exit.setFocusable(false);
                                Log.e("Test","nextView>>8"+nextView1);
                                autoStartSwitch.setFocusable(true);
                                autoStartSwitch.requestFocus();
                                if (autoStartSwitch.isFocusable()){
                                    Log.e("Test","parent_keep_awake>>");
                                    parent_keep_awake.setBackgroundColor(Color.TRANSPARENT);
                                    parent_auto_start.setBackgroundColor(R.drawable.menu_selection);

                                }

                            }
                        }
                        else if (autoStartSwitch.isFocusable()){
                            nextView9 = rootView5.focusSearch(View.FOCUS_UP);
                            if (nextView9 !=null && autoStartSwitch.isFocusable()){
                                autoStartSwitch.setFocusable(false);
                                keepAwakeSwitch.setFocusable(false);
                                keepOnTopSwitch.setFocusable(false);
                                parent_reload.setFocusable(false);

                                Log.e("Test","nextView>>9"+nextView9);
                                parent_exit.setFocusable(true);
                                parent_exit.requestFocus();
                                if (parent_exit.isFocusable()){
                                    Log.e("Test","parent_Exit>>");
                                    parent_auto_start.setBackgroundColor(Color.TRANSPARENT);
                                    parent_exit.setBackgroundColor(R.drawable.menu_selection);

                                }

                            }
                        }

                    }
                }

                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        if (autoStartSwitch.isFocusable()){
                            nextView1 = rootView1.focusSearch(View.FOCUS_DOWN);
                            if (nextView1 !=null && autoStartSwitch.isFocusable()){
                                autoStartSwitch.setFocusable(false);
                                keepOnTopSwitch.setFocusable(false);
                                parent_reload.setFocusable(false);
                                parent_exit.setFocusable(false);
                                Log.e("Test","nextView>>"+nextView1);
                                keepAwakeSwitch.setFocusable(true);
                                keepAwakeSwitch.requestFocus();
                                if (keepAwakeSwitch.isFocusable()){
                                    Log.e("Test","parent_keep_awake>>");
                                    parent_auto_start.setBackgroundColor(Color.TRANSPARENT);
                                    parent_keep_awake.setBackgroundColor(R.drawable.menu_selection);

                                }

                            }
                        }
                        else if (keepAwakeSwitch.isFocusable()){
                            nextView2 = rootView1.focusSearch(View.FOCUS_DOWN);
                            if (nextView2 !=null && keepAwakeSwitch.isFocusable()){
                                autoStartSwitch.setFocusable(false);
                                keepAwakeSwitch.setFocusable(false);
                                parent_reload.setFocusable(false);
                                parent_exit.setFocusable(false);
                                Log.e("Test","nextView>>2"+nextView2);
                                keepOnTopSwitch.setFocusable(true);
                                keepOnTopSwitch.requestFocus();
                                if (keepOnTopSwitch.isFocusable()){
                                    Log.e("Test","parent_keep_on_top>>");
                                    parent_keep_awake.setBackgroundColor(Color.TRANSPARENT);
                                    parent_keep_on_top.setBackgroundColor(R.drawable.menu_selection);
                                }


                            }
                        }
                        else if (keepOnTopSwitch.isFocusable()){
                            nextView3 = rootView1.focusSearch(View.FOCUS_DOWN);
                            if (nextView3 !=null && keepOnTopSwitch.isFocusable()){
                                autoStartSwitch.setFocusable(false);
                                keepAwakeSwitch.setFocusable(false);
                                keepOnTopSwitch.setFocusable(false);
                                parent_exit.setFocusable(false);
                                parent_reload.setFocusable(true);
                                parent_reload.requestFocus();
                                Log.e("Test","nextView>>3"+nextView3);
                                if (parent_reload.isFocusable()){
                                    Log.e("Test","parent_reload>>");
                                    parent_keep_on_top.setBackgroundColor(Color.TRANSPARENT);
                                    parent_reload.setBackgroundColor(R.drawable.menu_selection);
                                }
                            }
                        }
                        else if (parent_reload.isFocusable()){
                            nextView4 = rootView1.focusSearch(View.FOCUS_DOWN);
                            if (nextView4 !=null && parent_reload.isFocusable()){
                                autoStartSwitch.setFocusable(false);
                                keepAwakeSwitch.setFocusable(false);
                                keepOnTopSwitch.setFocusable(false);
                                parent_reload.setFocusable(false);
                                parent_exit.setFocusable(true);
                                parent_exit.requestFocus();
                                Log.e("Test","nextView>>4"+nextView4);
                                if (parent_exit.isFocusable()){
                                    Log.e("Test","parent_exit>>");
                                    parent_reload.setBackgroundColor(Color.TRANSPARENT);
                                    parent_exit.setBackgroundColor(R.drawable.menu_selection);
                                }
                            }
                        }
                        else if (parent_exit.isFocusable()){
                            nextView10 = rootView1.focusSearch(View.FOCUS_DOWN);
                            if (nextView10 !=null && parent_exit.isFocusable()){

                                keepAwakeSwitch.setFocusable(false);
                                keepOnTopSwitch.setFocusable(false);
                                parent_reload.setFocusable(false);
                                parent_exit.setFocusable(false);
                                autoStartSwitch.setFocusable(true);
                                autoStartSwitch.requestFocus();
                                Log.e("Test","nextView>>4"+nextView4);
                                if (autoStartSwitch.isFocusable()){
                                    Log.e("Test","parent_exit>>");
                                    parent_exit.setBackgroundColor(Color.TRANSPARENT);
                                    parent_auto_start.setBackgroundColor(R.drawable.menu_selection);
                                }
                            }
                        }
                    }
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                // Handle left key press
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                }
                else{
                    drawer.openDrawer(GravityCompat.START);
                    keepAwakeSwitch.setFocusable(false);
                    keepOnTopSwitch.setFocusable(false);
                    parent_exit.setFocusable(false);
                    parent_reload.setFocusable(false);
                    autoStartSwitch.setFocusable(true);
                    autoStartSwitch.requestFocus();
                    parent_keep_awake.setBackgroundColor(Color.TRANSPARENT);
                    parent_keep_on_top.setBackgroundColor(Color.TRANSPARENT);
                    parent_reload.setBackgroundColor(Color.TRANSPARENT);
                    parent_exit.setBackgroundColor(Color.TRANSPARENT);
                    parent_auto_start.setBackgroundColor(R.drawable.menu_selection);
                    videoView.setZOrderOnTop(true);
                }
                return true;
            case KeyEvent.KEYCODE_MENU:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    parent_auto_start.setBackgroundColor(Color.TRANSPARENT);
                    parent_keep_awake.setBackgroundColor(Color.TRANSPARENT);
                    parent_keep_on_top.setBackgroundColor(Color.TRANSPARENT);
                    parent_reload.setBackgroundColor(Color.TRANSPARENT);
                    parent_exit.setBackgroundColor(Color.TRANSPARENT);
                    autoStartSwitch.setFocusable(false);
                    keepAwakeSwitch.setFocusable(false);
                    keepOnTopSwitch.setFocusable(false);
                    parent_exit.setFocusable(false);
                    parent_reload.setFocusable(false);
                    videoView.setZOrderOnTop(false);
                    drawer.closeDrawer(GravityCompat.START);
                }
                else{
                    drawer.openDrawer(GravityCompat.START);
                    keepAwakeSwitch.setFocusable(false);
                    keepOnTopSwitch.setFocusable(false);
                    parent_exit.setFocusable(false);
                    parent_reload.setFocusable(false);
                    autoStartSwitch.setFocusable(true);
                    autoStartSwitch.requestFocus();
                    parent_keep_awake.setBackgroundColor(Color.TRANSPARENT);
                    parent_keep_on_top.setBackgroundColor(Color.TRANSPARENT);
                    parent_reload.setBackgroundColor(Color.TRANSPARENT);
                    parent_exit.setBackgroundColor(Color.TRANSPARENT);
                    parent_auto_start.setBackgroundColor(R.drawable.menu_selection);
                    videoView.setZOrderOnTop(true);
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                // Handle right key press
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    parent_auto_start.setBackgroundColor(Color.TRANSPARENT);
                    parent_keep_awake.setBackgroundColor(Color.TRANSPARENT);
                    parent_keep_on_top.setBackgroundColor(Color.TRANSPARENT);
                    parent_reload.setBackgroundColor(Color.TRANSPARENT);
                    parent_exit.setBackgroundColor(Color.TRANSPARENT);
                    autoStartSwitch.setFocusable(false);
                    keepAwakeSwitch.setFocusable(false);
                    keepOnTopSwitch.setFocusable(false);
                    parent_exit.setFocusable(false);
                    parent_reload.setFocusable(false);
                    videoView.setZOrderOnTop(false);
                    drawer.closeDrawer(GravityCompat.START);
                }
                return true;
            case KeyEvent.KEYCODE_ENTER:
                Toast.makeText(getApplicationContext(), "Test ok :",
                        Toast.LENGTH_LONG).show();
                // Handle enter key press

                return true;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        // Check if the key code is the OK button (DPAD_CENTER)
                        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
                            // Show the AlertDialog
                            if (autoStartSwitch.isFocusable()){
                                sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                sessionManagement = new SessionManagement(MainActivity.this);
                                HashMap<String, String> getAutoStartDetails = new HashMap<String, String>();
                                getAutoStartDetails = sessionManagement.getAutoStartDetails();
                                isAutoStart = Boolean.parseBoolean(getAutoStartDetails.get(IS_AUTOSTART));


                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle(R.string.app_name);
                                builder.setIcon(R.mipmap.neo_app_icon);
                                builder.setMessage("Do you want to change Auto Start status?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                if (isAutoStart){
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                                        if (Settings.canDrawOverlays(MainActivity.this)) {
                                                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                                                            startActivityForResult(intent, 0);
                                                        }
                                                    }else{
                                                        autoStartSwitch.setChecked(false);
                                                        sessionManagement.createAutoStartSession(false);
                                                    }
                                                }
                                                else{
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                                        if (!Settings.canDrawOverlays(MainActivity.this)) {
                                                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                                                            startActivityForResult(intent, 0);
                                                        }
                                                    }else{
                                                        autoStartSwitch.setChecked(true);
                                                        sessionManagement.createAutoStartSession(true);
                                                    }

                                                }

                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                            else if (keepAwakeSwitch.isFocusable()){
                                sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                sessionManagement = new SessionManagement(MainActivity.this);

                                HashMap<String, String> getUserDetails = new HashMap<String, String>();
                                getUserDetails = sessionManagement.getWakeupDetails();
                                isWakeUP = Boolean.parseBoolean(getUserDetails.get(IS_WAKEUP));


                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle(R.string.app_name);
                                builder.setIcon(R.mipmap.neo_app_icon);
                                builder.setMessage("Do you want to change Keep Awake status?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                                if (isWakeUP){
                                                    keepAwakeSwitch.setChecked(false);
                                                    //This code holds the CPU
                                                    if (powerLatch.isHeld()){
                                                        powerLatch.release();
                                                        sessionManagement.createWakeupSession(false);
                                                    }

                                                }
                                                else{
                                                    keepAwakeSwitch.setChecked(true);
                                                    //This code holds the CPU
                                                    //powerLatch.acquire(24*60*60*1000L);
                                                    powerLatch.acquire();
                                                    sessionManagement.createWakeupSession(true);
                                                }



                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            }
                            else if (keepOnTopSwitch.isFocusable()){
                                sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                sessionManagement = new SessionManagement(MainActivity.this);
                                HashMap<String, String> getKeepOnTopDetails = new HashMap<String, String>();
                                getKeepOnTopDetails = sessionManagement.getKeepOnTopDetails();
                                isKeepOnTop = Boolean.parseBoolean(getKeepOnTopDetails.get(IS_KEEPONTOP));

                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle(R.string.app_name);
                                builder.setIcon(R.mipmap.neo_app_icon);
                                builder.setMessage("Do you want to change Keep On Top status?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                if (isKeepOnTop){
                                                    keepOnTopSwitch.setChecked(false);
                                                    sessionManagement.createKeepOnTopSession(false);
                                                }
                                                else{
                                                    keepOnTopSwitch.setChecked(true);
                                                    sessionManagement.createKeepOnTopSession(true);
                                                }

                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                            else if (parent_reload.isFocusable()){
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle(R.string.app_name);
                                builder.setIcon(R.mipmap.neo_app_icon);
                                builder.setMessage("Do you want to reload this page?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                if (drawer.isDrawerOpen(GravityCompat.START)) {
                                                    parent_auto_start.setBackgroundColor(Color.TRANSPARENT);
                                                    parent_keep_awake.setBackgroundColor(Color.TRANSPARENT);
                                                    parent_keep_on_top.setBackgroundColor(Color.TRANSPARENT);
                                                    parent_reload.setBackgroundColor(Color.TRANSPARENT);
                                                    parent_exit.setBackgroundColor(Color.TRANSPARENT);
                                                    autoStartSwitch.setFocusable(false);
                                                    keepAwakeSwitch.setFocusable(false);
                                                    keepOnTopSwitch.setFocusable(false);
                                                    parent_exit.setFocusable(false);
                                                    parent_reload.setFocusable(false);
                                                    videoView.setZOrderOnTop(false);
                                                    drawer.closeDrawer(GravityCompat.START);
                                                }
                                                if(isNetworkAvailable()){
                                                    parentInternetLay.setVisibility(GONE);
                                                    slideShowCallCount=0;
                                                    initPairing(pairCode);
                                                }
                                                else{
                                                    parentInternetLay.setVisibility(VISIBLE);
                                                    showSnack();
                                                }
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            }
                            else if (parent_exit.isFocusable()){
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle(R.string.app_name);
                                builder.setIcon(R.mipmap.neo_app_icon);
                                builder.setMessage("Do you want to exit?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // on below line we are finishing activity.
                                                MainActivity.this.finish();

                                                // on below line we are exiting our activity
                                                System.exit(0);
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            }
                        }
                    }
                }

                return true;
            // Add more cases for other key events as needed
        }
        return super.dispatchKeyEvent(event);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int sdkVersion = Build.VERSION.SDK_INT;
        if (requestCode == PERMISSION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS) {
            Intent intent = new Intent();
            String packageName = MainActivity.this.getPackageName();
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
            startActivity(intent);
        }


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            Uri[] results = null;
            // Check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }
            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == this.mUploadMessage) {
                    return;
                }
                Uri result = null;
                try {
                    if (resultCode != RESULT_OK) {
                        result = null;
                    } else {
                        // retrieve from the private variable if the intent is null
                        result = data == null ? mCapturedImageURI : data.getData();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "activity :" + e,
                            Toast.LENGTH_LONG).show();
                }
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
        return;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = NetworkUtil.getConnectivityStatusString(context);
            Log.e("status","status>>>"+status);
            if(status.isEmpty()) {
                status="Poor Internet Connection";
                Toast.makeText(context, status, Toast.LENGTH_LONG).show();
            }else if(status.equals("null")) {
                status="Poor Internet Connection";
                Toast.makeText(context, status, Toast.LENGTH_LONG).show();
            }else if(status.equals("No internet is available")){
                status="No internet is available";
                showSnack();
            }else{

                initPairing(pairCode);
            }

        }
    }
    private void showSnack() {
        String message;
        int color;
        message = "Sorry! No internet is available";
        color = Color.RED;
        Snackbar snackbar = Snackbar
                .make(contentLay, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(color);
        snackbar.show();
    }

    // TimerTask to be executed
    class MyTask extends TimerTask {
        @Override
        public void run() {
            // This code will run every 5 seconds
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    initPairing(pairCode);
                }
            });
        }
    }
    public void clearTimeout() {
        handler.removeCallbacks(myRunnable);
    }

    private class BrowserPage extends WebViewClient {
        private final List<ContentModel> list;
        public BrowserPage(List<ContentModel> list) {
            this.list=list;

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if(isNetworkAvailable()){
                progressBar.setVisibility(View.VISIBLE);
                parentInternetLay.setVisibility(GONE);
                super.onPageStarted(view, url, favicon);
            }
            else{
                parentInternetLay.setVisibility(VISIBLE);
                progressBar.setVisibility(View.GONE);

            }

        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(isNetworkAvailable()){
                progressBar.setVisibility(View.GONE);
                Log.e("test>>>","list"+list);
                contentLay(list);
            }
            else{
                parentInternetLay.setVisibility(VISIBLE);
                progressBar.setVisibility(View.GONE);

            }
            // Both url and title is available in this stage
            mUrl = view.getUrl();
        }
    }
    private class Browser extends WebChromeClient {
        private static final String TAG = "WebVIEW-Home";
        // For Android 5.0
        public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath,
                                         FileChooserParams fileChooserParams) {
            // Double check that we don't have any existing callbacks
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = filePath;
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.e(TAG, "Unable to create Image File", ex);
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                } else {
                    takePictureIntent = null;
                }
            }
            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType("image/*");
            Intent[] intentArray;
            if (takePictureIntent != null) {
                intentArray = new Intent[]{takePictureIntent};
            } else {
                intentArray = new Intent[0];
            }
            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Choose Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
            startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
            return true;
        }
        // openFileChooser for Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            // Create AndroidExampleFolder at sdcard
            // Create AndroidExampleFolder at sdcard
            File imageStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES)
                    , folderName);
            if (!imageStorageDir.exists()) {
                // Create AndroidExampleFolder at sdcard
                imageStorageDir.mkdirs();
            }
            // Create camera captured image file path and name
            File file = new File(
                    imageStorageDir + File.separator + "IMG_"
                            + String.valueOf(System.currentTimeMillis())
                            + ".jpg");
            mCapturedImageURI = Uri.fromFile(file);
            // Camera capture image intent
            final Intent captureIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            // Create file chooser intent
            Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
            // Set camera intent to file chooser
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS
                    , new Parcelable[] { captureIntent });
            // On select image call onActivityResult method of activity
            startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
        }
        // openFileChooser for Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "");
        }
        //openFileChooser for other Android versions
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType,
                                    String capture) {
            openFileChooser(uploadMsg, acceptType);
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(MyReceiver);
    }
}