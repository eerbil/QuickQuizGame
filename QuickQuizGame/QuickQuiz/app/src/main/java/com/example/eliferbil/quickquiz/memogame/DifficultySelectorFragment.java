package com.example.eliferbil.quickquiz.memogame;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eliferbil.quickquiz.R;
import com.example.eliferbil.quickquiz.User;
import com.example.eliferbil.quickquiz.quickquiz.Game;

/**
 * A simple {@link Fragment} subclass.
 */
public class DifficultySelectorFragment extends Fragment implements View.OnClickListener {


    public DifficultySelectorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_difficulty_selector, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        listenClickFor(R.id.fourbyfour);
        listenClickFor(R.id.fivebyfive);
        listenClickFor(R.id.sixbysix);
    }

    protected void listenClickFor(int... ids) {
        for (int id : ids) {
            getView().findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Game game = Game.getInstance();
        User user;
        Fragment nextFragment;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.fourbyfour:
                nextFragment = new MatchingEasyFragment();
                ft.replace(R.id.content_frame, nextFragment, "visible_fragment");
                break;
            case R.id.fivebyfive:
                nextFragment = new MatchingMediumFragment();
                ft.replace(R.id.content_frame, nextFragment, "visible_fragment");
                break;
            case R.id.sixbysix:
                nextFragment = new MatchingHardFragment();
                ft.replace(R.id.content_frame, nextFragment, "visible_fragment");
                break;
            default:
                break;
        }
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

}
