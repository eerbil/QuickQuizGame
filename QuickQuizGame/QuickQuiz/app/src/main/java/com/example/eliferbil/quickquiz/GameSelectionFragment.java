package com.example.eliferbil.quickquiz;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eliferbil.quickquiz.memogame.DifficultySelectorFragment;
import com.example.eliferbil.quickquiz.memogame.MortalUser;
import com.example.eliferbil.quickquiz.memogame.PhoneMemoActivity;
import com.example.eliferbil.quickquiz.memogame.TabletMemoTransitionManager;
import com.example.eliferbil.quickquiz.quickquiz.Game;
import com.example.eliferbil.quickquiz.quickquiz.GameFragment;
import com.example.eliferbil.quickquiz.quickquiz.PhoneGameActivity;
import com.example.eliferbil.quickquiz.quickquiz.QuickQuizTabletTransitionManager;

import static android.R.attr.id;


/**
 * A simple {@link Fragment} subclass.
 */


public class GameSelectionFragment extends android.app.Fragment implements View.OnClickListener {
    // gameID == 0 for quickquiz 1 for memogame
    public int gameId = 0;
    public GameSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        gameId = getArguments().getInt("gameId");
        return inflater.inflate(R.layout.fragment_game_selection, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        listenClickFor(R.id.singlePlayer);
        listenClickFor(R.id.challenge);
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
        android.app.Fragment nextFragment;
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.singlePlayer:
                switch ((int) gameId) {
                    case 0:
                        nextFragment = new GameFragment();
                        ft.replace(R.id.content_frame, nextFragment, "visible_fragment");
                        break;
                    case 1:
                        nextFragment = new DifficultySelectorFragment();
                        ft.replace(R.id.content_frame, nextFragment, "visible_fragment");
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown Id");
                }
            case R.id.challenge:
                switch ((int) gameId) {
                    case 0:
                        nextFragment = new ChallengeFriendFragment();
                        ft.replace(R.id.content_frame, nextFragment, "visible_fragment");
                        break;
                    case 1:
                        nextFragment = new ChallengeFriendFragment();
                        ft.replace(R.id.content_frame, nextFragment, "visible_fragment");
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown Id");
                }
            default:
                break;
        }
        ft.addToBackStack(null);
        ft.setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}
