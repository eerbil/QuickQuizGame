package com.example.eliferbil.quickquiz;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eliferbil.quickquiz.memogame.DifficultySelectorFragment;
import com.example.eliferbil.quickquiz.quickquiz.Game;
import com.example.eliferbil.quickquiz.quickquiz.GameFragment;


/**
 * A simple {@link Fragment} subclass.
 */


public class GameSelectionFragment extends Fragment implements View.OnClickListener {
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
        listenClickFor(R.id.singlePlayer, R.id.challenge);
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
            case R.id.singlePlayer:
                switch (gameId) {
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
                break;
            case R.id.challenge:
                switch (gameId) {
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
                break;
            default:
                break;
        }
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}
