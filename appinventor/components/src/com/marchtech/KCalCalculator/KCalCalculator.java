package com.marchtech.KCalCalculator;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.ElementsUtil;
import com.google.appinventor.components.runtime.util.YailList;

import com.marchtech.Icon;

@DesignerComponent( version = 1,
                    description = "Extension to calculate KCal.",
                    category = ComponentCategory.EXTENSION,
                    nonVisible = true,
                    iconName = Icon.ICON)
@SimpleObject(external = true)
public class KCalCalculator extends AndroidNonvisibleComponent {
    private float height;
    private float weight;
    private float multiplier = 0.1f;
    private float dailyActivity = 0.1f;
    private float correction = 0.05f;
    //private float heightCorrection = 1.850f;

    private String[] dietClass = new String[]{"Class I", "Class II", "Class III", "Class IV", "Class V", "Class VI", "Class VII", "Class VIII"};
    private String activity;
    private String gender;

    private int age;
    private int activityIndex;
    private int genderIndex;
    private int times = 25;

    //private boolean bbi = false;

    private Object[] activityObjects = new Object[]{"Easy (Leisurely Strolling, Reading, Driving, etc.)", "Medium (Brisk, Sweep, Bicycle, etc.)", "Heavy (Aerobics, Climbing, Jogging, etc)"};
    private Object[] genderObjects = new Object[]{"Female", "Male"};

    private YailList listActivity;
    private YailList listGender;
    
    public KCalCalculator(ComponentContainer container) {
        super(container.$form());

        listActivity = fromObjects(activityObjects);
        listGender = fromObjects(genderObjects);
        ActivityIndex(1);
        GenderIndex(1);
    }

    @SimpleFunction(description = "Get BBI (Berat Badan Ideal) value.")
    public float GetBBI() {
        return (height - 100) - ((height - 100) * multiplier);
    }

    @SimpleFunction(description = "Get KKB (Kebutuhan Kalori Basal) value.")
    public float GetKKB() {
        return (GetBBI() * times);
    }

    @SimpleFunction(description = "Get KCal")
    public double GetKCal() {
        return (GetKKB() + (GetKKB() * dailyActivity) - (GetKKB() * correction));

        /*
        if (bbi) {
            return (GetKKB() + (GetKKB() * dailyActivity) - (GetKKB() * correction));
        } else {
            return multiplier + (dailyActivity * weight) + (heightCorrection * height) - (correction * age);
        }
        */
    }

    @SimpleFunction
    public float GetMultiplier() {
        return multiplier;
    }

    @SimpleFunction
    public float GetTimes() {
        return times;
    }

    @SimpleFunction
    public float GetActivity() {
        return dailyActivity;
    }

    @SimpleFunction
    public float GetCorrection() {
        return correction;
    }

    @SimpleFunction(description = "Get class of diet.")
    public String GetDietClass(float kcal) {
        if (kcal < 1300) return dietClass[0];
        else if (kcal >= 1300 && kcal < 1500) return dietClass[1];
        else if (kcal >= 1500 && kcal < 1700) return dietClass[2];
        else if (kcal >= 1700 && kcal < 1900) return dietClass[3];
        else if (kcal >= 1900 && kcal < 2100) return dietClass[4];
        else if (kcal >= 2100 && kcal < 2300) return dietClass[5];
        else if (kcal >= 2300 && kcal < 2500) return dietClass[6];
        else return dietClass[7];
    }

    @SimpleFunction
    public int GetDietClassIndex(float kcal) {
        if (kcal < 1300) return 1;
        else if (kcal >= 1300 && kcal < 1500) return 2;
        else if (kcal >= 1500 && kcal < 1700) return 3;
        else if (kcal >= 1700 && kcal < 1900) return 4;
        else if (kcal >= 1900 && kcal < 2100) return 5;
        else if (kcal >= 2100 && kcal < 2300) return 6;
        else if (kcal >= 2300 && kcal < 2500) return 7;
        else return 8;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_FLOAT, defaultValue = "165")
    @SimpleProperty(description = "Set the height in centimeters.", category = PropertyCategory.BEHAVIOR)
    public void Height(float height) {
        this.height = height;
    }

    @SimpleProperty
    public float Height() {
        return height;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_FLOAT, defaultValue = "50")
    @SimpleProperty(description = "Set the weight in kilograms.", category = PropertyCategory.BEHAVIOR)
    public void Weight(float weight) {
        this.weight = weight;
    }

    @SimpleProperty
    public float Weight() {
        return weight;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_CHOICES, editorArgs = {"Easy (Leisurely Strolling, Reading, Driving, etc.)", "Medium (Brisk, Sweep, Bicycle, etc.)", "Heavy (Aerobics, Climbing, Jogging, etc)"}, defaultValue = "Easy (Leisurely Strolling, Reading, Driving, etc.)")
    @SimpleProperty(description = "Set the activity level.", category = PropertyCategory.BEHAVIOR)
    public void Activity(String value) {
        activity = value;
        activityIndex = ElementsUtil.setSelectedIndexFromValue(value, listActivity);

        if (activityIndex == 1) dailyActivity = 0.1f;
        else if (activityIndex == 2) dailyActivity = 0.2f;
        else dailyActivity = 0.4f;

        /*
        if (bbi) {
            if (activityIndex == 1) dailyActivity = 0.1f;
            else if (activityIndex == 2) dailyActivity = 0.2f;
            else dailyActivity = 0.4f;
        }
        */
    }

