package com.marchtech.CheckboxUtils;

import java.util.ArrayList;
import java.util.List;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.YailList;

import com.marchtech.Icon;

@DesignerComponent(version = 1, description = "Extension to help you work with multi checkboxes.", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = Icon.ICON)
@SimpleObject(external = true)
public class CheckboxUtils extends AndroidNonvisibleComponent {
    private List<CheckBox> checkbox = new ArrayList<CheckBox>();

    private int min;
    private int max;

    private int checked = 0;

    public CheckboxUtils(ComponentContainer container) {
        super(container.$form());

    }

    @SimpleFunction(description = "To initialize checkboxes.")
    public void Initialize(YailList checkboxes, int min, int max) {
        checkbox = new ArrayList<CheckBox>();
        for (Object cb : checkboxes.toArray()) {
            checkbox.add((CheckBox) cb);
        }

        this.min = min;
        this.max = max;

        if (min > max)
            throw new IllegalArgumentException("\"min\" cannot be greater than \"max\"!");
        else if (max > checkbox.size())
            throw new IllegalArgumentException("\"max\" cannot be greater than the number of \"checkboxes\"!");

        getChecked();
    }

    @SimpleFunction(description = "Get all checked state of checkboxes as list.")
    public YailList GetAllChecked() {
        List<Boolean> checked = new ArrayList<>();
        for (CheckBox cb : checkbox) {
            checked.add(cb.Checked());
        }

        return YailList.makeList(checked);
    }

    @SimpleFunction(description = "To listen changed.")
    public void Listen(Component component) {
        if (checkbox.contains(component)) {
            CheckBox cb = (CheckBox) component;
            int index = checkbox.indexOf(cb);
            int num = getNumChecked();
            if (num < min) {
                if (!cb.Checked()) {
                    CheckBox mCb = null;
                    if (index < checkbox.size() - 1) {
                        for (int i = index + 1; i < checkbox.size(); i++) {
                            mCb = checkbox.get(i);
                            if (!mCb.Checked()) {
                                mCb.Checked(true);
                                return;
                            }
                        }

                        for (int i = 0; i < index; i++) {
                            mCb = checkbox.get(i);
                            if (!mCb.Checked()) {
                                mCb.Checked(true);
                                return;
                            }
                        }
                    } else {
                        for (int i = 0; i < checkbox.size(); i++) {
                            mCb = checkbox.get(i);
                            if (!mCb.Checked() && i != index) {
                                mCb.Checked(true);
                                return;
                            }
                        }
                    }
                }
            } else if (num > max) {
                if (cb.Checked()) {
                    CheckBox mCb = null;
                    if (index < checkbox.size() - 1) {
                        for (int i = index + 1; i < checkbox.size(); i++) {
                            mCb = checkbox.get(i);
                            if (mCb.Checked()) {
                                mCb.Checked(false);
                                return;
                            }
                        }

                        for (int i = 0; i < index; i++) {
                            mCb = checkbox.get(i);
                            if (mCb.Checked()) {
                                mCb.Checked(false);
                                return;
                            }
                        }
                    } else {
                        for (int i = 0; i < checkbox.size(); i++) {
                            mCb = checkbox.get(i);
                            if (mCb.Checked() && i != index) {
                                mCb.Checked(false);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private void getChecked() {
        for (CheckBox cb : checkbox) {
            if (cb.Checked())
                checked++;
        }

        if (checked == 0) {
            for (int i = 0; i < min; i++) {
                CheckBox cb = checkbox.get(i);
                cb.Checked(true);
                checked++;
            }
        } else if (checked < min) {
            for (int i = 0; i < min; i++) {
                CheckBox cb = checkbox.get(i);
                if (cb.Checked())
                    continue;
                else {
                    cb.Checked(true);
                    checked++;
                }
            }
        } else if (checked > max) {
            for (int i = checkbox.size() - 1; i > 0; i--) {
                CheckBox cb = checkbox.get(i);
                if (!cb.Checked())
                    continue;
                else {
                    cb.Checked(false);
                    checked--;

                    if (checked == min)
                        break;
                }
            }
        }
    }

    private int getNumChecked() {
        int num = 0;
        for (CheckBox cb : checkbox) {
            if (cb.Checked())
                num++;
        }

        return num;
    }
}
