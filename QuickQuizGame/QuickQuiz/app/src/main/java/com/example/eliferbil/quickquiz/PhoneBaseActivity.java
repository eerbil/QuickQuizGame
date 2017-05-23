package com.example.eliferbil.quickquiz;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Ata on 2.4.2017.
 */

public abstract class PhoneBaseActivity extends AppCompatActivity implements TransitionManager, TransitionManager.Provider {
    @Override
    public <T extends TransitionManager> T provide(Object tmUser) {
        return (T) this;
    }
}
