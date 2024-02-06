package com.matainja.bootapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.matainja.bootapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class TerminalShowActivity extends AppCompatActivity {
    String itemName, appId;
    ProgressBar terminalProgress;
    ImageView terminalLogo,arrowBack;
    TextView queueid,average_waiting_time,people_before,queueTitle;
    CardView terminalCard;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal_show);
        terminalProgress=findViewById(R.id.terminalProgress);
        terminalLogo=findViewById(R.id.terminalLogo);
        arrowBack=findViewById(R.id.arrowBack);
        queueid=findViewById(R.id.queueid);
        terminalCard=findViewById(R.id.terminalCard);
        average_waiting_time=findViewById(R.id.average_waiting_time);
        people_before=findViewById(R.id.people_before);
        queueTitle=findViewById(R.id.queueTitle);
        Intent intent= getIntent();
        itemName=intent.getStringExtra("item");
        appId=intent.getStringExtra("appId");
        Log.e("TerminalShowActivity","itemNameappId>>"+appId+">>>"+itemName);

        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Click","Click>>>");
                finish();
            }
        });
        initView();
    }

    private void initView() {
        terminalProgress.setVisibility(View.VISIBLE);
        terminalLogo.setVisibility(View.GONE);
        queueid.setVisibility(View.GONE);
        average_waiting_time.setVisibility(View.GONE);
        people_before.setVisibility(View.GONE);
        terminalCard.setVisibility(View.GONE);
        queueTitle.setVisibility(View.GONE);
        terminalCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        String url="";
        try {
            String encodedDeptName = URLEncoder.encode(itemName, "UTF-8");
            url= "https://app.neosign.tv/api/addinqueue"+"?"+"appid="+appId+"&"+"deptname="+encodedDeptName;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringRequest getRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAG","response>>>"+response);
                        terminalProgress.setVisibility(View.GONE);
                        terminalLogo.setVisibility(View.VISIBLE);
                        queueid.setVisibility(View.VISIBLE);
                        average_waiting_time.setVisibility(View.VISIBLE);
                        people_before.setVisibility(View.VISIBLE);
                        terminalCard.setVisibility(View.VISIBLE);
                        queueTitle.setVisibility(View.VISIBLE);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            Boolean status = jsonObject.getBoolean("success");
                            if(status){
                                int queue_id = jsonObject.getInt("queueid");
                                int peopleBefore = jsonObject.getInt("people_before");
                                String logo = jsonObject.getString("logo");
                                int averageWaitingTime = jsonObject.getInt("average_waiting_time");

                                SpannableStringBuilder builder = new SpannableStringBuilder();
                                String textPart1 = String.valueOf(peopleBefore);
                                builder.append(textPart1);
                                String textPart2 = " people before you.";
                                SpannableString spannableString2 = new SpannableString(textPart2);
                                spannableString2.setSpan(new ForegroundColorSpan( Color.parseColor("#666362")), 0, textPart2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                builder.append(spannableString2);
                                people_before.setText(builder, TextView.BufferType.SPANNABLE);

                                SpannableStringBuilder builder1 = new SpannableStringBuilder();

                                String textPart3 = "Average waiting time: ";
                                SpannableString spannableString3 = new SpannableString(textPart3);
                                spannableString3.setSpan(new ForegroundColorSpan( Color.parseColor("#666362")), 0, textPart3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                builder1.append(spannableString3);
                                String textPart4 = String.valueOf(averageWaitingTime)+" minutes";
                                builder1.append(textPart4);
                                average_waiting_time.setText(builder1, TextView.BufferType.SPANNABLE);

                                Glide.with(getApplicationContext())
                                        .load(logo)
                                        .error(R.drawable.neo_logo)
                                        .into(terminalLogo);
                                queueid.setText(String.valueOf(queue_id));
                                //people_before.setText(String.valueOf(peopleBefore)+" people before you.");
                                //average_waiting_time.setText("Average waiting time: "+String.valueOf(averageWaitingTime)+" minutes");
                            }else{

                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("Tag","jsonObject>>>"+jsonObject);







                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e("Error", "-----VollyError----: "+error.getMessage());
                        terminalProgress.setVisibility(View.GONE);
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(TerminalShowActivity.this);
        requestQueue.add(getRequest);
        getRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );

    }

}