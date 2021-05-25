package ru.ndevelop.reusersamsung.repositories;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import ru.ndevelop.reusersamsung.core.interfaces.TagDao;
import ru.ndevelop.reusersamsung.core.objects.Tag;
import ru.ndevelop.reusersamsung.core.other.Converters;

@Database(entities = {Tag.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract TagDao getTagDao();

}