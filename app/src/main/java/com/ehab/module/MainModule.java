package com.ehab.module;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.KeyEvent;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.XC_MethodHook;

public class MainModule implements IXposedHookLoadPackage {

    private static long lastClickTime = 0;
    private static int clickCount = 0;
    private static Handler handler = new Handler();

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        if (!lpparam.packageName.equals("android")) return;

        XposedBridge.log("BVR Module Loaded into android");

        // FORCE RECOMMENDED (strong hook)
        XposedHelpers.findAndHookMethod(
                "android.app.Activity",
                lpparam.classLoader,
                "onResume",
                new XC_MethodHook() {}
        );

        Class<?> pwm = XposedHelpers.findClass(
                "com.android.server.policy.PhoneWindowManager",
                lpparam.classLoader
        );

        XposedHelpers.findAndHookMethod(
                pwm,
                "interceptKeyBeforeQueueing",
                KeyEvent.class,
                int.class,
                new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                        KeyEvent event = (KeyEvent) param.args[0];

                        if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {

                            Context context = (Context) XposedHelpers.getObjectField(
                                    param.thisObject,
                                    "mContext"
                            );

                            if (event.getAction() == KeyEvent.ACTION_DOWN) {

                                long now = System.currentTimeMillis();

                                if (now - lastClickTime < 300) {
                                    clickCount++;
                                } else {
                                    clickCount = 1;
                                }

                                lastClickTime = now;

                                handler.removeCallbacksAndMessages(null);

                                handler.postDelayed(() -> {

                                    if (clickCount == 1) {
                                        sendAction(context, "CLICK");
                                    } else if (clickCount == 2) {
                                        sendAction(context, "DOUBLE");
                                    }

                                    clickCount = 0;

                                }, 300);

                                param.setResult(0);
                            }

                            if (event.getAction() == KeyEvent.ACTION_UP &&
                                    event.getEventTime() - event.getDownTime() > 500) {

                                sendAction(context, "LONG");
                                param.setResult(0);
                            }
                        }
                    }
                }
        );
    }

    private void sendAction(Context context, String type) {

        android.content.SharedPreferences prefs =
                context.getSharedPreferences("actions", Context.MODE_PRIVATE);

        String pkg = prefs.getString(type, null);

        if (pkg == null) return;

        Intent launch = context.getPackageManager()
                .getLaunchIntentForPackage(pkg);

        if (launch != null) {
            launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(launch);
        }
    }
}
