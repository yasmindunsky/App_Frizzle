package com.frizzl.app.frizzleapp;

import android.content.Context;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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

    public CodeSection(Context context, String code, boolean runnable, boolean editable, CodeKeyboard codeKeyboard) {
        super(context);
        setId(R.id.relativeLayout);

        TextToSpeech.OnInitListener onInitListener = new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {

                    int result = tts.setLanguage(Locale.US);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }

                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        };
        tts = new TextToSpeech(context, onInitListener, "com.google.android.tts");

        codeEditor = new CodeEditor(context, codeKeyboard);
        codeEditor.setText(code);
        codeEditor.setEnabled(editable);
        codeEditor.clearFocus();
        codeEditor.setBackground(getResources().getDrawable(R.drawable.code_bg));
        codeEditor.setGravity(Gravity.START);
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                                                 @Override
                                                                 public void onGlobalLayout() {
                                                                    codeEditor.setWidth(getWidth());
        RelativeLayout.LayoutParams codeEditorLayoutParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        codeEditor.setLayoutParams(codeEditorLayoutParams);
                                                                 }
                                                             });
        addView(codeEditor);

        if (runnable) {
            playButton = new ImageButton(context);
            playButton.setBackground(getResources().getDrawable(R.drawable.run_button_background));
            playButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
            playButton.setAdjustViewBounds(false);
            playButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_run_icon));
            OnClickListener runCode = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (codeIsValid()) {
                        speakOut();
                    }
                    else {
                        displayErrorPopup(v, "Uh oh, there is some problem with the code");
                    }
                }
            };
            playButton.setOnClickListener(runCode);

            RelativeLayout.LayoutParams playButtonLayoutParams = new RelativeLayout.LayoutParams(80, 80);
            playButtonLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            playButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
            playButtonLayoutParams.setMarginEnd(20);
            playButton.setLayoutParams(playButtonLayoutParams);
            addView(playButton);
        }


    }

    private void displayErrorPopup(View view, String errorMessage) {
        Context context = getContext();
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_code_section_run_error, null);
        TextView errorText = popupView.findViewById(R.id.errorText);
        errorText.setText(errorMessage);
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        ImageButton closeButton = popupView.findViewById(R.id.close);
        closeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    private boolean codeIsValid() {
        boolean valid = true;
        String code = codeEditor.getText().toString();
        // Contains 'speakOut'
        valid &= code.indexOf("speakOut") >= 0 ? true : false;
        // Contains '("'
        code = code.replaceAll("\\s+","");
        valid &= code.indexOf("(\"") >= 0 ? true : false;
        // Contains '");'
        valid &= code.indexOf("\");") >= 0 ? true : false;
        return valid;
    }

    private void speakOut() {
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
        return codeEditor.getText().toString();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        tts.shutdown();
    }
}
