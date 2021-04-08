package ru.ndevelop.reusersamsung;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;

import ru.ndevelop.reusersamsung.repositories.PreferencesRepository;

public class App extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        App.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return App.context;
    }
}
