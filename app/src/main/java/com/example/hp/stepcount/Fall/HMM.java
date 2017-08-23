package com.example.hp.stepcount.Fall;

import java.util.Arrays;

/**
 * Created by HP on 2017/1/10.
 */
public class HMM {

    public static double[][]transitionMatrix={{0.386579993382665,0.166692442718888,0.446727563898447},
            {0.108613366109935,0.837506708718509,0.0538799251715566},
            {1.24470355081331e-08,6.64115912381346e-18,0.999999987552964}},
            emissionMatrix={{0.0447317346526258,0.954320726196371,0.000947534692031852,1.35323041205900e-12,4.45761800179835e-09},
                    {0.999785743711636,0.000214256286405376,1.95880043629917e-12,7.89520555589155e-32,1.75102997720033e-19},
                    {8.30986608882873e-05,0.0421445878580840,0.336983301492071,0.163365530260940,0.457423481728017}};
    public static double[][] startMatrix={{0.215467028939723}, {0.784532971060277}, {3.27302953370115e-70}};

//	public static double[][] transitionMatrix = {{0.3, 0.7}, {0.1, 0.9}};
//	public static double[][] emissionMatrix = {{0.4, 0.6}, {0.5, 0.5}};
//	public static double[][] startMatrix = {{0.85}, {0.15}};

//	private static double[][] transitionMatrix = {{0.6, 0.4}, {0.3, 0.7}};
//	private static double[][] emissionMatrix = {{0.3, 0.4, 0.3}, {0.4, 0.3, 0.3}};
//	private static double[][] startMatrix = {{0.8}, {0.2}};

    private static int M = transitionMatrix[0].length;
    private static int N = emissionMatrix[0].length;

	/*
	 * double probsComputation(int[], int)
	 * 功能：计算输入观测序列与已知模型的匹配程度。
	 * 返回值：匹配概率值。
	 *
	 */

