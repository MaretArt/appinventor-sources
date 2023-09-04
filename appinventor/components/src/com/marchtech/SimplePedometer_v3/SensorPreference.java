package com.marchtech.SimplePedometer_v3;

import android.content.Context;
import android.preference.PreferenceManager;

public class SensorPreference {
    public static final String IS_RUNNING = "isServiceRunning";

    public static boolean isServiceRunning(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(IS_RUNNING, false);
    }

    public static void setServiceRunning(Context context, boolean defaultData) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putBoolean(IS_RUNNING, defaultData)
            .apply();
    }
}
