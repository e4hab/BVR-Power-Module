package com.ehab.module;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 40, 40, 40);

        // Click
        Button btnClick = new Button(this);
        btnClick.setText("Click");
        btnClick.setOnClickListener(v -> openPicker("click"));

        // Double Click
        Button btnDouble = new Button(this);
        btnDouble.setText("Double Click");
        btnDouble.setOnClickListener(v -> openPicker("double"));

        // Long Press
        Button btnLong = new Button(this);
        btnLong.setText("Long Press");
        btnLong.setOnClickListener(v -> openPicker("long"));

        layout.addView(btnClick);
        layout.addView(btnDouble);
        layout.addView(btnLong);

        setContentView(layout);
    }

    private void openPicker(String type) {
        Intent intent = new Intent(this, ActionPickerActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }
}
