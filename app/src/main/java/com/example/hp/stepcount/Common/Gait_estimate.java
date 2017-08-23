package com.example.hp.stepcount.Common;

import android.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by HP on 2017/7/18.
 */

public class Gait_estimate {
    private static double[] filterdata;
    private static filter mfilter = new filter();

    private final static String TAG = "com.example.hp.stepcount.Common.Gait_estimate" ;
    public static double ASI_Gait_Calculate(ArrayList<Integer> rawdata){
        if (rawdata.size() < 1)
            return -1;
        double[] datatemp = new double[rawdata.size()];
        for (int i=0;i<rawdata.size();i++)
            datatemp[i] = (double) rawdata.get(i);
        filterdata = mfilter.average_filter(datatemp, 11, 11);
        ArrayList<Integer> troughpoint = get_trough(filterdata, 125, -8500, 0.2);
        ArrayList<Integer> zeropoint = get_zeropoint(troughpoint, -8192.0);
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        String p = decimalFormat.format(get_ASI(zeropoint));
 //       Log.w(TAG, "ASI_Gait_Calculate" + " = " + zeropoint.get(10));
        return Double.valueOf(p);
    }

    public static double ASI_Gait_Calculate(int[] rawdata){
        double[] datatemp = new double[rawdata.length];
        for (int i=0;i<rawdata.length;i++)
            datatemp[i] = (double) rawdata[i];
        filterdata = mfilter.average_filter(datatemp, 11, 11);
        ArrayList<Integer> troughpoint = get_trough(filterdata, 125, -8500, 0.2);
        ArrayList<Integer> zeropoint = get_zeropoint(troughpoint, -8192.0);
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        String p = decimalFormat.format(get_ASI(zeropoint));
        Log.w(TAG, "ASI_Gait_Calculate" + " = " + zeropoint.get(10));
        return Double.valueOf(p);
    }

    //波谷检测,计算波谷点的位置:limit为数值检测的上限（低于上限的点进行运算）;simple为采样率;time为最波谷最小时间间隔
    private static ArrayList<Integer> get_trough(double[] data, int simple, int limit, double time) {
        ArrayList<Integer> troughlist = new ArrayList<>();
        for (int i = 2; i < data.length - 2; i++) {
            if (data[i] < limit) {
                if (data[i] < data[i - 1] && data[i] < data[i - 2] && data[i] < data[i + 1] && data[i] < data[i + 2]) {
                    int listlen = troughlist.size();
                    if (listlen > 0) {
                        if (i - troughlist.get(listlen - 1) > (simple * time)) {
                            troughlist.add(i);
                        } else {
                            if (data[troughlist.get(listlen - 1)] > data[i]){
                                troughlist.set(listlen - 1, i);
                            }
                        }
                    } else {
                        troughlist.add(i);
                    }
                }
            }
        }
        return troughlist;
    }

    //根据波谷位置计算相邻零点位置间隔: list为波谷位置；zerovalue为指定零点的值
    private static ArrayList<Integer> get_zeropoint(ArrayList<Integer> list, double zerovalue) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < list.size() - 1; i++) {
            int localtemp = get_zerospace(list.get(i), list.get(i + 1), zerovalue);
            if (localtemp != -1) {
                result.add(localtemp);
            }
        }
        return result;
    }

    //根据波谷位置计算零点位置间隔
    private static int get_zerospace(int start, int end, double zerovalue) {
        ArrayList<Integer> result = new ArrayList<>();
        int local = -1;
        for (int i = start; i < end; i++) {
            if ((filterdata[i]<=zerovalue && filterdata[i + 1]>=zerovalue) || (filterdata[i]>=zerovalue && filterdata[i + 1]<=zerovalue)) {
                if (Math.abs(filterdata[i] - zerovalue) > Math.abs(filterdata[i + 1] - zerovalue)) {
                    result.add(i + 1);
                } else {
                    result.add(i);
                }
            }
        }
        if (result.size() > 1) {
            local = result.get(result.size() - 1) - result.get(0);
            return local;
        }
        return local;
    }

    //计算不对称系数
    private static double get_ASI(ArrayList<Integer> data){
        double leftaverage = 0;
        double rightaverage = 0;
        int leftcount = 0;
        int rightcount = 0;
        for (int i = 2; i < data.size()-1; i++) {
            if (i % 2 == 0) {
                leftaverage += data.get(i);
                leftcount++;
            } else {
                rightaverage += data.get(i);
                rightcount++;
            }
        }
        leftaverage /= (double) leftcount;
        rightaverage /= (double) rightcount;

        return 2 * Math.abs(leftaverage - rightaverage) / (leftaverage + rightaverage) * 100.0;
    }


}
