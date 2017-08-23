package com.example.hp.stepcount.Common;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by HP on 2016/9/22.
 */
public class Filestore {
    private static final String TAG = "com.example.hp.hardwareimitate.Utils.";

    public Filestore(){}


    public void savepersonfile(String str,String nam) {

        String path = TimeUtils.getString(System.currentTimeMillis(), "yyyy_MM_dd_HH_mm_ss");
        //path = FileUtils.DATA_PATH + File.separator + path + FileUtils.SUFFER;

        path = FileUtiles.DATA_PATH + path + nam + ".txt";

        Log.w(TAG, "path" + " = " + path);

        try {
            File file = new File(path);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(str.getBytes());
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    };

    public void data_store(String data,String path,boolean state_save){

        try {
            File file = new File(path);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(data.getBytes());
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();

        }





    }





    public void array_save(double[] array,String name)
    {
        String ar_temp = "";

        for (int i=0;i<array.length;i++)
            ar_temp = String.valueOf(array[i]) + "\n";

        savepersonfile(ar_temp,name);
    }



}
