package com.matainja.bootapplication.Fragment;

import static com.matainja.bootapplication.session.SessionManagement.PAIRING_CODE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.matainja.bootapplication.R;
import com.matainja.bootapplication.session.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


public class PairingFragment extends Fragment {
    TextView pairingCode;
    SessionManagement sessionManagement;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    String pairCode;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pairing, container, false);
        pairingCode = view.findViewById(R.id.pairingCode);

        sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sessionManagement = new SessionManagement(requireContext());
        HashMap<String, String> getPairDetails = new HashMap<String, String>();
        getPairDetails = sessionManagement.getParingDetails();
        pairCode = getPairDetails.get(PAIRING_CODE);
        String deviceId = Settings.Secure.getString(requireContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("Tag","deviceId>>>"+deviceId);
        if (pairCode.equals("")){
            // Generate a random number
            Random random = new Random();
            pairCode = UUID.randomUUID().toString().substring(0,5);
            // Update the TextView with the random number and text
            pairingCode.setText(pairCode);
            sessionManagement.createPairingSession(pairCode);
        }else{
            // Update the TextView with the random number and text
            pairingCode.setText(pairCode);

        }
        initPairing(pairCode);
       /* Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // Call the API here
                initPairing(pairCode);

            }
        };
        timer.scheduleAtFixedRate(task, 0, 10000);*/
        return view;
    }
    private void initPairing(String pairCode){
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
                                pairingCode.setText("Paired");
                                JSONObject data = jsonObject.getJSONObject("data") ;
                                Boolean dataStatus = data.getBoolean("status");
                                if(dataStatus){
                                    JSONArray dataArray = data.getJSONArray("data");
                                    if(dataArray.length()>0){
                                        for(int i =0; i<dataArray.length(); i++){

                                            JSONObject dataObject = dataArray.getJSONObject(i);

                                            String type = dataObject.getString("type");
                                            String url = dataObject.getString("url");
                                            String duration = dataObject.getString("duration");

                                        }
                                    }
                                }


                            }else {

                            }
                        }catch (JSONException ex){
                            ex.printStackTrace();
                            Log.e("Error", "-----Json Array----: "+ex.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("Error", "-----VollyError----: "+error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(getRequest);


        getRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}