package com.prorl.quent.clickcounter;

import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Elliott Ewing on 6/23/2016.
 */
public class Count {

    private int count;
    private int defValue;

    private TextView display;

    public Count(int def, TextView display) {
        this(def, def, display);
    }

    public Count(int value, int def, TextView display) {
        count = value;
        defValue = def;
        this.display = display;
        updateDisplay();
    }

    public void increment() {
        count++;
        updateDisplay();
    }

    public void reset() {
        count = defValue;
        updateDisplay();
    }

    public int getCount() {
        return count;
    }

    public int getDefault() {
        return defValue;
    }

    public void setDefault(int newDefault) {
        defValue = newDefault;
    }

    public void setCount(int newCount) {
        count = newCount;
    }

    private void updateDisplay() {
        display.setText(count + "");
    }
}
