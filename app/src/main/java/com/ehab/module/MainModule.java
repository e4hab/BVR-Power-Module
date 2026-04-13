package com.ehab.module;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.view.KeyEvent;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainModule implements IXposedHookLoadPackage {

    long lastClick = 0;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        if (!lpparam.packageName.equals("android")) return;

        Class<?> phoneWindowManager = XposedHelpers.findClass(
                "com.android.server.policy.PhoneWindowManager",
                lpparam.classLoader
        );

        XposedHelpers.findAndHookMethod(
                phoneWindowManager,
                "interceptKeyBeforeQueueing",
                KeyEvent.class,
                int.class,
                new de.robv.android.xposed.XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                        KeyEvent event = (KeyEvent) param.args[0];

                        if (event.getKeyCode() == KeyEvent.KEYCODE_POWER
                                && event.getAction() == KeyEvent.ACTION_DOWN) {

                            Context context = (Context) XposedHelpers.getObjectField(param.thisObject, "mContext");

                            SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);

                            long now = SystemClock.elapsedRealtime();

                            if (now - lastClick < 300) {
                                launch(context, prefs.getString("double", null));
                            } else {
                                lastClick = now;
                            }

                            if (event.isLongPress()) {
                                launch(context, prefs.getString("long", null));
                            } else {
                                new Thread(() -> {
                                    try {
                                        Thread.sleep(350);
                                    } catch (Exception ignored) {}

                                    if (SystemClock.elapsedRealtime() - lastClick >= 300) {
                                        launch(context, prefs.getString("click", null));
                                    }
                                }).start();
                            }

                            param.setResult(0);
                        }
                    }
                }
        );
    }

    private void launch(Context context, String pkg) {
        if (pkg == null) return;

        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkg);
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } catch (Exception ignored) {}
    }
}
