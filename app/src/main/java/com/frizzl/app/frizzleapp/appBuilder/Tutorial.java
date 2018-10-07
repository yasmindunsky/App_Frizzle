package com.frizzl.app.frizzleapp.appBuilder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.frizzl.app.frizzleapp.R;
import com.tooltip.OnDismissListener;
import com.tooltip.Tooltip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noga on 06/09/2018.
 */

public class Tutorial {
    // TODO: make this a normal object
    private Context context;
    private int nextTutorialMessage;
    private String[] messages;
    private List<View> views;
    private OnDismissListener[] listeners;
    private int[] gravities;

    public Tutorial(Context context){
//        this.nextTutorialMessage = 0;
        this.context = context;

//        messages = new String[]{"Here you can see\n what your task is.", "Here you can\n add elements.", "Great!\n Move on to your next task.", "Good job!\n Here you can install your app."};
//        gravities = new int[]{Gravity.BOTTOM, Gravity.TOP, Gravity.BOTTOM, Gravity.BOTTOM};

//        views = new ArrayList<>();
//        views.add(appBuilderActivity.findViewById(R.id.viewPager));
//        views.add(designScreenFragment.getView().findViewById(R.id.button_expandable));
//        views.add(appBuilderActivity.findViewById(R.id.viewPager)); //TODO change to next button
//        views.add(appBuilderActivity.findViewById(R.id.play));

//        OnDismissListener presentNextOnDismissListener = new OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                presentNextTutorialMessage();
//            }
//        };
//        listeners = new OnDismissListener[]{presentNextOnDismissListener, null, null, null};
    }

//    public void presentNextTutorialMessage(){
//        presentTooltip(views.get(nextTutorialMessage), messages[nextTutorialMessage], listeners[nextTutorialMessage], gravities[nextTutorialMessage]);
//        nextTutorialMessage++;
//    }

    public void presentTooltip(View view, String text, OnDismissListener listener, int gravity) {
        Tooltip tooltip = new Tooltip.Builder(view)
                .setText(text)
                .setTextColor(context.getResources().getColor(R.color.TextGrey))
                .setTextSize(16f)
                .setTypeface(ResourcesCompat.getFont(context, R.font.calibri_regular))
                .setMargin(4f)
                .setGravity(gravity)
                .setBackgroundColor(Color.parseColor("#ebebeb"))
                .setCornerRadius(15f)
                .setPadding(35f)
                .setDismissOnClick(true)
                .setOnDismissListener(listener)
                .setCancelable(true)
                .show();
    }
}
