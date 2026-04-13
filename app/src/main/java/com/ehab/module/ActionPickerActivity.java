package com.ehab.module;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class ActionPickerActivity extends Activity {

    List<String> appNames = new ArrayList<>();
    List<String> packageNames = new ArrayList<>();
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        type = getIntent().getStringExtra("type");

        ListView listView = new ListView(this);

        PackageManager pm = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);

        for (ResolveInfo app : apps) {
            String name = app.loadLabel(pm).toString();
            String pkg = app.activityInfo.packageName;

            appNames.add(name);
            packageNames.add(pkg);
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, appNames);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {

            String pkg = packageNames.get(position);

            getSharedPreferences("prefs", MODE_PRIVATE)
                    .edit()
                    .putString(type, pkg)
                    .apply();

            finish();
        });

        setContentView(listView);
    }
}
