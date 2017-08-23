package com.example.hp.stepcount.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.hp.stepcount.Message.User;

import java.util.ArrayList;

/**
 * Created by HP on 2017/6/26.
 */

public class UserSQLiteOpertare {
    private static final String TAG = UserSQLiteOpertare.class.getCanonicalName();
    private DatabaseHelper mHelper;
    private SQLiteDatabase mdb;

    public UserSQLiteOpertare(Context context){
        mHelper = new DatabaseHelper(context);
//        mdb = mHelper.getReadableDatabase();
    }

    //新用户注册，插入新用户信息
    public boolean insertAccount(User user) {
        //  以读写方式打开数据库，按照表定义插入数据
        mdb = mHelper.getReadableDatabase();
//        SQLiteDatabase mdb = mHelper.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.USER_ID,user.getUid());
        cv.put(DatabaseHelper.USER_PSW,user.getUpsw());
        cv.put(DatabaseHelper.USER_NAME,user.getUname());
        cv.put(DatabaseHelper.USER_PHONE,user.getUphone());
        cv.put(DatabaseHelper.USER_AGE,user.getUage());
        cv.put(DatabaseHelper.USER_HEIGH,user.getUheigh());
        cv.put(DatabaseHelper.USER_WEIGHT,user.getUweight());
        if (mdb.insert(DatabaseHelper.USER_TB_NAME,null,cv) != -1){
            mdb.close();
            return true;
        }
        mdb.close();
        return false;
    }

    //通过用户ID数据更新
    public boolean updateAccount(User user) {
        mdb = mHelper.getReadableDatabase();
        String sql = "update " + DatabaseHelper.USER_TB_NAME + " set user_psw=?, user_name=?, user_phone=?,"
                + " user_age=?, user_heigh=?, user_weight=? where " + DatabaseHelper.USER_ID + " =?";
        Object[] obj = {user.getUpsw(), user.getUname(), user.getUphone(), user.getUage(), user.getUheigh(), user.getUweight(), user.getUid()};
        try {
            mdb.execSQL(sql, obj);
        } catch (Exception e){
            e.printStackTrace();
            mdb.close();
            return false;
        }
        mdb.close();
        return true;
    }

    //通过用户ID获取用户信息
    public User selectById(String id) {
        mdb = mHelper.getReadableDatabase();
        String sql = "select * from " + DatabaseHelper.USER_TB_NAME + " where user_id=?";
        String[] str_id = { id };
        Cursor result = mdb.rawQuery(sql, str_id);
        User user = new User();
        if (result.moveToFirst()) {
            user.setUid(id);
            user.setUpsw(result.getString(result.getColumnIndex(DatabaseHelper.USER_PSW)));
            user.setUname(result.getString(result.getColumnIndex(DatabaseHelper.USER_NAME)));
            user.setUphone(result.getString(result.getColumnIndex(DatabaseHelper.USER_PHONE)));
            user.setUage(result.getString(result.getColumnIndex(DatabaseHelper.USER_AGE)));
            user.setUheigh(result.getString(result.getColumnIndex(DatabaseHelper.USER_HEIGH)));
            user.setUweight(result.getString(result.getColumnIndex(DatabaseHelper.USER_WEIGHT)));
        }
        result.close();
        mdb.close();

        return user;
    }

    //判断此用户ID是否已经存在
    public boolean isExisteById(String id){
        mdb = mHelper.getReadableDatabase();
        String sql = "select * from " + DatabaseHelper.USER_TB_NAME + " where user_id=?";
        String[] str_id = { id };
        Cursor cursor = mdb.rawQuery(sql, str_id);
        while (cursor.moveToNext()) {
            mdb.close();
            return true;
        }
        mdb.close();
        return false;
    }

    //遍历所用用户ID
    public ArrayList<String> selectALLid(){
        mdb = mHelper.getReadableDatabase();
        ArrayList<String> result = new ArrayList<>();
        String sql = "select * from " + DatabaseHelper.USER_TB_NAME;
        Cursor cursor = mdb.rawQuery(sql, null);
        while (cursor.moveToNext()){
            result.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_ID)));
        }
        mdb.close();
        return result;
    }

    //删除ID用户信息
    public boolean deleteID(String id){
        mdb = mHelper.getReadableDatabase();
        try {
            mdb.delete(DatabaseHelper.USER_TB_NAME, " user_id=?" ,new String[]{ id });
        }catch (Exception e){
            e.printStackTrace();
            mdb.close();
            return false;
        }
        mdb.close();
        return true;
    }

}
