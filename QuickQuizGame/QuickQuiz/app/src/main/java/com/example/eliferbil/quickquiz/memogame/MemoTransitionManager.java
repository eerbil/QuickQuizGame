package com.example.eliferbil.quickquiz.memogame;

import com.example.eliferbil.quickquiz.TransitionManager;

/**
 * Created by Ata on 2.4.2017.
 */

interface MemoTransitionManager extends TransitionManager {

    int FINISH = 0;
    int EASY = 1;
    int MEDIUM = 2;
    int HARD = 3;

    void nextLevel(int levelCode);

}
