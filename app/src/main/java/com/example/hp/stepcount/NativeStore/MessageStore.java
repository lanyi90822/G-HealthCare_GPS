package com.example.hp.stepcount.NativeStore;

import android.content.SharedPreferences;

/**
 * Created by HP on 2017/2/15.
 */
public class MessageStore {

    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    public MessageStore(SharedPreferences spp){
        sp = spp;
        editor = sp.edit();
    }

    public static void StringStore(String dataname,String datavalue){
        editor.putString(dataname, datavalue);
        editor.commit();
    }

    public static String StringRead(String dataname){
        String name = sp.getString(dataname, "");
        return name;
    }


}
