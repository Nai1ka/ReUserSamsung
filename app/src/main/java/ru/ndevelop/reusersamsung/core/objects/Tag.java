package ru.ndevelop.reusersamsung.core.objects;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class Tag {
    String name = "";
    @PrimaryKey
    @NonNull
    String tagId = "";
    ArrayList<Action> actions = new ArrayList<Action>();
    boolean isExpanded = false;


    public Tag(String name, String tagId){
        this.name = name;
        this.tagId = tagId;
    }
    public String getName(){
        return name;
    }
    public String getTagId(){
        return tagId;
    }
    public ArrayList<Action> getActions(){
        return actions;
    }
    public boolean getIsExpanded(){
        return isExpanded;
    }
    public void setIsExpanded(boolean payload){
        isExpanded = payload;
    }
    public void setActions(ArrayList<Action> payload){
        actions = payload;
    }



}