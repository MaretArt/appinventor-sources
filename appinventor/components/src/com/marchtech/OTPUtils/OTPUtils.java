package com.marchtech.OTPUtils;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.TimerInternal;
import com.marchtech.Icon;

@DesignerComponent( version = 1,
                    description = "Extension to help you work with otp.",
                    category = ComponentCategory.EXTENSION,
                    nonVisible = true,
                    iconName = Icon.ICON)
@SimpleObject(external = true)
public class OTPUtils extends AndroidNonvisibleComponent implements AlarmHandler, OnDestroyListener, Deleteable {
    private int counter = 1;
    private int sec = 30;
    
    private TimerInternal timer;

    private Label lbl;
    private HorizontalArrangement lytSec;
    private VerticalArrangement lytResend;

    public OTPUtils(ComponentContainer container) {
        super(container.$form());
        timer = new TimerInternal(this, false, 1000);

        form.registerForOnDestroy(this);
    }

    public OTPUtils() {
        super(null);
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER, defaultValue = "30")
    @SimpleProperty(description = "seconds.")
    public void Second(int sec) {
        this.sec = sec;
    }

    @SimpleProperty
    public int Second() {
        return sec;
    }

    @SimpleFunction(description = "To initialize components. note: layout = Horizontal Arrangement, layout2 = Vertical Arrangement.")
    public void Initialize(Label label, HorizontalArrangement layout1, VerticalArrangement layout2) {
        lbl = label;
        lytSec = layout1;
        lytResend = layout2;

        lbl.Text(String.valueOf(sec));
    }

    @SimpleFunction(description = "To start utils.")
    public void Start() {
        timer.Enabled(true);
    }

    @SimpleFunction(description = "To re-count second.")
    public void ReCount() {
        counter++;
        sec = counter * 30;
        lbl.Text(String.valueOf(sec));
        lytSec.Visible(true);
        lytResend.Visible(false);
        timer.Enabled(true);
    }

    @Override
    public void alarm() {
        if (sec > 0) {
            sec -= 1;
            lbl.Text(String.valueOf(sec));
        } else {
            timer.Enabled(false);
            lytSec.Visible(false);
            lytResend.Visible(true);
        }
    }

    @Override
    public void onDestroy() {
        timer.Enabled(false);
    }

    @Override
    public void onDelete() {
        timer.Enabled(false);
    }
}
