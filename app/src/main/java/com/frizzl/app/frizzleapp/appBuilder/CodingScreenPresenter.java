package com.frizzl.app.frizzleapp.appBuilder;


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

import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.UserProfile;

import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CodingScreenPresenter {

    private CodingScreenFragment codingScreenFragment;

    public CodingScreenPresenter(CodingScreenFragment codingScreenFragment) {
        this.codingScreenFragment = codingScreenFragment;
    }

    public String prepareCodeForPresenting(String currentCode) {
        currentCode = currentCode.replaceAll(";", ";\n");
        currentCode = currentCode.replaceAll("\\{", "{\n");
        return currentCode;
    }

    public void getAndPresentCode() {
        String currentCode = UserProfile.user.getJava();
        if (currentCode.equals("")) {
            codingScreenFragment.showEmptyCode();
        } else {
            String codeForPresenting = prepareCodeForPresenting(currentCode);
            codingScreenFragment.showCode(codeForPresenting);
        }
    }
    
}
