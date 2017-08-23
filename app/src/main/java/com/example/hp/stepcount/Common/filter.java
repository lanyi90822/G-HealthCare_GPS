package com.example.hp.stepcount.Common;

/**
 * Created by HP on 2016/11/7.
 */
public class filter {


    private static float[] value_grave = new float[9];
    private static int fil_cou = 9;

    private static float[] value_grave1 = new float[11];
    private static int fil_cou1 = 11;

    private static int fil_cou2 = 11;
    private static double[] value_grave2 = new double[fil_cou2];


    private static double[] val = new double[2];
    private static double[] tem = new double[2];

    public void filter() {
        for (int i = 0; i < fil_cou; i++) {
            value_grave[i] = 0;
        }

        for (int i = 0; i < fil_cou1; i++) {
            value_grave1[i] = 0;
        }

        for (int i = 0; i < fil_cou2; i++) {
            value_grave2[i] = 0;
        }
        val[0] = 0;
        val[1] = 0;
        tem[0] = 0;
        tem[1] = 0;
    }

    //11点平均滤波
    public static float averagefilter9(float temp) {

        float value_temp = 0;
        value_grave1[fil_cou1 - 1] = temp;
        for (int i = 0; i < fil_cou1; i++) {
            value_temp = value_temp + value_grave1[i];
        }
        value_temp = value_temp / fil_cou1;
        value_grave1 = value_change(value_grave1, fil_cou1);

        return value_temp;
    }


    //11点平均滤波
    public static float averagefilter11(float temp) {

        float value_temp = 0;
        value_grave[fil_cou - 1] = temp;
        for (int i = 0; i < fil_cou; i++) {
            value_temp = value_temp + value_grave[i];
        }
        value_temp = value_temp / fil_cou;
        value_grave = value_change(value_grave, fil_cou);

        return value_temp;
    }

    //9点平均滤波
    public static double averagefilter_11(double temp) {

        double value_temp2 = 0;
        value_grave2[fil_cou2 - 1] = temp;

        for (int i = 0; i < fil_cou2; i++) {
            value_temp2 = value_temp2 + value_grave2[i];
        }
        value_temp2 = value_temp2 / fil_cou2;
        value_grave2 = value_change1(value_grave2, fil_cou2);

        return value_temp2;
    }


    private static float[] value_change(float val[], int len) {
        float temp[] = new float[len];
        for (int i = 0; i < len - 1; i++) {
            temp[i] = val[i + 1];
        }
        temp[len - 1] = val[0];

        return temp;
    }

    private static double[] value_change1(double val[], int len) {
        double temp[] = new double[len];
        for (int i = 0; i < len - 1; i++) {
            temp[i] = val[i + 1];
        }

        temp[len - 1] = val[0];

        return temp;
    }

    public static double value_normal(double data) {
        val[0] = val[1];
        val[1] = data;
        double diff = val[1] - val[0];
        if (diff > 0) {
            tem[1] = tem[0] + 160.0;
            tem[0] = tem[1];
        }
        if (diff < 0) {
            tem[1] = tem[0] - 160.0;
            tem[0] = tem[1];
        }
        return tem[1];
    }

    /**************************智慧衣数据滤波******************************/


    //为了让平滑效果好，我用了两次平滑每次都是11个点的，这样会把波峰值降低，但是整体趋势不变
    /**
     *@Title 二次平滑lvbo
     *@Description 对数据进行，两次平均滤波
     *@param data为原始数据； first_order为第一次滤波点数；second_order为第二次滤波点数；注：阶数只能是奇数不可以是偶数
     *@return 返回二次滤波后的序列
     */
    public double[] average_filter(double[] data,int first_order, int second_order){

        double[] temp = average_filter_st(data, first_order);

        return average_filter_st(temp, second_order);
    }

    /**
     *@Title 平滑滤波函数
     *@Description 对序列进行filter_order点的平滑滤波
     *@param filter_order为每次滤波点数；只能为奇数
     *@return 返回滤波后序列
     */
    private double[] average_filter_st(double[] data, int filter_order){
        double[] result = new double[data.length];
        System.arraycopy(data,0,data,0,data.length);
        for (int i=0;i<filter_order/2;i++){
            result[i] = data[i];
            result[data.length - i - 1] = data[data.length - i - 1];
        }
        for (int j=filter_order/2;j<data.length-filter_order/2;j++){
            result[j] = get_average_st(data, j-filter_order/2, j+filter_order/2);
        }

        return result;
    }

    //求平均数函数
    private double get_average_st(double[] data, int start, int end){
        double average = 0.0;
        for (int i=start;i<=end;i++){
            average += data[i];
        }
        return average / (double)(end - start + 1);
    }

}
