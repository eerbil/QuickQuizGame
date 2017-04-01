package com.example.eliferbil.quickquiz.QuickQuiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.eliferbil.quickquiz.MenuFragment;
import com.example.eliferbil.quickquiz.R;

public class MainActivity extends AppCompatActivity implements MenuFragment.GameListListener, GameFragment.TransitionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void itemClicked(long id) {

        View fragmentContainer = findViewById(R.id.gameFragment);
        if (fragmentContainer != null) {
            changeDetailFragment(new GameFragment());
        } else {
            Intent intent = new Intent(this, PhoneGameActivity.class);
            intent.putExtra(PhoneGameActivity.SELECTION_ID_EXTRA_KEY, (int) id);
            startActivity(intent);
        }
    }

    private void changeDetailFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.gameFragment, fragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    public void startOverPressed() {
        finish();
    }

    @Override
    public void questionSelected(Question selected) {

    }

    @Override
    public void gameEnded() {

    }
}
