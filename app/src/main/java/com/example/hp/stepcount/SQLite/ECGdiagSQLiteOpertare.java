package com.example.hp.stepcount.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.hp.stepcount.Message.ECGdiag;

import java.util.ArrayList;

/**
 * Created by HP on 2017/7/29.
 */

public class ECGdiagSQLiteOpertare {
    private final static String TAG = ECGdiagSQLiteOpertare.class.getSimpleName();
    private DatabaseHelper mHelper;
    private SQLiteDatabase mdb;

    public ECGdiagSQLiteOpertare(Context context){
        mHelper = new DatabaseHelper(context);
    }

    //插入新纪录
    public boolean insertAccount(ECGdiag ecgdiag){
        Log.w(TAG, "insertAccount=" + " " + ecgdiag.getEid() + " " + ecgdiag.getEdate() + " " + ecgdiag.getEsign() +
                " " + ecgdiag.getEresult() + " " + ecgdiag.getEsuggest());
        if (!isExisteByIdandDate(ecgdiag.getEid(), ecgdiag.getEdate())) {
            //  以读写方式打开数据库，按照表定义插入数据
            mdb = mHelper.getReadableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.ECGDIAG_ID, ecgdiag.getEid());
            cv.put(DatabaseHelper.ECGDIAG_DATE, ecgdiag.getEdate());
            cv.put(DatabaseHelper.ECGDIAG_SIGN, ecgdiag.getEsign());
            cv.put(DatabaseHelper.ECGDIAG_RESULT, ecgdiag.getEresult());
            cv.put(DatabaseHelper.ECGDIAG_SUGGEST, ecgdiag.getEsuggest());
            cv.put(DatabaseHelper.ECGDIAG_ADDRESS, ecgdiag.getEaddress());
            if (mdb.insert(DatabaseHelper.ECGDIAG_TB_NAME, null, cv) != -1) {
                mdb.close();
                return true;
            }
            mdb.close();
            return false;
        } else {
            mdb = mHelper.getReadableDatabase();
            String sql = "update " + DatabaseHelper.ECGDIAG_TB_NAME + " set ecgdiag_sign=?, ecgdiag_result=?, ecgdiag_suggest=?, ecgdiag_address=? "
                    + "where " + DatabaseHelper.ECGDIAG_ID + "=? and " + DatabaseHelper.ECGDIAG_DATE + "=?";
            String[] obj = {ecgdiag.getEsign(), ecgdiag.getEresult(), ecgdiag.getEsuggest(), ecgdiag.getEaddress(), ecgdiag.getEid(), ecgdiag.getEdate()};
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
    }

    //通过用户ID获取所有用户记录
    public ArrayList<ECGdiag> selectById(String id){
        ArrayList<ECGdiag> mlist = null;
        mdb = mHelper.getReadableDatabase();
        String sql = "SELECT  * FROM " + DatabaseHelper.ECGDIAG_TB_NAME
                + " where " + DatabaseHelper.ECGDIAG_ID
                + " =?";
        String[] obj = {id};
        Cursor cursor = mdb.rawQuery(sql, obj);
        if (cursor != null) {
            mlist = new ArrayList<>();
            while (cursor.moveToNext()) {
                ECGdiag mecgdiag = new ECGdiag();
                mecgdiag.setEid(id);
                mecgdiag.setEdate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ECGDIAG_DATE)));
                mecgdiag.setEsign(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ECGDIAG_SIGN)));
                mecgdiag.setEresult(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ECGDIAG_RESULT)));
                mecgdiag.setEsuggest(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ECGDIAG_SUGGEST)));
                mecgdiag.setEaddress(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ECGDIAG_ADDRESS)));
                Log.w(TAG, "selectById" + " = " + mecgdiag.getEaddress());
                mlist.add(mecgdiag);
            }
        }
        mdb.close();
        return mlist;
    }

    //删除ID用户信息
    public boolean deleteID(ArrayList<ECGdiag> list){
        if (list!=null && list.size() > 0){
            mdb = mHelper.getReadableDatabase();
            try {
                for (int i=0;i<list.size();i++){
                    ECGdiag mtemp = list.get(i);
                    mdb.delete(DatabaseHelper.ECGDIAG_TB_NAME, " ecgdiag_id=? and ecgdiag_date=?" ,new String[]{mtemp.getEid(), mtemp.getEdate()});
                }
            }catch (Exception e){
                e.printStackTrace();
                mdb.close();
                return false;
            }
            mdb.close();
            return true;
        }
        return false;
    }

    //删除ID用户信息
    public boolean deleteID(ECGdiag data) {
        if (data != null) {
            mdb = mHelper.getReadableDatabase();
            try {
                mdb.delete(DatabaseHelper.ECGDIAG_TB_NAME, " ecgdiag_id=? and ecgdiag_date=?", new String[]{data.getEid(), data.getEdate()});
            } catch (Exception e) {
                e.printStackTrace();
                mdb.close();
                return false;
            }
            mdb.close();
            return true;
        }
        return false;
    }

    //通过用户ID和DATE数据更新
    public boolean updateAccount(ECGdiag ecgdiag) {
        mdb = mHelper.getReadableDatabase();
        String sql = "update " + DatabaseHelper.ECGDIAG_TB_NAME + " set ecgdiag_sign=?, ecgdiag_result=?, ecgdiag_suggest=?, ecgdiag_address=? "
                + "where " + DatabaseHelper.ECGDIAG_ID + "=? and " + DatabaseHelper.ECGDIAG_DATE + "=?";
        Object[] obj = {ecgdiag.getEsign(), ecgdiag.getEresult(), ecgdiag.getEsuggest(), ecgdiag.getEid(), ecgdiag.getEdate()};
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


    //判断此用户ID和DATE是否已经存在
    public boolean isExisteByIdandDate(String id, String date){
        mdb = mHelper.getReadableDatabase();
        String sql = "select * from " + DatabaseHelper.ECGDIAG_TB_NAME + " where ecgdiag_id=? and ecgdiag_date=?";
        String[] str_id = {id, date};
        Cursor cursor = mdb.rawQuery(sql, str_id);
        if (cursor != null){
            while (cursor.moveToNext()) {
                mdb.close();
                return true;
            }
        }
        mdb.close();
        return false;
    }

}
