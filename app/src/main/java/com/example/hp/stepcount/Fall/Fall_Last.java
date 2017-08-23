package com.example.hp.stepcount.Fall;

/**
 * Created by HP on 2017/1/10.
 */
public class Fall_Last {
    private static int date_temp_len = 500; //缓存区长度
    private static double[] data_temp_ = new double[date_temp_len];
    private static double[] MaxMin;

    public Fall_Last() {
        for (int i = 0; i < data_temp_.length; i++)
            data_temp_[i] = 1;
    }

    public static double Fall_Remand(double data_raw)
    {

        double marching_degree = 0;
        double da_temp;

        //da_temp = mfilter.averagefilter_11(data_raw);
        da_temp = data_raw;
        Data_Storage(da_temp);

        MaxMin = Maxandmin(data_temp_);
        double mami_diff =  MaxMin[1] - MaxMin[3];

        if (MaxMin[0] > 3 && MaxMin[2] < 0.6 && mami_diff > 0 && MaxMin[1] >= 50) {

            int[] observationSequences = HMM.obsExtraction(data_temp_);

            marching_degree = HMM.probsComputation(observationSequences, observationSequences.length);


        }

        return marching_degree;
    }
    private static double[] Maxandmin(double[] raw_a) {
        double[] MN_stor = new double[4];
        MN_stor[0] = raw_a[0]; //储存最大值
        MN_stor[2] = raw_a[0]; //储存最小值

        for (int i = 0; i < raw_a.length; i++) {
            if (MN_stor[0] < raw_a[i]){
                MN_stor[0] = raw_a[i];
                MN_stor[1] = i;
            }
            if (MN_stor[2] > raw_a[i]){
                MN_stor[2] = raw_a[i];
                MN_stor[3] = i;
            }
        }

        return MN_stor;
    }
    private static void Data_Storage(double data_) {
        for (int i = 0; i < data_temp_.length - 1; i++) {
            data_temp_[i] = data_temp_[i + 1];
        }
        data_temp_[data_temp_.length - 1] = data_;
    }







}
