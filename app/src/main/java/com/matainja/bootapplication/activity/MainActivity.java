package com.matainja.bootapplication.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.matainja.bootapplication.session.SessionManagement.IS_AUTOSTART;
import static com.matainja.bootapplication.session.SessionManagement.IS_KEEPONTOP;
import static com.matainja.bootapplication.session.SessionManagement.IS_WAKEUP;
import static com.matainja.bootapplication.session.SessionManagement.ORIENTATION;
import static com.matainja.bootapplication.session.SessionManagement.PAIRING_CODE;
import static com.matainja.bootapplication.session.SessionManagement.PAIRING_STATUS;
import static com.matainja.bootapplication.session.SessionManagement.STRECH;

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
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.PictureDrawable;
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
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matainja.bootapplication.Model.ContentModel;
import com.matainja.bootapplication.Model.RSSModel;
import com.matainja.bootapplication.R;
import com.matainja.bootapplication.helper.RotatableMediaController;
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
import java.util.Arrays;
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
    private RotatableMediaController mediaControls;
    private int position = 0;
    TextView pairingCode;
    String pairCode;
    String countdownText;
    String orientation="Landscape";
    String strech="off";
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
    RelativeLayout webView_lay,parentContentRssFeed,childContentRssFeed;
    RelativeLayout parentTopOverlay,parentLeftOverlay,parentRightOverlay,parentBottomOverlay;
    TextView rssTitle,rssDescription,rssDate,textTopOverlay,textLeftOverlay,textRightOverlay,textBottomOverlay;
    ImageView rssImageView,rssQR;
    WebView myWebView;
    long newDuration=0;
    Handler handler;
    Runnable myRunnable;
    Handler handler1;
    Runnable myRunnable1;
    Handler handler2;
    Runnable myRunnable2;
    int currentIndex;
    int contentCurrentIndex=0;
    int slideShowCallCount=0;
    int rssContentCurrentIndex=0;
    int rssSlideShowCallCount=0;
    int overlayRssSlideShowCallCount=0;
    int overlaysRssContentCurrentIndex=0;
    private int currentPage = 0;
    int firstdataCount=0;
    int firstRssFeeddataCount=0;
    int firstRssFeedLaysdataCount=0;
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
    TextureView videoView;
    private MediaPlayer mediaPlayer;
    ProgressBar progressBar,video_progress,rssProgrss,pairProgress;
    List<ContentModel> slideItems = new ArrayList<>();
    List<ContentModel> newSlideItems = new ArrayList<>();
    ContentModel overLaysContentModel;



    @SuppressLint({"CutPasteId", "MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        initSession();





        //accessAllPermission();

        handler = new Handler();
        handler1 = new Handler();
        handler2 = new Handler();


        parentInternetLay=(LinearLayout) findViewById(R.id.parentInternetLay);
        contentLay=(CoordinatorLayout) findViewById(R.id.contentLay);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView =(NavigationView)findViewById(R.id.nav_view);

        parentTopOverlay =(RelativeLayout)findViewById(R.id.parentTopOverlay);
        parentLeftOverlay =(RelativeLayout)findViewById(R.id.parentLeftOverlay);
        parentRightOverlay =(RelativeLayout)findViewById(R.id.parentRightOverlay);
        parentBottomOverlay =(RelativeLayout)findViewById(R.id.parentBottomOverlay);
        textTopOverlay =(TextView)findViewById(R.id.textTopOverlay);
        textLeftOverlay =(TextView)findViewById(R.id.textLeftOverlay);
        textRightOverlay =(TextView)findViewById(R.id.textRightOverlay);
        textBottomOverlay =(TextView)findViewById(R.id.textBottomOverlay);







        parentPairing= findViewById(R.id.parentPairing);
        pairingCode = findViewById(R.id.pairingCode);
        parentContentRssFeed=(RelativeLayout)findViewById(R.id.parentContentRssFeed);
        childContentRssFeed=(RelativeLayout)findViewById(R.id.childContentRssFeed);
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
        pairProgress=(ProgressBar)findViewById(R.id.pairProgress);
        progressBar=(ProgressBar)findViewById(R.id.progress_Bar);
        video_progress=(ProgressBar)findViewById(R.id.video_progress);
        rssProgrss=(ProgressBar)findViewById(R.id.rssProgrss);
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
                                slideShowCallCount=0;
                                rssSlideShowCallCount=0;
                                overlayRssSlideShowCallCount=0;
                                clearTimeout();
                                clearTimeout1();
                                parentTopOverlay.setVisibility(GONE);
                                parentLeftOverlay.setVisibility(GONE);
                                parentRightOverlay.setVisibility(GONE);
                                parentBottomOverlay.setVisibility(GONE);
                                parentVideoView.setVisibility(GONE);
                                parentContentImage.setVisibility(GONE);
                                webView_lay.setVisibility(GONE);
                                parentContentRssFeed.setVisibility(GONE);
                                pairProgress.setVisibility(VISIBLE);
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
                                    //videoView.setZOrderOnTop(false);
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
                parentTopOverlay.setVisibility(GONE);
                parentLeftOverlay.setVisibility(GONE);
                parentRightOverlay.setVisibility(GONE);
                parentBottomOverlay.setVisibility(GONE);
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
                parentTopOverlay.setVisibility(GONE);
                parentLeftOverlay.setVisibility(GONE);
                parentRightOverlay.setVisibility(GONE);
                parentBottomOverlay.setVisibility(GONE);
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
                                    String orientations = data.getString("orientation");
                                    String stretch = data.getString("stretch");
                                    sessionManagement.createStrechSession(stretch);
                                    sessionManagement.createOrientationSession(orientations);
                                }

                                Boolean dataStatus = data.getBoolean("status");
                                if(dataStatus){
                                    sessionManagement.createPairingSession(true);
                                    pairingCode.setText("Paired");
                                    JSONArray dataArray = data.getJSONArray("data");
                                    if(dataArray.length()>0){
                                        parentPairing.setVisibility(GONE);
                                        pairProgress.setVisibility(GONE);
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

                                            String laysId="",laysCID="",laysType="",laysName="",laysheight="",
                                                    laysBgColor="",laysFontSize="",laysFontColor="",
                                                    laysFontFamily="",
                                                    laysContentType="",laysContent="",laysRssInfo="",laysDeleted="";
                                            if (dataObject.has("overlays") && !dataObject.isNull("overlays")) {
                                                JSONObject overlays = dataObject.getJSONObject("overlays");
                                                Log.e("overlays","overlays>>>"+overlays);
                                                laysId= String.valueOf(overlays.getInt("id"));
                                                laysCID=overlays.getString("cid");
                                                laysType=overlays.getString("type");
                                                laysName=overlays.getString("name");
                                                laysheight=overlays.getString("height");
                                                laysBgColor=overlays.getString("bg_color");
                                                laysFontSize=overlays.getString("font_size");
                                                laysFontColor=overlays.getString("font_color");
                                                laysFontFamily=overlays.getString("font_family");
                                                laysContentType=overlays.getString("content_type");
                                                laysContent=overlays.getString("content");
                                                laysRssInfo=overlays.getString("rssinfo");
                                                laysDeleted=overlays.getString("is_deleted");
                                                Log.e("overlays","laysContentType>>>"+laysContentType);
                                            }






                                            newSlideItems.add(new ContentModel(type, url, duration, extention,app_clock_hands_color,
                                                    app_clock_text,app_clock_timezone,app_clock_size,app_clock_minor_indicator_color,
                                                    app_clock_major_indicator_color,app_clock_innerdot_size,app_clock_innerdot_color,
                                                    cdtime,cdtranslation,app_cd_text,rssinfo,laysId,laysCID,laysType,laysName,laysheight,laysBgColor,laysFontSize,laysFontColor,laysFontFamily,
                                                    laysContentType,laysContent,laysRssInfo,laysDeleted
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
                                    else{
                                        parentVideoView.setVisibility(GONE);
                                        parentContentImage.setVisibility(GONE);
                                        webView_lay.setVisibility(GONE);
                                        parentContentRssFeed.setVisibility(GONE);
                                        parentTopOverlay.setVisibility(GONE);
                                        parentLeftOverlay.setVisibility(GONE);
                                        parentRightOverlay.setVisibility(GONE);
                                        parentBottomOverlay.setVisibility(GONE);
                                        pairProgress.setVisibility(VISIBLE);
                                        //parentPairing.setVisibility(VISIBLE);
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

    private void overLays(ContentModel item) {
        overLaysContentModel=item;

        if (item.getLaysContentType().equals("RSS feed")){
            if(item.getLaysType().equals("Right")){
                parentTopOverlay.setVisibility(GONE);
                parentLeftOverlay.setVisibility(GONE);
                parentRightOverlay.setVisibility(VISIBLE);
                parentBottomOverlay.setVisibility(GONE);

                String colorCode = item.getLaysBgColor();
                String solid = colorCode.substring(0, 7);
                String alphaStr = colorCode.substring(colorCode.length() - 2);
                // Convert the alpha component to an integer
                int alpha = Integer.parseInt(alphaStr, 16); // Parse hexadecimal to integer
                // Convert the solid color to an integer
                int solidColor = Color.parseColor(solid);
                // Apply alpha to the solid color
                int finalColor = Color.argb(alpha, Color.red(solidColor), Color.green(solidColor), Color.blue(solidColor));
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setColor(finalColor);
                gradientDrawable.setCornerRadii(new float[]{20, 20, 0, 0, 0, 0, 20, 20});
                parentRightOverlay.setBackground(gradientDrawable);

                textRightOverlay.setTextSize(Float.parseFloat(item.getLaysFontSize()));
                textRightOverlay.setTextColor(Color.parseColor(item.getLaysFontColor()));

                setWidthPercentage(parentRightOverlay, Integer.parseInt("20"));
                setHeightPercentage(parentRightOverlay, Integer.parseInt(item.getLaysheight()));
            }
            else if(item.getLaysType().equals("Left")){
                parentTopOverlay.setVisibility(GONE);
                parentLeftOverlay.setVisibility(VISIBLE);
                parentRightOverlay.setVisibility(GONE);
                parentBottomOverlay.setVisibility(GONE);
                String colorCode = item.getLaysBgColor();
                String solid = colorCode.substring(0, 7);
                String alphaStr = colorCode.substring(colorCode.length() - 2);
                // Convert the alpha component to an integer
                int alpha = Integer.parseInt(alphaStr, 16); // Parse hexadecimal to integer
                // Convert the solid color to an integer
                int solidColor = Color.parseColor(solid);
                // Apply alpha to the solid color
                int finalColor = Color.argb(alpha, Color.red(solidColor), Color.green(solidColor), Color.blue(solidColor));
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setColor(finalColor);
                gradientDrawable.setCornerRadii(new float[]{0, 0, 20, 20, 20, 20, 0, 0});
                parentLeftOverlay.setBackground(gradientDrawable);
                textLeftOverlay.setTextSize(Float.parseFloat(item.getLaysFontSize()));
                textLeftOverlay.setTextColor(Color.parseColor(item.getLaysFontColor()));
                setWidthPercentage(parentLeftOverlay, Integer.parseInt("20"));
                setHeightPercentage(parentLeftOverlay, Integer.parseInt(item.getLaysheight()));
            }
            else if(item.getLaysType().equals("Top")){
                parentTopOverlay.setVisibility(VISIBLE);
                parentLeftOverlay.setVisibility(GONE);
                parentRightOverlay.setVisibility(GONE);
                parentBottomOverlay.setVisibility(GONE);
                String colorCode = item.getLaysBgColor();
                String solid = colorCode.substring(0, 7);
                String alphaStr = colorCode.substring(colorCode.length() - 2);
                // Convert the alpha component to an integer
                int alpha = Integer.parseInt(alphaStr, 16); // Parse hexadecimal to integer
                // Convert the solid color to an integer
                int solidColor = Color.parseColor(solid);
                // Apply alpha to the solid color
                int finalColor = Color.argb(alpha, Color.red(solidColor), Color.green(solidColor), Color.blue(solidColor));
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setColor(finalColor);
                parentTopOverlay.setBackground(gradientDrawable);
                textTopOverlay.setTextSize(Float.parseFloat(item.getLaysFontSize()));
                textTopOverlay.setTextColor(Color.parseColor(item.getLaysFontColor()));
                setHeightPercentage(parentTopOverlay, Integer.parseInt(item.getLaysheight()));
            }
            else if(item.getLaysType().equals("Bottom")){
                parentTopOverlay.setVisibility(GONE);
                parentLeftOverlay.setVisibility(GONE);
                parentRightOverlay.setVisibility(GONE);
                parentBottomOverlay.setVisibility(VISIBLE);
                String colorCode = item.getLaysBgColor();
                String solid = colorCode.substring(0, 7);
                String alphaStr = colorCode.substring(colorCode.length() - 2);
                // Convert the alpha component to an integer
                int alpha = Integer.parseInt(alphaStr, 16); // Parse hexadecimal to integer
                // Convert the solid color to an integer
                int solidColor = Color.parseColor(solid);
                // Apply alpha to the solid color
                int finalColor = Color.argb(alpha, Color.red(solidColor), Color.green(solidColor), Color.blue(solidColor));
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setColor(finalColor);
                parentBottomOverlay.setBackground(gradientDrawable);
                textBottomOverlay.setTextSize(Float.parseFloat(item.getLaysFontSize()));
                textBottomOverlay.setTextColor(Color.parseColor(item.getLaysFontColor()));
                setHeightPercentage(parentBottomOverlay, Integer.parseInt(item.getLaysheight()));
            }


            Log.e("Tag","testing>>>6");
            List<RSSModel> overlaysRssList = new ArrayList<>();
            String overlayContent=item.getLaysContent();
            String newString = overlayContent.replace("https://app.neosign.tv/", "");

            String apiUrl = "https://app.neosign.tv/api/rss-feed";
            String apiUrlWithParams="";
            try {
                apiUrlWithParams = apiUrl + "?url=" + URLEncoder.encode(newString, "UTF-8");
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
                            rssProgrss.setVisibility(GONE);
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                if (jsonArray.length()>0){
                                    overlaysRssList.clear();
                                    for(int i=0;i<jsonArray.length();i++){
                                        JSONObject dataObject = jsonArray.getJSONObject(i);
                                        String title = dataObject.getString("title");
                                        String description = dataObject.getString("description");

                                        String date = dataObject.getString("date");
                                        String qr_code = dataObject.getString("qr_code").replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>","");
                                        Log.e("TAG","qr_code>>>"+qr_code);
                                        String photo= dataObject.getString("photo");
                                        RSSModel rssModel=new RSSModel(title,description,date,qr_code,photo);
                                        overlaysRssList.add(rssModel);
                                    }
                                }

                                if (overlayRssSlideShowCallCount==0){
                                    clearTimeout2();
                                    overlaysRssContentCurrentIndex=0;
                                    overlayRssContentLay(overlaysRssList,item);
                                    firstRssFeedLaysdataCount= overlaysRssList.size();
                                }

                            int newDataCount=overlaysRssList.size();
                            if(firstRssFeedLaysdataCount != newDataCount){
                                overlayRssSlideShowCallCount=0;
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
        else if (item.getLaysContentType().equals("Written text")){
            Log.e("Tag","testing>>>6");
            if(item.getLaysType().equals("Right")){
                parentTopOverlay.setVisibility(GONE);
                parentLeftOverlay.setVisibility(GONE);
                parentRightOverlay.setVisibility(VISIBLE);
                parentBottomOverlay.setVisibility(GONE);

                String colorCode = item.getLaysBgColor();
                String solid = colorCode.substring(0, 7);
                String alphaStr = colorCode.substring(colorCode.length() - 2);
                // Convert the alpha component to an integer
                int alpha = Integer.parseInt(alphaStr, 16); // Parse hexadecimal to integer
                // Convert the solid color to an integer
                int solidColor = Color.parseColor(solid);
                // Apply alpha to the solid color
                int finalColor = Color.argb(alpha, Color.red(solidColor), Color.green(solidColor), Color.blue(solidColor));
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setColor(finalColor);
                gradientDrawable.setCornerRadii(new float[]{20, 20, 0, 0, 0, 0, 20, 20});
                parentRightOverlay.setBackground(gradientDrawable);

                textRightOverlay.setTextSize(Float.parseFloat(item.getLaysFontSize()));
                textRightOverlay.setTextColor(Color.parseColor(item.getLaysFontColor()));

                setWidthPercentage(parentRightOverlay, Integer.parseInt("20"));
                setHeightPercentage(parentRightOverlay, Integer.parseInt(item.getLaysheight()));
                textRightOverlay.setText(item.getLaysContent());


                textAnimation(textRightOverlay);

            }
            else if(item.getLaysType().equals("Left")){
                parentTopOverlay.setVisibility(GONE);
                parentLeftOverlay.setVisibility(VISIBLE);
                parentRightOverlay.setVisibility(GONE);
                parentBottomOverlay.setVisibility(GONE);

                String colorCode = item.getLaysBgColor();
                String solid = colorCode.substring(0, 7);
                String alphaStr = colorCode.substring(colorCode.length() - 2);
                // Convert the alpha component to an integer
                int alpha = Integer.parseInt(alphaStr, 16); // Parse hexadecimal to integer
                // Convert the solid color to an integer
                int solidColor = Color.parseColor(solid);
                // Apply alpha to the solid color
                int finalColor = Color.argb(alpha, Color.red(solidColor), Color.green(solidColor), Color.blue(solidColor));
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setColor(finalColor);
                gradientDrawable.setCornerRadii(new float[]{0, 0, 20, 20, 20, 20, 0, 0});
                parentLeftOverlay.setBackground(gradientDrawable);

                textLeftOverlay.setTextSize(Float.parseFloat(item.getLaysFontSize()));
                textLeftOverlay.setTextColor(Color.parseColor(item.getLaysFontColor()));

                setWidthPercentage(parentLeftOverlay, Integer.parseInt("20"));
                setHeightPercentage(parentLeftOverlay, Integer.parseInt(item.getLaysheight()));
                Log.e("Tag","testing>>>8");
                textLeftOverlay.setText(item.getLaysContent());

                textAnimation(textLeftOverlay);
            }
            else if(item.getLaysType().equals("Top")){
                parentTopOverlay.setVisibility(VISIBLE);
                parentLeftOverlay.setVisibility(GONE);
                parentRightOverlay.setVisibility(GONE);
                parentBottomOverlay.setVisibility(GONE);

                String colorCode = item.getLaysBgColor();
                String solid = colorCode.substring(0, 7);
                String alphaStr = colorCode.substring(colorCode.length() - 2);
                // Convert the alpha component to an integer
                int alpha = Integer.parseInt(alphaStr, 16); // Parse hexadecimal to integer
                // Convert the solid color to an integer
                int solidColor = Color.parseColor(solid);
                // Apply alpha to the solid color
                int finalColor = Color.argb(alpha, Color.red(solidColor), Color.green(solidColor), Color.blue(solidColor));
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setColor(finalColor);
                parentTopOverlay.setBackground(gradientDrawable);

                textTopOverlay.setTextSize(Float.parseFloat(item.getLaysFontSize()));
                textTopOverlay.setTextColor(Color.parseColor(item.getLaysFontColor()));
                setHeightPercentage(parentTopOverlay, Integer.parseInt(item.getLaysheight()));
                Log.e("Tag","testing>>>9");
                textTopOverlay.setText(item.getLaysContent());

                textAnimation(textTopOverlay);
            }
            else if(item.getLaysType().equals("Bottom")){
                parentTopOverlay.setVisibility(GONE);
                parentLeftOverlay.setVisibility(GONE);
                parentRightOverlay.setVisibility(GONE);
                parentBottomOverlay.setVisibility(VISIBLE);
                Log.e("Tag","Color>>>"+item.getLaysBgColor());
                Log.e("Tag","Color>>>"+item.getLaysFontColor());

                String colorCode = item.getLaysBgColor();
                String solid = colorCode.substring(0, 7);
                String alphaStr = colorCode.substring(colorCode.length() - 2);
                // Convert the alpha component to an integer
                int alpha = Integer.parseInt(alphaStr, 16); // Parse hexadecimal to integer
                // Convert the solid color to an integer
                int solidColor = Color.parseColor(solid);
                // Apply alpha to the solid color
                int finalColor = Color.argb(alpha, Color.red(solidColor), Color.green(solidColor), Color.blue(solidColor));
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setColor(finalColor);
                parentBottomOverlay.setBackground(gradientDrawable);

                textBottomOverlay.setTextSize(Float.parseFloat(item.getLaysFontSize()));
                textBottomOverlay.setTextColor(Color.parseColor(item.getLaysFontColor()));
                setHeightPercentage(parentBottomOverlay, Integer.parseInt(item.getLaysheight()));
                Log.e("Tag","testing>>>10");

                textBottomOverlay.setText(item.getLaysContent());

                textAnimation(textBottomOverlay);






            }
        }else{
            parentTopOverlay.setVisibility(GONE);
            parentLeftOverlay.setVisibility(GONE);
            parentRightOverlay.setVisibility(GONE);
            parentBottomOverlay.setVisibility(GONE);
        }

    }
    private void textAnimation(TextView textOverlay) {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        Paint textPaint = textOverlay.getPaint();
        // Get the text content of the TextView
        CharSequence text = textOverlay.getText();
        // Calculate the width of the text based on the text size and content
        int width = (int) Math.ceil(textPaint.measureText(text.toString()));
        Log.e("screenWidth>>",""+screenWidth+" "+width);
        if (width <= screenWidth) {
            // Create a translation animation to make it scroll horizontally
            TranslateAnimation marqueeAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 1f,
                    Animation.RELATIVE_TO_SELF, -1f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f);

            // Set the animation properties
            marqueeAnimation.setInterpolator(new LinearInterpolator());
            marqueeAnimation.setRepeatCount(Animation.INFINITE);
            marqueeAnimation.setRepeatMode(Animation.RESTART);
            marqueeAnimation.setDuration(15000); // Adjust the duration as needed
            // Start the animation
            textOverlay.startAnimation(marqueeAnimation);
        }else{
            textOverlay.setHorizontallyScrolling(true);
            textOverlay.setSelected(true);
        }
    }

    private void overlayRssContentLay(List<RSSModel> overlaysRssList, ContentModel contentModel) {
        overlayRssSlideShowCallCount++;
        long duration = 20000;

        if (overlaysRssList.size()>0){
            RSSModel item = overlaysRssList.get(overlaysRssContentCurrentIndex);
            String ovelaytext;
            if (overLaysContentModel.getLaysRssInfo() != null) {
                String rssinfo = overLaysContentModel.getLaysRssInfo();
                String[] rssinfoArray = rssinfo.split(",");
                List<String> rssinfoList = Arrays.asList(rssinfoArray);

                ovelaytext=item.getDate();

                if (rssinfoList.contains("1")) {
                    String text = item.getTitle();
                    SpannableString spannableString = new SpannableString(text);
                    // Set text size for a specific part of the string
                    float relativeSize = 22f; // Change this value to adjust the size
                    spannableString.setSpan(new RelativeSizeSpan(relativeSize), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    // Set text style for a specific part of the string (e.g., bold)
                    spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ovelaytext=String.format(ovelaytext, 18f)+ "  " +spannableString;
                }
                if (rssinfoList.contains("2")) {
                    ovelaytext=ovelaytext+ "  " + String.format(item.getDescription(), 18f);
                }

            }else{
                ovelaytext=String.format(item.getDate(), 18f) + "  " + String.format(item.getTitle(), 20f,true) +  "  " + String.format(item.getDescription(), 20f);
            }
            if(overLaysContentModel.getLaysType().equals("Right")){
                Log.e("Tag","testingRight>>>9");
                textRightOverlay.setText(ovelaytext);
                textAnimation(textRightOverlay);
            }
            else if(overLaysContentModel.getLaysType().equals("Left")){
                Log.e("Tag","testingLeft>>>9");
                textLeftOverlay.setText(ovelaytext);
                textAnimation(textLeftOverlay);

            }
            else if(overLaysContentModel.getLaysType().equals("Top")){
                Log.e("Tag","testingTop>>>9");
                textTopOverlay.setText(ovelaytext);
                textAnimation(textTopOverlay);

            }
            else if(overLaysContentModel.getLaysType().equals("Bottom")){
                Log.e("Tag","testingBottom>>>9");
                textBottomOverlay.setText(ovelaytext);
                textAnimation(textBottomOverlay);
            }






        }
        overlaysRssContentCurrentIndex++;
        if (overlaysRssContentCurrentIndex >= overlaysRssList.size()) {
            overlaysRssContentCurrentIndex = 0;
        }
        myRunnable2 = new Runnable() {
            @Override
            public void run() {
                overlayRssContentLay(overlaysRssList, overLaysContentModel);
            }
        };
        handler2.postDelayed(myRunnable2, duration);
    }

    private void setWidthPercentage(RelativeLayout view, int percentage) {
        // Get the screen width in pixels
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;

        // Calculate the desired width based on the percentage
        int desiredWidth = (int) (screenWidth * (percentage / 100.0));

        // Set the width of the view
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = desiredWidth;
        view.setLayoutParams(params);
    }
    private void setHeightPercentage(RelativeLayout view, int percentage) {
        // Get the screen height in pixels
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenHeight = displayMetrics.heightPixels;

        // Calculate the desired height based on the percentage
        int desiredHeight = (int) (screenHeight * (percentage / 100.0));

        // Set the height of the view
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = desiredHeight;
        view.setLayoutParams(params);
    }

    private void contentLay(List<ContentModel> list) {
        HashMap<String, String> getOrientationDetails = new HashMap<String, String>();
        getOrientationDetails = sessionManagement.getOrientDetails();
        orientation=getOrientationDetails.get(ORIENTATION);
        HashMap<String, String> getStrechsDetails = new HashMap<String, String>();
        getStrechsDetails = sessionManagement.getStrechDetails();
        strech=getStrechsDetails.get(STRECH);
        slideShowCallCount++;
        long duration = Long.parseLong(list.get(contentCurrentIndex).getDuration()); // Set the duration in milliseconds

        ContentModel item = list.get(contentCurrentIndex);
        Log.e("newDuration","getLaysContentType()>>>"+item.getLaysContentType());
        parentTopOverlay.setVisibility(GONE);
        parentLeftOverlay.setVisibility(GONE);
        parentRightOverlay.setVisibility(GONE);
        parentBottomOverlay.setVisibility(GONE);


        if (item.getType().equals("image")){
            parentVideoView.setVisibility(GONE);
            parentContentRssFeed.setVisibility(GONE);
            parentContentImage.setVisibility(VISIBLE);
            webView_lay.setVisibility(GONE);
            content_image.setImageBitmap(null);
            content_image.destroyDrawingCache();
            if (strech.equals("off")){

            }else{
                content_image.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            Glide.with(getApplicationContext())
                    .load(item.getUrl())
                    .error(R.drawable.neo_logo)
                    .into(content_image);


            if (orientation.equals("90 degrees")) {
                // Rotate the DrawerLayout
                content_image.setRotation(90);
            }
            else if (orientation.equals("180 degrees")) {
                // Rotate the DrawerLayout
                content_image.setRotation(180);
            }
            else if (orientation.equals("270 degrees")) {
                // Rotate the DrawerLayout
                content_image.setRotation(270);
            }
            else {
                // Rotate the DrawerLayout
                content_image.setRotation(0);
            }

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
            Log.e("Tag","videoview0"+videoView.getSurfaceTexture());

            if(videoView.getSurfaceTexture()!=null){
                releaseMediaPlayer();
                initializeAndPrepareMediaPlayer2(item.getUrl(), duration, list);
            }

            videoView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                    initializeAndPrepareMediaPlayer(surface,item.getUrl(), duration, list);
                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                    // Handle size changes if needed
                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                    // Release the MediaPlayer when the TextureView is destroyed
                    releaseMediaPlayer();
                    return false;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surface) {


                }
            });


        }

        else if(item.getType().equals("app")&&item.getExtention().equals("Youtube")){
            parentContentImage.setVisibility(GONE);
            parentVideoView.setVisibility(GONE);
            parentContentRssFeed.setVisibility(GONE);
            webView_lay.setVisibility(VISIBLE);
            if (orientation.equals("90 degrees")) {

                myWebView.setRotation(90);
            }
            else if (orientation.equals("180 degrees")) {

                myWebView.setRotation(180);
            }
            else if (orientation.equals("270 degrees")) {
                // Rotate the DrawerLayout
                myWebView.setRotation(270);
            }
            else {

                myWebView.setRotation(0);
            }

            iFrameLay(item.getUrl(),list,duration);

        }
        else if(item.getType().equals("app")&&item.getExtention().equals("Clock")){
            parentContentImage.setVisibility(GONE);
            parentContentRssFeed.setVisibility(GONE);
            parentVideoView.setVisibility(GONE);
            webView_lay.setVisibility(VISIBLE);
            if (orientation.equals("90 degrees")) {
                myWebView.setRotation(90);
            }
            else if (orientation.equals("180 degrees")) {

                myWebView.setRotation(180);
            }
            else if (orientation.equals("270 degrees")) {

                myWebView.setRotation(270);
            }
            else {

                myWebView.setRotation(0);
            }
            clockiFrameLay(item.getUrl(),list,item,duration);

        }
        else if(item.getType().equals("app")&&item.getExtention().equals("Countdown")){
            parentContentImage.setVisibility(GONE);
            parentContentRssFeed.setVisibility(GONE);
            parentVideoView.setVisibility(GONE);
            webView_lay.setVisibility(VISIBLE);
            if (orientation.equals("90 degrees")) {

                myWebView.setRotation(90);
            }
            else if (orientation.equals("180 degrees")) {

                myWebView.setRotation(180);
            }
            else if (orientation.equals("270 degrees")) {

                myWebView.setRotation(270);
            }
            else {
                myWebView.setRotation(0);
            }
            countDowniFrameLay(item.getUrl(),list,item,duration);

        }
        else if(item.getType().equals("app")&&item.getExtention().equals("WebUrl")){
            parentContentImage.setVisibility(GONE);
            parentContentRssFeed.setVisibility(GONE);
            parentVideoView.setVisibility(GONE);
            webView_lay.setVisibility(VISIBLE);
            if (orientation.equals("90 degrees")) {

                myWebView.setRotation(90);
            }
            else if (orientation.equals("180 degrees")) {

                myWebView.setRotation(180);
            }
            else if (orientation.equals("270 degrees")) {

                myWebView.setRotation(270);
            }
            else {

                myWebView.setRotation(0);
            }
            webUriiFrameLay(item.getUrl(),list,item,duration);

        }
        else if(item.getType().equals("app")&&item.getExtention().equals("Vimeo")){
            parentContentImage.setVisibility(GONE);
            parentVideoView.setVisibility(GONE);
            parentContentRssFeed.setVisibility(GONE);
            webView_lay.setVisibility(VISIBLE);
            if (orientation.equals("90 degrees")) {

                myWebView.setRotation(90);
            }
            else if (orientation.equals("180 degrees")) {

                myWebView.setRotation(180);
            }
            else if (orientation.equals("270 degrees")) {

                myWebView.setRotation(270);
            }
            else {

                myWebView.setRotation(0);
            }
            vimeoiFrameLay(item.getUrl(),list,item,duration);
        }
        else if(item.getType().equals("app")&&item.getExtention().equals("RSS FEED")){
            parentContentImage.setVisibility(GONE);
            parentVideoView.setVisibility(GONE);
            webView_lay.setVisibility(GONE);
            parentContentRssFeed.setVisibility(VISIBLE);
            rssProgrss.setVisibility(VISIBLE);
            String rssFeedUrl = item.getUrl();
            if (orientation.equals("90 degrees")) {
                childContentRssFeed.setRotation(90);
            }
            else if (orientation.equals("180 degrees")) {
                childContentRssFeed.setRotation(180);
            }
            else if (orientation.equals("270 degrees")) {
                childContentRssFeed.setRotation(270);
            }
            else {
                childContentRssFeed.setRotation(0);
            }
            rssFeediFrameLay(item.getUrl(),list,item,duration);

        }

        overLays(item);
        contentCurrentIndex++;
        if (contentCurrentIndex >= list.size()) {
            contentCurrentIndex = 0;
        }
        Log.e("newDuration","duration>>>"+duration);
        Log.e("newDuration","newDuration>>>"+newDuration);



    }

    private void initializeAndPrepareMediaPlayer(SurfaceTexture surface,String videoUrl, long duration, List<ContentModel> list) {
        // Initialize MediaPlayer
        mediaPlayer = new MediaPlayer();
        try {
            // Set the data source to a sample video URL, replace with your own video source
            mediaPlayer.setDataSource(MainActivity.this, Uri.parse(videoUrl));

            // Set the Surface for the MediaPlayer
            mediaPlayer.setSurface(new Surface(surface));
            Log.e("Tag","videoview");

            // Prepare the MediaPlayer asynchronously
            mediaPlayer.prepareAsync();

            // Set an event listener to start playing when prepared
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    // Adjust the rotation of the TextureView
                    if (orientation.equals("90 degrees")) {
                        // Rotate the DrawerLayout
                        videoView.setRotation(90);
                    }
                    else if (orientation.equals("180 degrees")) {
                        // Rotate the DrawerLayout
                        videoView.setRotation(180);
                    }
                    else if (orientation.equals("270 degrees")) {
                        // Rotate the DrawerLayout
                        videoView.setRotation(270);
                    }
                    else {
                        // Rotate the DrawerLayout
                        videoView.setRotation(0);
                    }

                    video_progress.setVisibility(GONE);
                    // Start playing the video
                    mp.start();
                    myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            contentLay(list);

                        }
                    };
                    handler.postDelayed(myRunnable, duration);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initializeAndPrepareMediaPlayer2(String videoUrl, long duration, List<ContentModel> list) {
        // Initialize MediaPlayer
        mediaPlayer = new MediaPlayer();
        try {
            // Set the data source to a sample video URL, replace with your own video source
            mediaPlayer.setDataSource(MainActivity.this, Uri.parse(videoUrl));
            mediaPlayer.setSurface(new Surface(videoView.getSurfaceTexture()));
            // Prepare the MediaPlayer asynchronously
            mediaPlayer.prepareAsync();

            // Set an event listener to start playing when prepared
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    // Adjust the rotation of the TextureView
                    if (orientation.equals("90 degrees")) {
                        // Rotate the DrawerLayout
                        videoView.setRotation(90);
                    }
                    else if (orientation.equals("180 degrees")) {
                        // Rotate the DrawerLayout
                        videoView.setRotation(180);
                    }
                    else if (orientation.equals("270 degrees")) {
                        // Rotate the DrawerLayout
                        videoView.setRotation(270);
                    }
                    else {
                        // Rotate the DrawerLayout
                        videoView.setRotation(0);
                    }

                    video_progress.setVisibility(GONE);
                    // Start playing the video
                    mp.start();
                    myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            contentLay(list);

                        }
                    };
                    handler.postDelayed(myRunnable, duration);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }



    private void rssFeediFrameLay(String url, List<ContentModel> list, ContentModel item, long duration) {
        List<RSSModel> rsslist = new ArrayList<>();
        String originalString = item.getUrl();
        String newString = originalString.replace("https://app.neosign.tv/", "");
        String rssFeedUrl = newString;


        if (item.getRssinfo() != null) {
            String rssinfo = item.getRssinfo();
            String[] rssinfoArray = rssinfo.split(",");
            List<String> rssinfoList = Arrays.asList(rssinfoArray);
            if (rssinfoList.contains("1")) {
                rssTitle.setVisibility(VISIBLE);
            }
            if (rssinfoList.contains("2")) {
                rssDescription.setVisibility(VISIBLE);
            }
            if (rssinfoList.contains("3")) {
                rssImageView.setVisibility(VISIBLE);
            }
            if (rssinfoList.contains("4")) {
                rssQR.setVisibility(VISIBLE);
            }

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
                        rssProgrss.setVisibility(GONE);
                        myRunnable = new Runnable() {
                            @Override
                            public void run() {
                                contentLay(list);
                            }
                        };
                        handler.postDelayed(myRunnable, duration);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length()>0){
                                rsslist.clear();
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject dataObject = jsonArray.getJSONObject(i);
                                    String title = dataObject.getString("title");
                                    String description = dataObject.getString("description");

                                    String date = dataObject.getString("date");
                                    String qr_code = dataObject.getString("qr_code").replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>","");
                                    Log.e("TAG","qr_code>>>"+qr_code);
                                    String photo= dataObject.getString("photo");
                                    RSSModel rssModel=new RSSModel(title,description,date,qr_code,photo);
                                    rsslist.add(rssModel);
                                }
                            }



                            if (rssSlideShowCallCount==0){
                                clearTimeout1();
                                rssContentCurrentIndex=0;
                                rssContentLay(rsslist);
                                firstRssFeeddataCount= rsslist.size();
                            }
                            int newDataCount=rsslist.size();
                            if(firstRssFeeddataCount != newDataCount){
                                rssSlideShowCallCount=0;
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

    private void rssContentLay(List<RSSModel> rsslist) {
        rssSlideShowCallCount++;
        long duration = 10000;
        if (rsslist.size()>0){
            RSSModel item = rsslist.get(rssContentCurrentIndex);
            Glide.with(getApplicationContext())
                    .load(item.getPhoto())
                    .error(R.drawable.neo_logo)
                    .into(rssImageView);
            rssTitle.setText(item.getTitle());
            rssDescription.setText(item.getDescription());
            rssDate.setText(item.getDate());
            try {
                // Load the SVG data from the string
                String svgData = "<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" width=\"80\" height=\"80\" viewBox=\"0 0 80 80\"><rect x=\"0\" y=\"0\" width=\"80\" height=\"80\" fill=\"#FFFFFF\"></rect><g transform=\"scale(1.951)\"><g transform=\"translate(0,0)\"><path fill-rule=\"evenodd\" d=\"M10 0L10 2L11 2L11 3L10 3L10 5L11 5L11 4L13 4L13 5L12 5L12 8L10 8L10 7L11 7L11 6L10 6L10 7L9 7L9 6L8 6L8 7L9 7L9 8L10 8L10 9L9 9L9 10L8 10L8 8L5 8L5 9L3 9L3 10L1 10L1 11L3 11L3 12L4 12L4 15L7 15L7 14L9 14L9 13L11 13L11 12L12 12L12 13L13 13L13 14L11 14L11 16L10 16L10 15L8 15L8 16L9 16L9 18L8 18L8 19L7 19L7 18L5 18L5 19L3 19L3 18L2 18L2 19L3 19L3 21L5 21L5 22L4 22L4 23L3 23L3 22L2 22L2 23L1 23L1 26L0 26L0 33L1 33L1 31L2 31L2 33L3 33L3 32L4 32L4 31L5 31L5 30L4 30L4 28L5 28L5 27L7 27L7 28L6 28L6 29L7 29L7 30L6 30L6 31L7 31L7 30L8 30L8 32L9 32L9 33L8 33L8 36L9 36L9 37L10 37L10 35L11 35L11 34L12 34L12 33L13 33L13 37L12 37L12 38L13 38L13 37L14 37L14 35L15 35L15 38L14 38L14 40L13 40L13 39L12 39L12 41L14 41L14 40L15 40L15 39L16 39L16 40L17 40L17 41L18 41L18 40L17 40L17 38L21 38L21 39L23 39L23 38L24 38L24 40L22 40L22 41L28 41L28 40L29 40L29 41L31 41L31 40L29 40L29 33L32 33L32 34L30 34L30 36L31 36L31 37L30 37L30 39L31 39L31 38L32 38L32 39L33 39L33 41L34 41L34 40L35 40L35 41L36 41L36 39L37 39L37 41L39 41L39 39L40 39L40 40L41 40L41 38L39 38L39 39L38 39L38 38L36 38L36 39L35 39L35 38L34 38L34 37L38 37L38 36L37 36L37 32L38 32L38 33L40 33L40 31L41 31L41 28L40 28L40 26L41 26L41 25L40 25L40 24L39 24L39 25L40 25L40 26L37 26L37 25L38 25L38 22L37 22L37 21L39 21L39 22L40 22L40 21L41 21L41 20L40 20L40 19L41 19L41 17L39 17L39 14L41 14L41 11L39 11L39 14L38 14L38 13L36 13L36 10L40 10L40 9L38 9L38 8L36 8L36 9L35 9L35 10L34 10L34 11L31 11L31 12L34 12L34 15L33 15L33 13L31 13L31 14L30 14L30 13L29 13L29 12L28 12L28 10L30 10L30 8L29 8L29 6L30 6L30 7L31 7L31 10L33 10L33 9L32 9L32 7L33 7L33 6L32 6L32 7L31 7L31 5L33 5L33 2L31 2L31 0L30 0L30 2L31 2L31 3L29 3L29 2L28 2L28 9L27 9L27 10L26 10L26 8L27 8L27 6L26 6L26 5L25 5L25 4L27 4L27 2L26 2L26 3L23 3L23 1L22 1L22 2L19 2L19 4L18 4L18 3L15 3L15 2L14 2L14 3L12 3L12 2L13 2L13 1L15 1L15 0ZM18 0L18 1L19 1L19 0ZM25 0L25 1L26 1L26 0ZM28 0L28 1L29 1L29 0ZM8 3L8 5L9 5L9 3ZM14 3L14 4L15 4L15 5L13 5L13 8L14 8L14 9L10 9L10 10L9 10L9 12L7 12L7 11L8 11L8 10L7 10L7 9L6 9L6 10L3 10L3 11L5 11L5 13L6 13L6 14L7 14L7 13L9 13L9 12L10 12L10 10L11 10L11 11L12 11L12 10L13 10L13 13L15 13L15 12L16 12L16 11L15 11L15 12L14 12L14 9L16 9L16 10L17 10L17 8L19 8L19 9L20 9L20 10L19 10L19 11L17 11L17 13L16 13L16 14L17 14L17 15L14 15L14 14L13 14L13 15L12 15L12 16L15 16L15 17L12 17L12 19L11 19L11 18L10 18L10 19L9 19L9 20L7 20L7 19L6 19L6 20L7 20L7 21L6 21L6 22L5 22L5 23L4 23L4 24L2 24L2 28L1 28L1 29L2 29L2 30L3 30L3 26L4 26L4 27L5 27L5 26L4 26L4 24L7 24L7 23L9 23L9 22L10 22L10 24L12 24L12 23L13 23L13 25L12 25L12 26L10 26L10 25L6 25L6 26L7 26L7 27L9 27L9 28L8 28L8 30L9 30L9 29L10 29L10 27L12 27L12 26L13 26L13 29L11 29L11 31L9 31L9 32L10 32L10 33L9 33L9 35L10 35L10 33L11 33L11 31L12 31L12 32L14 32L14 31L12 31L12 30L13 30L13 29L14 29L14 30L15 30L15 29L17 29L17 30L16 30L16 34L18 34L18 33L17 33L17 32L20 32L20 34L19 34L19 35L20 35L20 36L21 36L21 37L23 37L23 36L22 36L22 35L25 35L25 37L24 37L24 38L25 38L25 40L28 40L28 39L26 39L26 38L27 38L27 37L28 37L28 35L27 35L27 34L26 34L26 33L27 33L27 32L28 32L28 31L25 31L25 30L27 30L27 29L24 29L24 28L27 28L27 26L25 26L25 25L26 25L26 24L28 24L28 29L29 29L29 32L32 32L32 31L33 31L33 30L32 30L32 31L30 31L30 30L31 30L31 27L32 27L32 29L37 29L37 30L36 30L36 31L35 31L35 32L36 32L36 31L38 31L38 32L39 32L39 31L40 31L40 28L39 28L39 29L37 29L37 28L38 28L38 27L37 27L37 26L36 26L36 28L35 28L35 26L34 26L34 24L36 24L36 25L37 25L37 23L35 23L35 22L36 22L36 21L37 21L37 20L33 20L33 19L35 19L35 18L34 18L34 17L37 17L37 18L39 18L39 19L38 19L38 20L39 20L39 21L40 21L40 20L39 20L39 19L40 19L40 18L39 18L39 17L37 17L37 15L38 15L38 14L37 14L37 15L36 15L36 16L33 16L33 15L32 15L32 14L31 14L31 17L33 17L33 19L31 19L31 20L30 20L30 18L29 18L29 17L28 17L28 16L30 16L30 15L27 15L27 16L26 16L26 15L25 15L25 14L26 14L26 13L27 13L27 12L26 12L26 10L22 10L22 8L26 8L26 6L25 6L25 5L23 5L23 3L22 3L22 4L20 4L20 5L19 5L19 7L18 7L18 5L17 5L17 8L16 8L16 4L15 4L15 3ZM29 4L29 5L30 5L30 4ZM21 5L21 6L20 6L20 7L19 7L19 8L21 8L21 6L22 6L22 7L23 7L23 6L22 6L22 5ZM14 6L14 8L15 8L15 6ZM24 6L24 7L25 7L25 6ZM0 8L0 9L2 9L2 8ZM6 10L6 11L7 11L7 10ZM21 10L21 11L22 11L22 13L23 13L23 14L24 14L24 13L25 13L25 11L24 11L24 13L23 13L23 11L22 11L22 10ZM19 11L19 12L20 12L20 11ZM34 11L34 12L35 12L35 11ZM37 11L37 12L38 12L38 11ZM0 12L0 15L3 15L3 14L2 14L2 13L1 13L1 12ZM6 12L6 13L7 13L7 12ZM17 13L17 14L20 14L20 15L22 15L22 14L21 14L21 13ZM28 13L28 14L29 14L29 13ZM40 15L40 16L41 16L41 15ZM2 16L2 17L3 17L3 16ZM6 16L6 17L7 17L7 16ZM17 16L17 17L15 17L15 19L16 19L16 20L13 20L13 19L14 19L14 18L13 18L13 19L12 19L12 20L13 20L13 21L12 21L12 22L13 22L13 23L14 23L14 22L15 22L15 23L18 23L18 24L19 24L19 25L18 25L18 26L22 26L22 29L23 29L23 31L22 31L22 30L21 30L21 29L20 29L20 28L19 28L19 31L20 31L20 30L21 30L21 31L22 31L22 32L23 32L23 33L22 33L22 34L21 34L21 35L22 35L22 34L24 34L24 33L26 33L26 32L24 32L24 29L23 29L23 26L24 26L24 24L23 24L23 23L22 23L22 24L23 24L23 26L22 26L22 25L21 25L21 24L20 24L20 23L21 23L21 21L19 21L19 22L18 22L18 20L17 20L17 18L18 18L18 16ZM19 16L19 19L20 19L20 18L21 18L21 19L22 19L22 22L24 22L24 23L28 23L28 24L29 24L29 23L30 23L30 22L32 22L32 25L30 25L30 27L31 27L31 26L33 26L33 28L34 28L34 26L33 26L33 24L34 24L34 21L33 21L33 22L32 22L32 20L31 20L31 21L30 21L30 20L29 20L29 19L28 19L28 17L26 17L26 16L24 16L24 17L23 17L23 16L22 16L22 17L21 17L21 16ZM0 17L0 18L1 18L1 17ZM24 17L24 19L25 19L25 20L27 20L27 18L26 18L26 17ZM22 18L22 19L23 19L23 18ZM10 19L10 20L9 20L9 21L11 21L11 19ZM16 20L16 22L17 22L17 20ZM23 20L23 21L24 21L24 22L27 22L27 21L24 21L24 20ZM28 20L28 22L29 22L29 20ZM0 21L0 22L1 22L1 21ZM13 21L13 22L14 22L14 21ZM6 22L6 23L7 23L7 22ZM19 22L19 23L20 23L20 22ZM14 25L14 27L16 27L16 28L17 28L17 29L18 29L18 28L17 28L17 27L16 27L16 26L17 26L17 25ZM29 28L29 29L30 29L30 28ZM17 30L17 31L18 31L18 30ZM5 32L5 33L7 33L7 32ZM14 33L14 34L15 34L15 33ZM33 33L33 36L36 36L36 33ZM25 34L25 35L26 35L26 36L27 36L27 35L26 35L26 34ZM34 34L34 35L35 35L35 34ZM38 34L38 35L39 35L39 37L40 37L40 36L41 36L41 35L39 35L39 34ZM16 35L16 36L17 36L17 37L19 37L19 36L18 36L18 35ZM31 35L31 36L32 36L32 35ZM25 37L25 38L26 38L26 37ZM32 37L32 38L33 38L33 37ZM9 38L9 39L8 39L8 41L10 41L10 40L9 40L9 39L10 39L10 38ZM19 39L19 40L20 40L20 39ZM0 0L0 7L7 7L7 0ZM1 1L1 6L6 6L6 1ZM2 2L2 5L5 5L5 2ZM34 0L34 7L41 7L41 0ZM35 1L35 6L40 6L40 1ZM36 2L36 5L39 5L39 2ZM0 34L0 41L7 41L7 34ZM1 35L1 40L6 40L6 35ZM2 36L2 39L5 39L5 36Z\" fill=\"#000000\"></path></g></g></svg>"; // Replace with your actual resource name
                Log.e("QRData","svgData"+item.getQr());
                SVG svg = SVG.getFromString(item.getQr());

                // Set the SVG to the ImageView
                rssQR.setLayerType(ImageView.LAYER_TYPE_SOFTWARE, null);
                rssQR.setImageDrawable(new PictureDrawable(svg.renderToPicture()));
            } catch (SVGParseException e) {
                e.printStackTrace();
            }

        }
        rssContentCurrentIndex++;
        if (rssContentCurrentIndex >= rsslist.size()) {
            rssContentCurrentIndex = 0;
        }
        myRunnable1 = new Runnable() {
            @Override
            public void run() {
                rssContentLay(rsslist);
            }
        };
        handler1.postDelayed(myRunnable1, duration);
    }

    private void vimeoiFrameLay(String url, List<ContentModel> list, ContentModel item, long duration) {
        String originalString = item.getUrl();
        String newString = originalString.replace("https://app.neosign.tv/", "");
        String webUrl = newString;
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        String customCSS = "iframe { border: none; }";
        String javascript = "javascript:(function() {" +
                "var parent = document.getElementsByTagName('head').item(0);" +
                "var style = document.createElement('style');" +
                "style.type = 'text/css';" +
                "style.innerHTML = '" + customCSS + "';" +
                "parent.appendChild(style);" +
                "})()";
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
        myWebView.setWebViewClient(new WebViewClient() {
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
                    view.loadUrl(javascript);
                    myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            contentLay(list);
                        }
                    };
                    handler.postDelayed(myRunnable, duration);
                }
                else{
                    parentInternetLay.setVisibility(VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }
                // Both url and title is available in this stage
                mUrl = view.getUrl();
            }

        });
        //myWebView.setWebViewClient(new BrowserPage(list));
        myWebView.setWebChromeClient(new Browser());
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        myWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        myWebView.getSettings().setUseWideViewPort(true);

        if(isNetworkAvailable()){
            progressBar.setVisibility(View.VISIBLE);
            parentInternetLay.setVisibility(GONE);
            myWebView.loadUrl(newString+"?autoplay=1");
            //String dataUrl = "<iframe src='" + newString+"?autoplay=1"+"' frameborder='0' allowfullscreen='true' autoplay='true' muted='true' style='width:100%; height:100%;'></iframe>";
            //String dataUrl = "<iframe width=\"100%\" height=\"100%\"  src=\" + docUrl + \" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay=true; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
            //myWebView.loadData(dataUrl, "text/html", "utf-8");

        }
        else{
            parentInternetLay.setVisibility(VISIBLE);
            progressBar.setVisibility(View.GONE);

        }
    }

    private void webUriiFrameLay(String url, List<ContentModel> list, ContentModel item, long duration) {
        String originalString = item.getUrl();
        String newString = originalString.replace("https://app.neosign.tv/", "");
        String webUrl = newString;
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        String customCSS = "iframe { border: none; }";
        String javascript = "javascript:(function() {" +
                "var parent = document.getElementsByTagName('head').item(0);" +
                "var style = document.createElement('style');" +
                "style.type = 'text/css';" +
                "style.innerHTML = '" + customCSS + "';" +
                "parent.appendChild(style);" +
                "})()";

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
        myWebView.setWebViewClient(new WebViewClient() {
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
                    view.loadUrl(javascript);
                    myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            contentLay(list);
                        }
                    };
                    handler.postDelayed(myRunnable, duration);
                }
                else{
                    parentInternetLay.setVisibility(VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }
                // Both url and title is available in this stage
                mUrl = view.getUrl();
            }

        });
        //myWebView.setWebViewClient(new BrowserPage(list));
        myWebView.setWebChromeClient(new Browser());
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        myWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        myWebView.getSettings().setUseWideViewPort(true);

        if(isNetworkAvailable()){
            progressBar.setVisibility(View.VISIBLE);
            parentInternetLay.setVisibility(GONE);
            //String dataUrl ="<iframe width=\"100%\" height=\"100%\" src=" + webUrl + " ></iframe>";
            myWebView.loadUrl(webUrl);
            //String dataUrl = "<iframe width=\"100%\" height=\"100%\"  src=\" + docUrl + \" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay=true; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
            //myWebView.loadData(dataUrl, "text/html", "utf-8");

        }
        else{
            parentInternetLay.setVisibility(VISIBLE);
            progressBar.setVisibility(View.GONE);

        }

    }

    private void countDowniFrameLay(String url, List<ContentModel> list, ContentModel item, long duration) {
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        String customCSS = "iframe { border: none; }";
        String javascript = "javascript:(function() {" +
                "var parent = document.getElementsByTagName('head').item(0);" +
                "var style = document.createElement('style');" +
                "style.type = 'text/css';" +
                "style.innerHTML = '" + customCSS + "';" +
                "parent.appendChild(style);" +
                "})()";
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
        myWebView.setWebViewClient(new WebViewClient() {
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
                    view.loadUrl(javascript);
                    myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            contentLay(list);
                        }
                    };
                    handler.postDelayed(myRunnable, duration);
                }
                else{
                    parentInternetLay.setVisibility(VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }
                // Both url and title is available in this stage
                mUrl = view.getUrl();
            }

        });
        //myWebView.setWebViewClient(new BrowserPage(list));
        myWebView.setWebChromeClient(new Browser());
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        myWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        myWebView.getSettings().setUseWideViewPort(true);
        // Inject custom CSS to remove the default border around iframes

        if(isNetworkAvailable()){
            progressBar.setVisibility(View.VISIBLE);
            parentInternetLay.setVisibility(GONE);
            String timeString = convertTimeFormat(item.getCdtime());
            if (item.getApp_cd_text().equals("null")){
                countdownText = "";
            }else{
                countdownText = item.getApp_cd_text();
            }
            if (item.getCdtranslation().equals("null")){
                translationString = ",,,";
            }else{
                translationString = item.getCdtranslation();
            }
            String[] translation = translationString.split(",");

            String day="",hour="",minute="",second="";
            if(translation.length>0){
                if(translation[0].equals("null")){
                    day = "Days";
                }else{
                    day = translation[0];
                }
                if(translation[1].equals("null")){
                    hour = "Hours";
                }else{
                    hour = translation[1];
                }
                if(translation[2].equals("null")){
                    minute ="Minutes";
                }else{
                    minute = translation[2];
                }
                if(translation[3].equals("null")){
                    second = "Seconds";
                }else{
                    second = translation[3];
                }

            }

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
            myWebView.loadUrl(clockurl);
            String dataUrl ="<iframe width=\"100%\" height=\"100%\" src=" + clockurl + "></iframe>";
            //String dataUrl = "<iframe width=\"100%\" height=\"100%\"  src=\" + docUrl + \" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay=true; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
            //myWebView.loadData(dataUrl, "text/html", "utf-8");
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

    private void clockiFrameLay(String url, List<ContentModel> list, ContentModel item, long duration) {
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        String customCSS = "iframe { border: none; }";
        String javascript = "javascript:(function() {" +
                "var parent = document.getElementsByTagName('head').item(0);" +
                "var style = document.createElement('style');" +
                "style.type = 'text/css';" +
                "style.innerHTML = '" + customCSS + "';" +
                "parent.appendChild(style);" +
                "})()";
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
        myWebView.setWebViewClient(new WebViewClient() {
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
                    view.loadUrl(javascript);
                    myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            contentLay(list);
                        }
                    };
                    handler.postDelayed(myRunnable, duration);
                }
                else{
                    parentInternetLay.setVisibility(VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }
                // Both url and title is available in this stage
                mUrl = view.getUrl();
            }

        });
        //myWebView.setWebViewClient(new BrowserPage(list));
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
            String clockText=item.getApp_clock_text();
            if(clockText.equals("null")){
                clockText="";
            }else{
                clockText=item.getApp_clock_text();
            }
            // Construct the URL using the parameters
            String baseUrl = "https://webplayer.neosign.tv/clock.php?";
            Uri.Builder builder = Uri.parse(baseUrl).buildUpon();
            builder.appendQueryParameter("text", clockText);
            builder.appendQueryParameter("size", item.getApp_clock_size());
            builder.appendQueryParameter("hourColor", appClockHandsColorArray[0]);
            builder.appendQueryParameter("minuteColor", appClockHandsColorArray[1]);
            builder.appendQueryParameter("secondColor", appClockHandsColorArray[2]);
            builder.appendQueryParameter("minorIndicator",item.getApp_clock_minor_indicator_color());
            builder.appendQueryParameter("majorIndicator", item.getApp_clock_major_indicator_color());
            builder.appendQueryParameter("innerDotSize",  item.getApp_clock_innerdot_size());
            builder.appendQueryParameter("innerDotColor", item.getApp_clock_innerdot_color());

            String clockurl = builder.build().toString();
            myWebView.loadUrl(clockurl);
            //String dataUrl ="<iframe width=\"100%\" height=\"100%\" src=" + clockurl + " ></iframe>";
            //String dataUrl = "<iframe width=\"100%\" height=\"100%\"  src=\" + docUrl + \" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay=true; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
            //myWebView.loadData(dataUrl, "text/html", "utf-8");

        }
        else{
            parentInternetLay.setVisibility(VISIBLE);
            progressBar.setVisibility(View.GONE);

        }
    }

    private void iFrameLay(String newurl, List<ContentModel> list, long duration) {
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        String customCSS = "iframe { border: none; }";
        String javascript = "javascript:(function() {" +
                "var parent = document.getElementsByTagName('head').item(0);" +
                "var style = document.createElement('style');" +
                "style.type = 'text/css';" +
                "style.innerHTML = '" + customCSS + "';" +
                "parent.appendChild(style);" +
                "})()";
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
        myWebView.setWebViewClient(new WebViewClient() {
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
                    view.loadUrl(javascript);
                    myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            contentLay(list);
                        }
                    };
                    handler.postDelayed(myRunnable, duration);
                }
                else{
                    parentInternetLay.setVisibility(VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }
                // Both url and title is available in this stage
                mUrl = view.getUrl();
            }

        });
        //myWebView.setWebViewClient(new BrowserPage(list));
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
            myWebView.loadUrl(embeddedLink);
            //String dataUrl ="<iframe width=\"100%\" height=\"100%\" src=" + embeddedLink + " ></iframe>";
            //String dataUrl = "<iframe width=\"100%\" height=\"100%\"  src=\" + docUrl + \" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay=true; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
            //myWebView.loadData(dataUrl, "text/html", "utf-8");

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
                    //videoView.setZOrderOnTop(true);
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
                    //videoView.setZOrderOnTop(false);
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
                    //videoView.setZOrderOnTop(true);
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
                    //videoView.setZOrderOnTop(false);
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
                                                slideShowCallCount=0;
                                                rssSlideShowCallCount=0;
                                                overlayRssSlideShowCallCount=0;
                                                clearTimeout();
                                                clearTimeout1();
                                                parentTopOverlay.setVisibility(GONE);
                                                parentLeftOverlay.setVisibility(GONE);
                                                parentRightOverlay.setVisibility(GONE);
                                                parentBottomOverlay.setVisibility(GONE);
                                                parentVideoView.setVisibility(GONE);
                                                parentContentImage.setVisibility(GONE);
                                                webView_lay.setVisibility(GONE);
                                                parentContentRssFeed.setVisibility(GONE);
                                                pairProgress.setVisibility(VISIBLE);
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
                                                    //videoView.setZOrderOnTop(false);
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
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
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
    public void clearTimeout1() {
        handler1.removeCallbacks(myRunnable1);
    }
    public void clearTimeout2() {
        handler2.removeCallbacks(myRunnable2);
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
        //releaseMediaPlayer();
        unregisterReceiver(MyReceiver);
    }
}