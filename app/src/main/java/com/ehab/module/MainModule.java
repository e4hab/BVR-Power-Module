package com.ehab.module;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.XC_MethodHook;

public class MainModule implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    private static Context mContext;

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        if (!lpparam.packageName.equals("android")) return;

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

                        if (event.getKeyCode() == KeyEvent.KEYCODE_POWER &&
                                event.getAction() == KeyEvent.ACTION_DOWN) {

                            Context context = (Context) XposedHelpers.getObjectField(
                                    param.thisObject,
                                    "mContext"
                            );

                            Intent intent = new Intent("com.ehab.POWER_ACTION");
                            context.sendBroadcast(intent);

                            // prevent default power button behavior
                            param.setResult(0);
                        }
                    }
                }
        );
    }
}
