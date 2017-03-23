package com.example.eliferbil.quickquiz;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void itemClicked(long id) {

        View fragmentContainer = findViewById(R.id.gameFragment);
        if (id==0) {
            if (fragmentContainer != null) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.gameFragment, new GameFragment());
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            } else {
                Intent intent = new Intent(this, PhoneGameActivity.class);
                intent.putExtra("selectionId", (int) id);
                startActivity(intent);
            }

        } else if (id==1) {
            if (fragmentContainer != null) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                //TODO: Wrong type???
                //ft.replace(R.id.gameFragment, new MatchingEasyFragment());
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            } else {
                Intent intent = new Intent(this, PhoneGameActivity.class);
                intent.putExtra("selectionId", (int) id);
                startActivity(intent);
            }
        }
    }
}
