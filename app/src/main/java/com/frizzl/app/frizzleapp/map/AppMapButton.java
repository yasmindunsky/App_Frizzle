package com.frizzl.app.frizzleapp.map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frizzl.app.frizzleapp.R;

/**
 * Created by Noga on 13/09/2018.
 */

public class AppMapButton extends LinearLayout implements MapButton{
    private int enabledColor;
    private int disabledColor;
    private Drawable completedDrawable;
    private boolean completed = false;
    private int appID;

    public AppMapButton(Context context, AttributeSet attrs){
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AppMapButton,
                0, 0);
        try {
            enabledColor = a.getColor(R.styleable.AppMapButton_enabledColor, Color.WHITE);
            disabledColor = a.getColor(R.styleable.AppMapButton_disabledColor, Color.BLACK);
            completedDrawable = a.getDrawable(R.styleable.AppMapButton_completedDrawable);
            appID = a.getInt(R.styleable.AppMapButton_appID, 0);
        } finally {
            a.recycle();
        }
    }

    private void setAppIconAlpha() {
        ImageButton appIcon = (ImageButton) getChildAt(0);
        appIcon.setAlpha(isEnabled() ? 1 : .2f);
    }

    private void setAppNameColor() {
        TextView appName = (TextView) getChildAt(1);
        boolean enabled = isEnabled();
        int color = Color.WHITE;
        if (!enabled){
            color = disabledColor;
        }
        else if (enabled && !completed) {
            color = enabledColor;
        }
        appName.setTextColor(color);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setAppNameColor();
        setAppIconAlpha();
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
            setAppNameColor();
        }
    }

    public int getAppID() {
        return appID;
    }
}
