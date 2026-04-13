package com.ehab.module;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class ActionPickerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView listView = new ListView(this);
        setContentView(listView);

        String type = getIntent().getStringExtra("type");

        PackageManager pm = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);
        List<String> appNames = new ArrayList<>();
        List<String> packageNames = new ArrayList<>();

        for (ResolveInfo app : apps) {
            appNames.add(app.loadLabel(pm).toString());
            packageNames.add(app.activityInfo.packageName);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                appNames
        );

        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {

            SharedPreferences prefs = getSharedPreferences("actions", MODE_PRIVATE);

            prefs.edit()
                    .putString(type, packageNames.get(position))
                    .apply();

            finish();
        });
    }
}
