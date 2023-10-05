package com.matainja.bootapplication.activity;

import static com.matainja.bootapplication.session.SessionManagement.IS_AUTOSTART;
import static com.matainja.bootapplication.session.SessionManagement.IS_KEEPONTOP;
import static com.matainja.bootapplication.session.SessionManagement.IS_WAKEUP;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.matainja.bootapplication.R;
import com.matainja.bootapplication.session.SessionManagement;
import com.matainja.bootapplication.util.NetworkUtil;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity {
    Handler handler;
    LinearLayout webviewLay;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch autoStartSwitch;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch keepAwakeSwitch;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch keepOnTopSwitch;
    TextView auto_start,auto_start_title;
    ImageView menuBook;
    SessionManagement sessionManagement;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    boolean isWakeUP,isAutoStart,isKeepOnTop,isOptimizationPopUP=true,isAutoPopUP=true;
    private WebView myWebView;
    ProgressBar progressBar;
    String mUrl;
    private static final int FILECHOOSER_RESULTCODE   = 1;
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private String folderName="com.matainja";
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    ImageView action_image;
    TextView txtNoInternet;
    private  String[] AndroidBatteryPermission=new String[] {Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS};
    public static final int PERMISSION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS =  10;
    DrawerLayout drawer;
    NavigationView navigationView;
    private MainActivity.MyReceiver MyReceiver=null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initSession();
        accessAllPermission();
        webviewLay=(LinearLayout)findViewById(R.id.webviewLay);
        myWebView=(WebView)findViewById(R.id.webview);
        menuBook=(ImageView)findViewById(R.id.menuBook);
        action_image=(ImageView)findViewById(R.id.action_image);
        txtNoInternet=(TextView)findViewById(R.id.txtNoInternet);
        progressBar=(ProgressBar)findViewById(R.id.progress_Bar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView =(NavigationView)findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);


        autoStartSwitch=(Switch) header.findViewById(R.id.autoStartSwitch);
        keepAwakeSwitch=(Switch) header.findViewById(R.id.keepAwakeSwitch);
        keepOnTopSwitch=(Switch) header.findViewById(R.id.keepOnTopSwitch);
        TextView keep_awake = (TextView) header.findViewById(R.id.keep_awake);
        TextView exit = (TextView) header.findViewById(R.id.keep_exit);
        TextView keep_reload = (TextView) header.findViewById(R.id.keep_reload);
        auto_start=(TextView)header.findViewById(R.id.auto_start);
        auto_start_title=(TextView)header.findViewById(R.id.auto_start_title);
        ImageView keepExit = (ImageView) header.findViewById(R.id.keepExit);
        ImageView keepReload = (ImageView) header.findViewById(R.id.keepReload);



        menuBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }else{
                    drawer.openDrawer(GravityCompat.START);
                }

            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
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
        keepExit.setOnClickListener(new View.OnClickListener() {
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
        keep_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if(isNetworkAvailable()){
                    //String url = "https://matainja.com/";
                    //String url = "https://webplayer.neosign.tv/test.php";
                    String url = "https://webplayer.neosign.tv/";
                    webviewCall(url);
                }
                else{
                    showSnack();
                }

            }
        });
        keepReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if(isNetworkAvailable()){
                    //String url = "https://matainja.com/";
                    //String url = "https://webplayer.neosign.tv/test.php";
                    String url = "https://webplayer.neosign.tv/";
                    webviewCall(url);
                }
                else{
                    showSnack();
                }
            }
        });



        MyReceiver = new MyReceiver();
        broadcastIntent();

    }
    @Override
    protected void onResume() {
        super.onResume();
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

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sessionManagement = new SessionManagement(MainActivity.this);
        HashMap<String, String> getAutoStartDetails = new HashMap<String, String>();
        getAutoStartDetails = sessionManagement.getAutoStartDetails();
        isAutoStart = Boolean.parseBoolean(getAutoStartDetails.get(IS_AUTOSTART));
        Log.e("TAG", "isAutoStart>>> " + isAutoStart);

        accessAllPermission();
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
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock powerLatch = powerManager.newWakeLock(
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

    }
    public void broadcastIntent() {
        registerReceiver(MyReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
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

    @SuppressLint("JavascriptInterface")
    public void webviewCall(String newurl)
    {
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
        myWebView.setWebViewClient(new BrowserPage());
        myWebView.setWebChromeClient(new Browser());
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        if(isNetworkAvailable()){
            progressBar.setVisibility(View.VISIBLE);
            action_image.setVisibility(View.GONE);
            txtNoInternet.setVisibility(View.GONE);
            myWebView.loadUrl(newurl);
        }
        else{
            action_image.setVisibility(View.VISIBLE);
            txtNoInternet.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            action_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_network_check_24));

        }
    }
    private class BrowserPage extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if(isNetworkAvailable()){
                progressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }
            else{
                action_image.setVisibility(View.VISIBLE);
                txtNoInternet.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                action_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_network_check_24));
            }

        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(isNetworkAvailable()){
                progressBar.setVisibility(View.GONE);
            }
            else{
                action_image.setVisibility(View.VISIBLE);
                txtNoInternet.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                action_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_network_check_24));
            }
            // Both url and title is available in this stage
            mUrl = view.getUrl();
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private class Browser extends WebChromeClient {
        private static final String TAG = "WebVIEW-Home";
        // For Android 5.0
        public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath,
                                         WebChromeClient.FileChooserParams fileChooserParams) {
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
                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
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



    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = NetworkUtil.getConnectivityStatusString(context);
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
                //String url = "https://matainja.com/";
                //String url = "https://webplayer.neosign.tv/test.php";
                String url = "https://webplayer.neosign.tv/";
                webviewCall(url);
            }

        }
    }
    private void showSnack() {
        String message;
        int color;
        message = "Sorry! No internet is available";
        color = Color.RED;
        Snackbar snackbar = Snackbar
                .make(webviewLay, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(color);
        //TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        //textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(MyReceiver);
    }
}