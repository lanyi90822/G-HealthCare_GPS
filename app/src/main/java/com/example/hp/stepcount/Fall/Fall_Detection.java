package com.example.hp.stepcount.Fall;

import com.example.hp.stepcount.Common.filter;

/**
 * Created by HP on 2016/12/12.
 */
public class Fall_Detection {
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


    public Fall_Detection() {
        for (int i = 0; i < data_temp_.length; i++)
            data_temp_[i] = 1;
    }

    public static double[] Fall_Remand(double data_raw)
    {

        double[] marching_degree = {0.00};
        double[] data_normalization;
        double da_temp = data_raw;

    //    da_temp = mfilter.averagefilter_11(data_raw);
       // da_temp = data_raw;
        Data_Storage(da_temp);

        MaxMin = Maxandmin(data_temp_);

        if (MaxMin[0] > 3 && MaxMin[2] < 0.6) {
            data_normalization = DataNormalization(data_temp_,30);
            marching_degree = FallDetection(data_normalization);
        }

        return marching_degree;
    }

    private static double[] FallDetection(double[] input)
    {
        int i,j;
        double maxdata = MaxMin[0], mindata = MaxMin[2];
        double unit = (maxdata - mindata) / 5;
        double[] data_temp = new double[50], marching_degree = new double[500];
        double max_value, min_value;
        double[] ats;

        for (i = 0; i < 500; i++) {
            marching_degree[i] = 0;
        }
        for (i = 0; i < 451; i++) {

            max_value = input[i];
            min_value = input[i];
            for (j = i; j < i + 50; j++) {
                data_temp[j - i] = input[j];
                if (input[j] > max_value) {
                    max_value = input[j];
                }
                if (input[j] < min_value) {
                    min_value = input[j];
                }
            }
            if (max_value > mindata + unit * 2 && min_value < mindata + unit) {
                ats = AccCharacteristicSeriesExtraction(data_temp, 10, 5, mindata, unit);
                marching_degree[i + 49] = probsComputation(ats, ats.length);
            } else {
                marching_degree[i + 49] = 0;
            }

        }

        return marching_degree;
    }


    public static double probsComputation(double[] observationSequences, int L){
        double[][] forwardProbs = new double[M][L];
        double conditionalProbs = 0;

        for (int i = 0; i < N; i++){
            if (observationSequences[0] == i){
                for (int j = 0; j < M; j++){
                    forwardProbs[j][0] = DoubleUtil.mul(startMatrix[j], emissionMatrix[j][i]);
                }
            }
        }
        double sec_temp = 0;
        for (int i = 1; i < L; i++){
            for (int j = 0; j < M; j++){
                for (int k = 0; k < N; k++){
                    if (observationSequences[i] == k){
                        for (int jj = 0; jj < M; jj++){
                            sec_temp = DoubleUtil.add(sec_temp, DoubleUtil.mul(forwardProbs[jj][i - 1], DoubleUtil.mul(transitionMatrix[jj][j], emissionMatrix[j][k])));
                        }
                        forwardProbs[j][i] = sec_temp;
                        sec_temp = 0;
                    }
                }
            }
        }

        double thr_temp = 0;
        for (int i = 0; i < M; i++){
            thr_temp = DoubleUtil.add(thr_temp, forwardProbs[i][L - 1]);
        }

        conditionalProbs = DoubleUtil.divide(forwardProbs[M - 1][L - 1], thr_temp, 4);

        return conditionalProbs;
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



    private static double[] DataNormalization(double[] input, int fixed_length) {
        int i, j;
        int length = input.length;
        double maxValue = MaxMin[0], minValue = MaxMin[2];
        int maxIndex = (int) MaxMin[1], minIndex = (int) MaxMin[3];

        int distance = maxIndex - minIndex + 1;

        if (minIndex >= maxIndex) {
            return input;
        } else if (distance >= fixed_length) {
            return input;
        } else {
            int difference = fixed_length - distance;
            double[] temp = new double[difference];
            for (i = 0; i < difference; i++) {
                temp[i] = minValue + (i + 1) * (maxValue - minValue) / (difference + 1);
            }

            double[] data_middle_part = new double[maxIndex - minIndex + 1 + difference];

            for (i = minIndex; i <= maxIndex; i++) {
                data_middle_part[i - minIndex] = input[i];
            }
            for (i = maxIndex - minIndex + 1; i < maxIndex - minIndex + 1 + difference; i++) {
                data_middle_part[i] = temp[i - maxIndex + minIndex - 1];
            }

            double tmp;
            for (i = 0; i < data_middle_part.length; i++) {
                for (j = i + 1; j < data_middle_part.length; j++) {
                    if (data_middle_part[j] < data_middle_part[i]) {
                        tmp = data_middle_part[j];
                        data_middle_part[j] = data_middle_part[i];
                        data_middle_part[i] = tmp;
                    }
                }
            }

            double[] data_final = new double[500];
            for (i = 0; i < minIndex; i++) {
                data_final[i] = input[i];
            }
            for (i = 0; i < data_middle_part.length; i++) {
                data_final[i + minIndex] = data_middle_part[i];
            }
            for (i = maxIndex + 1; i < input.length - difference; i++) {
                data_final[i + minIndex + data_middle_part.length - maxIndex - 1] = input[i];
            }

            return data_final;
        }
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
