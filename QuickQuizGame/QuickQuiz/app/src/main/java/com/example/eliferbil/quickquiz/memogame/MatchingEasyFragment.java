package com.example.eliferbil.quickquiz.memogame;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchingEasyFragment extends MatchingBaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(com.example.eliferbil.quickquiz.R.layout.fragment_matching_easy, container, false);
    }


    @Override
    public int getEdgeLength() {
        return 4;
    }

    @Override
    public int getTargetNum() {
        return 4;
    }

    @Override
    public int getNextLevelCode() {
        return MemoTransitionManager.MEDIUM;
    }
}
