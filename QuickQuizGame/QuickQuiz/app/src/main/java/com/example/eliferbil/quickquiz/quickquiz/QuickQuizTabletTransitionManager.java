package com.example.eliferbil.quickquiz.quickquiz;

import com.example.eliferbil.quickquiz.BackPressedListener;
import com.example.eliferbil.quickquiz.TabletActivity;

/**
 * Created by Ata on 2.4.2017.
 */
public class QuickQuizTabletTransitionManager implements GameFragment.TransitionManager, QuestionPresenter.TransitionManager, BackPressedListener, android.support.v4.app.FragmentManager.OnBackStackChangedListener {
    private static QuickQuizTabletTransitionManager ins;

    public static QuickQuizTabletTransitionManager get(TabletActivity mainActivity) {
        if (ins == null) {
            ins = new QuickQuizTabletTransitionManager(mainActivity);
        } else {
            ins.mainActivity = mainActivity;
        }
        return ins;
    }

    private TabletActivity mainActivity;
    private Question selectedQuestion;
    private BackPressedListener bpl;
    private Runnable nextOnBackStackChangedAction;

    private QuickQuizTabletTransitionManager(TabletActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void startOverPressed() {
        mainActivity.startOver();
    }

    @Override
    public void questionSelected(Question selected) {
        selectedQuestion = selected;
        mainActivity.pushDetailFragment(new QuestionFragment());
    }

    @Override
    public void gameEnded() {
        mainActivity.endGame();
    }

    @Override
    public Question getSuppliedQuestion() {
        return selectedQuestion;
    }

    @Override
    public void setQuestionToSend(Question question) {
        selectedQuestion = question;
    }

    @Override
    public void questionEnded() {
        mainActivity.popDetailFragment();
        nextOnBackStackChangedAction = new Runnable() {
            @Override
            public void run() {
               // ((GameFragment) mainActivity.getCurrentDetailFragment()).onQuestionEnded(selectedQuestion);
            }
        };
    }

    @Override
    public void setBackPressedListener(BackPressedListener bpl) {
        this.bpl = bpl;
    }

    @Override
    public boolean onBackPressed() {
        return bpl == null || bpl.onBackPressed();
    }

    @Override
    public void onBackStackChanged() {
        if (nextOnBackStackChangedAction != null) {
            nextOnBackStackChangedAction.run();
            nextOnBackStackChangedAction = null;
        }
    }
}
