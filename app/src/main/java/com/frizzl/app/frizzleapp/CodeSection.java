package com.frizzl.app.frizzleapp;

import android.content.Context;
import android.speech.tts.TextToSpeech;
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
    private CodeEditor codeEditor;
    private ImageButton playButton;
    private TextToSpeech tts;
    // This variable represents the listener passed in by the owning object
    // The listener must implement the events interface and passes messages up to the parent.
    private readyForCTAListener readyForCTAListener;

    public CodeSection(Context context, String code, boolean runnable, boolean editable, boolean waitForCTA, CodeKeyboard codeKeyboard) {
        super(context);
        setId(R.id.relativeLayout);
        this.readyForCTAListener = null;

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

        codeEditor = new CodeEditor(context, codeKeyboard);
        codeEditor.setText(code);
        codeEditor.setEnabled(editable);
        codeEditor.clearFocus();
        codeEditor.setBackground(getResources().getDrawable(R.drawable.code_bg));
        codeEditor.setGravity(Gravity.START);
        this.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            codeEditor.setWidth(getWidth());
            LayoutParams codeEditorLayoutParams =
                    new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            codeEditor.setLayoutParams(codeEditorLayoutParams);
        });
        addView(codeEditor);

        if (runnable) {
            playButton = new ImageButton(context);
            playButton.setBackground(getResources().getDrawable(R.drawable.run_button_background));
            playButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
            playButton.setAdjustViewBounds(false);
            playButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_run_icon));
            OnClickListener runCode = v -> {
                if (waitForCTA && readyForCTAListener != null) readyForCTAListener.onReadyForCTA();
                if (codeIsValid()) {
                    speakOut();
                }
                else {
                    displayErrorPopup(v, getResources().getString(R.string.problem_with_code_error));
                }
            };
            playButton.setOnClickListener(runCode);

            RelativeLayout.LayoutParams playButtonLayoutParams = new RelativeLayout.LayoutParams(80, 80);
            playButtonLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            playButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            playButtonLayoutParams.setMarginEnd(20);
            playButton.setLayoutParams(playButtonLayoutParams);
            addView(playButton);
        }


    }

    private void displayErrorPopup(View view, String errorMessage) {
        Context context = getContext();
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View popupView = inflater.inflate(R.layout.popup_code_section_run_error, null);
        TextView errorText = popupView.findViewById(R.id.errorText);
        errorText.setText(errorMessage);
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
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
        super.onDetachedFromWindow();
        tts.shutdown();
    }



    public void setReadyForCTAListener(CodeSection.readyForCTAListener readyForCTAListener) {
        this.readyForCTAListener = readyForCTAListener;
    }

    // This interface defines the type of messages I want to communicate to my owner
    public interface readyForCTAListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        void onReadyForCTA();
    }
}
