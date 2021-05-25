package ru.ndevelop.reusersamsung.utils;



import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.ndevelop.reusersamsung.core.objects.Action;


public class Utils {
    public static String byteArrayToHexString(byte[] inarray) {
        int i;
        int in;
        String[] hex = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";
        int j = 0;
        if (inarray != null) {
            while(j < inarray.length) {
                in = inarray[j] & 255;
                i = in >> 4 & 15;
                out = out + hex[i];
                i = in & 15;
                out = out + hex[i];
                ++j;
            }
        }

        return out;
    }


    public static ArrayList<Action> getActionsFromString(String actionsString) {
        ArrayList<Action> result = new ArrayList<>();
        List<String> tempActions = Arrays.asList(actionsString.split("~"));
        //tempActions = tempActions.subList(0, tempActions.size() - 1);
        for(int i=0;i<tempActions.size();i++){
            String[] tempAction = tempActions.get(i).split("-");
            if(tempAction.length==4) {
                Action resultAction = new Action(ActionTypes.valueOf(tempAction[1]));
                resultAction.setStatus(tempAction[2].equals("1"));
                resultAction.setSpecialData(tempAction[3]);
                result.add(resultAction);
            }
            else if(tempAction.length==3){
                Action resultAction = new Action(ActionTypes.valueOf(tempAction[1]));
                resultAction.status = tempAction[2] == "1";
                result.add(resultAction);
            }

        }


        return result;
    }


    public static ArrayList<Action> getActionsList() {

        ArrayList<Action> resultArrayList = new ArrayList<>();
        ActionTypes[] var4 = ActionTypes.values();
        int var5 = var4.length;

        for(int var3 = 0; var3 < var5; ++var3) {
            ActionTypes i = var4[var3];
            resultArrayList.add(new Action(i));
        }

        return resultArrayList;
    }

    public static boolean checkCameraPermission(Context context) {
        String permission = Manifest.permission.CAMERA;
        int res = context.checkCallingOrSelfPermission(permission);
        return res == PackageManager.PERMISSION_GRANTED;
    }


}
