package com.marchtech.SimplePedometer_v2;

import android.content.Context;
import android.content.Intent;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.annotations.androidmanifest.ServiceElement;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;

@DesignerComponent( version = 2,
                    description = "Extension to using pedometer",
                    category = ComponentCategory.EXTENSION,
                    nonVisible = true,
                    iconName = "images/pedometer.png")
@UsesPermissions(permissionNames = "android.permission.FOREGROUND_SERVICE")
@UsesServices(services = {
    @ServiceElement(
        name = "com.marchtech.SimplePedometer_v2.BackgroundService",
        exported = "false",
        enabled = "true"
    )
})
@SimpleObject(external = true)
public class SimplePedometer_v2 extends AndroidNonvisibleComponent implements StepListener {
    private final Context context;

    public SimplePedometer_v2(ComponentContainer container) {
        super(container.$form());
        context = container.$context();

        BackgroundService serv = new BackgroundService();
        serv.registerListener(this);
    }

    @SimpleEvent(description = "This event is run when a raw step is detected")
    public void SimpleStep(int steps, float distance) {
        EventDispatcher.dispatchEvent(this, "SimpleStep", steps, distance);
    }

    @SimpleEvent(description = "This event is run when a walking step is detected")
    public void WalkStep(int steps, float distance) {
        EventDispatcher.dispatchEvent(this, "WalkStep", steps, distance);
    }

    @SimpleFunction(description = "To start testing")
    public void Start() {
        Intent intent = new Intent(this.context, BackgroundService.class);
        context.startForegroundService(intent);
    }

    @Override
    public void onSimpleStep(int steps, float distances) {
        SimpleStep(steps, distances);
    }

    @Override
    public void onWalkStep(int steps, float distances) {
        WalkStep(steps, distances);
    }
}
