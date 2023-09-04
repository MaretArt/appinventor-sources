package com.marchtech.TextBoxUtils;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;

import com.marchtech.Icon;
import com.marchtech.TextBoxUtils.helpers.TypeOff;
import com.marchtech.TextBoxUtils.helpers.TypeOn;

@DesignerComponent(version = 1, description = "Extension to help you work with textbox password type.", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = Icon.ICON)
@SimpleObject(external = true)
public class TextBoxUtils extends AndroidNonvisibleComponent {
    private Label lbl;
    private Label lblError;

    private TextBox tb1;
    private TextBox tb2;
    private TextBox tb3;
    private TextBox tb4;
    private TextBox tb5;
    private TextBox tb6;

    private int on = 1;
    private int off = 2;
    private int inputType = off;

    public TextBoxUtils(ComponentContainer container) {
        super(container.$form());
    }

    @SimpleFunction(description = "To initialize components.")
    public void Initialize(Label label, @Options(TypeOn.class) int stateOn, @Options(TypeOff.class) int stateOff) {
        lbl = label;
        on = stateOn;
        off = stateOff;
    }

    @SimpleFunction(description = "To initialize components for otp code with 6 textboxs.")
    public void InitializeOTP(Label labelError, TextBox textBox1, TextBox textBox2, TextBox textBox3, TextBox textBox4,
            TextBox textBox5, TextBox textBox6) {
        lblError = labelError;
        tb1 = textBox1;
        tb2 = textBox2;
        tb3 = textBox3;
        tb4 = textBox4;
        tb5 = textBox5;
        tb6 = textBox6;

        lblError.Visible(false);
    }

    @SimpleFunction(description = "To show error label.")
    public void ShowError() {
        lblError.Visible(true);
    }

    @SimpleFunction(description = "To occurs OnTextChanged event.")
    public void OnTextChanged(int textBoxNum) {
        lblError.Visible(false);
        onTextChanged(textBoxNum);
    }

    @SimpleFunction(description = "To occurs GotFocus event.")
    public void GotFocus(int textBoxNum) {
        gotFocus(textBoxNum);
    }

    @SimpleFunction(description = "To change visibility of textbox.")
    public void ChangeVisibility() {
        if (lbl.Text() == "visibility_off") {
            lbl.Text("visibility");
            inputType = on;
        } else {
            lbl.Text("visibility_off");
            inputType = off;
        }
    }

    @SimpleFunction(description = "To reset textbox.")
    public void Reset() {
        lbl.Text("visibility_off");
        inputType = off;
    }

    @SimpleFunction(description = "To get input type of textbox.")
    public int InputType() {
        return inputType;
    }

    @SimpleFunction(description = "Return true or false if textbox is empty or less then 6 digits.")
    public boolean isEmpty() {
        if (tb1.Text().length() < 1 || tb2.Text().length() < 1 || tb3.Text().length() < 1 || tb4.Text().length() < 1
                || tb5.Text().length() < 1 || tb6.Text().length() < 1)
            return true;
        else
            return false;
    }

    @SimpleFunction(description = "Get text of all textbox.")
    public String Text() {
        return tb1.Text() + tb2.Text() + tb3.Text() + tb4.Text() + tb5.Text() + tb6.Text();
    }

    private void onTextChanged(int num) {
        if (num == 1) {
            if (tb1.Text().length() > 1) {
                tb2.Text(tb1.Text().substring(1, 2));
                tb1.Text(tb1.Text().substring(0, 1));
                tb2.RequestFocus();
            }
        } else if (num == 2) {
            if (tb2.Text().length() < 1)
                tb1.RequestFocus();
            else if (tb2.Text().length() > 1) {
                tb3.Text(tb2.Text().substring(1, 2));
                tb2.Text(tb2.Text().substring(0, 1));
                tb3.RequestFocus();
            }
        } else if (num == 3) {
            if (tb3.Text().length() < 1)
                tb2.RequestFocus();
            else if (tb3.Text().length() > 1) {
                tb4.Text(tb3.Text().substring(1, 2));
                tb3.Text(tb3.Text().substring(0, 1));
                tb4.RequestFocus();
            }
        } else if (num == 4) {
            if (tb4.Text().length() < 1)
                tb3.RequestFocus();
            else if (tb4.Text().length() > 1) {
                tb5.Text(tb4.Text().substring(1, 2));
                tb4.Text(tb4.Text().substring(0, 1));
                tb5.RequestFocus();
            }
        } else if (num == 5) {
            if (tb5.Text().length() < 1)
                tb4.RequestFocus();
            else if (tb5.Text().length() > 1) {
                tb6.Text(tb5.Text().substring(1, 2));
                tb5.Text(tb5.Text().substring(0, 1));
                tb6.RequestFocus();
            }
        } else {
            if (tb6.Text().length() < 1)
                tb5.RequestFocus();
            else if (tb6.Text().length() > 1) {
                tb6.Text(tb6.Text().substring(0, 1));
            }
        }
    }

    private void gotFocus(int num) {
        if (num == 2 && tb1.Text().length() < 1)
            tb1.RequestFocus();
        else if (num == 3 && tb2.Text().length() < 1)
            tb2.RequestFocus();
        else if (num == 4 && tb3.Text().length() < 1)
            tb3.RequestFocus();
        else if (num == 5 && tb4.Text().length() < 1)
            tb4.RequestFocus();
        else if (num == 6 && tb5.Text().length() < 1)
            tb5.RequestFocus();
    }
}
