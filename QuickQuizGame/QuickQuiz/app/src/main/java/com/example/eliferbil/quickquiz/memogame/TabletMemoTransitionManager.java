package com.example.eliferbil.quickquiz.memogame;

import android.support.v4.app.Fragment;

import com.example.eliferbil.quickquiz.TabletActivity;


public class TabletMemoTransitionManager implements MemoTransitionManager {

    private static TabletMemoTransitionManager ins;

    public static TabletMemoTransitionManager get(TabletActivity mainActivity) {
        if (ins == null) {
            ins = new TabletMemoTransitionManager(mainActivity);
        } else {
            ins.mainActivity = mainActivity;
        }
        return ins;
    }

    private TabletActivity mainActivity;

    private TabletMemoTransitionManager(TabletActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void nextLevel(int levelCode) {
        Fragment fragment;
        switch (levelCode) {
            case EASY:
                fragment = new MatchingEasyFragment();
                break;
            case MEDIUM:
                fragment = new MatchingMediumFragment();
                break;
            case HARD:
                fragment = new MatchingHardFragment();
                break;
            default:
                throw new IllegalStateException("This shouldn't come here");
        }
        mainActivity.pushDetailFragment(fragment);
    }
}
