package com.example.hp.stepcount.Activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocationListener;
import com.example.hp.stepcount.Fall.Fall_Last;
import com.example.hp.stepcount.R;
import com.example.hp.stepcount.Service.stepcountservice;

import static com.example.hp.stepcount.R.drawable.analyze_ing;
import static com.example.hp.stepcount.R.drawable.back;

public class Step_analyze extends Activity {
    private static final String TAG = "com.example.hp.stepcount.Activity";
    public static final String ACTION_START_ANALYSE = "com.example.hp.stepcount.Activity.ACTION_START_ANALYSE";
    private static ImageButton start_analyze;
    private static TextView analyze_result;
    private static TextView state_show;
    private static TextView analyze_suggest;
    private static TextView step_them;
    private int j = 10;
    private Thread mThread = null;
    private final static int msg_hasrecieved = 0;
    private final static int msg_statechange = 1;
    private final static int msg_gaithasstart = 2;
    private final static int msg_gaithasstop = 3;
    private final static int msg_gaitariseerro = 4;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_analyze);
        registermReceiver();

        sp = getSharedPreferences("sp_demo", Context.MODE_PRIVATE);
        editor = sp.edit();

        view_init();
    }

    private void view_init(){
        analyze_result = (TextView)findViewById(R.id.analyze_result);
        state_show = (TextView)findViewById(R.id.state_show);
        analyze_suggest = (TextView)findViewById(R.id.analyze_suggest);
        step_them = (TextView)findViewById(R.id.step_them);
        start_analyze = (ImageButton)findViewById(R.id.start_analyze);
        start_analyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ACTION_START_ANALYSE);
                sendBroadcast(intent);
            }
        });

        float asi_data = sp.getFloat("gait_estimate_result",-1);
        if (asi_data != -1){
            state_show.setText("上次评估结果");
            ASI_result_show(asi_data);
        }


    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            while (j>0){
                mhandler.sendEmptyMessage(msg_hasrecieved);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                j--;
            }
            mhandler.sendEmptyMessage(msg_statechange);
            j = 10;
        }
    };

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (stepcountservice.ACTION_GAIT_STAET.equals(action))
                mhandler.sendEmptyMessage(msg_gaithasstart);
            if (stepcountservice.ACTION_GAIT_STOP.equals(action)){
                mhandler.sendEmptyMessage(msg_gaithasstop);
                Log.w(TAG, "onReceive_result" + " = " + "收到结束广播");
            }
            if (stepcountservice.ACTION_GAIT_ERRO.equals(action))
                mhandler.sendEmptyMessage(msg_gaitariseerro);
        }
    };

    private void registermReceiver(){
        IntentFilter mfilter = new IntentFilter();
        mfilter.addAction(stepcountservice.ACTION_GAIT_STAET);
        mfilter.addAction(stepcountservice.ACTION_GAIT_STOP);
        mfilter.addAction(stepcountservice.ACTION_GAIT_ERRO);
        registerReceiver(mReceiver,mfilter);
    }

    private void unregistermReceiver(){
        unregisterReceiver(mReceiver);
    }

    private android.os.Handler mhandler = new android.os.Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what) {
                case msg_hasrecieved:
                    analyze_result.setText(String.valueOf(j) + "秒");
                    break;
                case msg_statechange:
                    state_show.setText("步态评估结果");
                    step_them.setText("步态评价及建议:");
                    analyze_result.setText("正常");
                    analyze_suggest.setTextSize(15);
                    analyze_suggest.setText("步行过程中，双侧肢体用力均匀，步态平衡，状态正常，建议坚持锻炼，保持健康。");
                    start_analyze.setEnabled(true);
                    start_analyze.setBackgroundResource(R.drawable.start_analyze);
                    break;
                case msg_gaithasstart:
                    start_analyze.setBackgroundResource(R.drawable.analyze_ing);
                    state_show.setText("数据采集中");
                    analyze_result.setTextSize(40);
                    analyze_result.setText("— —");
                    start_analyze.setEnabled(false);
                    break;
                case msg_gaithasstop:
                    start_analyze.setBackgroundResource(R.drawable.start_analyze);
                    float ASI_Result = sp.getFloat("gait_estimate_result",-1);
                    Log.w(TAG, "onReceive_result" + " = " + "读取到结果" + ASI_Result);
                    state_show.setText("步态评估结果");
//                    step_them.setText("步态评价及建议:");
                    ASI_result_show(ASI_Result);
                    break;
                case msg_gaitariseerro:
                    state_show.setText("数据采集错误");
                    step_them.setText("步态评估错误提示:");
                    analyze_suggest.setText("数据采集过程出错，请检查智慧衣连接状态后重新测量！");
                    start_analyze.setBackgroundResource(R.drawable.start_analyze);
                    break;
                default:
                    break;

            }
        }
    };

    private void ASI_result_show(float ASI_Result){
        int asi_index = -1;
        if (ASI_Result == -1)
            asi_index = -1;
        if (ASI_Result <= 15)
            asi_index = 0;
        if (ASI_Result > 15 && ASI_Result <= 20)
            asi_index = 1;
        if (ASI_Result > 20 && ASI_Result <= 25)
            asi_index = 2;
        if (ASI_Result > 25 && ASI_Result <= 35)
            asi_index = 3;
        if (ASI_Result > 35)
            asi_index = 4;
        switch (asi_index){
            case -1:
                state_show.setText("数据采集出错");
                start_analyze.setEnabled(true);
                start_analyze.setBackgroundResource(R.drawable.start_analyze);
                Toast.makeText(getApplicationContext(),"数据采集失败，请检查原因并重新采集！",Toast.LENGTH_SHORT).show();
                break;
            case 0:
//                state_show.setText("步态评估结果");
                step_them.setText("步态评价及建议:");
                analyze_result.setTextSize(30);
                analyze_result.setText("正常");
                analyze_suggest.setTextSize(15);
                analyze_suggest.setText("肢体不对称系数为" + ASI_Result + "，步行过程中，双侧肢体用力均匀，步态平衡，状态正常，建议坚持锻炼，保持健康。");
                start_analyze.setEnabled(true);
                start_analyze.setBackgroundResource(R.drawable.start_analyze);
                break;
            case 1:
//                state_show.setText("步态评估结果");
                step_them.setText("步态评价及建议:");
                analyze_result.setTextSize(30);
                analyze_result.setText("异常");
                analyze_suggest.setTextSize(15);
                analyze_suggest.setText("肢体不对称系数为" + ASI_Result + "，步态稍有失衡，双侧肢体运动不对称程度较高，建议多加锻炼，定期去医院检查。");
                start_analyze.setEnabled(true);
                start_analyze.setBackgroundResource(R.drawable.start_analyze);
                break;
            case 2:
//                state_show.setText("步态评估结果");
                step_them.setText("步态评价及建议:");
                analyze_result.setTextSize(30);
                analyze_result.setText("异常");
                analyze_suggest.setTextSize(15);
                analyze_suggest.setText("肢体不对称系数为" + ASI_Result + "，步态失衡较重，双侧肢体运动不对称程度明显，建议去医院进行身体检查。");
                start_analyze.setEnabled(true);
                start_analyze.setBackgroundResource(R.drawable.start_analyze);
                break;
            case 3:
//                state_show.setText("步态评估结果");
                step_them.setText("步态评价及建议:");
                analyze_result.setTextSize(30);
                analyze_result.setText("异常");
                analyze_suggest.setTextSize(15);
                analyze_suggest.setText("肢体不对称系数为" + ASI_Result + "，步态明显失衡，双侧肢体运动严重不对称，建议去医院进行身体检查，多加护理。");
                start_analyze.setEnabled(true);
                start_analyze.setBackgroundResource(R.drawable.start_analyze);
                break;
            case 4:
                step_them.setText("步态评价及建议:");
                analyze_result.setTextSize(30);
                analyze_result.setText("异常");
                analyze_suggest.setTextSize(15);
                analyze_suggest.setText("肢体不对称系数为" + ASI_Result + "，步态明显失衡，双侧肢体运动严重不对称，建议去医院进行身体检查，多加护理。");
                start_analyze.setEnabled(true);
                start_analyze.setBackgroundResource(R.drawable.start_analyze);
                break;
            default:
                break;
        }
    }






    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregistermReceiver();
    }

}
