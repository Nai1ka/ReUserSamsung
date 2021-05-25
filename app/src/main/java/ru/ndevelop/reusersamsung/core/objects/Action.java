package ru.ndevelop.reusersamsung.core.objects;

import java.io.Serializable;

import ru.ndevelop.reusersamsung.utils.ActionTypes;

public class Action implements Serializable {

    public ActionTypes actionType;
    public boolean status = false;
    public String specialData = "";

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
