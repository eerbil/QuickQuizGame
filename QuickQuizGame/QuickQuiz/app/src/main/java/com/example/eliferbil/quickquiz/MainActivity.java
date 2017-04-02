package com.example.eliferbil.quickquiz;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.eliferbil.quickquiz.memogame.MatchingEasyFragment;
import com.example.eliferbil.quickquiz.memogame.MortalUser;
import com.example.eliferbil.quickquiz.memogame.PhoneMemoActivity;
import com.example.eliferbil.quickquiz.memogame.TabletMemoTransitionManager;
import com.example.eliferbil.quickquiz.quickquiz.Game;
import com.example.eliferbil.quickquiz.quickquiz.GameFragment;
import com.example.eliferbil.quickquiz.quickquiz.PhoneGameActivity;
import com.example.eliferbil.quickquiz.quickquiz.QuickQuizTabletTransitionManager;

public class MainActivity extends AppCompatActivity implements MenuFragment.GameListListener, TabletActivity, TransitionManager.Provider {

    BackPressedListener bpl;
    TransitionManager transitionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }

    @Override
    public void itemClicked(long id) {

        View fragmentContainer = findViewById(R.id.gameFragment);
        if (fragmentContainer != null) {


            Fragment nextFragment;
            switch ((int) id) {
                case 0:
                    QuickQuizTabletTransitionManager tm = QuickQuizTabletTransitionManager.get(this);
                    bpl = tm;
                    transitionManager = tm;
                    getSupportFragmentManager().addOnBackStackChangedListener(tm);
                    nextFragment = new GameFragment();
                    break;
                case 1:
                    transitionManager = TabletMemoTransitionManager.get(this);
                    Game game = Game.getInstance();
                    game.setUser(new MortalUser(game.getUser()));
                    nextFragment = new MatchingEasyFragment();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown Id");
            }

            pushDetailFragment(nextFragment);
        } else {
            Class<?> nextActivity;
            switch ((int) id) {
                case 0:
                    nextActivity = PhoneGameActivity.class;
                    break;
                case 1:
                    nextActivity = PhoneMemoActivity.class;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown Id");
            }
            Intent intent = new Intent(this, nextActivity);
            startActivity(intent);
        }
    }

    public void pushDetailFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.gameFragment, fragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void popDetailFragment() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public Fragment getCurrentDetailFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.gameFragment);
    }

    @Override
    public void startOver() {
        finish();
    }

    @Override
    public void onBackPressed() {

        if (bpl == null || bpl.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void endGame() {
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public <T extends TransitionManager> T provide() {
        return (T) transitionManager;
    }
}
