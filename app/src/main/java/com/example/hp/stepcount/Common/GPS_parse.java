package com.example.hp.stepcount.Common;

import android.util.Log;

/**
 * Created by HP on 2017/1/17.
 */
public class GPS_parse {
    private static int packetStat = 0;
    private static String latitude = "";
    private static String lotatude = "";
    private static int comma_count = 0;
    private static double[] gps_data = new double[2];
    private static final String TAG = "com.example.hp.stepcount.Common";

    public GPS_parse(){
        gps_data[0] = 0;
        gps_data[1] = 0;
    }

    public static double[] Get_GPS_Data(char[] data) {
        char[] data_tm = data;
        int len = data.length;
        for (int i = 0; i < len; i++) {
            double[] data_ = parseDataPacket(data_tm[i]);
            if (!(data_[0] == 0 || data_[1] == 0)) {

                gps_data = gps_data_get(data_);

            }
        }
        Log.w(TAG, "gps_data" + "=" + gps_data[0] + "   " + gps_data[1]);
        return gps_data;
    }

    private static double[] parseDataPacket(char data_char) {
        double[] data_temp = new double[2];
        data_temp[0] = 0;
        data_temp[1] = 0;

        switch (packetStat) {
            case 0:
                if (data_char == '$') {
                    latitude = "";
                    lotatude = "";
                    packetStat = 0;
                    comma_count = 0;

                    packetStat++;
                }
                break;
            case 1:
                if (data_char == 'G') {
                    packetStat++;
                }
                break;
            case 2:
                if (data_char == 'P') {
                    packetStat++;
                }
                break;
            case 3:
                if (data_char == 'G') {
                    packetStat++;
                }
                break;
            case 4:
                if (data_char == 'G') {
                    packetStat++;
                }
                break;
            case 5:
                if (data_char == 'A') {
                    packetStat++;
                }
                break;
            case 6:
                if (data_char == ',') {
                    comma_count++;
                } else {
                    if (comma_count == 2) {
                        latitude = latitude + String.valueOf(data_char);
                    }
                    if (comma_count == 4) {
                        lotatude = lotatude + String.valueOf(data_char);
                    }
                }
                break;
        }
        if (data_char == '*') {
            if (!(latitude == "" || lotatude == "")) {
                data_temp[0] = Double.valueOf(latitude);
                data_temp[1] = Double.valueOf(lotatude);
            }

            Log.w(TAG,"latitudelotatude" + "=" + latitude + "   " + lotatude);
          //  Log.w(TAG,"data_temp" + "=" + data_temp[0] + "   " + data_temp[1]);

            latitude = "";
            lotatude = "";
            packetStat = 0;
            comma_count = 0;


        }
        Log.w(TAG,"data_temp" + "=" + data_temp[0] + "   " + data_temp[1]);
        return data_temp;
    }

    private static double[] gps_data_get(double[] data){
        double[] data_gps = data;
        double[] data_get = new double[2];
        int[] data_temp = new int[2];

        data_temp[0] = (int)(data_gps[0]/100);
        data_temp[1] = (int)(data_gps[1]/100);

        data_get[0] = (data_gps[0] - data_temp[0]*100)/60 +data_temp[0];
        data_get[1] = (data_gps[1] - data_temp[1]*100)/60 +data_temp[1];

        return data_get;
    }



}
