package com.example.hp.stepcount.Common;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by HP on 2016/9/20.
 */
public class FileUtiles {

    public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String PROGRAM_PATH = ROOT_PATH + File.separator + "HealthCare";
    public static final String DATA_PATH = PROGRAM_PATH + File.separator;
    //public static final String SUFFER      = ".data";

    public static final String SUFFER      = ".dat";

    public static void createPath(File f){
        if(!f.exists()){
            f.mkdirs();
        }
    }

    public static void checkFileOrCreate(File file){
        if(file.getParentFile() != null && !file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public static String getVersionName(Context context) {
        try {
            String pkName = context.getPackageName();
            return context.getPackageManager()
                    .getPackageInfo(pkName, 0).versionName;
        } catch (Exception e) {
        }
        return " ";
    }

    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }


}