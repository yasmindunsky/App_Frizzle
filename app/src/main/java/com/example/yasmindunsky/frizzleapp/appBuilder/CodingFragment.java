package com.example.yasmindunsky.frizzleapp.appBuilder;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yasmindunsky.frizzleapp.R;


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

        return inflater.inflate(R.layout.fragment_coding, container, false);
    }

}