    public static double probsComputation(int[] observationSequences, int L){

        double[][] forwardProbs = new double[M][L];
        double conditionalProbs = 0;

        for (int i = 0; i < N; i++){
            if (observationSequences[0] == i){
                for (int j = 0; j < M; j++){
                    // forwardProbs[j][0] = DoubleUtil.mul(startMatrix[j][0], emissionMatrix[j][i]);
                    forwardProbs[j][0] = startMatrix[j][0] * emissionMatrix[j][i];
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

        // conditionalProbs = DoubleUtil.divide(forwardProbs[M - 1][L - 1], thr_temp, 4);
        conditionalProbs = forwardProbs[M - 1][L - 1] / thr_temp;

        return conditionalProbs;
    }

	/*
	 * int[] obsExtraction(double[])
	 * 功能：提取输入序列中目标区段的观测序列。
	 * 返回值：观测序列。
	 * 注：
	 * 目标区段指，原始数据中最大值点前的一段时间（在此跌倒检测中，为0.5s）的数据。
	 */

    public static int[] obsExtraction(double[] rawdata){

        int maxIndex, minIndex;
        double max, min;

        maxIndex = 0;
        minIndex = 0;
        max = rawdata[0];
        min = rawdata[0];

        for (int i = 0; i < rawdata.length; i++){
            if (rawdata[i] > max){
                max = rawdata[i];
                maxIndex = i;
            }
        }

        for (int i = 0; i < rawdata.length; i++){
            if (rawdata[i] < min){
                min = rawdata[i];
                minIndex = i;
            }
        }

        double[] data = outputData(max, min, maxIndex, minIndex, rawdata);

        int[] O = outputObs(max, min, data);

        return O;
    }

	/*
	 * double[] outputData(double, double, int, int, double[])
	 * 功能：截取原始数据中最大值点前50点作为data，并做标准化处理。
	 * 返回值：50个点的data。
	 * 注：
	 * （1）50个点的返回值在此为固定值；
	 * （2）标准化的过程，最低点与最高点间距离为30个点距，30在此为固定值。
	 */

    private static double[] outputData(double max, double min, int maxIndex, int minIndex, double[] rawdata){

        int dis = 0;
        double unit = 0;
        double[] data = new double[50];

//		dis = 30 - (maxIndex - minIndex + 1);

        double[] temp_a = null;
        double[] temp_b = null;
        double[] temp_c = null;
        double[] temp_d = null;

        if (maxIndex < minIndex){
            for (int i = 0; i < 50; i++){
                data[i] = rawdata[maxIndex - 50 + 1 + i];
                System.out.println(data[i] + "****" + "data1");
            }
        }
        else{
            if (dis <= 0){
                for (int i = 0; i < 50; i++){
                    data[i] = rawdata[maxIndex - 50 + 1 + i];
                    System.out.println(data[i] + "****" + "data2");
                }
            }
            else{
                dis = 30 - (maxIndex - minIndex + 1);
                temp_a = new double[dis];
                temp_b = new double[maxIndex - minIndex + 1];
                temp_c = new double[20];
                temp_d = new double[30];

                // unit = DoubleUtil.divide(DoubleUtil.sub(max, min), (double)(dis + 1), 4);
                unit = (max - min) / (dis + 1);
                for (int i = 0; i < dis; i++){
                    // temp_a[i] = DoubleUtil.add(min, DoubleUtil.mul((double)(i + 1), unit));
                    temp_a[i] = min + unit * (i + 1);
                }
                for (int i = 0; i < maxIndex - minIndex + 1; i++){
                    temp_b[i] = rawdata[minIndex + i];
                }
                for (int i = 0; i < 20; i++){
                    temp_c[i] = rawdata[minIndex - 20 + i];
                }
                temp_d = merge(temp_b, temp_a);
                Arrays.sort(temp_d);

                data = merge(temp_c, temp_d);
                for (int i = 0; i < data.length; i++){
                    System.out.println(data[i] + " **** " + i);
                }
            }
        }

        return data;
    }

	/*
	 * int[] outputObs(double, double, double[])
	 * 功能：将data转换为观测序列。具体为每5点做平均，并将平均值转化为观测变量。
	 * 返回值：10点长度的观测变量。
	 *
	 */

    private static int[] outputObs(double max, double min, double[] data){

        double[] o = new double[10];
        int[] O = new int[10];

        double sum = 0;
        for (int i = 0; i < 10; i++){
            for (int j = i * 5; j < i * 5 + 5; j++){
                // sum = DoubleUtil.add(sum, data[j]);
                sum = sum + data[j];
            }
            // o[i] = DoubleUtil.divide(sum, 5.0, 2);
            o[i] = sum / 5;
            sum = 0;
            System.out.println(o[i] + " **** " + "o" + i);
        }

        // double range = DoubleUtil.divide(DoubleUtil.sub(max, min), 5.0, 2);
       double range = (max - min) / 5;
        for (int i = 0; i < 10; i ++){
            if (o[i] >= min & o[i] < min + range){
                O[i] = 0;
            }
            else if (o[i] > min + range & o[i] < min + range * 2){
                O[i] = 1;
            }
            else if (o[i] > min + range * 2 & o[i] < min + range * 3){
                O[i] = 2;
            }
            else if (o[i] > min + range * 3 & o[i] < min + range * 4){
                O[i] = 3;
            }
            else{
                O[i] = 4;
            }
            System.out.println(O[i] + "****" + "O" + i);
        }

        return O;
    }

	/*
	 * double[] merge(double[], double[])
	 * 功能：两个数组拼接。
	 * 返回值：数组拼接后的结果。
	 *
	 */

    private static double[] merge(double[] a1, double[] a2){

        double[] result = new double[a1.length + a2.length];

        System.arraycopy(a1, 0, result, 0, a1.length);
        System.arraycopy(a2, 0, result, a1.length, a2.length);

        return result;
    }

}