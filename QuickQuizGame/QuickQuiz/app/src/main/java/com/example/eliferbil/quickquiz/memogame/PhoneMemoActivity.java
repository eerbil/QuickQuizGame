package com.example.eliferbil.quickquiz.memogame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.example.eliferbil.quickquiz.PhoneBaseActivity;
import com.example.eliferbil.quickquiz.R;
import com.example.eliferbil.quickquiz.ScoreActivity;

public class PhoneMemoActivity extends PhoneBaseActivity implements MemoTransitionManager {

    public static final String LEVEL_CODE = "LEVEL_CODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_memo);
       /* Intent callingIntent = getIntent();
        android.app. Fragment fragment;
        switch (callingIntent.getIntExtra(LEVEL_CODE, EASY)) {
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

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();*/
    }

    @Override
    public void nextLevel(int levelCode) {
        Intent intent;
        switch (levelCode) {
            case FINISH:
                intent = new Intent(this, ScoreActivity.class);
                break;
            default:
                intent = new Intent(this, PhoneMemoActivity.class);
                intent.putExtra(LEVEL_CODE, levelCode);
                break;
        }
        startActivity(intent);
        finish();
    }
}
