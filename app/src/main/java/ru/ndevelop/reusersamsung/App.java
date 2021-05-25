package ru.ndevelop.reusersamsung;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.room.Room;

import ru.ndevelop.reusersamsung.repositories.AppDatabase;
import ru.ndevelop.reusersamsung.repositories.PreferencesRepository;

public class App extends Application {


    public static App instance;
    private AppDatabase database;



    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "tag").allowMainThreadQueries()
                .build();
    }


    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
