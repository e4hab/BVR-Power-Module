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

        Button click = new Button(this);
        click.setText("Click");

        Button dbl = new Button(this);
        dbl.setText("Double Click");

        Button lng = new Button(this);
        lng.setText("Long Press");

        click.setOnClickListener(v -> open("CLICK"));
        dbl.setOnClickListener(v -> open("DOUBLE"));
        lng.setOnClickListener(v -> open("LONG"));

        layout.addView(click);
        layout.addView(dbl);
        layout.addView(lng);

        setContentView(layout);
    }

    private void open(String type) {
        Intent i = new Intent(this, ActionPickerActivity.class);
        i.putExtra("type", type);
        startActivity(i);
    }
}
