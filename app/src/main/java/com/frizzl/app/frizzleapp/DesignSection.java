package com.frizzl.app.frizzleapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.frizzl.app.frizzleapp.appBuilder.UserCreatedButton;
import com.frizzl.app.frizzleapp.practice.PracticeSlideFragment;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Noga on 12/09/2018.
 */

public class DesignSection extends RelativeLayout {
    private TextToSpeech tts;
    private final UserCreatedButton userCreatedButton;
    private ViewGroup layout;
    private DisplayNotificationListener displayNotificationListener;

    public DesignSection(Context context, boolean runnable, boolean withOnClickSet, String onClickFunction, PracticeSlideFragment practiceSlideFragment) {
        super(context);

        TextToSpeech.OnInitListener onInitListener = status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(Locale.US);
                if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "This Language is not supported");
                }
            } else {
                Log.e("TTS", "Initialization Failed!");
            }
        };
        tts = new TextToSpeech(context, onInitListener, "com.google.android.tts");

        setId(R.id.constraintLayout);

        ImageView imageView = new ImageView(context);
        imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.design_element_bg, null));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setAdjustViewBounds(true);
        addView(imageView);

        userCreatedButton = new UserCreatedButton(context, 1, 1);
//        userCreatedButton.setBackgroundColor(getResources().getColor(R.color.yellow));
//        userCreatedButton.setTextColor(getResources().getColor(R.color.darkBlue));
        userCreatedButton.setText(getContext().getString(R.string.my_button));
        if (withOnClickSet) {
            userCreatedButton.setOnClick("myFunction");
        }
        Button buttonView = userCreatedButton.getThisView();
        buttonView.setOnClickListener(v -> {
            // show the popup window
            Set<String> functions = new HashSet<>();
            boolean hasOnClickFunction = !onClickFunction.equals("false");
            if (hasOnClickFunction) functions.add(onClickFunction);
            userCreatedButton.setFunctions(functions);
            userCreatedButton.setDisplayOnClick(hasOnClickFunction);
            PopupWindow popupWindow = userCreatedButton.getPropertiesTablePopupWindow(getContext());
            v.post(() ->
            ViewUtils.presentPopup(popupWindow, null, v, layout, context));

        });
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5,5,5,5);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        buttonView.setLayoutParams(layoutParams);

        addView(buttonView);

        if (runnable) {
            ImageButton playButton = new ImageButton(context);
            OnClickListener runDesign = v -> {
                Context context1 = getContext();
                LayoutInflater inflater = (LayoutInflater)
                        context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert inflater != null;
                View runPopupView = inflater.inflate(R.layout.popup_design_section_run, null);
                Button button = runPopupView.findViewById(R.id.button);
                Button thisView = userCreatedButton.getThisView();
                button.setText(thisView.getText());
                button.setTextColor(thisView.getCurrentTextColor());
                button.setTypeface(thisView.getTypeface());
                int originalButtonDrawableRes = R.drawable.user_button_background;
                Drawable buttonDrawable = ContextCompat.getDrawable(context1, originalButtonDrawableRes);
                if (buttonDrawable != null) {
                    buttonDrawable.setColorFilter(Color.parseColor(userCreatedButton.getProperties().get("android:backgroundTint")), PorterDuff.Mode.MULTIPLY);
                    button.setBackground(buttonDrawable);
                }
                PopupWindow runPopupWindow = new PopupWindow(runPopupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, true);
                Runnable displayNotification = () -> {
                    if (UserProfile.user.getCurrentLevel() == ContentUtils.ONCLICK_PRACTICE_LEVEL_ID
                            && practiceSlideFragment.getCurrentSlide() == 1) {
                        practiceSlideFragment.presentNotificationFromSection(context1.getResources().getString(R.string.our_button_does_nothing));
                    }
                };
                ViewUtils.presentPopup(runPopupWindow, displayNotification, v, layout, context1);
                ImageButton closeButton = runPopupView.findViewById(R.id.close);
                closeButton.setOnClickListener(v12 -> runPopupWindow.dismiss());
                button.setOnClickListener(v1 -> {
                    boolean onClickSet = userCreatedButton.getOnClick().equals("myFunction");
                    if (onClickSet) {
                        if (ViewUtils.volumeIsLow(context1)) ViewUtils.presentVolumeToast(context1);
                        tts.speak("Hello", TextToSpeech.QUEUE_ADD, null);
                    }
                    if (displayNotificationListener != null)
                        displayNotificationListener.onDisplayNotification(runPopupWindow);

                });
            };
            playButton.setOnClickListener(runDesign);
            playButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.play_button_practice, null));
            playButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
            playButton.setAdjustViewBounds(false);
            playButton.setCropToPadding(false);
            playButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play_run_icon, null));
            playButton.setPadding(24,12,12,12);
            LayoutParams playButtonLayoutParams = new LayoutParams(80, 80);
            playButtonLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            playButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            playButtonLayoutParams.rightMargin = 20;
            playButton.setLayoutParams(playButtonLayoutParams);
            addView(playButton);
        }
    }

    public UserCreatedButton getUserCreatedButton() {
        return userCreatedButton;
    }

    public void setBackgroundLayout(ViewGroup layout) {
        this.layout = layout;
    }

    public void setDisplayNotificationListener(DisplayNotificationListener displayNotificationListener) {
        this.displayNotificationListener = displayNotificationListener;
    }

    // This interface defines the type of messages I want to communicate to my owner
    public interface DisplayNotificationListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        void onDisplayNotification(PopupWindow popupWindow);
    }

    @Override
    protected void onDetachedFromWindow() {
        //Close the Text to Speech Library
        if(tts != null) {
            tts.stop();
            tts.shutdown();
            Log.d("tts", "TTS Destroyed");
        }
        super.onDetachedFromWindow();
    }
}
