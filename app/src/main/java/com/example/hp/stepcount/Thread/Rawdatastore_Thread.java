package com.example.hp.stepcount.Thread;

import android.util.Log;

import com.example.hp.stepcount.Common.FileUtiles;
import com.example.hp.stepcount.Common.TimeUtils;
import com.example.hp.stepcount.Service.stepcountservice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by HP on 2016/12/21.
 */
public class Rawdatastore_Thread extends Thread{
    private static final String TAG = "com.example.hp.stepcount.Service.Rawdatastore_Thread";
    private static String mname;
    private static String path;
    private static File file;
    private static FileOutputStream outStream = null;
    public static boolean raw_recive_state = false;

    public Rawdatastore_Thread(String name){
        mname = name;
        path = TimeUtils.getString(System.currentTimeMillis(), "yyyy_MM_dd_HH_mm_ss");
        path = FileUtiles.DATA_PATH + path + mname + ".txt";
        file = new File(path);
        if (!file.exists()) {
            File dir = new File(file.getParent());
            dir.mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            outStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void run()
    {
        while (raw_recive_state){
            if (stepcountservice.Raw_data_get_state){
                stepcountservice.Raw_data_get_state = false;

                String str;
                if (stepcountservice.blueMethodchoose){
                    str = String.valueOf(stepcountservice.raw_data_temp[3]) + " " + String.valueOf(stepcountservice.raw_data_temp[4]) + " " + String.valueOf(stepcountservice.raw_data_temp[5]) + " " + String.valueOf(stepcountservice.raw_data_temp[6]) + "\n";
                }else {
                    str = String.valueOf(stepcountservice.gps_data[0]) + " " + String.valueOf(stepcountservice.gps_data[1])  + "\n";
                }
                Log.w(TAG,"srsrwsrs"+"="+str);
                try {
                    outStream.write(str.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        try {
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
