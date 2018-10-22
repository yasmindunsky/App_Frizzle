package com.frizzl.app.frizzleapp.map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frizzl.app.frizzleapp.R;

/**
 * Created by Noga on 13/09/2018.
 */

public class IntroMapButton extends LinearLayout implements MapButton{
    private int enabledColor;
    private int disabledColor;
    private Drawable completedDrawable;
    private boolean completed = false;
    private int levelID;

    public IntroMapButton(Context context, AttributeSet attrs){
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.IntroMapButton,
                0, 0);
        try {
            enabledColor = a.getColor(R.styleable.IntroMapButton_enabledColor, Color.WHITE);
            disabledColor = a.getColor(R.styleable.IntroMapButton_disabledColor, Color.BLACK);
            completedDrawable = a.getDrawable(R.styleable.IntroMapButton_completedDrawable);
            levelID = a.getInt(R.styleable.IntroMapButton_levelID, 1);
        } finally {
            a.recycle();
        }
    }

    private void setIconAlpha() {
        ImageButton icon = (ImageButton) getChildAt(0);
        icon.setAlpha(isEnabled() ? 1 : .2f);
    }

    private void setIntroNameColor() {
        TextView name = (TextView) getChildAt(1);
        boolean enabled = isEnabled();
        int color = Color.WHITE;
        if (!enabled){
            color = disabledColor;
        }
        else if (enabled && !completed) {
            color = enabledColor;
        }
        name.setTextColor(color);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setIntroNameColor();
        setIconAlpha();
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
            setIntroNameColor();
        }
    }

    public int getLevelID() {
        return levelID;
    }

    public void setOnClickListeners(OnClickListener onClickListener) {
        setOnClickListener(onClickListener);
        IntroMapButton parentLayout = this;
        OnClickListener childrenListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                parentLayout.callOnClick();
            }
        };
        ImageButton icon = (ImageButton) getChildAt(0);
        icon.setOnClickListener(childrenListener);
        TextView name = (TextView) getChildAt(1);
        name.setOnClickListener(childrenListener);
    }
}
