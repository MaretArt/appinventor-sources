package com.marchtech.Task;

import android.app.Activity;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.common.*;

import java.util.Timer;
import java.util.TimerTask;

@DesignerComponent(version = 1,
                   description = "To determine if an application is already ignoring optimizations.",
                   category = ComponentCategory.EXTENSION,
                   nonVisible = true,
                   iconName = "images/extension.png")
@SimpleObject(external = true)
public class Task extends AndroidNonvisibleComponent {
    private Timer timer = new Timer("Main");
    private Activity activity = new Activity();

    public Task(ComponentContainer container) {
        super(container.$form());
    }

    @SimpleEvent(description = "Occurs when the delay and interval are reached.")
    public void Reached() {
        EventDispatcher.dispatchEvent(this, "Reached");
    }

    @SimpleFunction(description = "Create and start schedule.")
    public void CreateSchedule(long delay, long interval) {
        timer.scheduleAtFixedRate(task, delay, interval);
    }

    @SimpleFunction(description = "Stop schedule.")
    public void StopSchedule() {
        timer.cancel();
    }

    protected TimerTask task = new TimerTask() {
        @Override
        public void run() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Reached();
                }
            });
        }
    };
}
