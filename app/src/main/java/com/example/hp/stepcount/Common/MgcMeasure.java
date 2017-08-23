package com.example.hp.stepcount.Common;

/**
 * Created by bin on 10/03/2017.
 */
public class MgcMeasure {
    static {
        //加载库文件
        System.loadLibrary("mgc_mobile");
    }

    private MgcMeasure() {

    }

    public static MgcMeasure getInstance() {
        return MgcMeasureHelper.instance;
    }

    static class MgcMeasureHelper {
        private static MgcMeasure instance = new MgcMeasure();
    }

    // devType == 0x21
    // name : 存储名称
    // Leadnum  = 1
    // sampling = 250
    // blocksize = 30
//    public native void Init_Default(short devType, String name, short leadNum, short sampling, short blocksize);
//
//    public native int PushData(int[] data_in, int nchan, int nsmp);
//
//    // 返回心率
//    public native short[] GetVitalSigns();    //心跳
//
//    public native double[] GetOutDataPrt();   //绘图数据
//
//    public native short[] GetReportInfo();  //  报告数据
//
//    public native short[] GetUploadDataAddr(); //上传数据指针
//
//    public native int GetUploadDataType(); //上传数据类型
//
//    public native int GetUploadWaningType(); //上传warning类型
//
//    public native void OnStop();
//
//    public native void Release();


}
