package com.marchtech.TestBackground;

import static android.Manifest.permission.FOREGROUND_SERVICE;

import android.content.Context;
import android.content.Intent;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.annotations.androidmanifest.ServiceElement;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;

@DesignerComponent( version = 1,
                    description = "For testing background service",
                    category = ComponentCategory.EXTENSION,
                    nonVisible = true,
                    iconName = "images/extension.png")
@UsesPermissions(permissionNames = "android.permission.FOREGROUND_SERVICE")
@UsesServices(services = {
    @ServiceElement(
        name = "com.marchtech.TestBackground.BackgroundService",
        exported = "false",
        enabled = "true"
    )
})
@SimpleObject(external = true)
public class TestBackground extends AndroidNonvisibleComponent {
    private final Context context;

    public TestBackground(ComponentContainer container) {
        super(container.$form());
        context = container.$context();
    }
/*
    @SimpleEvent(description = "This event is run when data has changed")
    public void OnChanged(long value) {
        EventDispatcher.dispatchEvent(this, "OnChanged", value);
    }
*/
    @SimpleFunction(description = "To start testing")
    public void Start() {
        Intent intent = new Intent(this.context, BackgroundService.class);
        context.startForegroundService(intent);
        
    }
/*
    @SimpleFunction(description = "To stop testing")
    public void Stop() {
        context.stopService(new Intent(context, BackgroundService.class));
    }
*/
}
