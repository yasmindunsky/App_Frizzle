package com.frizzl.app.frizzleapp.appBuilder;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import com.frizzl.app.frizzleapp.CodeKeyboard;
import com.frizzl.app.frizzleapp.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Noga on 12/09/2018.
 */

public class CodeEditor extends android.support.v7.widget.AppCompatEditText {
    private final CodeKeyboard keyboard;
    private final List<String> savedWords;
    private final List<String> importantCommands;
    private int currentPosition = 0;

    public CodeEditor(Context context, CodeKeyboard keyboard) {
        super(context);
        this.setTextColor(context.getColor(R.color.codeText));
        this.setTextSize(18f);
        this.setLetterSpacing(0.02f);
        this.setLineSpacing(0,1.5f);
        this.setTypeface(ResourcesCompat.getFont(context, R.font.calibri_regular));
        this.setPadding(36,36,36,36);
        this.setBackgroundResource(android.R.color.transparent);
        this.setLayoutDirection(LAYOUT_DIRECTION_LTR);
        setShowSoftInputOnFocus(false);

        this.keyboard = keyboard;

        savedWords = Arrays.asList("Button", "TextView");
        importantCommands = Arrays.asList("findViewById", "setText");

        init();

        colorText(this.getEditableText());
    }

    private void colorText(Editable editableText) {
        markQuotationMarks(editableText);
        markFunctionNames(editableText);
        markComments(editableText);
    }

    private void init() {
        // Set keyboard.
        if (keyboard != null) {
            this.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            this.setTextIsSelectable(true);
            InputConnection ic = this.onCreateInputConnection(new EditorInfo());
            keyboard.setInputConnection(ic);
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.setInputMethodManager(imm);
            setKeyboardVisibility(INVISIBLE);
            setOnFocusChangeListener((v, hasFocus) -> setKeyboardVisibility(hasFocus ? VISIBLE : INVISIBLE));
        }

        // Set coloring
        this.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentPosition = count;
            }

            @Override
            public void afterTextChanged(Editable s) {
                colorText(s);
            }
        });
    }

    public void setKeyboardVisibility(int visibility) {
        keyboard.setVisibility(visibility);
    }

    private void markQuotationMarks(Editable s) {
        int startPosition = 0;
        int firstQuotationMark = s.toString().indexOf("\"", startPosition);
        int secondQuotationMark;
        while (firstQuotationMark >= 0) {
            secondQuotationMark = (s.toString()).indexOf("\"", firstQuotationMark + 1);

            s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.codeSpeakOut)),
                    firstQuotationMark,
                    firstQuotationMark + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            currentPosition++;
            if (secondQuotationMark > 0) {
                s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.codeSpeakOut)), firstQuotationMark, secondQuotationMark + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                startPosition = secondQuotationMark + 1;
            } else {
                startPosition = firstQuotationMark + 1;
            }
            currentPosition = 0;
            firstQuotationMark = s.toString().indexOf("\"", startPosition);
        }
    }

    private void markFunctionNames(Editable s) {
        String functionIdentification = "function";
        int index = s.toString().indexOf(functionIdentification);
        while (index >= 0) {
            String subSequence = s.subSequence(index, s.length()).toString();
            int functionNameEnd = subSequence.indexOf("(");
            if (functionNameEnd > 0) {
                s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.codeFunction)),
                        index + functionIdentification.length(),
                        index + functionNameEnd,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            index = s.toString().indexOf(functionIdentification, index + 1);
        }
    }

    private void markComments(Editable s) {
        int startPosition = 0;
        int commentStart = s.toString().indexOf("//", startPosition);
        int commentEnd;
        while (commentStart >= 0) {
            commentEnd = (s.toString()).indexOf("\n", commentStart + 1);
            if (commentEnd < 0) commentEnd = s.length();
            s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.codeComment)),
                    commentStart,
                    commentEnd,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            startPosition = commentEnd + 1;
            if (startPosition > 0) {
                commentStart = s.toString().indexOf("//", startPosition);
            }
            else commentStart = -1;
        }
    }

    private void markSavedWords(Editable s) {
        for (String word : savedWords) {
            int index = s.toString().indexOf(word);

            while (index >= 0) {
                s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.codeFunction)), index, index + word.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                index = s.toString().indexOf(word, index + 1);
            }
        }
    }


    private void markCommands(Editable s) {
        for (String word : importantCommands) {
            int index = s.toString().indexOf(word);

            while (index >= 0) {
                s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.frizzle_blue)), index, index + word.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                index = s.toString().indexOf(word, index + 1);
            }
        }
    }

    public void setText(String text){
        super.setText(text);
        Editable editableText = getText();
        if (editableText != null) setSelection(editableText.length());
    }

}
