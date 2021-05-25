package ru.ndevelop.reusersamsung.core.interfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

import ru.ndevelop.reusersamsung.core.objects.Tag;

@Dao
public interface TagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addTag(Tag tag);

    @Query("DELETE FROM tag WHERE tagId = :id")
    void delete(String id);

    @Query("SELECT * FROM tag")
    List<Tag> getAllTags();

    @Query("SELECT * FROM tag WHERE tagId = :id")
    Tag getTagById(String id);



}
