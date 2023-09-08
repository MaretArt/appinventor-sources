package com.marchtech.SimpleMenu;

import android.content.Context;
import android.widget.RelativeLayout;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;

import com.marchtech.Icon;

@DesignerComponent(version = 1, description = "Extension to help you create menu.", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = Icon.ICON)
@SimpleObject(external = true)
public class SimpleMenu extends AndroidNonvisibleComponent {
    private final Context context;

    private RelativeLayout rLayout;

    public SimpleMenu(ComponentContainer container) {
        super(container.$form());
        context = container.$context();

        rLayout = new RelativeLayout(context);
    }

    @SimpleFunction(description = "To initialize.")
    public void Initialize(Component layout) {
        if (layout instanceof HorizontalArrangement) {
            HorizontalArrangement lyt = (HorizontalArrangement) layout;
            rLayout.addView(lyt.getView());
        } else if (layout instanceof VerticalArrangement) {
            VerticalArrangement lyt = (VerticalArrangement) layout;
            rLayout.addView(lyt.getView());
        }
    }

    @SimpleFunction(description = "To add view.")
    public void Add(Component component) {
        if (component instanceof HorizontalArrangement) {
            HorizontalArrangement comp = (HorizontalArrangement) component;
            rLayout.addView(comp.getView(), RelativeLayout.CENTER_IN_PARENT);
        }
    }
}
