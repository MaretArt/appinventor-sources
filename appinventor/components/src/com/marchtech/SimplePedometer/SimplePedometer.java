package com.marchtech.SimplePedometer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.YailList;
import com.marchtech.Icon;
import com.marchtech.SimplePedometer.helpers.Measure;
import com.marchtech.SimplePedometer.helpers.Mode;

@DesignerComponent( version = 1,
                    description = "Extension to using pedometer",
                    category = ComponentCategory.EXTENSION,
                    nonVisible = true,
                    iconName = Icon.ICON)
@SimpleObject(external = true)
public class SimplePedometer extends AndroidNonvisibleComponent implements Component, SensorEventListener {
    private static final String TAG = "SimplePedometer";
    public static final String PREFS_NAME = "SimplePedometerPrefs";

    public static final int INTERVAL_VARIATION = 250;
    private static final int NUM_INTERVALS = 2;
    public static final int WIN_SIZE = 100;
    private static final float STRIDE_LENGTH = (float) 0.73;
    private static final float PEAK_VALLEY_RANGE = (float) 40.0;
    private static final float DIVIDER_KM = (float) 1000.0;
    private static final float DIVIDER_MILIES = (float) 1609.34;

    private final Context context;
    private final SensorManager sensorManager;

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
    private boolean resetDaily = false;

    private String mode = "Walk";

    private Activity activity = new Activity();
    private Timer schedule;
    
    private Functions march = new Functions();

    public SimplePedometer(ComponentContainer container) {
        super(container.$form());
        context = container.$context();

        winPos = 0;
        startPeaking = false;
        numStepsWithFilter = 0;
        numStepsRaw = 0;

        foundValley = false;
        lastValley = 0;

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        strideLength = settings.getFloat("SimplePedometer.strideLength", STRIDE_LENGTH);
        totalDistance = settings.getFloat("SimplePedometer.distance", 0);
        numStepsRaw = settings.getInt("SimplePedometer.prevStepCount", 0);
        prevStopClockTime = settings.getLong("SimplePedometer.clocktime", 0);
        numStepsWithFilter = numStepsRaw;
        startTime = System.currentTimeMillis();
        Log.d(TAG, "SimplePedometer Created");
    }

    @SimpleEvent(description = "This event is run when a raw step is detected")
    public void StepsDetected() {
        EventDispatcher.dispatchEvent(this, "StepsDetected");
    }

    @SimpleFunction(description = "To initialize counting steps.")
    public void Initialize(YailList data) {
        String[] newData = data.toStringArray();
        strideLength = Float.parseFloat(newData[0]);
        totalDistance = Float.parseFloat(newData[1]);
        numStepsRaw = Integer.parseInt(newData[2]);
        prevStopClockTime = Long.parseLong(newData[3]);
        numStepsWithFilter = numStepsRaw;
        startTime = System.currentTimeMillis();

        Log.d(TAG, "SimplePedometer Initialized");
    }

    @SimpleFunction(description = "To get saved data.")
    public YailList GetRawData() {
        List<String> data = new ArrayList<String>();
        data.add(String.valueOf(strideLength));
        data.add(String.valueOf(totalDistance));
        data.add(String.valueOf(numStepsRaw));
        if (pedometerPaused) data.add(String.valueOf(prevStopClockTime));
        else data.add(String.valueOf(prevStopClockTime + (System.currentTimeMillis() - startTime)));

        YailList newData = fromList(data);
        return newData;
    }

