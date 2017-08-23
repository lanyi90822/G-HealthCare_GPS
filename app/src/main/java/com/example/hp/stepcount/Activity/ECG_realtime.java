package com.example.hp.stepcount.Activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.platform.comapi.map.L;
import com.example.hp.stepcount.R;
import com.example.hp.stepcount.Service.stepcountservice;
import com.example.hp.stepcount.View.ECGDrawSurface;
import com.example.hp.stepcount.View.MapBackgroundView;

import java.io.File;
import java.util.ArrayList;

public class ECG_realtime extends AppCompatActivity {
    private static final String TAG = "com.example.hp.stepcount.Activity.ECG_realtime.TAG";
    private Button ECG_Start;
    public static final String ACTION_BULE_START = "com.example.hp.stepcount.Activity.MainMeauActivity.ACTION_BULE_START";
    public static final String ACTION_BULE_STOP = "com.example.hp.stepcount.Activity.MainMeauActivity.ACTION_BULE_STOP";
    private static boolean ecg_start_stop = false;
    private ArrayList<Integer> ECGBlock1;
    private ArrayList<Integer> ECGBlock2;
    private ArrayList<Integer> ECGBlock3;
    private final static int msg_ecgchanged = 0;
    private final static int msg_ecghrdisplay = 1;
    private ECGDrawSurface mdrwaview;
    private MapBackgroundView mMapBGView;
    private TextView hr_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ecg_realtime);

        view_Init();
        registermReciver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mdrwaview.setGridPixel(mMapBGView.getMapSpace());
    }

    private void view_Init(){
        mMapBGView = (MapBackgroundView)findViewById(R.id.mapmin);
        mdrwaview = (ECGDrawSurface) findViewById(R.id.cardiograph3);

        hr_show = (TextView)findViewById(R.id.hr_show);
        ECG_Start = (Button)findViewById(R.id.ECG_Start);
        ECG_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ecg_start_stop){
                    ecg_start_stop = false;
                    ECG_Start.setText("开始");
                    Intent intent = new Intent(ACTION_BULE_STOP);
                    sendBroadcast(intent);
                }else {
                    ecg_start_stop = true;
                    ECG_Start.setText("结束");
                    Intent intent = new Intent(ACTION_BULE_START);
                    sendBroadcast(intent);
                }
            }
        });

    }



    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case msg_ecgchanged:
                    mdrwaview.ECGDataUpdate(ECGBlock1,ECGBlock2,ECGBlock3);
                    Log.w(TAG, "handleMessage+++" + "= 正在画图");
                    break;
                case msg_ecghrdisplay:
                    if (stepcountservice.real_heart_rate>60 && stepcountservice.real_heart_rate<180)
                        hr_show.setText(String.valueOf(stepcountservice.real_heart_rate));
                    break;
                default:
                    break;
            }
        }
    };

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (stepcountservice.ACTION_DATA_CHANGE.equals(action)){
                ECGBlock1 = new ArrayList<>();
                ECGBlock2 = new ArrayList<>();
                ECGBlock3 = new ArrayList<>();
                ECGBlock1.addAll(intent.getIntegerArrayListExtra("ECGBlock1"));
                ECGBlock2.addAll(intent.getIntegerArrayListExtra("ECGBlock2"));
                ECGBlock3.addAll(intent.getIntegerArrayListExtra("ECGBlock3"));

                Log.w(TAG, "recive_ecg_data=" + ECGBlock1.size() + "   " + ECGBlock1.get(ECGBlock1.size()-1));
                mHandler.sendEmptyMessage(msg_ecgchanged);
                mHandler.sendEmptyMessage(msg_ecghrdisplay);
            }
        }
    };

    private void registermReciver(){
        IntentFilter mfilter = new IntentFilter();
        mfilter.addAction(stepcountservice.ACTION_DATA_CHANGE);
        registerReceiver(mReceiver,mfilter);
    }

    private void unregistermReciver(){
        unregisterReceiver(mReceiver);
    }


    @Override
    protected void onStop() {
        Intent intent = new Intent(ACTION_BULE_STOP);
        sendBroadcast(intent);
        ecg_start_stop = false;
        ECG_Start.setText("开始");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(ACTION_BULE_STOP);
        sendBroadcast(intent);
        unregistermReciver();
        mMapBGView = null;
        mdrwaview = null;
        mHandler.removeCallbacksAndMessages(null);
        ecg_start_stop = false;
        super.onDestroy();
    }
}
