package com.store.aurora;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Session {

    private SharedPreferences prefs;

    public Session(Context context){
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setUserId(int userId){
        prefs.edit().putInt("userId",userId).apply();

    }

    public int getUserId(){
        return prefs.getInt("userId",0);
    }
}
