package com.frizzl.app.frizzleapp;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;

public class CodeKeyboard extends LinearLayout implements View.OnClickListener {

    private static final int NUM_OF_CHARS_TO_BACK_AFTER_SPEAKOUT = 3;
    private static final String FUNCTION_PART_2 = "(View view){\n\t\n}";
    private static final int NUM_OF_CHARS_TO_BACK_AFTER_FUNCTION = FUNCTION_PART_2.length();

    private final SparseArray<String> keyValues = new SparseArray<>();

    private InputConnection inputConnection;
    private InputMethodManager inputMethodManager;
    private ImageView orangeKeyImage;
    private ImageView greenKeyImage;

    boolean pressedDelete = false;

    public CodeKeyboard(Context context) {
        this(context, null, 0);
    }

    public CodeKeyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CodeKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.keyboard_code, this, true);
        Button speakOutButton = findViewById(R.id.button_speakout);
        speakOutButton.setOnClickListener(this);
        Button functionButton = findViewById(R.id.button_function);
        functionButton.setOnClickListener(this);
        ImageButton englishButton = findViewById(R.id.button_english);
        englishButton.setOnClickListener(view ->
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0));
        ImageButton buttonDelete = findViewById(R.id.button_delete);
        buttonDelete.setOnTouchListener(new RepeatListener(400, 100, new OnClickListener() {
            @Override
            public void onClick(View view) {
                // the code to execute repeatedly
                delete();
            }
        }));
        ImageButton buttonEnter = findViewById(R.id.button_enter);
        buttonEnter.setOnClickListener(this);

        keyValues.put(R.id.button_speakout, "speakOut(\"\");");
        keyValues.put(R.id.button_function, "public void nameYouChoose(View view){\n\t\n}");
        keyValues.put(R.id.button_enter, "\n");

        orangeKeyImage = findViewById(R.id.orange_key);
        orangeKeyImage.setAlpha(0f);
        greenKeyImage = findViewById(R.id.green_key);
        greenKeyImage.setAlpha(0f);
    }

    @Override
    public void onClick(View view) {
        if (inputConnection == null)
            return;

        if (view.getId() == R.id.button_delete) {
            delete();
        } else {
            String value = keyValues.get(view.getId());
            inputConnection.commitText(value, 1);
            if (view.getId() == R.id.button_speakout) {
                moveCursor(-NUM_OF_CHARS_TO_BACK_AFTER_SPEAKOUT);
            }
            if (view.getId() == R.id.button_function) {
                moveCursor(-NUM_OF_CHARS_TO_BACK_AFTER_FUNCTION);
            }
        }
    }

    private void moveCursor(int numOfChars) {
        ExtractedText extractedText = inputConnection.getExtractedText(new ExtractedTextRequest(), 0);
        int cursorPosition = extractedText.selectionStart;
        inputConnection.setSelection(cursorPosition + numOfChars, cursorPosition + numOfChars);
    }

    public void setInputConnection(InputConnection ic) {
        inputConnection = ic;
    }

    public void setInputMethodManager(InputMethodManager inputMethodManager) {
        this.inputMethodManager = inputMethodManager;
    }

    public void enableSpeakOutKey(boolean firstTime){
        Button speakOutKey = findViewById(R.id.button_speakout);
        speakOutKey.setEnabled(true);
        speakOutKey.setText(R.string.speakout_keyboard);
        if (firstTime){
            setVisibility(VISIBLE);
            orangeKeyImage.setAlpha(1f);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    orangeKeyImage.animate().alpha(0f).setDuration(2000);
                }
            }, 400);
        }
    }

    public void enableFunctionKey(boolean firstTime){
        Button functionKey = findViewById(R.id.button_function);
        functionKey.setEnabled(true);
        functionKey.setText(R.string.function_keyboard);
        if (firstTime){
            setVisibility(VISIBLE);
            greenKeyImage.setAlpha(1f);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    greenKeyImage.animate().alpha(0f).setDuration(2000);
                }
            }, 400);
        }
    }

    private void delete() {
        CharSequence selectedText = inputConnection.getSelectedText(0);
        if (TextUtils.isEmpty(selectedText)) {
            // no selection, so delete previous character
            ExtractedText extractedText = inputConnection.getExtractedText(new ExtractedTextRequest(), 0);
            int cursorPosition = extractedText.selectionStart;
            inputConnection.setSelection(cursorPosition - 1, cursorPosition);
            inputConnection.commitText("", 1);
        } else {
            // delete the selection
            inputConnection.commitText("", 1);
        }
    }


    public void presentKeyboardAndPlaceCursor() {
        setVisibility(VISIBLE);
        int length = "public void myFunction(View view){\n".length();
        inputConnection.setSelection(length, length);
    }
}