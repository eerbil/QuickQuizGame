package com.example.eliferbil.quickquiz;

/**
 * Created by Ata on 2.4.2017.
 */

public interface TransitionManager {
    interface Provider {
        <T extends TransitionManager> T provide();
    }
}
