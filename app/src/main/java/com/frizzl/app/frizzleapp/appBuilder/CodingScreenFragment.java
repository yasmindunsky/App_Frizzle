package com.frizzl.app.frizzleapp.appBuilder;


import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.frizzl.app.frizzleapp.CodeKeyboard;
import com.frizzl.app.frizzleapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CodingScreenFragment extends Fragment {

    private CodingScreenPresenter codingScreenPresenter;

    private CodeEditor codeEditor;

    public CodingScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_code, container, false);

        ScrollView scrollView = view.findViewById(R.id.code_scroll);
        ConstraintLayout constraintLayout = view.findViewById(R.id.code_constraint_layout);

        Context context = getContext();
        CodeKeyboard codeKeyboard = new CodeKeyboard(context);
        ConstraintLayout.LayoutParams keyboardLayoutParams = new
                ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        keyboardLayoutParams.bottomToBottom = R.id.code_constraint_layout;
        codeKeyboard.setLayoutParams(keyboardLayoutParams);
        codeKeyboard.setOrientation(LinearLayout.VERTICAL);

        codeEditor = new CodeEditor(context, codeKeyboard);
        codeEditor.setKeyboardVisibility(View.VISIBLE);
        ScrollView.LayoutParams editorLayoutParams = new
                ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        codeEditor.setLayoutParams(editorLayoutParams);
        codeEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // For temp testing
                String code = s.toString();
                boolean taskCompleted = codingScreenPresenter.isTaskCompleted(code);
                if (taskCompleted){
                    AppBuilderActivity appBuilderActivity = (AppBuilderActivity) getActivity();
                    if (appBuilderActivity != null) appBuilderActivity.taskCompleted();
                }
            }
        });

        scrollView.addView(codeEditor);
        ConstraintLayout.LayoutParams scrollViewLayoutParams = (ConstraintLayout.LayoutParams) scrollView.getLayoutParams();
        scrollViewLayoutParams.bottomToTop = codeKeyboard.getId();
        scrollView.setLayoutParams(scrollViewLayoutParams);
        constraintLayout.addView(codeKeyboard);

        if (codingScreenPresenter != null) {
            codingScreenPresenter.getAndPresentCode();
        } else {
            Log.e("APP_BUILDER", "codingScreenPresenter was not set.");
        }


        return view;
    }


    public void setPresenter(CodingScreenPresenter codingScreenPresenter){
        this.codingScreenPresenter = codingScreenPresenter;
    }

    public void showEmptyCode() {
        showCode(getResources().getString(R.string.initial_code));
    }

    public void showCode(String codeForPresenting) {
        codeEditor.setText(codeForPresenting);
    }

    public String getCode() {
        String code = "";
        if (codeEditor != null && codeEditor.getText() != null){
            code = codeEditor.getText().toString();
        }
        return code;
    }

    @Override
    public void onPause() {
        super.onPause();
        codingScreenPresenter.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        codingScreenPresenter.onResume();
    }


}
