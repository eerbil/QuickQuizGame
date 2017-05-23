package com.example.eliferbil.quickquiz.memogame;

import android.support.annotation.IdRes;

import static com.example.eliferbil.quickquiz.R.mipmap.aruba;
import static com.example.eliferbil.quickquiz.R.mipmap.barbados;
import static com.example.eliferbil.quickquiz.R.mipmap.bulgaria;
import static com.example.eliferbil.quickquiz.R.mipmap.cameroon;
import static com.example.eliferbil.quickquiz.R.mipmap.chile;
import static com.example.eliferbil.quickquiz.R.mipmap.colombia;
import static com.example.eliferbil.quickquiz.R.mipmap.czechrepublic;
import static com.example.eliferbil.quickquiz.R.mipmap.egypt;
import static com.example.eliferbil.quickquiz.R.mipmap.eritrea;
import static com.example.eliferbil.quickquiz.R.mipmap.estonia;
import static com.example.eliferbil.quickquiz.R.mipmap.france;
import static com.example.eliferbil.quickquiz.R.mipmap.gambia;
import static com.example.eliferbil.quickquiz.R.mipmap.ghana;
import static com.example.eliferbil.quickquiz.R.mipmap.guam;
import static com.example.eliferbil.quickquiz.R.mipmap.haiti;
import static com.example.eliferbil.quickquiz.R.mipmap.indonesia;
import static com.example.eliferbil.quickquiz.R.mipmap.italy;
import static com.example.eliferbil.quickquiz.R.mipmap.lebanon;
import static com.example.eliferbil.quickquiz.R.mipmap.mali;
import static com.example.eliferbil.quickquiz.R.mipmap.malta;
import static com.example.eliferbil.quickquiz.R.mipmap.nedherlands;
import static com.example.eliferbil.quickquiz.R.mipmap.paraguay;
import static com.example.eliferbil.quickquiz.R.mipmap.peru;
import static com.example.eliferbil.quickquiz.R.mipmap.poland;
import static com.example.eliferbil.quickquiz.R.mipmap.portugal;
import static com.example.eliferbil.quickquiz.R.mipmap.russia;
import static com.example.eliferbil.quickquiz.R.mipmap.stvincent;
import static com.example.eliferbil.quickquiz.R.mipmap.suriname;
import static com.example.eliferbil.quickquiz.R.mipmap.venezuela;
import static com.example.eliferbil.quickquiz.R.mipmap.virginislands;

/**
 * Created by Ata on 2.4.2017.
 */

public class Flag {
    private OpenState state = OpenState.OPEN;
    private Country country;

    public Flag(Country c) {
        this.country = c;
    }

    public OpenState getState() {
        return state;
    }

    public Country getCountry() {
        return country;
    }

    public boolean sameCountry(Flag f) {
        return this.getCountry() == f.getCountry();
    }

    public void setState(OpenState state) {
        this.state = state;
    }

    public enum Country {
        BARBADOS(barbados), CZECHIA(czechrepublic), GUAM(guam),
        HAITI(haiti), INDONESIA(indonesia), ITALY(italy),
        PERU(peru), POLAND(poland), PORTUGAL(portugal), RUSSIA(russia),
        ARUBA(aruba), BULGARIA(bulgaria), CAMEROON(cameroon), CHILE(chile),
        COLOMBIA(colombia), EGYPT(egypt), ERITREA(eritrea), ESTONIA(estonia),
        FRANCE(france), GAMBIA(gambia), GHANA(ghana), LEBANON(lebanon),
        NETHERLANDS(nedherlands), PARAGUAY(paraguay), VENEZUELA(venezuela),
        SURINAME(suriname), MALI(mali), MALTA(malta), ST_VINCENT(stvincent),
        VIRGIN_ISLANDS(virginislands);

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

//    public class Country{
//
//    }

    public enum OpenState {
        OPEN, CLOSED, ELIMINATED;
    }
}
