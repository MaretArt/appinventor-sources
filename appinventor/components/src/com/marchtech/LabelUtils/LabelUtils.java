package com.marchtech.LabelUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.YailDictionary;
import com.marchtech.Icon;

@DesignerComponent( version = 1,
                    description = "Extension to help you work with multi language label",
                    category = ComponentCategory.EXTENSION,
                    nonVisible = true,
                    iconName = Icon.ICON)
@SimpleObject(external = true)
public class LabelUtils extends AndroidNonvisibleComponent {
    private final HashMap<String, Label> LABELS = new HashMap<String, Label>();
    private final HashMap<String, Map<String, String>> TEXTS = new HashMap<String, Map<String, String>>();

    private List<String> allId = new ArrayList<String>();

    private String def = "en";

    public LabelUtils(ComponentContainer container) {
        super(container.$form());
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "en")
    @SimpleProperty(description = "Set default language.", category = PropertyCategory.APPEARANCE)
    public void Language(String lang) {
        def = lang;
    }

    @SimpleProperty
    public String Language() {
        return def;
    }

    @SimpleFunction(description = "To set components.")
    public void SetComponent(Label label, YailDictionary multiLanguage) {
        String id = getId(label);
        LABELS.put(id, label);
        TEXTS.put(id, toMap(multiLanguage));
        allId.add(id);

        ChangeLanguage(def);
    }

    @SimpleFunction(description = "To delete component.")
    public void DeleteComponent(Label label) {
        String id = getId(label);
        LABELS.remove(id);
        TEXTS.remove(id);
        allId.remove(id);
    }

    @SimpleFunction(description = "To change text with specific language and id.")
    public void Text(Label label, String text, String language) {
        String id = getId(label);
        Map<String, String> langMap = TEXTS.get(id);
        langMap.replace(language, text);
        TEXTS.replace(id, langMap);
        
        ChangeLanguage(def);
    }

    @SimpleFunction(description = "To change all of texts and languages with specific id.")
    public void ReplaceAll(Label label, YailDictionary multiLanguage) {
        TEXTS.replace(getId(label), toMap(multiLanguage));

        ChangeLanguage(def);
    }

    @SimpleFunction(description = "To change language of all labels.")
    public void ChangeLanguage(String language) {
        for (String id : allId) {
            Label label = LABELS.get(id);
            Map<String, String> texts = TEXTS.get(id);
            String text = texts.get(language);
            label.Text(text);
        }

        def = language;
    }

    private String getId(Label label) {
        String id = label.toString();
        return id.split("@")[1];
    }

    private Map<String, String> toMap(YailDictionary dictionary) {
        Map<String, String> dictMap = new HashMap<String, String>();
        String data = dictionary.toString();
        data = data.substring(1, data.length() - 2);
        data = data.replaceAll("\"", "");
        String[] datas = data.split(",");
        for (String text : datas) {
            String[] splited = text.split(":");
            String key = splited[0];
            String value = splited[1];
            dictMap.put(key, value);
        }

        return dictMap;
    }
}
