package com.ehab.module;

import android.view.KeyEvent;
import android.content.Context;
import android.content.Intent;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.XC_MethodHook;

public class MainModule implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        if (!lpparam.packageName.equals("android")) return;

        Class<?> ims = XposedHelpers.findClass(
                "com.android.server.input.InputManagerService",
                lpparam.classLoader
        );

        XposedHelpers.findAndHookMethod(
                ims,
                "interceptKeyBeforeQueueing",
                KeyEvent.class,
                int.class,
                new XC_MethodHook() {

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                        KeyEvent event = (KeyEvent) param.args[0];

                        if (event.getKeyCode() == KeyEvent.KEYCODE_POWER &&
                                event.getAction() == KeyEvent.ACTION_DOWN) {

                            Object thisObj = param.thisObject;

                            Context context = (Context) XposedHelpers.getObjectField(
                                    thisObj,
                                    "mContext"
                            );

                            Intent i = context.getPackageManager()
                                    .getLaunchIntentForPackage("com.android.settings");

                            if (i != null) {
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(i);
                            }
                        }
                    }
                }
        );
    }
}
