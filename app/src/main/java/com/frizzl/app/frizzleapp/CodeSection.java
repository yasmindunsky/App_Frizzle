package com.frizzl.app.frizzleapp;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.frizzl.app.frizzleapp.appBuilder.CodeEditor;

import java.util.Locale;

/**
 * Created by Noga on 12/09/2018.
 */

public class CodeSection extends RelativeLayout {
    private final CodeEditor codeEditor;
    private TextToSpeech tts;
    private  CodeKeyboard codeKeyboard;
    // This variable represents the listener passed in by the owning object
    // The listener must implement the events interface and passes messages up to the parent.
    private ReadyForCTAListener readyForCTAListener;
    private PresentNotificationListener presentNotificationListener;

    public CodeSection(Context context, String code, boolean runnable, boolean mutable, boolean waitForCTA, CodeKeyboard codeKeyboard, ViewGroup layout) {
        super(context);
        setId(R.id.relativeLayout);
        this.readyForCTAListener = null;
        this.presentNotificationListener = null;

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

        codeKeyboard = codeKeyboard;
        codeEditor = new CodeEditor(context, codeKeyboard);
        codeEditor.setText(code);
        codeEditor.setEnabled(mutable);
        codeEditor.clearFocus();

        codeEditor.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.code_bg, null));
        codeEditor.setGravity(Gravity.START);
        this.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            codeEditor.setWidth(getWidth());
            LayoutParams codeEditorLayoutParams =
                    new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            codeEditor.setLayoutParams(codeEditorLayoutParams);
        });
        addView(codeEditor);

        if (runnable) {
            ImageButton playButton = new ImageButton(context);
            OnClickListener runCode = v -> {
                if (waitForCTA && readyForCTAListener != null) readyForCTAListener.onReadyForCTA();
                int currentSlide = UserProfile.user.getCurrentSlideInLevel();
                if (UserProfile.user.getCurrentLevel() == ContentUtils.SPEAKOUT_PRACTICE_LEVEL_ID
                        && presentNotificationListener != null && ((currentSlide == 4) || (currentSlide == 3))) {
                    if (currentSlide == 3)
                        presentNotificationListener.onPresentNotification(getResources().getString(R.string.notification_semicolon));
                    else presentNotificationListener.onPresentNotification(getResources().getString(R.string.notification_space));
                        }
                else if (codeIsValid()){
                    if (ViewUtils.volumeIsLow(context)) ViewUtils.presentVolumeToast(context);
                    speakOut();
                }
                else displayErrorPopup(getResources().getString(R.string.problem_with_syntax), layout);
            };
            playButton.setOnClickListener(runCode);
            playButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.play_button_practice, null));
            playButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
            playButton.setAdjustViewBounds(false);
            playButton.setCropToPadding(false);
            playButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play_run_icon, null));
            playButton.setPadding(24,12,12,12);
            RelativeLayout.LayoutParams playButtonLayoutParams = new RelativeLayout.LayoutParams(80, 80);
            playButtonLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            playButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            playButtonLayoutParams.rightMargin = 20;
            playButton.setLayoutParams(playButtonLayoutParams);
            addView(playButton);
        }
    }

    private void displayErrorPopup(String errorMessage, ViewGroup layout) {
        Context context = getContext();
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View popupView = inflater.inflate(R.layout.popup_code_section_run_error, null);
        TextView errorText = popupView.findViewById(R.id.errorText);
        errorText.setText(errorMessage);
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        ViewUtils.presentPopup(popupWindow, null, codeEditor, layout, context);
        ImageButton closeButton = popupView.findViewById(R.id.close);
        closeButton.setOnClickListener(v -> popupWindow.dismiss());
    }

    private boolean codeIsValid() {
        if (codeEditor.getText() == null){
            return false;
        }
        boolean valid;
        String code = codeEditor.getText().toString();
        // Contains 'speakOut'
        valid = code.contains("speakOut");
        // Contains '("'
        code = code.replaceAll("\\s+","");
        valid &= code.contains("(\"");
        // Contains '");'
        valid &= code.contains("\");");
        // Contains " twice
        int numOfParentheses = code.length() - code.replace("\"", "").length();
        valid &= (numOfParentheses == 2);
        return valid;
    }

    private void speakOut() {
        if (codeEditor.getText() == null){
            return;
        }
        String code = codeEditor.getText().toString();
        String textToSay = "";
        int startIndex = code.indexOf("\"") + 1;
        int endIndex = code.indexOf("\"", startIndex);
        if (startIndex > 0 && endIndex > 0) {
            textToSay = (String) code.subSequence(startIndex, endIndex);
        }

        tts.speak(textToSay, TextToSpeech.QUEUE_ADD, null);
    }

    public String getCode() {
        return codeEditor.getText() == null ? "" : codeEditor.getText().toString();
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

    public void setReadyForCTAListener(ReadyForCTAListener readyForCTAListener) {
        this.readyForCTAListener = readyForCTAListener;
    }
    
    public void setPresentNotificationListener(PresentNotificationListener presentNotificationListener){
        this.presentNotificationListener = presentNotificationListener;
    }

    public void setEditorOnClickListener(OnClickListener onClickListener) {
        if(codeEditor != null){
            codeEditor.setEnabled(true);
            codeEditor.setClickable(true);
            codeEditor.setLongClickable(false);
            codeEditor.setFocusable(false);
            codeEditor.setOnClickListener(onClickListener);
        }
    }

    public void focusOnEditor() {
        codeEditor.requestFocus();
    }

    // This interface defines the type of messages I want to communicate to my owner
    public interface ReadyForCTAListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        void onReadyForCTA();
    }

    // This interface defines the type of messages I want to communicate to my owner
    public interface PresentNotificationListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        void onPresentNotification(String notification);
    }
}
