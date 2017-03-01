package com.example.eliferbil.quickquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class UsernameActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);
        listenClickFor(R.id.start);

    }

    protected void listenClickFor(int... ids) {
        for (int id : ids) {
            findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:

                EditText usernameField = (EditText) findViewById(R.id.username);
                Game.getInstance().setUser(new User(usernameField.getText().toString()));

                Intent nextActivity = new Intent(this, GameActivity.class);
                startActivity(nextActivity);

                break;
            default:
                break;
        }
    }
}
