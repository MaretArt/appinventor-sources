package com.marchtech.BatteryOptimizations;

import static android.Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS;

import javax.swing.event.ChangeListener;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.common.*;

@DesignerComponent(version = 1,
                   description = "To determine if an application is already ignoring optimizations.",
                   category = ComponentCategory.EXTENSION,
                   nonVisible = true,
                   iconName = "images/extension.png")
@SimpleObject(external = true)
@UsesPermissions(permissionNames = "android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS")
public class BatteryOptimizations extends AndroidNonvisibleComponent {
    private String packageName;

    private final ComponentContainer container;
    private final PowerManager powerManager;

    private int requestCode;
    private int IGNORE_BATTERY_OPTIMIZATION_REQUEST = 1002;

    public BatteryOptimizations(ComponentContainer container) {
        super(container.$form());
        this.container = container;
        powerManager = (PowerManager) container.$context().getSystemService(Context.POWER_SERVICE);
        PackageName("");
        packageName = container.$context().getPackageName();
    }
/*
    @SimpleEvent(description = "Event after the user has ignored battery optimizations.")
    public void AfterIgnoring(int result) {
        EventDispatcher.dispatchEvent(this, "AfterIgnoring", result);
    }
*/
    @SimpleFunction(description = "Ask for ignoring battery optimizations")
    public void AskIgnoreOptimizations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
                Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                container.$context().startActivityForResult(intent, IGNORE_BATTERY_OPTIMIZATION_REQUEST);
            }
        }
    }

    @SimpleProperty(description = "Package name of application.", category = PropertyCategory.BEHAVIOR)
    public String PackageName() {
        return packageName;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "")
    @SimpleProperty
    public void PackageName(String packageName) {
        this.packageName = packageName.trim();
    }

    @SimpleFunction(description =  "To determine if an application is already ignoring optimizations.")
    public boolean IsIgnoring() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return powerManager.isIgnoringBatteryOptimizations(packageName);
        }
        else return false;
    }
}