package com.example.eliferbil.quickquiz.memogame;

import android.support.annotation.IdRes;

import static com.example.eliferbil.quickquiz.R.mipmap.barbados;
import static com.example.eliferbil.quickquiz.R.mipmap.czechrepublic;
import static com.example.eliferbil.quickquiz.R.mipmap.guam;
import static com.example.eliferbil.quickquiz.R.mipmap.haiti;
import static com.example.eliferbil.quickquiz.R.mipmap.indonesia;
import static com.example.eliferbil.quickquiz.R.mipmap.italy;
import static com.example.eliferbil.quickquiz.R.mipmap.peru;
import static com.example.eliferbil.quickquiz.R.mipmap.poland;
import static com.example.eliferbil.quickquiz.R.mipmap.portugal;
import static com.example.eliferbil.quickquiz.R.mipmap.russia;

/**
 * Created by Ata on 2.4.2017.
 */

public class Flag {
    private boolean isOpen;
    private Country country;

    public enum Country {
        BARBADOS(barbados), CZECHIA(czechrepublic), GUAM(guam),
        HAITI(haiti), INDONESIA(indonesia), ITALY(italy),
        PERU(peru), POLAND(poland), PORTUGAL(portugal), RUSSIA(russia);

        private final
        @IdRes
        int mipmap;

        Country(int mipmap) {
            this.mipmap = mipmap;
        }

        public int getMipmapId() {
            return mipmap;
        }
    }
}
