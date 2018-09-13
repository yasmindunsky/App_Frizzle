package com.frizzl.app.frizzleapp.appBuilder;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
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
    private CodeKeyboard keyboard;
    private List<String> savedWords;
    private List<String> importantCommands;
    private int currentPosition = 0;

    public CodeEditor(Context context, CodeKeyboard keyboard) {
        super(context);
        this.setTextColor(context.getColor(R.color.codeGrey));
        this.setTextSize((float) 16);
        this.setLetterSpacing((float)0);
        this.setTypeface(ResourcesCompat.getFont(context, R.font.calibri_regular));

        this.keyboard = keyboard;

        savedWords = Arrays.asList("Button", "TextView");
        importantCommands = Arrays.asList("findViewById", "setText");

        init();

        colorText(this.getEditableText());
    }

    private void colorText(Editable editableText) {
        markQuotationMarks(editableText);
//        markCommands(editableText);
//        markSavedWords(editableText);
    }

    private void init() {
        // Set keyboard.
        this.setRawInputType(InputType.TYPE_CLASS_TEXT);
        this.setTextIsSelectable(true);
        InputConnection ic = this.onCreateInputConnection(new EditorInfo());
        keyboard.setInputConnection(ic);
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.setInputMethodManagar(imm);

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

    private void markQuotationMarks(Editable s) {
        // for quotation mark search
        int firstQuotationMark = -1;
        int secondQuotationMark = -1;
        int startPosition = 0;

        //quotation marks
        firstQuotationMark = (s.toString()).indexOf("\"", startPosition);
        if (firstQuotationMark >= 0) {
            secondQuotationMark = (s.toString()).indexOf("\"", firstQuotationMark + 1);

            s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.frizzle_orange)),
                    firstQuotationMark,
                    firstQuotationMark + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            currentPosition++;
        }

        if (secondQuotationMark > 0) {
            s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.frizzle_orange)), firstQuotationMark, secondQuotationMark + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            startPosition = secondQuotationMark + 1;
            secondQuotationMark = -1;
            currentPosition = 0;
        }
    }

    private void markSavedWords(Editable s) {

        for (String word : savedWords) {
            int index = s.toString().indexOf(word);

            while (index >= 0) {
                s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.frizzle_green)), index, index + word.length(),
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
}