    @SimpleProperty
    public String Activity() {
        return activity;
    }

    /*
    @SimpleProperty(description = "Set the activity level selection.", category = PropertyCategory.BEHAVIOR)
    public void ActivitySelection(String value) {
        activity = value;
        activityIndex = ElementsUtil.setSelectedIndexFromValue(value, listActivity);

        if (activityIndex == 1) dailyActivity = 0.1f;
        else if (activityIndex == 2) dailyActivity = 0.2f;
        else dailyActivity = 0.4f;

        if (bbi) {
            if (activityIndex == 1) dailyActivity = 0.1f;
            else if (activityIndex == 2) dailyActivity = 0.2f;
            else dailyActivity = 0.4f;
        }
    }

    @SimpleProperty
    public String ActivitySelection() {
        return activity;
    }
    */

    @SimpleProperty(description = "Set the activity level with index. range: 1 - 3", category = PropertyCategory.BEHAVIOR)
    public void ActivityIndex(int value) {
        activity = ElementsUtil.setSelectionFromIndex(value, listActivity);
        activityIndex = ElementsUtil.selectionIndex(value, listActivity);

        if (activityIndex == 1) dailyActivity = 0.1f;
        else if (activityIndex == 2) dailyActivity = 0.2f;
        else dailyActivity = 0.4f;

        /*
        if (bbi) {
            if (activityIndex == 1) dailyActivity = 0.1f;
            else if (activityIndex == 2) dailyActivity = 0.2f;
            else dailyActivity = 0.4f;
        }
        */
    }

    @SimpleProperty
    public int ActivityIndex() {
        return activityIndex;
    }

    @SimpleProperty(description = "Set the activity level elements.")
    public void ActivityElements(YailList elements) {
        listActivity = ElementsUtil.elements(elements, "KCalCalculator");
    }

    @SimpleProperty
    public YailList ActivityElements() {
        return listActivity;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_CHOICES, editorArgs = {"Female", "Male"}, defaultValue = "Female")
    @SimpleProperty(description = "Set the gender.", category = PropertyCategory.BEHAVIOR)
    public void Gender(String value) {
        gender = value;
        genderIndex = ElementsUtil.setSelectedIndexFromValue(value, listGender);

        if (value == "Female") {
            multiplier = 0.1f;
            times = 25;
        }
        else {
            multiplier = 0.15f;
            times = 30;
        }

        /*
        if (bbi) {
            if (value == "Female") {
                multiplier = 0.1f;
                times = 25;
            }
            else {
                multiplier = 0.15f;
                times = 30;
            }
        } else {
            if (value == "Female") {
                multiplier = 655.1f;
                dailyActivity = 9.563f;
                heightCorrection = 5.003f;
                correction = 6.75f;
            } else {
                multiplier = 66.5f;
                dailyActivity = 13.75f;
                heightCorrection = 5.003f;
                correction = 6.75f;
            }
        }
        */
    }

    @SimpleProperty
    public String Gender() {
        return gender;
    }

    @SimpleProperty(description = "Set the gender with index. value 1 or 2", category = PropertyCategory.BEHAVIOR)
    public void GenderIndex(int value) {
        gender = ElementsUtil.setSelectionFromIndex(value, listGender);
        genderIndex = ElementsUtil.selectionIndex(value, listGender);

        if (value == 1) {
            multiplier = 0.1f;
            times = 25;
        }
        else {
            multiplier = 0.15f;
            times = 30;
        }

        /*
        if (bbi) {
            if (value == 1) {
                multiplier = 0.1f;
                times = 25;
            }
            else {
                multiplier = 0.15f;
                times = 30;
            }
        } else {
            if (value == 1) {
                multiplier = 655.1f;
                dailyActivity = 9.563f;
                heightCorrection = 5.003f;
                correction = 6.75f;
            } else {
                multiplier = 66.5f;
                dailyActivity = 13.75f;
                heightCorrection = 5.003f;
                correction = 6.75f;
            }
        }
        */
    }

    @SimpleProperty
    public int GenderIndex() {
        return genderIndex;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER, defaultValue = "40")
    @SimpleProperty(description = "Set the age.", category = PropertyCategory.BEHAVIOR)
    public void Age(int value) {
        age = value;

        if (value < 60) correction = 0.05f;
        else if (value >= 60 && value < 70) correction = 0.1f;
        else correction = 0.2f;

        /*
        if (bbi) {
            if (value < 60) correction = 0.05f;
            else if (value >= 60 && value < 70) correction = 0.1f;
            else correction = 0.2f;
        }
        */
    }

    @SimpleProperty
    public int Age() {
        return age;
    }

/*
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "true")
    @SimpleProperty(description = "Check if not using weight.", category = PropertyCategory.BEHAVIOR)
    public void BBI(boolean value) {
        bbi = value;
    }

    @SimpleProperty
    public boolean BBI() {
        return bbi;
    }
*/

    private static YailList fromObjects(Object[] item) {
        YailList items = new YailList();
        items = YailList.makeList(item);

        return items;
    }
}