    @SimpleFunction(description = "Start counting steps")
    public void Start() {
        if (pedometerPaused) {
            pedometerPaused = false;
            sensorManager.registerListener(this, sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0), SensorManager.SENSOR_DELAY_FASTEST);
            startTime = System.currentTimeMillis();

            StepsDetected();

            if (resetDaily) {
                schedule = new Timer("schedule");
                TimerTask resetValue = new TimerTask() {
                    @Override
                    public void run() {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LocalDate localDate = LocalDate.now();
                                boolean newDate = localDate.isBefore(LocalDate.now());
                                
                                if (newDate) {
                                    Reset();
                                    StepsDetected();
                                }
                            }
                        });
                    }
                };

                schedule.scheduleAtFixedRate(resetValue, 10, 1000);
            }
        }
    }

    @SimpleFunction(description = "Stop counting steps")
    public void Stop() {
        if (!pedometerPaused) {
            pedometerPaused = true;
            sensorManager.unregisterListener(this);
            Log.d(TAG, "Unregistered listener on pause");
            prevStopClockTime += (System.currentTimeMillis() - startTime);

            if (resetDaily) {
                try {
                    schedule.cancel();
                    schedule.purge();
                } catch (NullPointerException e) {
                    return;
                }
            }
        }
    }

    @SimpleFunction(description = "Resets the step counter, distance measure and time running")
    public void Reset() {
        numStepsWithFilter = 0;
        numStepsRaw = 0;
        totalDistance = 0;
        prevStopClockTime = 0;
        startTime = System.currentTimeMillis();

        StepsDetected();
    }

    @SimpleFunction(description = "Saves the pedometer state to the phone")
    public void Save() {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
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

    @SimpleFunction(description = "The approximate distance traveled.")
    public @Options(Measure.class) float GetDistances(@Options(Measure.class) String measure) {
        if (measure == "km") return totalDistance / DIVIDER_KM;
        else if (measure == "m") return totalDistance;
        else return totalDistance / DIVIDER_MILIES;
    }

    @SimpleFunction(description = "The number of simple steps taken since the SimplePedometer has started.")
    public int GetSteps() {
        if (mode == "Walk") return numStepsWithFilter;
        else return numStepsRaw;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_FLOAT, defaultValue = "73")
    @SimpleProperty(description = "Set the average stride length in centimeters", category = PropertyCategory.BEHAVIOR)
    public void StrideLength(float length) {
        strideLength = length / 100;
    }

    @SimpleProperty
    public float StrideLength() {
        return strideLength * 100;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER, defaultValue = "2000")
    @SimpleProperty(description = "The duration in milliseconds of idleness (no steps detected) after which to go into a \"stopped\" state", category = PropertyCategory.BEHAVIOR)
    public void StopDetectionTimeout(int timeout) {
        stopDetectionTimeout = timeout;
    }

    @SimpleProperty
    public int StopDetectionTimeout() {
        return stopDetectionTimeout;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_CHOICES, editorArgs = {"Simple", "Walk"}, defaultValue = "Walk")
    @SimpleProperty(description = "The mode of step detector.", category = PropertyCategory.BEHAVIOR)
    public void StepMode(@Options(Mode.class) String mode) {
        this.mode = mode;
    }

    @SimpleProperty
    public String StepMode() {
        return mode;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "false")
    @SimpleProperty(description = "To reset daily of detected steps", category = PropertyCategory.BEHAVIOR)
    public void ResetDaily(boolean reset) {
        resetDaily = reset;
    }

    @SimpleProperty
    public boolean ResetDaily() {
        return resetDaily;
    }

    @SimpleProperty(description = "Time elapsed in milliseconds since the SimplePedometer was started", category = PropertyCategory.BEHAVIOR)
    public long ElapsedTime() {
        if (pedometerPaused) return prevStopClockTime;
        else return prevStopClockTime + (System.currentTimeMillis() - startTime);
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

        if (startPeaking && march.isPeak(winPos, lastValues)) {
            if (foundValley && lastValues[mid] - lastValley > PEAK_VALLEY_RANGE) {
                long timestamp = System.currentTimeMillis();
                stepInterval[intervalPos] = timestamp - stepTimestamp;
                intervalPos = (intervalPos + 1) % NUM_INTERVALS;
                stepTimestamp = timestamp;
                if (march.areStepsEquallySpaced(stepInterval)) {
                    if (foundNonStep) {
                        numStepsWithFilter += NUM_INTERVALS;
                        totalDistance += strideLength * NUM_INTERVALS;
                        foundNonStep = false;
                    }

                    numStepsWithFilter++;
                    if (mode == "Walk") StepsDetected();
                    totalDistance += strideLength;
                } else foundNonStep = true;

                numStepsRaw++;
                if (mode == "Simple") StepsDetected();
                foundValley = false;
            }
        }

        if (startPeaking && march.isValley(winPos, lastValues)) {
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

    private static YailList fromList(List item) {
        YailList items = new YailList();
        items = YailList.makeList(item);

        return items;
    }
}
