package com.marchtech.SimpleBluetooth;

import static android.Manifest.permission.BLUETOOTH;
import static android.Manifest.permission.BLUETOOTH_ADMIN;
import static android.Manifest.permission.BLUETOOTH_ADVERTISE;
import static android.Manifest.permission.BLUETOOTH_CONNECT;
import static android.Manifest.permission.BLUETOOTH_SCAN;

import android.os.Build;
import com.google.appinventor.components.runtime.BluetoothServer;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.PermissionResultHandler;
import com.google.appinventor.components.runtime.util.BulkPermissionRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Util {
    @SuppressWarnings("unused")
    public static boolean requestPermissionsForScanning(Form form, SimpleBluetooth client, String caller, PermissionResultHandler continuation) {
        List<String> permsNeeded = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permsNeeded.add(BLUETOOTH_SCAN);
        } else {
            permsNeeded.add(BLUETOOTH);
            permsNeeded.add(BLUETOOTH_ADMIN);
        }

        return performRequest(form, client, caller, permsNeeded, continuation);
    }

    public static boolean requestPermissionsForConnecting(Form form, SimpleBluetooth client, String caller, PermissionResultHandler continuation) {
        return requestPermissionsForS(BLUETOOTH_CONNECT, form, client, caller, continuation);
    }

    public static boolean requestPermissionsForAdvertising(Form form, BluetoothServer server, String caller, PermissionResultHandler continuation) {
        return requestPermissionsForS(BLUETOOTH_ADVERTISE, form, server, caller, continuation);
    }

    public static boolean requestPermissionsForS(String sdk31Permission, Form form, Component source, String caller, PermissionResultHandler continuation) {
        return requestPermissionsForS(new String[] { sdk31Permission }, form, source, caller, continuation);
    }

    public static boolean requestPermissionsForS(String[] sdk31Permissions, Form form, Component source, String caller, PermissionResultHandler continuation) {
        List<String> permsNeeded = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Collections.addAll(permsNeeded, sdk31Permissions);
        } else {
            permsNeeded.add(BLUETOOTH);
            permsNeeded.add(BLUETOOTH_ADMIN);
        }
        return performRequest(form, source, caller, permsNeeded, continuation);
    }

    private static boolean performRequest(Form form, Component source, String caller, final List<String> permsNeeded, final PermissionResultHandler continuation) {
        boolean ready = true;
        for (String permission : permsNeeded) {
            if (form.isDeniedPermission(permission)) {
                ready = false;
                break;
            }
        }
        if (!ready) {
            final String[] permissions = permsNeeded.toArray(new String[0]);
            form.askPermission(new BulkPermissionRequest(source, caller, permissions) {
                @Override
                public void onGranted() {
                    continuation.HandlePermissionResponse(permsNeeded.get(0), true);
                }
            });
        }
        return !ready;
    }
}
