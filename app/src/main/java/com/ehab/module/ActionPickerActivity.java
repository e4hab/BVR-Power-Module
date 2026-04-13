package com.ehab.module;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ActionPickerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String type = getIntent().getStringExtra("type");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        Button btnBroadcast = new Button(this);
        btnBroadcast.setText("Set Broadcast");

        btnBroadcast.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("config", MODE_PRIVATE);
            prefs.edit().putString(type, "broadcast").apply();

            Toast.makeText(this, type + " saved", Toast.LENGTH_SHORT).show();
            finish();
        });

        layout.addView(btnBroadcast);

        setContentView(layout);
    }
}
