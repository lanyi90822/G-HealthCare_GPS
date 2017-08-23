package com.example.hp.stepcount.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.hp.stepcount.Common.filter;
import com.example.hp.stepcount.R;
import com.example.hp.stepcount.Service.stepcountservice;
import com.example.hp.stepcount.View.DrawChart;

public class drawdata extends Activity {
    private DisplayMetrics monitorsize;
    private int W, H;
    private DrawChart drawChart;
    private DrawChart drawChart1;
    private filter mfilter = new filter();
    private static final String TAG = "com.example.hp.stepcount.Service.drawdata";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawdata);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        monitorsize = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(monitorsize);
       // W = monitorsize.widthPixels * 71 / 80;
       // H = monitorsize.heightPixels * 18 / 100;
        W = monitorsize.widthPixels*9/10;
        H = monitorsize.heightPixels*4/5;
        drawChart = (DrawChart) findViewById(R.id.main_chart);
        drawChart1 = (DrawChart) findViewById(R.id.main_chart1);
        drawChart1.SetWH(W, H);
        drawChart.SetWH(W, H);
        drawChart1.redraw();
        drawChart.redraw();
        registerMReceiver();
    }


    private void registerMReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(stepcountservice.ACTION_LOCATE_CHANGE);
        registerReceiver(mmReceiver, filter); // Don't forget to unregister during onDestroy
    }
    private void unregisterMReceiver(){
        unregisterReceiver(mmReceiver);
    }

    private BroadcastReceiver mmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
           // Log.w(TAG, "action" + "=" + action);
            if (stepcountservice.ACTION_LOCATE_CHANGE.equals(action)){

                int block = intent.getIntExtra("temp_locate",0);
                Log.w(TAG, "ddddaaaa" + "=" + block);
             //   data_show.setText(String.valueOf(block));

                drawChart1.prepareLine2(block);
            }
        }
    };




    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterMReceiver();
    }
}
