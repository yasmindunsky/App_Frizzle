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
import android.widget.LinearLayout;

import java.util.HashSet;

public class CodeKeyboard extends LinearLayout implements View.OnClickListener {

    private Button button1, button2, button3, button4,
            button5, button0, buttonDelete, buttonEnter;

    private SparseArray<String> keyValues = new SparseArray<>();
    private HashSet<String> writeWithinBrackets = new HashSet<>();

    private InputConnection inputConnection;
    private InputMethodManager inputMethodManagar;

    public CodeKeyboard(Context context) {
        this(context, null, 0);
    }

    public CodeKeyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CodeKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.keyboard_code, this, true);
        button1 = (Button) findViewById(R.id.button_1);
        button1.setOnClickListener(this);
        button2 = (Button) findViewById(R.id.button_2);
        button2.setOnClickListener(this);
        button3 = (Button) findViewById(R.id.button_3);
        button3.setOnClickListener(this);
        button4 = (Button) findViewById(R.id.button_4);
        button4.setOnClickListener(this);
        button5 = (Button) findViewById(R.id.button_5);
        button5.setOnClickListener(this);
        button0 = (Button) findViewById(R.id.button_0);
        button0.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                inputMethodManagar.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            }
        });
        buttonDelete = (Button) findViewById(R.id.button_delete);
        buttonDelete.setOnClickListener(this);
        buttonEnter = (Button) findViewById(R.id.button_enter);
        buttonEnter.setOnClickListener(this);

        keyValues.put(R.id.button_1, "findViewById()");
        keyValues.put(R.id.button_2, "speakOut()");
        keyValues.put(R.id.button_3, "\"\"");
        keyValues.put(R.id.button_4, ";");
        keyValues.put(R.id.button_5, "public void () {\n}");
        keyValues.put(R.id.button_enter, "\n");

        writeWithinBrackets.add("findViewById()");
        writeWithinBrackets.add("speakOut()");
        writeWithinBrackets.add("\"\"");
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
            if (writeWithinBrackets.contains(value)) {
                moveCursor(-1);
            }
            if (view.getId()==R.id.button_5) {
                moveCursor(-7);
            }
        }
    }

    private void moveCursor(int numOfChars) {
        ExtractedText extractedText = inputConnection.getExtractedText(new ExtractedTextRequest(), 0);
        int cursorPosition = extractedText.selectionStart;
        inputConnection.setSelection(cursorPosition+numOfChars, cursorPosition+numOfChars);
    }

    public void setInputConnection(InputConnection ic) {
        inputConnection = ic;
    }

    public void setInputMethodManagar(InputMethodManager inputMethodManagar) {
        this.inputMethodManagar = inputMethodManagar;
    }
}