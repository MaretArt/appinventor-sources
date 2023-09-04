package com.marchtech.DynamicComponents;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;

import com.marchtech.Icon;
import com.marchtech.DynamicComponents.helpers.CheckBoxProperty;
import com.marchtech.DynamicComponents.helpers.Components;
import com.marchtech.DynamicComponents.helpers.HorizontalArrangementProperty;
import com.marchtech.DynamicComponents.helpers.LabelProperty;
import com.marchtech.DynamicComponents.helpers.SpaceProperty;
import com.marchtech.DynamicComponents.helpers.VerticalArrangementProperty;

@DesignerComponent( version = 1,
                    description = "Utility of DynamicComponents Extension.",
                    category = ComponentCategory.EXTENSION,
                    nonVisible = true,
                    iconName = Icon.ICON)
@SimpleObject(external = true)
public class Utils extends AndroidNonvisibleComponent {
    public Utils(ComponentContainer container) {
        super(container.$form());
    }

    @SimpleFunction(description = "Return a component.")
    public @Options(Components.class) String Component(@Options(Components.class) String select) {
        return select;
    }

    @SimpleFunction(description = "Return a property name.")
    public @Options(LabelProperty.class) String LabelProperty(@Options(LabelProperty.class) String select) {
        return select;
    }

    @SimpleFunction(description = "Return a property name.")
    public @Options(CheckBoxProperty.class) String CheckboxProperty(@Options(CheckBoxProperty.class) String select) {
        return select;
    }

    @SimpleFunction(description = "Return a property name.")
    public @Options(SpaceProperty.class) String SpaceProperty(@Options(SpaceProperty.class) String select) {
        return select;
    }

    @SimpleFunction(description = "Return a property name.")
    public @Options(VerticalArrangementProperty.class) String VerticalArrangementProperty(@Options(VerticalArrangementProperty.class) String select) {
        return select;
    }

    @SimpleFunction(description = "Return a property name.")
    public @Options(HorizontalArrangementProperty.class) String HorizontalArrangementProperty(@Options(HorizontalArrangementProperty.class) String select) {
        return select;
    }
}
