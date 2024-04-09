package com.matainja.bootapplication.activity;

// CustomAlertDialog.java
import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.ContextCompat.startActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.matainja.bootapplication.R;

public class CustomAlertDialog extends Dialog implements View.OnClickListener {
    private String title;
    private String message;
    private TextView titleTextView;
    private TextView messageTextView;
    private Button okButton;

    public CustomAlertDialog(Context context, String title, String message) {
        super(context);
        this.title = title;
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_custom_alert_dialog);

        // Initialize views
        titleTextView = findViewById(R.id.alertTitle);
        messageTextView = findViewById(R.id.alertMessage);
        okButton = findViewById(R.id.okButton);

        // Set title and message
        titleTextView.setText(title);
        messageTextView.setText(message);

        // Set click listener for the OK button
        okButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        // Dismiss the dialog when OK button is clicked
        dismiss();

    }
}
