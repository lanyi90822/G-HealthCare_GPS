package com.example.hp.stepcount.Fall;

import com.example.hp.stepcount.Common.filter;

/**
 * Created by HP on 2016/12/26.
 */
public class Fall_new {
    private final static String TAG = "com.example.hp.falltest.TAG";
    private static double[][] transitionMatrix = {{0.386579993382665, 0.166692442718888, 0.446727563898447},
            {0.108613366109935, 0.837506708718509, 0.0538799251715566},
            {1.24470355081331e-08, 6.64115912381346e-18, 0.999999987552964}},
            emissionMatrix = {{0.0447317346526258, 0.954320726196371, 0.000947534692031852, 1.35323041205900e-12, 4.45761800179835e-09},
                    {0.999785743711636, 0.000214256286405376, 1.95880043629917e-12, 7.89520555589155e-32, 1.75102997720033e-19},
                    {8.30986608882873e-05, 0.0421445878580840, 0.336983301492071, 0.163365530260940, 0.457423481728017}};
    private static double[] startMatrix = {0.215467028939723, 0.784532971060277, 3.27302953370115e-70};
    private static int M = transitionMatrix[0].length;
    private static int N = emissionMatrix[0].length;
    private static int date_temp_len = 500; //缓存区长度
    private static double[] data_temp_ = new double[date_temp_len];
    private static filter mfilter = new filter();
    private static double[] MaxMin;
    private static double[] Fifty_Point = new double[50];

    public Fall_new() {
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

           // System.arraycopy(data_temp_,(int)MaxMin[1]-49,Fifty_Point,0,50);
            for (int i = 0; i < 50; i++) {
                Fifty_Point[i] = data_temp_[((int) MaxMin[1]) - 49 + i];
            }
            marching_degree = FallDetection(Fifty_Point);
        }

        return marching_degree;
    }

    private static double FallDetection(double[] input) {
        int i, j;
        double marching_degree;
        double maxdata = MaxMin[0], mindata = MaxMin[2];
        double unit = (maxdata - mindata) / 5;
        double max_value, min_value;
        double[] ats;

        max_value = input[0];
        min_value = input[0];
        for (j = 0; j < 50; j++) {
            if (input[j] > max_value) {
                max_value = input[j];
            }
            if (input[j] < min_value) {
                min_value = input[j];
            }
        }
        if (max_value > mindata + unit * 2 && min_value < mindata + unit) {
            ats = AccCharacteristicSeriesExtraction(input, 10, 5, mindata, unit);
            marching_degree = probsComputation(ats, ats.length);
        } else {
            marching_degree = 0;
        }

        return marching_degree;
    }

    private static double[] AccCharacteristicSeriesExtraction(double[] input, int elements_num, int elements_length, double minInput, double unit)
    {
        int i, j;
        double[][] firinput = new double[10][5];
        double[] secinput = new double[10];
        for (i = 0; i < elements_num; i++) {
            for (j = 0; j < elements_length; j++) {
                firinput[i][j] = input[i * elements_length + j];
            }
            secinput[i] = 0;
            for (j = 0; j < firinput[i].length; j++) {
                secinput[i] += firinput[i][j];
            }
            secinput[i] /= firinput[i].length;
        }
        double[] thrinput = new double[10];

        for (i = 0; i < elements_num; i++) {
            if (secinput[i] >= minInput && secinput[i] < minInput + unit)
                thrinput[i] = 0;
            else if (secinput[i] >= minInput + unit && secinput[i] < minInput + unit * 2)
                thrinput[i] = 1;
            else if (secinput[i] >= minInput + unit * 2 && secinput[i] < minInput + unit * 3)
                thrinput[i] = 2;
            else if (secinput[i] >= minInput + unit * 3 && secinput[i] < minInput + unit * 4)
                thrinput[i] = 3;
            else if (secinput[i] >= minInput + unit * 4)
                thrinput[i] = 4;

        }

        return thrinput;
    }


    public static double probsComputation(double[] observationSequences, int L){

        double[][] forwardProbs = new double[M][L];
        double conditionalProbs = 0;

        for (int i = 0; i < N; i++){
            if (observationSequences[0] == i){
                for (int j = 0; j < M; j++){
                    // forwardProbs[j][0] = DoubleUtil.mul(startMatrix[j][0], emissionMatrix[j][i]);
                    forwardProbs[j][0] = startMatrix[j] * emissionMatrix[j][i];
                }
            }
        }

        double sec_temp = 0;
        for (int i = 1; i < L; i++){
            for (int j = 0; j < M; j++){
                for (int k = 0; k < N; k++){
                    if (observationSequences[i] == k){
                        for (int jj = 0; jj < M; jj++){
                            // sec_temp = DoubleUtil.add(sec_temp, DoubleUtil.mul(forwardProbs[jj][i - 1], DoubleUtil.mul(transitionMatrix[jj][j], emissionMatrix[j][k])));
                            sec_temp = sec_temp + forwardProbs[jj][i - 1] * transitionMatrix[jj][j] * emissionMatrix[j][k];
                        }
                        forwardProbs[j][i] = sec_temp;
                        sec_temp = 0;
                    }
                }
            }
        }

        double thr_temp = 0;
        for (int i = 0; i < M; i++){
            // thr_temp = DoubleUtil.add(thr_temp, forwardProbs[i][L - 1]);
            thr_temp = thr_temp + forwardProbs[i][L - 1];
        }
        conditionalProbs = forwardProbs[M - 1][L - 1] / thr_temp;

        return conditionalProbs;
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
