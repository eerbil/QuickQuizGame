package com.example.eliferbil.quickquiz;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class PhoneGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_game);
        if(savedInstanceState.getInt("selectionId")==0){
            FrameLayout layout = (FrameLayout) findViewById(R.id.phoneGameFragment);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.phoneGameFragment, new GameFragment());
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }
    }
}
