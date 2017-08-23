package com.example.hp.stepcount.SQLite;

import android.content.Context;

import com.example.hp.stepcount.Message.Sleep;

/**
 * Created by HP on 2017/6/26.
 */

public class SleepSQLiteOpertare {
    private DatabaseHelper mHelper;

    public SleepSQLiteOpertare(Context context){
        mHelper = new DatabaseHelper(context);
    }

    public boolean insertItem(Sleep Item, String id, String date) {
        return false;
    }

    public boolean updateItem(Sleep Item, String id, String date) {
        return false;
    }

    public Sleep getsleepItem(String id, String date) {
        return null;
    }
}
