package ua.in.zeusapps.acarsoy.services;

import android.content.SharedPreferences;

import ua.in.zeusapps.acarsoy.common.Const;

/**
 * Created by oleg on 26.08.2017.
 */

public class LocalDataService {

    private SharedPreferences mSharedPreferences;

    public LocalDataService(SharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }

    public void saveEmail(String email) {
        putData(Const.EXTRA_EMAIL, email);
    }

    public void savePassword(String password) {
        putData(Const.EXTRA_PASSWORD, password);
    }

    private void putData(String key, String value) {
        SharedPreferences.Editor ed = mSharedPreferences.edit();
        ed.putString(key, value);
        ed.apply();
    }

    public String getEmail() {
        return mSharedPreferences.getString(Const.EXTRA_EMAIL, null);
    }

    public String getPassword() {
        return mSharedPreferences.getString(Const.EXTRA_PASSWORD, null);
    }
}
