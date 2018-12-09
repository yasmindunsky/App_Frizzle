package com.frizzl.app.frizzleapp.map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.ViewUtils;

/**
 * Created by Noga on 13/09/2018.
 */

public class PracticeMapButton extends android.support.v7.widget.AppCompatButton implements MapButton{
    private Drawable completedDrawable = getResources().getDrawable(R.drawable.map_practice_button_completed);
    private Drawable disabledDrawable = getResources().getDrawable(R.drawable.map_button_disabled);
    private Drawable currentDrawable = getResources().getDrawable(R.drawable.map_practice_button_current);
    private int textAppearance = R.style.Text_Map_Practice;
    int originalSize;
    int originalTopMargin;
    int originalBottomMargin;

    private int practiceID;
    Status status = Status.disabled;

    public PracticeMapButton(Context context, AttributeSet attrs){
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PracticeMapButton,
                0, 0);
        setBackground(disabledDrawable);
        setTextAppearance(textAppearance);
        originalSize = ViewUtils.dpStringToPixel("80dp", getContext());
        try {
            String layout_marginBottom = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_marginBottom");
            originalBottomMargin = layout_marginBottom != null ?
                    ViewUtils.dpStringToPixel(layout_marginBottom, getContext()) :
                    0;
            String layout_marginTop = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_marginTop");
            originalTopMargin = layout_marginTop != null ?
                    ViewUtils.dpStringToPixel(layout_marginTop, getContext()) :
                    0;
            practiceID = a.getInt(R.styleable.PracticeMapButton_levelID, 1);
        } finally {
            a.recycle();
        }
    }

    private void setPracticeNameColor(int color) {
        setTextColor(color);
    }

    @Override
    public void setCurrent() {
        super.setEnabled(true);
        status = Status.current;
        updateState(currentDrawable,
                (int) (originalSize * 1.6),
                0,
                0);
    }

    @Override
    public void setCompleted() {
        super.setEnabled(true);
        status = Status.completed;
        updateState(completedDrawable,
                (int) (originalSize * 1.2),
                originalTopMargin,
                originalBottomMargin);
    }

    @Override
    public void setDisabled() {
        status = Status.disabled;
        updateState(disabledDrawable,
                (int) (originalSize),
                originalTopMargin,
                originalBottomMargin);
    }

    private void updateState(Drawable drawable, int size, int topMargin, int bottomMargin) {
        setBackground(drawable);
        setSize(size, topMargin, bottomMargin);
    }

    private void setSize(int size, int topMargin, int bottomMargin) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) getLayoutParams();
        layoutParams.width = size;
        layoutParams.height = size;
        layoutParams.setMargins(layoutParams.leftMargin,
                topMargin,
                layoutParams.rightMargin,
                bottomMargin);
        setLayoutParams(layoutParams);
    }


    public int getPracticeID() {
        return practiceID;
    }
}
