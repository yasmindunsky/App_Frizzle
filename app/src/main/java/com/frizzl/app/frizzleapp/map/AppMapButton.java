package com.frizzl.app.frizzleapp.map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.ViewUtils;

/**
 * Created by Noga on 13/09/2018.
 */

public class AppMapButton extends LinearLayout implements MapButton{
    private Drawable completedDrawable;
    private Drawable disabledDrawable;
    private Drawable currentDrawable;
    private Drawable completedIcon;
    private Drawable disabledIcon;
    private Drawable currentIcon;
    int originalSize;
    int originalPadding;

    private int levelID;
    Status status = Status.disabled;

    public AppMapButton(Context context, AttributeSet attrs){
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AppMapButton,
                0, 0);
        try {
            completedDrawable = a.getDrawable(R.styleable.AppMapButton_completedDrawable);
            disabledDrawable = a.getDrawable(R.styleable.AppMapButton_disabledDrawable);
            currentDrawable = a.getDrawable(R.styleable.AppMapButton_currentDrawable);

            completedIcon = a.getDrawable(R.styleable.AppMapButton_completedIcon);
            disabledIcon = a.getDrawable(R.styleable.AppMapButton_disabledIcon);
            currentIcon = a.getDrawable(R.styleable.AppMapButton_currentIcon);

            levelID = a.getInt(R.styleable.AppMapButton_levelID, 0);
            originalSize = ViewUtils.dpStringToPixel("170dp", getContext());
            originalPadding = ViewUtils.dpStringToPixel("15dp", getContext());
        } finally {
            a.recycle();
        }
    }

    private void setSize(int size, int additionalSidesPadding, int margin) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) getLayoutParams();
        layoutParams.width = size;
        layoutParams.height = size;
        layoutParams.setMargins(layoutParams.leftMargin,
                margin,
                layoutParams.rightMargin,
                margin);
        setLayoutParams(layoutParams);
        setPadding(originalPadding + additionalSidesPadding,
                originalPadding,
                originalPadding + additionalSidesPadding,
                originalPadding);
    }

    private void setAppIconAlpha() {
        ImageButton appIcon = (ImageButton) getChildAt(0);
        appIcon.setAlpha(isEnabled() ? 1 : .2f);
    }

    private void setAppNameColor(int color) {
        TextView appName = (TextView) getChildAt(1);
        appName.setTextColor(color);
    }

    private void setIcon(Drawable icon) {
        ImageButton appIcon = (ImageButton) getChildAt(0);
        appIcon.setBackground(icon);
    }

    private void updateState(Drawable drawable, Drawable icon, int color, int size, int additionalSidesPadding, int margin) {
        setBackground(drawable);
        setIcon(icon);
        setAppNameColor(color);
        setSize(size,additionalSidesPadding,margin);
    }

    @Override
    public void setCurrent() {
        super.setEnabled(true);
        status = Status.current;
        updateState(currentDrawable,
                currentIcon,
                getResources().getColor(R.color.glowy_pink),
                (int) (originalSize * 1.4),
                100,
                -50);
    }

    @Override
    public void setCompleted() {
        status = Status.completed;
        updateState(completedDrawable,
                completedIcon,
                getResources().getColor(R.color.dark_blue),
                (int) (originalSize * 1.05),
                0,
                110);
    }

    @Override
    public void setDisabled() {
        status = Status.disabled;
        updateState(disabledDrawable,
                disabledIcon
                , getResources().getColor(R.color.dark_blue),
                originalSize,
                0,
                110);
    }

    public int getLevelID() {
        return levelID;
    }

    public void setOnClickListeners(OnClickListener onClickListener) {
        setOnClickListener(onClickListener);
        AppMapButton parentLayout = this;
        OnClickListener childrenListener = v -> parentLayout.callOnClick();
        ImageButton icon = (ImageButton) getChildAt(0);
        icon.setOnClickListener(childrenListener);
        TextView name = (TextView) getChildAt(1);
        name.setOnClickListener(childrenListener);
    }
}
