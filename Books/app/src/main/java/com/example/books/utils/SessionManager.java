package com.example.books.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "Login";

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        // commit changes
        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void saveStringData(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getStringData(String key) {
        return pref.getString(key, "");
    }

    public void deleteStringData(String key) {
        editor.remove(key);
        editor.commit();
    }
}
