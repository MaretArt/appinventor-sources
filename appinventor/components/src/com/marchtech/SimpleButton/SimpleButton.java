package com.marchtech.SimpleButton;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;

import com.marchtech.Icon;

@DesignerComponent( version = 1,
                    description = "Extension to help you work with strings..",
                    category = ComponentCategory.EXTENSION,
                    nonVisible = false,
                    iconName = Icon.ICON)
@SimpleObject(external = true)
public class SimpleButton extends ButtonBase {
    public SimpleButton(ComponentContainer container) {
        super(container);
    }

    @Override
    public void click() {
        Click();
    }

    @SimpleEvent(description = "User tapped and released the button.")
    public void Click() {
        EventDispatcher.dispatchEvent(this, "Click");
    }

    @Override
    public boolean longClick() {
        return LongClick();
    }

    @SimpleEvent(description = "User held the button down.")
    public boolean LongClick() {
        return EventDispatcher.dispatchEvent(this, "LongClick");
    }
}
