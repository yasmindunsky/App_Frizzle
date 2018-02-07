package com.example.yasmindunsky.frizzleapp.appBuilder;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.yasmindunsky.frizzleapp.R;

import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CodingFragment extends Fragment {

    EditText editText;

    public CodingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_coding, container, false);

        EditText codeEditor = view.findViewById(R.id.code);

        codeEditor.addTextChangedListener(new TextWatcher() {
            final List<String> savedWords = Arrays.asList("public", "void", "Button", "TextView");
            final List<String> importantCommands = Arrays.asList("findViewById", "setText");

            // for quotation mark search
            int firstQuotationMark = -1;
            int secondQuotationMark = -1;
            int startPosition = 0;
            int currentPosition = 0;

            // for words
            int startIndex = 0;


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // mark saved words such as 'public', 'return', 'Button'
                markSavedWords(s);

                // mark commands such as 'setText'
                markCommands(s);

                // mark any String in quotes
                markQuotationMarks(s);
            }

            private void markSavedWords(Editable s) {
                for (String word : savedWords) {
                    int index = s.toString().indexOf(word, startPosition);

                    if (index >= 0) {
                        s.setSpan(new ForegroundColorSpan(Color.BLUE), index, index + word.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        startPosition = index + word.length();
                    }
                }
            }

            private void markCommands(Editable s) {
                for (String word : importantCommands) {
                    int index = s.toString().indexOf(word, startPosition);

                    if (index >= 0) {
                        s.setSpan(new ForegroundColorSpan(Color.RED), index, index + word.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        startPosition = index + word.length();
                    }
                }
            }

            private void markQuotationMarks(Editable s) {
                //quotation marks
                firstQuotationMark = (s.toString()).indexOf("\"", startPosition);
                if(firstQuotationMark >= 0){
                    secondQuotationMark = (s.toString()).indexOf("\"", firstQuotationMark + 1);

                    s.setSpan(new ForegroundColorSpan(Color.GREEN), firstQuotationMark, firstQuotationMark + currentPosition + 1,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    currentPosition++;
                }

                if(secondQuotationMark > 0){
                    s.setSpan(new ForegroundColorSpan(Color.GREEN), firstQuotationMark, secondQuotationMark + 1,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    startPosition = secondQuotationMark + 1;
                    secondQuotationMark = -1;
                    currentPosition = 0;
                }
            }
        });

        editText = (EditText) view.findViewById(R.id.code);
        return view;
    }

    public String getCode() {
        return editText.getText().toString();
    }
}
