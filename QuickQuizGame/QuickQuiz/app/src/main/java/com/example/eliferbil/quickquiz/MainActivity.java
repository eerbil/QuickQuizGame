package com.example.eliferbil.quickquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.eliferbil.quickquiz.memogame.MatchingEasyFragment;
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
    public void itemClicked(long id) {

        View fragmentContainer = findViewById(R.id.gameFragment);
        if (fragmentContainer != null) {
            QuickQuizTabletTransitionManager tm = new QuickQuizTabletTransitionManager(this);
            bpl = tm;
            transitionManager = tm;
            getSupportFragmentManager().addOnBackStackChangedListener(tm);

            Fragment nextFragment = null;
            switch ((int) id) {
                case 0:
                    nextFragment = new GameFragment();
                    break;
                case 1:
                    nextFragment = new MatchingEasyFragment();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown Id");
            }
            pushDetailFragment(nextFragment);
        } else {
            Class<?> nextActivity = null;
            switch ((int) id) {
                case 0:
                    nextActivity = PhoneGameActivity.class;
                    break;
                case 1:

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
    public <T extends TransitionManager> T provide() {
        return (T) transitionManager;
    }
}
