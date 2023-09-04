package com.marchtech.SimplePedometer_v3;

import static android.Manifest.permission.FOREGROUND_SERVICE;

import android.content.Context;
import android.content.Intent;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.annotations.androidmanifest.ServiceElement;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;

@DesignerComponent( version = 3,
                    description = "For testing background service",
                    category = ComponentCategory.EXTENSION,
                    nonVisible = true,
                    iconName = "images/extension.png")
@UsesPermissions(permissionNames = "android.permission.FOREGROUND_SERVICE")
@UsesServices(services = {
    @ServiceElement(
        name = "com.marchtech.SimplePedometer_v3.MarchService",
        exported = "false",
        enabled = "true"
    )
})
@SimpleObject(external = true)
public class SimplePedometer_v3 extends AndroidNonvisibleComponent {
    private final Context context;

    public SimplePedometer_v3(ComponentContainer container) {
        super(container.$form());
        context = container.$context();
    }

    @SimpleEvent(description = "This event is run when a raw step is detected")
    public void SimpleStep(int steps, float distance) {
        EventDispatcher.dispatchEvent(this, "SimpleStep", steps, distance);
    }

    @SimpleEvent(description = "This event is run when a walking step is detected")
    public void WalkStep(int steps, float distance) {
        EventDispatcher.dispatchEvent(this, "WalkStep", steps, distance);
    }

    @SimpleFunction(description = "Start counting steps")
    public void Start() {
        context.startForegroundService(new Intent(this.context, MarchService.class));
    }

    @SimpleFunction(description = "Stop counting steps")
    public void Stop() {
        context.stopService(new Intent(this.context, MarchService.class));
    }
}
