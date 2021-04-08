package ru.ndevelop.reusersamsung.utils;

import java.io.Serializable;

public class Action implements Serializable {
    ActionTypes actionType;
    boolean status = false;
    String specialData = "";

    public Action(ActionTypes actionType) {
        this.actionType = actionType;
    }
    public ActionTypes getActionType(){
        return actionType;
    }
    public boolean getStatus(){
        return status;
    }
    public String getSpecialData(){
        return specialData;
    }
    public void setSpecialData(String payload){
        specialData = payload;
    }
    public void setStatus(boolean payload){
        status = payload;
    }

}
