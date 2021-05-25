package ru.ndevelop.reusersamsung.core.other;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ru.ndevelop.reusersamsung.core.objects.Action;

public class Converters {
    @TypeConverter
    public static ArrayList<Action> fromString(String value) {
        Type listType = new TypeToken<ArrayList<Action>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Action> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}