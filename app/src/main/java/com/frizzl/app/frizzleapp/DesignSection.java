package com.frizzl.app.frizzleapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.frizzl.app.frizzleapp.appBuilder.DefinedFunctionsViewModel;
import com.frizzl.app.frizzleapp.appBuilder.UserCreatedButton;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Noga on 12/09/2018.
 */

public class DesignSection extends RelativeLayout {
    private ImageButton playButton;
    private TextToSpeech tts;
    private UserCreatedButton userCreatedButton;
    private ViewGroup layout;
    private DisplayErrorListener displayErrorListener;

    public DesignSection(Context context, boolean runnable, boolean withOnClickSet, String onClickFunction, FragmentActivity activity) {
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
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.design_element_bg));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setAdjustViewBounds(true);
        addView(imageView);

        userCreatedButton = new UserCreatedButton(context, 1, 1);
        userCreatedButton.setBackgroundColor("#f8b119");
        userCreatedButton.setTextColor("#FFFFFF");
        userCreatedButton.setText(getContext().getString(R.string.my_button));
        if (withOnClickSet) {
            userCreatedButton.setOnClick("myFunction");
        }
        Button buttonView = userCreatedButton.getThisView();
        buttonView.setOnClickListener(v -> {
            // show the popup window
            Set<String> functions = new HashSet<>();
            if (onClickFunction != "false") functions.add(onClickFunction);
            userCreatedButton.setFunctions(functions);
            PopupWindow popupWindow = userCreatedButton.getPropertiesTablePopupWindow(getContext());
            v.post(() ->
                    Support.presentPopup(popupWindow, null, v, layout, context));
        });
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5,5,5,5);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        buttonView.setLayoutParams(layoutParams);

        addView(buttonView);

        if (runnable) {
            playButton = new ImageButton(context);
            playButton.setBackground(getResources().getDrawable(R.drawable.run_button_background));
            playButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
            playButton.setAdjustViewBounds(false);
            playButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_run_icon));

            playButton.setOnClickListener(runDesign);

            LayoutParams playButtonLayoutParams = new LayoutParams(80, 80);
            playButtonLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            playButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
            playButtonLayoutParams.setMarginEnd(20);
            playButton.setLayoutParams(playButtonLayoutParams);
            addView(playButton);
        }
    }
    private OnClickListener runDesign = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Context context = getContext();
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            View popupView = inflater.inflate(R.layout.popup_design_section_run, null);

            Button button = popupView.findViewById(R.id.button);
            Button thisView = userCreatedButton.getThisView();
            button.setText(thisView.getText());
            button.setTextColor(thisView.getCurrentTextColor());
            button.setTypeface(thisView.getTypeface());
            int originalButtonDrawableRes = R.drawable.user_button_background;
            Drawable buttonDrawable = ContextCompat.getDrawable(context, originalButtonDrawableRes);
            if (buttonDrawable != null) {
                buttonDrawable.setColorFilter(Color.parseColor(userCreatedButton.getProperties().get("android:backgroundTint")), PorterDuff.Mode.DARKEN);
                button.setBackground(buttonDrawable);
            }

            button.setOnClickListener(v1 -> {
                boolean onClickSet = userCreatedButton.getOnClick().equals("myFunction");
                if (onClickSet) {
                    tts.speak("Hello", TextToSpeech.QUEUE_ADD, null);
                }
                if (displayErrorListener != null) displayErrorListener.onDisplayError();

            });

            PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
            Support.presentPopup(popupWindow, null, v, layout, context);
            ImageButton closeButton = popupView.findViewById(R.id.close);
            closeButton.setOnClickListener(v12 -> popupWindow.dismiss());
        }
    };

    public UserCreatedButton getUserCreatedButton() {
        return userCreatedButton;
    }

    public void setBackgroundLayout(ViewGroup layout) {
        this.layout = layout;
    }

    public void setDisplayErrorListener(DisplayErrorListener displayErrorListener) {
        this.displayErrorListener = displayErrorListener;
    }

    // This interface defines the type of messages I want to communicate to my owner
    public interface DisplayErrorListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        void onDisplayError();
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
