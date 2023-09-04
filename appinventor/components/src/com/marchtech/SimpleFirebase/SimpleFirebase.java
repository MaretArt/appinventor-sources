package com.marchtech.SimpleFirebase;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;

import com.marchtech.Icon;

@DesignerComponent( version = 1,
                    description = "Extension to help you with firebase.",
                    category = ComponentCategory.EXTENSION,
                    nonVisible = true,
                    iconName = Icon.ICON)
@SimpleObject(external = true)
public class SimpleFirebase extends AndroidNonvisibleComponent {
    private static final String LOG_TAG = "SimpleFirebase";

    private String firebaseUrl = null;
    private String defaultUrl = null;
    private String firebaseToken;
    public SimpleFirebase(ComponentContainer container) {
        super(container.$form());
        
    }
}
