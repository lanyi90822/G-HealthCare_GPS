package com.example.hp.stepcount.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSONObject;
import com.example.hp.stepcount.Http.DateUtils;
import com.example.hp.stepcount.Http.HttpTask;
import com.example.hp.stepcount.Http.JSONTask;
import com.example.hp.stepcount.Http.MagicClient;
import com.example.hp.stepcount.Http.MagicClientImpl;
import com.example.hp.stepcount.R;

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

public class ECGData extends Activity {
    public static final String ACTION_WORK_START = "com.example.lenovo.caseapp.Activity.MainMeauActivity.ACTION_WORK_START";
    public static final String ACTION_WORK_STOP = "com.example.lenovo.caseapp.Activity.MainMeauActivity.ACTION_WORK_STOP";
    private static final String TAG = "com.example.lenovo.caseapp.Activity.MainMeauActivity.TAG";
    private Button start;
    private Button stop;
    MagicClient client = new MagicClientImpl();
    private HashMap<String, Object> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecgdata);

        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        HttpURLsend(3);
//                    }
//                }).start();
                Intent stepchange2 = new Intent(ACTION_WORK_START);
                sendBroadcast(stepchange2);

            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stepchange1 = new Intent(ACTION_WORK_STOP);
                sendBroadcast(stepchange1);
            }
        });
    }

    public void HttpURLsend(int signalNum) {
        URL url = null;
        String user_id = "e82423d4ecbe4d36bed032c134d7073a";
        String target = "http://172.19.4.158:8080/homehealth/app/user/" + user_id + "/ecgsegment";
        short raw_short;
        StringBuilder temp = new StringBuilder();
        int statusCode = -1;
        InputStream in = null;
        BufferedReader rd = null;
        HttpURLConnection urlConn = null;

        byte[] raw = new byte[10 * 250 * 3];
        for (int i = 0; i < raw.length / 2; i++) {
            raw_short = (short) ((Math.sin(i * Math.PI / 90) + 1) * 50);
            raw[2 * i] = (byte) (raw_short & 0xff);
            raw[2 * i + 1] = (byte) (raw_short >> 8);
        }

        params = new HashMap<>();
        Date startTime = null;
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
        params.put("rawData", raw);
        params.put("type", 0);
        params.put("exceptionType", 32);
        params.put("curHeartrate", 60);

        try {
            StringBuffer paramStringBuffer = new StringBuffer();
            if (params != null && params.size() > 0) {
                JSONObject obj = new JSONObject(params);
                paramStringBuffer.append(obj.toJSONString());
            }
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

            //    doCookie(httpURLConnection);

            statusCode = urlConn.getResponseCode();
            if(statusCode == 200){
                in = urlConn.getInputStream();
            } else {
                in = urlConn.getErrorStream();
            }

            rd = new BufferedReader(new InputStreamReader(in));

            String line = null;
            while ((line = rd.readLine()) != null) {
                Log.w(TAG, "HttpURLsend" + " = " + line);
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
            if (urlConn != null){
                urlConn.disconnect();
            }

        }
    }
}
