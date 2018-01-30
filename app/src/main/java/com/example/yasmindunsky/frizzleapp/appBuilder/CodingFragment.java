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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.kbiakov.codeview.CodeView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CodingFragment extends Fragment {

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
            final List<String> blueWords = Arrays.asList("public", "void", "int");

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                for (String word : blueWords) {
                    int index = s.toString().indexOf(word);

                    if (index >= 0) {
                        s.setSpan(new ForegroundColorSpan(Color.BLUE), index, index + word.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        });

        return view;
    }

}
