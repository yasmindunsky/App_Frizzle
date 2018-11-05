package com.frizzl.app.frizzleapp.practice;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.Support;

/**
 * Created by Noga on 13/09/2018.
 */

public class PracticeErrorView extends LinearLayout{
   private AppCompatTextView errorTextView;
   private ImageView iconImageView;

    public PracticeErrorView(Context context){
        super(context);
        this.setOrientation(HORIZONTAL);
        this.setLayoutDirection(LAYOUT_DIRECTION_INHERIT);
        this.setBackground(context.getResources().getDrawable(R.drawable.error_background));
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        int px = Support.dpStringToPixel(context, "15dp");
        this.setPadding(px, px, px, px);

        iconImageView = new ImageView(context);
        iconImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_try_again));
        iconImageView.setLayoutParams(new ViewGroup.LayoutParams( Support.dpStringToPixel(context, "40dp"),
                Support.dpStringToPixel(context, "40dp")));
        iconImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        this.addView(iconImageView);

        int errorTextStyle = R.style.Text_PracticeSlide_error;
        errorTextView = new AppCompatTextView(new ContextThemeWrapper(context, errorTextStyle));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMarginStart(Support.dpStringToPixel(context, "10dp"));
        errorTextView.setLayoutParams(layoutParams);
        this.addView(errorTextView);
    }

    public void setText(String error) {
        if (this.errorTextView != null) this.errorTextView.setText(error);
    }

//    public void setVisibility(int visibility){
//        super.setVisibility(visibility);
//        if (this.errorTextView != null) this.errorTextView.setVisibility(visibility);
//        if (this.iconImageView != null) this.iconImageView.setVisibility(visibility);
//    }
}
