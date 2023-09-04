package com.marchtech.SimplePedometer_v1;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;

@DesignerComponent( version = 1,
                    description = "Extension to using pedometer",
                    category = ComponentCategory.EXTENSION,
                    nonVisible = true,
                    iconName = "images/extension.png")
@SimpleObject(external = true)
public class SimplePedometer_v1 extends AndroidNonvisibleComponent implements SensorEventListener, StepListener, OnResumeListener {
    private StepDetector stepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private final ComponentContainer container;
    private static final String TEXT_NUM_STEPS = "Steps: ";
    private int numSteps;

    public SimplePedometer_v1(ComponentContainer container) {
        super(container.$form());
        this.container = container;

        form.registerForOnResume(this);

        sensorManager = (SensorManager) container.$context().getSystemService(Context.SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        stepDetector = new StepDetector();
        stepDetector.registerListener(this);
    }

    @SimpleEvent(description = "An event that occurs when steps changed")
    public void walkSteps(int steps) {
        EventDispatcher.dispatchEvent(this, "walkSteps", steps);
    }

    @Override
    public void onResume() {
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            stepDetector.updateAccel(event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        walkSteps(numSteps);
    }
}
