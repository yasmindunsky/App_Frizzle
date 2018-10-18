package com.frizzl.app.frizzleapp;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class CodeKeyboard extends LinearLayout implements View.OnClickListener {

    private static final int NUM_OF_CHARS_TO_BACK_AFTER_SPEAKOUT = 3;
    private static final int NUM_OF_CHARS_TO_BACK_AFTER_FUNCTION = 21;

    private SparseArray<String> keyValues = new SparseArray<>();

    private InputConnection inputConnection;
    private InputMethodManager inputMethodManager;

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
        englishButton.setOnClickListener(view -> inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0));
        ImageButton buttonDelete = findViewById(R.id.button_delete);
        buttonDelete.setOnClickListener(this);
        ImageButton buttonEnter = findViewById(R.id.button_enter);
        buttonEnter.setOnClickListener(this);

        keyValues.put(R.id.button_speakout, "speakOut(\"\");");
        keyValues.put(R.id.button_function, "public void  (View view) {\n    \n}");
        keyValues.put(R.id.button_enter, "\n");
    }

    @Override
    public void onClick(View view) {
        if (inputConnection == null)
            return;

        if (view.getId() == R.id.button_delete) {
            CharSequence selectedText = inputConnection.getSelectedText(0);

            if (TextUtils.isEmpty(selectedText)) {
                inputConnection.deleteSurroundingText(1, 0);
            } else {
                inputConnection.commitText("", 1);
            }
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
}