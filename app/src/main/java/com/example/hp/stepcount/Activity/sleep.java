package com.example.hp.stepcount.Activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.hp.stepcount.Fall.Fall_Last;
import com.example.hp.stepcount.Fall.Fall_Test;
import com.example.hp.stepcount.R;

public class sleep extends Activity {

    private TextView analyze_result;
    private TextView time_show;
    private TextView sleep_length;
    private TextView deep_length;
    private TextView low_length;
    private TextView sleep_time;
    private TextView getup_time;
    private TextView getup_count;
    private ImageButton time_back;
    private ImageButton time_forward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        view_init();
    }

    private void view_init(){
        analyze_result = (TextView)findViewById(R.id.analyze_result);
        time_show = (TextView)findViewById(R.id.time_show);
        sleep_length = (TextView)findViewById(R.id.sleep_length);
        deep_length = (TextView)findViewById(R.id.deep_length);
        low_length = (TextView)findViewById(R.id.low_length);
        sleep_time = (TextView)findViewById(R.id.sleeped_time);
        getup_time = (TextView)findViewById(R.id.getup_time);
        getup_count = (TextView)findViewById(R.id.getup_count);
        time_back = (ImageButton)findViewById(R.id.time_back);
        time_forward = (ImageButton)findViewById(R.id.time_forward);
        time_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });
        time_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });



    }









}
