package com.ehab.module;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        Button btn1 = new Button(this);
        btn1.setText("Click");

        Button btn2 = new Button(this);
        btn2.setText("Double Click");

        Button btn3 = new Button(this);
        btn3.setText("Long Press");

        btn1.setOnClickListener(v -> open("click"));
        btn2.setOnClickListener(v -> open("double"));
        btn3.setOnClickListener(v -> open("long"));

        layout.addView(btn1);
        layout.addView(btn2);
        layout.addView(btn3);

        setContentView(layout);
    }

    void open(String type) {
        Intent i = new Intent(this, ActionPickerActivity.class);
        i.putExtra("type", type);
        startActivity(i);
    }
}
