package com.marchtech.SimpleMenu;

import android.widget.RelativeLayout;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;

import com.marchtech.Icon;

@DesignerComponent( version = 1,
                    description = "Extension to help you create menu.",
                    category = ComponentCategory.EXTENSION,
                    nonVisible = true,
                    iconName = Icon.ICON)
@SimpleObject(external = true)
public class SimpleMenu extends AndroidNonvisibleComponent {
    public SimpleMenu(ComponentContainer container) {
        super(container.$form());
    }

    private void initialize() {

    }
}
