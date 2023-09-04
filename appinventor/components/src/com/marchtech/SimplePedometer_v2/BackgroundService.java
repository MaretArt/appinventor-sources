package com.marchtech.SimplePedometer_v2;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.Deleteable;

public class BackgroundService extends Service implements SensorEventListener {
    private static final String TAG = "SimplePedometer";
    private static final String PREFS_NAME = "SimplePedometerPrefs";

    private static final int INTERVAL_VARIATION = 250;
    private static final int NUM_INTERVALS = 2;
    private static final int WIN_SIZE = 100;
    private static final float STRIDE_LENGTH = (float) 0.73;
    private static final float PEAK_VALLEY_RANGE = (float) 40.0;

    private Context context;
    private SensorManager sensorManager;
    private StepListener listener;

    private int stopDetectionTimeout = 2000;
    private int winPos = 0;
    private int intervalPos = 0;
    private int numStepsWithFilter = 0;
    private int numStepsRaw = 0;
    private int avgPos = 0;

    private float lastValley = 0;
    private float strideLength = STRIDE_LENGTH;
    private float totalDistance = 0;
    private float[] lastValues = new float[WIN_SIZE];
    private float[] avgWindow = new float[10];

    private long stepTimestamp = 0;
    private long startTime = 0;
    private long prevStopClockTime = 0;
    private long[] stepInterval = new long[NUM_INTERVALS];

    private boolean foundValley = false;
    private boolean startPeaking = false;
    private boolean foundNonStep = true;
    private boolean pedometerPaused = true;

    private String measure = "km";

    public void registerListener(StepListener listener) {
        this.listener = listener;
    }

    private void Save() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat("SimplePedometer.strideLength", strideLength);
        editor.putFloat("SimplePedometer.distance", totalDistance);
        editor.putInt("SimplePedometer.prevStepCount", numStepsRaw);
        if (pedometerPaused) {
            editor.putLong("SimplePedometer.clockTime", prevStopClockTime);
        } else {
            editor.putLong("SimplePedometer.clockTime", prevStopClockTime + (System.currentTimeMillis() - startTime));
        }

        editor.putLong("SimplePedometer.closeTime", System.currentTimeMillis());
        editor.commit();
        Log.d(TAG, "SimplePedometer state saved");
    }

    private boolean areStepsEquallySpaced() {
        float avg = 0;
        int num = 0;
        for (long interval : stepInterval) {
            if (interval > 0) {
                num++;
                avg += interval;
            }
        }

        avg = avg / num;
        for (long interval : stepInterval) {
            if (Math.abs(interval - avg) > INTERVAL_VARIATION) return false;
        }

        return true;
    }

    private boolean isPeak() {
        int mid = (winPos + WIN_SIZE / 2) % WIN_SIZE;
        for (int i = 0; i < WIN_SIZE; i++) {
            if (i != mid && lastValues[i] > lastValues[mid]) return false;
        }

        return true;
    }

    private boolean isValley() {
        int mid = (winPos + WIN_SIZE / 2) % WIN_SIZE;
        for (int i = 0; i < WIN_SIZE; i++) {
            if (i != mid && lastValues[i] < lastValues[mid]) return false;
        }

        return true;
    }

    @Override
    public void onCreate() {
        winPos = 0;
        startPeaking = false;
        numStepsWithFilter = 0;
        numStepsRaw = 0;

        foundValley = false;
        lastValley = 0;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        strideLength = settings.getFloat("SimplePedometer.strideLength", STRIDE_LENGTH);
        totalDistance = settings.getFloat("SimplePedometer.distance", 0);
        numStepsRaw = settings.getInt("SimplePedometer.prevStepCount", 0);
        prevStopClockTime = settings.getLong("SimplePedometer.clocktime", 0);
        numStepsWithFilter = numStepsRaw;
        startTime = System.currentTimeMillis();
        Log.d(TAG, "SimplePedometer Created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        pedometerPaused = false;
        sensorManager.registerListener(this, sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0), SensorManager.SENSOR_DELAY_FASTEST);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        strideLength = settings.getFloat("SimplePedometer.strideLength", STRIDE_LENGTH);
        totalDistance = settings.getFloat("SimplePedometer.distance", 0);
        numStepsRaw = settings.getInt("SimplePedometer.prevStepCount", 0);
        prevStopClockTime = settings.getLong("SimplePedometer.clocktime", 0);
        numStepsWithFilter = numStepsRaw;
        startTime = System.currentTimeMillis();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "Accelerometer accuracy changed");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;

        float[] values = event.values;
        float magnitude = 0;
        for (float v : values) magnitude += v * v;

        int mid = (winPos + WIN_SIZE / 2) % WIN_SIZE;

        if (startPeaking && isPeak()) {
            if (foundValley && lastValues[mid] - lastValley > PEAK_VALLEY_RANGE) {
                long timestamp = System.currentTimeMillis();
                stepInterval[intervalPos] = timestamp - stepTimestamp;
                intervalPos = (intervalPos + 1) % NUM_INTERVALS;
                stepTimestamp = timestamp;
                if (areStepsEquallySpaced()) {
                    if (foundNonStep) {
                        numStepsWithFilter += NUM_INTERVALS;
                        totalDistance += strideLength * NUM_INTERVALS;
                        foundNonStep = false;
                    }

                    numStepsWithFilter++;
                    listener.onWalkStep(numStepsWithFilter, totalDistance);
                    totalDistance += strideLength;
                } else foundNonStep = true;

                numStepsRaw++;
                listener.onSimpleStep(numStepsRaw, totalDistance);
                foundValley = false;
            }
        }

        if (startPeaking && isValley()) {
            foundValley = true;
            lastValley = lastValues[mid];
        }

        avgWindow[avgPos] = magnitude;
        avgPos = (avgPos + 1) % avgWindow.length;
        lastValues[winPos] = 0;
        for (float m : avgWindow) lastValues[winPos] += m;

        lastValues[winPos] /= avgWindow.length;
        if (startPeaking || winPos > 1) {
            int i = winPos;
            if (--i < 0) i += WIN_SIZE;

            lastValues[winPos] += 2 * lastValues[i];
            if (--i < 0) i += WIN_SIZE;
            lastValues[winPos] += lastValues[i];
            lastValues[winPos] /= 4;
        } else if (!startPeaking && winPos == 1) lastValues[1] = (lastValues[1] + lastValues[0]) / 2f;

        long elapsedTimestamp = System.currentTimeMillis();
        if (elapsedTimestamp - stepTimestamp > stopDetectionTimeout) stepTimestamp = elapsedTimestamp;

        if (winPos == WIN_SIZE - 1 && !startPeaking) startPeaking = true;

        winPos = (winPos + 1) % WIN_SIZE;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
