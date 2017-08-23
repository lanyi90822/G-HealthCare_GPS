package com.example.hp.stepcount.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hp.stepcount.R;
import com.example.hp.stepcount.Service.stepcountservice;

import java.text.DecimalFormat;

public class Sport_monitor extends Activity {

    private static final String TAG = "com.example.lenovo.healthcaresystem.device_state";
    private TextView stepcount;
    private static final int  msg_STEP_CHANG = 0;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private TextView distance;
    private TextView energy;
    private Button data_clear;
    private Button step_stop;
    private boolean step_state = false;
    private stepcountservice mservice = new stepcountservice();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sp = getSharedPreferences("sp_demo", Context.MODE_PRIVATE);
        editor = sp.edit();

        Log.w(TAG, "msg_STEP_CHANG" + msg_STEP_CHANG);

        registerMReceiver();

        stepcount = (TextView)findViewById(R.id.step_count);
        distance = (TextView)findViewById(R.id.distance);
        energy = (TextView)findViewById(R.id.energy);
        data_clear = (Button)findViewById(R.id.data_clear);
        data_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepcountservice.step_count = 0;
                editor.putInt("step_count",0);
                editor.putFloat("person_energy", 0);
                editor.commit();
                stepcountservice.history_step_count = 0;
                distance.setText("0");
                energy.setText("0");
                stepcount.setText("0" + "步");
            }
        });
        step_stop = (Button)findViewById(R.id.step_stop);
        step_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                step_state = sp.getBoolean("step_state",false);
                if (step_state == false)
                {
//                    Intent intent = new Intent(Sport_monitor.this,stepcountservice.class);
//                    startService(intent);
                    editor.putBoolean("step_state", true);
                    editor.commit();
                    step_stop.setText("停止计步");
                   // data_clear.setEnabled(false);
                }else {

//                    Intent intent = new Intent(Sport_monitor.this,stepcountservice.class);
//                    stopService(intent);
                    editor.putBoolean("step_state",false);
                    editor.commit();
                    step_stop.setText("开始计步");
                    //data_clear.setEnabled(true);
                }
            }
        });


        View_Inite();
    }

    private void View_Inite(){
        int mstep = sp.getInt("step_count",0);
        float mmdistance = (float) ((0.65 * mstep)/1000);
        float mmenergy = sp.getFloat("person_energy",0);
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        distance.setText(decimalFormat.format(mmdistance));
        energy.setText(decimalFormat.format(mmenergy));
        stepcount.setText(String.valueOf(mstep) + "步");

        step_state = sp.getBoolean("step_state",false);
        if (step_state == true){
            step_stop.setText("停止计步");
            //data_clear.setEnabled(false);
        }else {
            step_stop.setText("开始计步");
            //data_clear.setEnabled(true);
        }



    }




    private android.os.Handler mmHandler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case msg_STEP_CHANG:
                    int mmstep = sp.getInt("step_count", 0);
                    //计算距离
                    float mmdistance = (float) ((0.65 * mmstep)/1000);
                    //计算能量
                    float mmenergy = sp.getFloat("person_energy", 0);
                    DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                    String p = decimalFormat.format(mmdistance);//format 返回的是字符串
                    String e = decimalFormat.format(mmenergy);
                    stepcount.setText(String.valueOf(mmstep) + "步");
                    distance.setText(p);
                    energy.setText(e);

                default:
                    return false;
            }
        }
    });


    //广播接收，接收来自MapService的消息、接收数据
    private BroadcastReceiver messageReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            } else if (stepcountservice.ACTION_STEP_CHANG.equals(action)){

                mmHandler.sendEmptyMessage(msg_STEP_CHANG);

            }
        }
    };

    private void registerMReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(stepcountservice.ACTION_STEP_CHANG);
        registerReceiver(messageReciver, filter); // Don't forget to unregister during onDestroy
    }

    private void unregisterMReceiver(){
        unregisterReceiver(messageReciver);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterMReceiver();
    }


}
