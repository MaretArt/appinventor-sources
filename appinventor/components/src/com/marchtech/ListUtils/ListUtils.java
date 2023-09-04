package com.marchtech.ListUtils;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.YailList;
import com.marchtech.Icon;

@DesignerComponent( version = 1,
                    description = "Extension to help you using list.",
                    category = ComponentCategory.EXTENSION,
                    nonVisible = true,
                    iconName = Icon.ICON)
@SimpleObject(external = true)
public class ListUtils extends AndroidNonvisibleComponent {
    public ListUtils(ComponentContainer container) {
        super(container.$form());
    }

    @SimpleFunction(description = "To using replace all text function on all item in list.")
    public YailList ReplaceAllItems(YailList list, String segment, String replacement) {
        String[] text = list.toStringArray();
        for (int i = 0; i < text.length; i++) {
            text[i] = text[i].replaceAll(segment, replacement);
        }

        return YailList.makeList(text);
    }
}
