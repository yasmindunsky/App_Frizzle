package com.frizzl.app.frizzleapp.map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frizzl.app.frizzleapp.R;

/**
 * Created by Noga on 13/09/2018.
 */

public class IntroMapButton extends LinearLayout implements MapButton{
    private Drawable completedDrawable;
    private int levelID;
    Status status = Status.disabled;

    public IntroMapButton(Context context, AttributeSet attrs){
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.IntroMapButton,
                0, 0);
        try {
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
            color = disabledTextColor;
        }
        else if (enabled) {
            color = disabledTextColor;
        }
        name.setTextColor(color);
    }

    @Override
    public void setCurrent() {
        super.setEnabled(true);
        status = Status.current;

        setIntroNameColor();
        setIconAlpha();
    }

    @Override
    public void setDisabled() {

    }

    @Override
    public void setCompleted() {
        status = Status.completed;
        setBackground(completedDrawable);
        setIntroNameColor();
    }

    public int getLevelID() {
        return levelID;
    }

    public void setOnClickListeners(OnClickListener onClickListener) {
        setOnClickListener(onClickListener);
        IntroMapButton parentLayout = this;
        OnClickListener childrenListener = v -> parentLayout.callOnClick();
        ImageButton icon = (ImageButton) getChildAt(0);
        icon.setOnClickListener(childrenListener);
        TextView name = (TextView) getChildAt(1);
        name.setOnClickListener(childrenListener);
    }
}
