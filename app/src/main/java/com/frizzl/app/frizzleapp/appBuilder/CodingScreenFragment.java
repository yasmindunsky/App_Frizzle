package com.frizzl.app.frizzleapp.appBuilder;


import android.arch.lifecycle.ViewModelProviders;
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
import com.frizzl.app.frizzleapp.Utils;
import com.frizzl.app.frizzleapp.UserProfile;


/**
 * A simple {@link Fragment} subclass.
 */
public class CodingScreenFragment extends Fragment {

    private CodingScreenPresenter codingScreenPresenter;
    private DefinedFunctionsViewModel definedFunctionsViewModel;

    private CodeEditor codeEditor;
    private CodeKeyboard codeKeyboard;
    private ScrollView scrollView;
    private ConstraintLayout constraintLayout;

    public CodingScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_code, container, false);

        scrollView = view.findViewById(R.id.code_scroll);
        constraintLayout = view.findViewById(R.id.code_constraint_layout);

        Context context = getContext();
        codeKeyboard = new CodeKeyboard(context);
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
                if (UserProfile.user.getCurrentLevel() == Utils.POLLY_APP_LEVEL_ID) {
                    String code = s.toString();
                    boolean taskCompleted = false;
                    if (UserProfile.user.getCurrentAppTaskNum() == 2) {
                        int beforeName = code.indexOf("public void") + String.valueOf("public void").length();
                        int afterName = code.indexOf("(View view)");
                        if (afterName - beforeName > 2) {
                            taskCompleted = true;
                        }
                    } else if (UserProfile.user.getCurrentAppTaskNum() == 3) {
                        int beforeTextToSay = code.indexOf("speakOut\"") + String.valueOf("speakOut\"").length();
                        int afterTextToSay = code.indexOf("\");");
                        if (afterTextToSay - beforeTextToSay > 0){
                            taskCompleted = true;
                        }
                    }
                    if (taskCompleted){
                        AppBuilderActivity appBuilderActivity = (AppBuilderActivity) getActivity();
                        if (appBuilderActivity != null) appBuilderActivity.taskCompleted();
                    }
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

        definedFunctionsViewModel = ViewModelProviders.of(getActivity()).get(DefinedFunctionsViewModel.class);

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
        extractDefinedFunctionsAndUpdateViewModel();
    }

    private void extractDefinedFunctionsAndUpdateViewModel() {
        String code = getCode();
        definedFunctionsViewModel.clearFunctions();

        String functionIdentification = "public void";
        int index = code.indexOf(functionIdentification);
        while (index >= 0) {
            String substring = code.substring(index, code.length());
            int functionNameEnd = substring.indexOf("(");
            if (functionNameEnd > 0) {
                String function = code.substring(
                        index + functionIdentification.length() + 1,
                        index + functionNameEnd);
                definedFunctionsViewModel.addFunction(function.trim());
            }
            index = code.indexOf(functionIdentification, index + 1);
        }

    }
}
