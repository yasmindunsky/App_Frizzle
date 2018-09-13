package com.frizzl.app.frizzleapp.appBuilder;


import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.frizzl.app.frizzleapp.CodeKeyboard;
import com.frizzl.app.frizzleapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CodingScreenFragment extends Fragment {

    private CodingScreenPresenter codingScreenPresenter;
    private CodeEditor codeEditor;
    private CodeKeyboard codeKeyboard;
    private LinearLayout linearLayout;

    public CodingScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_code, container, false);

        Context context = getContext();
        CodeKeyboard codeKeyboard = new CodeKeyboard(context);
        LinearLayout.LayoutParams keyboardLayoutParams = new
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        codeKeyboard.setLayoutParams(keyboardLayoutParams);
        codeEditor = new CodeEditor(context, codeKeyboard);
        LinearLayout.LayoutParams editorLayoutParams = new
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        codeEditor.setLayoutParams(editorLayoutParams);

        linearLayout = view.findViewById(R.id.codeLinearLayout);
        linearLayout.addView(codeEditor);
        linearLayout.addView(codeKeyboard);

        codingScreenPresenter = new CodingScreenPresenter(this);
        codingScreenPresenter.getAndPresentCode();

        return view;
    }

    public void showEmptyCode() {
        showCode(getResources().getString(R.string.initial_code));
    }

    public void showCode(String codeForPresenting) {
        codeEditor.setText(codeForPresenting);
    }
}
