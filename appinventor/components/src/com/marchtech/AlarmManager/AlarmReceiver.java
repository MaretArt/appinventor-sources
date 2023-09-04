package com.marchtech.AlarmManager;

import android.content.Context;
import android.content.Intent;

public interface AlarmReceiver {
    public void onReceive(Context context, Intent intent);
}