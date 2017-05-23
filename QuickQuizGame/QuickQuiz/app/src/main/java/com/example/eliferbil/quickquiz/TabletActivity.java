package com.example.eliferbil.quickquiz;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by Ata on 2.4.2017.
 */

public interface TabletActivity extends TransitionManager.Provider {
    void pushDetailFragment(Fragment fragment);

    void pushDetailFragment(Fragment fragment, boolean addToBackStack);

    void popDetailFragment();

    Fragment getCurrentDetailFragment();

    void startOver();

    void endGame();

    void setTransitionManager(TransitionManager tm);

    void setBackPressedListener(BackPressedListener bpl);

    void addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener listener);
}
