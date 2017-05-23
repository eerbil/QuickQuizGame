package com.example.eliferbil.quickquiz.memogame;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eliferbil.quickquiz.R;
import com.example.eliferbil.quickquiz.TabletActivity;
import com.example.eliferbil.quickquiz.User;
import com.example.eliferbil.quickquiz.quickquiz.Game;

/**
 * A simple {@link Fragment} subclass.
 */
public class DifficultySelectorFragment extends Fragment implements View.OnClickListener {


    public DifficultySelectorFragment() {
        // Required empty public constructor
    }

    private TabletActivity getTabletActivity() {
        return (TabletActivity) getActivity();
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
        listenClickFor(R.id.fourbyfour, R.id.fivebyfive, R.id.sixbysix);
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
                break;
            case R.id.fivebyfive:
                nextFragment = new MatchingMediumFragment();
                break;
            case R.id.sixbysix:
                nextFragment = new MatchingHardFragment();
                break;
            default:
                throw new IllegalArgumentException("No such difficulty");
        }
        getTabletActivity().pushDetailFragment(nextFragment);
    }

}
