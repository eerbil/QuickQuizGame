package com.example.eliferbil.quickquiz.memogame;

import com.example.eliferbil.quickquiz.User;

/**
 * Created by Ata on 2.4.2017.
 */

public class MortalUser extends User {
    public static final int INIT_HEALTH = 4;
    private int health;

    public MortalUser(String username, int score, int health) {
        super(username, score);
        this.health = health;
    }

    public MortalUser(String username, int health) {
        super(username);
        this.health = health;
    }

    public MortalUser(String username) {
        this(username, INIT_HEALTH);
    }

    public MortalUser(User u) {
        super(u);
        this.health = INIT_HEALTH;
    }

    @Override
    public MortalUser newWithResetTransientStats() {
        MortalUser newUser = new MortalUser(super.newWithResetTransientStats());
        this.health = INIT_HEALTH;
        return newUser;
    }

    public int getHealth() {
        return health;
    }

    public void decreaseHealth(int i) {
        health -= i;
    }
}
