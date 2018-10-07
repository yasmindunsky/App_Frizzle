package com.frizzl.app.frizzleapp.map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.frizzl.app.frizzleapp.R;

/**
 * Created by Noga on 13/09/2018.
 */

public class PracticeMapButton extends android.support.v7.widget.AppCompatButton implements MapButton{
    private int enabledColor;
    private int disabledColor;
    private Drawable completedDrawable;
    private boolean completed = false;
    private int practiceID;

    public PracticeMapButton(Context context, AttributeSet attrs){
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PracticeMapButton,
                0, 0);
        try {
            enabledColor = a.getColor(R.styleable.PracticeMapButton_enabledColor, Color.WHITE);
            disabledColor = a.getColor(R.styleable.PracticeMapButton_disabledColor, Color.BLACK);
            completedDrawable = a.getDrawable(R.styleable.PracticeMapButton_completedDrawable);
            practiceID = a.getInt(R.styleable.PracticeMapButton_practiceID, 1);
        } finally {
            a.recycle();
        }
    }

    private void setPracticeNameColor() {
        boolean enabled = isEnabled();
        int color;
        if (completed) {
            color = Color.WHITE;
        } else if (enabled) {
            color = enabledColor;
        } else {
            color = disabledColor;
        }
        setTextColor(color);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setPracticeNameColor();
    }

    public int getEnabledColor() {
        return enabledColor;
    }

    public void setEnabledColor(int enabledColor) {
        this.enabledColor = enabledColor;
        invalidate();
        requestLayout();
    }

    public int getDisabledColor() {
        return disabledColor;
    }

    public void setDisabledColor(int disabledColor) {
        this.disabledColor = disabledColor;
        invalidate();
        requestLayout();
    }

    @Override
    public void setCompleted(boolean completed) {
        this.completed = completed;
        if (completed) {
            setBackground(completedDrawable);
            setPracticeNameColor();
        }
    }

    public int getPracticeID() {
        return practiceID;
    }
}
