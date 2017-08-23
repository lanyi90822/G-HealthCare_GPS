package com.example.hp.stepcount.Common;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import com.example.hp.stepcount.Interface.BluetoothCB;

import java.util.Date;

/**
 * Created by HP on 2016/12/17.
 */
public class Data_parse {
    private static final String TAG = "com.example.hp.stepcount.Common.Data_parse";
    private static byte header = -91;  // header档,读数用
    private static int sequence = 0;
    private static int sum_1 = 0;
    private static int sum_2 = 0;
    private static int sum_3 = 0;
    private static int sum_4 = 0;
    private static int sum_5 = 0;
    private static int sum_6 = 0;
    private static int count = 0;
    private static int timeNow = 0;
    private static filter mfilter = new filter();

//    public static int[] Gesonser_parse(byte[] byteData) {
//        int[] G_temp_data = new int[7];
//        byte[] BluetoothTemp = byteData;
//        if (BluetoothTemp.length % 18 == 0 && BluetoothTemp.length != 0) {
//            for (int j = 0; j < BluetoothTemp.length / 18; j++) {
//                int tempSequence = From2ComplementtoUnsigned(BluetoothTemp[j * 18 + 2]) +
//                        From2ComplementtoUnsigned(BluetoothTemp[j * 18 + 3]) * 256;
//                if (BluetoothTemp[j * 18] == header && BluetoothTemp[j * 18 + 1] == header &&
//                        (tempSequence > sequence || sequence - tempSequence == 999)) {
//                    for (int k = 0; k < 2; k++) {
//                        combineLowHigh(BluetoothTemp, j, k);
//                        if (++count >= 2) {
//                            int temp_data = sum_4 * sum_4 + sum_5 * sum_5 + sum_6 * sum_6;
//                            G_temp_data[0] = sum_1;  //心电1
//                            G_temp_data[1] = sum_2;  //心电2
//                            G_temp_data[2] = sum_3;  //心电3
//                            G_temp_data[3] = sum_4;  //重力加速度1
//                            G_temp_data[4] = sum_5;  //重力加速度2
//                            G_temp_data[5] = sum_6;  //重力加速度3
//                            G_temp_data[6] = (int) mfilter.averagefilter11((float) Math.sqrt(temp_data));
//
//                            count = 0;
//                            sum_1 = 0;
//                            sum_2 = 0;
//                            sum_3 = 0;
//                            sum_4 = 0;
//                            sum_5 = 0;
//                            sum_6 = 0;
//                        }
//                    }
//                    sequence = tempSequence;
//                }
//            }
//        }
//        return G_temp_data;
//    }
static byte[] BluetoothTemp;
    static int[] G_temp_data;
    public static int[] Gesonser_parse(byte[] byteData) {
        BluetoothTemp = byteData;
        if (BluetoothTemp.length % 18 == 0 && BluetoothTemp.length != 0) {
            for (int j = 0; j < BluetoothTemp.length / 18; j++) { //循环没用只执行一次
                int tempSequence = From2ComplementtoUnsigned(BluetoothTemp[j * 18 + 2]) +
                        From2ComplementtoUnsigned(BluetoothTemp[j * 18 + 3]) * 256;
                if (BluetoothTemp[j * 18] == header && BluetoothTemp[j * 18 + 1] == header &&
                        (tempSequence > sequence || sequence - tempSequence == 999)) {
//                                gsensorMode(BluetoothTemp[j * 18 + 16], BluetoothTemp[j * 18 + 17]); //display G-sensor stat
                    for (int k = 0; k < 2; k++) {
                        combineLowHigh(BluetoothTemp, j, k);//combine low byte and high byte
                        if (++count >= 2) {
                            timeNow++;
                            //saveEMDRawData(sum_3);
        //                    if (timeNow % 2 == 0 ) { // downsample
                                G_temp_data = new int[7];
//                                G_temp_data[0] = sum_1;  //心电1
//                                G_temp_data[1] = sum_2;  //心电2
//                                G_temp_data[2] = sum_3;  //心电3
//                                G_temp_data[3] = sum_4;  //重力加速度1
//                                G_temp_data[4] = sum_5;  //重力加速度2
//                                G_temp_data[5] = sum_6;  //重力加速度3

                                int temp_data = sum_4 * sum_4 + sum_5 * sum_5 + sum_6 * sum_6;
                                G_temp_data[0] = sum_1;  //心电1
                                G_temp_data[1] = sum_2;  //心电2
                                G_temp_data[2] = sum_3;  //心电3
                                G_temp_data[3] = sum_4;  //重力加速度1
                                G_temp_data[4] = sum_5;  //重力加速度2
                                G_temp_data[5] = sum_6;  //重力加速度3
                                G_temp_data[6] = (int) mfilter.averagefilter11((float) Math.sqrt(temp_data));
    //                        }
                            count = 0;
                            sum_1 = 0;
                            sum_2 = 0;
                            sum_3 = 0;
                            sum_4 = 0;
                            sum_5 = 0;
                            sum_6 = 0;
                        }
                    }
                    sequence = tempSequence;
                }
            }
        }else {
            G_temp_data = new int[1];
        }
        return G_temp_data;
    }

    private static void combineLowHigh(byte[] temp, int j, int k) {
        int value_1 = 0;
        int value_2 = 0;
        int value_3 = 0;
        int value_4 = 0;
        int value_5 = 0;
        int value_6 = 0;

        value_1 = From2ComplementtoUnsigned(temp[j * 18 + k + 4]);
        value_2 = From2ComplementtoUnsigned(temp[j * 18 + k + 6]);
        value_3 = From2ComplementtoUnsigned(temp[j * 18 + k + 8]);

        value_4 = (temp[j * 18 + k + 10]);
        value_5 = (temp[j * 18 + k + 12]);
        value_6 = (temp[j * 18 + k + 14]);

        sum_1 = sum_1 + (value_1 << ((count % 2) * 8));
        sum_2 = sum_2 + (value_2 << ((count % 2) * 8));
        sum_3 = sum_3 + (value_3 << ((count % 2) * 8));
        sum_4 = sum_4 + (value_4 << ((count % 2) * 8));
        sum_5 = sum_5 + (value_5 << ((count % 2) * 8));
        sum_6 = sum_6 + (value_6 << ((count % 2) * 8));
    }

    public static int From2ComplementtoUnsigned(int data) {
        String binary = Integer.toBinaryString(data); // Decimal to Binary
        if (binary.length() > 8)
            binary = binary.substring(binary.length() - 8); // Binary to 8
        // digit.
        return Integer.parseInt(binary, 2);// Binary to Decimal.
    }
}
