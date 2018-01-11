package com.example.yasmindunsky.frizzleapp.appBuilder;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yasmindunsky.frizzleapp.R;

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
        CodeView codeView = (CodeView) view.findViewById(R.id.code_view);
//        codeView.setCode(getString("public class CodingFragment"), "java");

        return view;
    }

}
