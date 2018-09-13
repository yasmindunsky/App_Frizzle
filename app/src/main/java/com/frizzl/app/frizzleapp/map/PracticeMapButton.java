package com.frizzl.app.frizzleapp.map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frizzl.app.frizzleapp.R;

/**
 * Created by Noga on 13/09/2018.
 */

public class PracticeMapButton extends android.support.v7.widget.AppCompatButton implements MapButton{
    private int enabledColor;
    private int disabledColor;
    private Drawable completedDrawable;
    private boolean completed = false;

    public PracticeMapButton(Context context, AttributeSet attrs){
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AppMapButton,
                0, 0);
        try {
            enabledColor = a.getColor(R.styleable.AppMapButton_enabledColor, Color.WHITE);
            disabledColor = a.getColor(R.styleable.AppMapButton_disabledColor, Color.BLACK);
            completedDrawable = a.getDrawable(R.styleable.AppMapButton_completedDrawable);
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
}
