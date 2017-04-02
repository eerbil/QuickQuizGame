package com.example.eliferbil.quickquiz.quickquiz;

import android.content.Intent;
import android.os.Bundle;

import com.example.eliferbil.quickquiz.PhoneBaseActivity;
import com.example.eliferbil.quickquiz.R;
import com.example.eliferbil.quickquiz.ScoreActivity;

public class PhoneGameActivity extends PhoneBaseActivity implements GameFragment.TransitionManager {

    public static final String SELECTION_ID_EXTRA_KEY = "selectionId";
    private static final int DEFAULT_SELECTION_ID = -1;
    private static final int QUESTION_REQUEST = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_game);
        /*Intent callingIntent = getIntent();

        if (callingIntent.getIntExtra(SELECTION_ID_EXTRA_KEY, DEFAULT_SELECTION_ID) == 0) {
            //FrameLayout layout = (FrameLayout) findViewById(R.id.phoneGameFragment);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.phoneGameFragment, new GameFragment());
            //ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }*/
    }

    @Override
    public void startOverPressed() {
        onBackPressed();
    }

    @Override
    public void questionSelected(Question selected) {
        Intent intent = new Intent(this, PhoneQuestionActivity.class);
        intent.putExtra(PhoneQuestionActivity.QUESTION_KEY, selected);
        startActivityForResult(intent, QUESTION_REQUEST);
    }

    @Override
    public void gameEnded() {
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == QUESTION_REQUEST && resultCode == RESULT_OK && data != null) {
            Question q = data.getParcelableExtra(PhoneQuestionActivity.QUESTION_RESULT_KEY);
            GameFragment gf = (GameFragment) getSupportFragmentManager().findFragmentById(R.id.phoneQuestionFragment);
            gf.onQuestionEnded(q);

        }
    }
}
