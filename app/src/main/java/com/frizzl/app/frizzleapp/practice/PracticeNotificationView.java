package com.frizzl.app.frizzleapp.practice;

import android.content.Context;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.ViewUtils;

/**
 * Created by Noga on 13/09/2018.
 */

public class PracticeNotificationView extends LinearLayout{
    private final AppCompatTextView notificationTextView;
    private ImageView iconImageView;

    public PracticeNotificationView(Context context, boolean isError){
        super(context);
        this.setOrientation(HORIZONTAL);
        this.setLayoutDirection(LAYOUT_DIRECTION_INHERIT);
        this.setBackground(context.getResources().getDrawable(R.drawable.error_background));
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        int px = ViewUtils.dpStringToPixel("15dp", context);
        this.setPadding(px, px, px, px);
        this.setGravity(Gravity.CENTER_VERTICAL);

        iconImageView = new ImageView(context);
        int drawableResource = isError ? R.drawable.ic_try_again : R.drawable.ic_notification;
        iconImageView.setImageDrawable(context.getResources().getDrawable(drawableResource));
        iconImageView.setLayoutParams(new ViewGroup.LayoutParams( ViewUtils.dpStringToPixel("40dp", context),
                ViewUtils.dpStringToPixel("40dp", context)));
        iconImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        this.addView(iconImageView);

        int textStyle = isError ? R.style.Text_PracticeSlide_error : R.style.Text_PracticeSlide_notice;
        notificationTextView = new AppCompatTextView(new ContextThemeWrapper(context, textStyle));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMarginStart(ViewUtils.dpStringToPixel("10dp", context));
        notificationTextView.setLayoutParams(layoutParams);
        this.addView(notificationTextView);
    }

    public void setText(String error) {
        if (this.notificationTextView != null) this.notificationTextView.setText(error);
    }

    public void setErrorVisibility(int visibility){
        super.setVisibility(visibility);
        iconImageView.setVisibility(visibility);
        notificationTextView.setVisibility(visibility);
        setBackground(null);
        invalidate();
    }
}
