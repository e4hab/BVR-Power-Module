package com.ehab.module;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import android.util.Log;

public class MainModule implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        if (!lpparam.packageName.equals("android")) return;

        Log.i("BVR_Module", "Module is working");

        Runtime.getRuntime().exec(
            "am broadcast -a com.jozein.xedgepro.PERFORM -e data 33030236F6D6E216272656C637F6C6574796F6E637E224652555C64796D61647560236F6D6E216272656C637F6C6574796F6E637E224652555C64796D6164756E2255636F62746E4F67714364796679647973556474796E6763702"
        );
    }
}
