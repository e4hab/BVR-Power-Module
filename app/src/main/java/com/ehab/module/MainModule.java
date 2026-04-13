package com.ehab.module;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XC_MethodHook;

import android.view.KeyEvent;

public class MainModule implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

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

                        if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {

                            if (event.getAction() == KeyEvent.ACTION_DOWN) {

                                Runtime.getRuntime().exec(new String[]{
                                        "su",
                                        "-c",
                                        "am broadcast -a com.jozein.xedgepro.PERFORM"
                                });

                                param.setResult(0);
                            }
                        }
                    }
                }
        );
    }
}
