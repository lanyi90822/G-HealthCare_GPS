package com.example.hp.stepcount.SQLite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by HP on 2017/6/26.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    // 定义本地用户数据库的名称和版本
    private static final String DB_NAME = "healthcare.db";
    private static final int DB_VERSION = 1;
    static final String USER_TB_NAME = "tb_user";
    static final String SLEEP_TB_NAME = "tb_sleep";
    static final String ECGDIAG_TB_NAME = "tb_ecgdiag";
    //定义用户信息
    static final String USER_ID = "user_id";
    static final String USER_PSW = "user_psw";
    static final String USER_NAME = "user_name";
    static final String USER_PHONE = "user_phone";
    static final String USER_AGE = "user_age";
    static final String USER_HEIGH = "user_heigh";
    static final String USER_WEIGHT = "user_weight";
    //定义睡眠信息
    static final String SLEEP_ID = "sleep_id";
    static final String SLEEP_DATE = "sleep_date";
    static final String SLEEP_DURATION = "sleep_duration";
    static final String SLEEP_DEEP_DURATION = "sleep_deep_duration";
    static final String SLEEP_LOW_DURATION = "sleep_low_duration";
    static final String SLEEP_START_TIME = "sleep_start_time";
    static final String SLEEP_END_TIME = "sleep_end_time";
    static final String SLEEP_GETUP_TIMES = "sleep_getup_times";
    static final String SLEEP_ESTIMATE = "sleep_estimate";
    //定义心电诊断信息
    static final String ECGDIAG_ID = "ecgdiag_id";
    static final String ECGDIAG_DATE = "ecgdiag_date";
    static final String ECGDIAG_SIGN = "ecgdiag_sign";
    static final String ECGDIAG_RESULT = "ecgdiag_result";
    static final String ECGDIAG_SUGGEST = "ecgdiag_suggest";
    static final String ECGDIAG_ADDRESS = "ecgdiag_address";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(TAG, "DatabaseHelper Constructor");
    }

    /*
     * 数据库第一次创建时onCreate()方法会被调用
     * @see
     * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
     * .SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "DatabaseHelper onCreate");
        //创建用户信息表
        String sqlUser = "CREATE TABLE IF NOT EXISTS "
                + USER_TB_NAME
                + " (user_id TEXT PRIMARY KEY, user_psw TEXT, user_name TEXT, user_phone TEXT,"
                + "user_age TEXT, user_heigh TEXT, user_weight TEXT)";
        db.execSQL(sqlUser);
        //创建睡眠信息记录表
        String sqlSleep = "CREATE TABLE IF NOT EXISTS "
                + SLEEP_TB_NAME
                + " (sleep_id TEXT, sleep_date TEXT, sleep_duration INTEGER, sleep_deep_duration INTEGER, sleep_low_duration INTEGER,"
                + "sleep_start_time TEXT, sleep_end_time TEXT,  sleep_getup_times INTEGER, sleep_estimate TEXT,"
                + "PRIMARY KEY(sleep_id,sleep_date))"; //设置用户ID和时间为联合主键
        db.execSQL(sqlSleep);
        //创建心电诊断信息记录表
        String sqlecgdiag = "CREATE TABLE IF NOT EXISTS "
                + ECGDIAG_TB_NAME
                + " (ecgdiag_id TEXT, ecgdiag_date TEXT, ecgdiag_sign TEXT, ecgdiag_result TEXT, ecgdiag_suggest TEXT, ecgdiag_address TEXT,"
                + " PRIMARY KEY(ecgdiag_id, ecgdiag_date))";
        db.execSQL(sqlecgdiag);
    }

    /*
    * 如果DATABASE_VERSION值被改为别的数,系统发现现有数据库版本不同,即会调用onUpgrade
    * @see
    * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
    * .SQLiteDatabase, int, int)
    */
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sqlUser = "DROP TABLE IF EXISTS " + USER_TB_NAME;
        db.execSQL(sqlUser);
        String sqlSleep = "DROP TABLE IF EXISTS " + SLEEP_TB_NAME;
        db.execSQL(sqlSleep);
        String sqlecgdiag = "DROP TABLE IF EXISTS " + ECGDIAG_TB_NAME;
        db.execSQL(sqlecgdiag);
    }

    /**
     * 每次打开数据库之后首先被执行
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            try {
                db.execSQL("PRAGMA foreign_keys = ON;");
                Log.i(TAG, "foreign key constraints success!");
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
