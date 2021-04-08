package ru.ndevelop.reusersamsung.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ru.ndevelop.reusersamsung.App;

public class PreferencesRepository {
    private static PreferencesRepository instance;
    private static SharedPreferences preferences;
    private Context mCxt;
    private PreferencesRepository(Context ctx) {
        this.mCxt = ctx;
        preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
    }
    public static PreferencesRepository getInstance(Context ctx) {
        if (instance == null) {
            instance = new PreferencesRepository(ctx.getApplicationContext());

        }
        return instance;
    }
    public void setIsNotFirstLaunch(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("FIRST_LAUNCH",false );
        editor.apply();
    }
    public boolean isFirstLaunch() {
        return preferences.getBoolean("FIRST_LAUNCH", true);
    }
}
