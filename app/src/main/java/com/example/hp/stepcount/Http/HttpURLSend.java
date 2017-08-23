package com.example.hp.stepcount.Http;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by HP on 2017/6/6.
 */

public class HttpURLSend {
    private static String user_id = "e82423d4ecbe4d36bed032c134d7073a";
    private static String target = "http://172.19.4.158:8080/homehealth/app/user/" + user_id + "/ecgsegment";
    private HashMap<String,Object> params;
    private URL url = null;
    private Date startTime = null;
    private StringBuffer paramStringBuffer;

    int statusCode = -1;
    InputStream in = null;
    BufferedReader rd = null;
    HttpURLConnection urlConn = null;

    public HttpURLSend(){
        params = new HashMap<>();
    };


    public void addparams(int signalNum,short[] total_data) {
        try {
            startTime = DateUtils.parseDate("2017-02-08 14:00:39 001", DateUtils.SDF_yyyy_MM_dd_HH_mm_ss_SSS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        params.put("userId", user_id);
        params.put("channel", signalNum);
        params.put("measureStartTime", startTime.getTime());
        params.put("segmentTime", new Date());
        params.put("deviceMac", "00:11:22:AA:BB:CC");
        params.put("rawData", toByteArray(total_data));
        params.put("type", 0);
        params.put("exceptionType", 32);
        params.put("curHeartrate", 60);

        paramStringBuffer = new StringBuffer();
        if (params != null && params.size() > 0) {
            JSONObject obj = new JSONObject(params);
            paramStringBuffer.append(obj.toJSONString());
        }
    }

    public String Http_Data_send(){
        String result = "";

        try {

            url = new URL(target);
            urlConn = (HttpURLConnection) url.openConnection(); //创建一个http连接
            urlConn.setRequestMethod("POST"); //指定post请求方式
            urlConn.setDoInput(true); //向连接中写入数据
            urlConn.setUseCaches(false); //禁止缓存
            urlConn.setConnectTimeout(2 * 1000);
            urlConn.setReadTimeout(5 * 1000);
            //  addSessionID(urlConn);

            if (paramStringBuffer.length() > 0){
                byte[] data = paramStringBuffer.toString().getBytes();
                urlConn.setRequestProperty("Content-Type", "application/json"); //设置内容类型
                urlConn.setRequestProperty("Content-Length", data.length + "");// 注意是字节长度, 不是字符长度
                urlConn.setDoOutput(true); //从连接中读取数据
                DataOutputStream out = new DataOutputStream(urlConn.getOutputStream()); //获取输出流

                out.write(data);
                out.flush(); //输出缓存
                out.close();
            }
            urlConn.getOutputStream().flush();
            urlConn.getOutputStream().close();

            statusCode = urlConn.getResponseCode();
            if(statusCode == 200){
                in = urlConn.getInputStream();
            } else {
                in = urlConn.getErrorStream();
            }

            rd = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = rd.readLine()) != null) {
                result += line;
            }

        } catch (IOException e) {
            e.printStackTrace();
            statusCode = -1;
            //再次呼叫操作
        } finally {
            if (rd != null){
                try {
                    rd.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (urlConn != null) {
                urlConn.disconnect();
            }
        }
        return result;
    }

    public static byte[] toByteArray(short[] src) {

        int count = src.length;
        byte[] dest = new byte[count << 1];
        for (int i = 0; i < count; i++) {
            dest[2 * i] = (byte) (src[i] & 0xff);
            dest[2 * i +1] = (byte) (src[i] >> 8);
        }
        return dest;

    }









}
