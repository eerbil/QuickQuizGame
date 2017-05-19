package com.example.eliferbil.quickquiz.memogame;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eliferbil.quickquiz.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DifficultySelectorFragment extends Fragment {


    public DifficultySelectorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_difficulty_selector, container, false);
    }

}
