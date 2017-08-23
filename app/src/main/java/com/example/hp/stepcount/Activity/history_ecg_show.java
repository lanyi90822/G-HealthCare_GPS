package com.example.hp.stepcount.Activity;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.hp.stepcount.Common.DataConvert;
import com.example.hp.stepcount.Message.ECGdiag;
import com.example.hp.stepcount.R;
import com.example.hp.stepcount.View.HistoryECGView;
import com.example.hp.stepcount.View.MapBackgroundView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class history_ecg_show extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    private TextView ecg_massage_show;
    private TextView ecg_dialog_result;
    private TextView ecg_dialog_suggestion;
    private ECGdiag mECGdiag;
    private SeekBar mSeekBar;
    public final String TAG = history_ecg_show.class.getSimpleName();
    public static final int MSG_UPDATE_DATA = 1;
    private HistoryECGView mHistoryECGView;
    private Handler handler;
    private boolean feedData = false;
    public static int capacity;
    private long mIntDataLen = 0;
    private MapBackgroundView mMapBGView;
    private int FrameLen;
    private int mMaxProgress;
    private int mCurProgress;
    private int mStartind;

    private ArrayList<Integer> ECG_BLOCK_1;
    private ArrayList<Integer> ECG_BLOCK_2;
    private ArrayList<Integer> ECG_BLOCK_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_ecg_show);
        viewinit();

        mStartind = 0;
        ECG_BLOCK_1 = new ArrayList<>();
        ECG_BLOCK_2 = new ArrayList<>();
        ECG_BLOCK_3 = new ArrayList<>();

        capacity = (int) getFileSize(new File(mECGdiag.getEaddress())); //获取文件数据长度
        Log.w(TAG, "onCreate" + " = " + capacity);
        mIntDataLen = capacity/24; //原值为4
        HanleInit();
        mMaxProgress = (int) mIntDataLen;
        mSeekBar.setMax(mMaxProgress);
        mSeekBar.setProgress(0);

    }

    private void viewinit() {
        ecg_massage_show = (TextView)findViewById(R.id.ecg_massage_show);
        ecg_dialog_result = (TextView)findViewById(R.id.ecg_dialog_result);
        ecg_dialog_suggestion = (TextView)findViewById(R.id.ecg_dialog_suggestion);
        mMapBGView = (MapBackgroundView)findViewById(R.id.backmin);
        mHistoryECGView = (HistoryECGView)findViewById(R.id.ecgview);
        mSeekBar = (SeekBar)findViewById(R.id.seek_bar);
        mSeekBar.setOnSeekBarChangeListener(this);
        if (history_message.temp_ECGdiag != null) {
            mECGdiag = history_message.temp_ECGdiag;
            ecg_massage_show.setText(mECGdiag.getEdate());
            ecg_dialog_result.setText(mECGdiag.getEresult());
            ecg_dialog_suggestion.setText(mECGdiag.getEsuggest());
        }
    }

    public static long getFileSize(File file)  {
        if (file == null) {
            return 0;
        }
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return size;
    }

    private void HanleInit(){
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case MSG_UPDATE_DATA:
                        int progress =(Integer) msg.obj;
                        synchronized (this) {
                            ShowHistoryData(progress);
                        }
                        break;
                }
            }
        };
    }

    @Override
    protected void onResume() {
        mHistoryECGView.setGridPixel(mMapBGView.getMapSpace());
        mHistoryECGView.setSeekBar(mSeekBar);
        if (FrameLen == 0) {
            FrameLen = mHistoryECGView.getViewContainDataCount();
        }
        Message message = new Message();
        message.what = MSG_UPDATE_DATA;
        message.obj = 0;
        handler.sendMessageDelayed(message, 100);
        super.onResume();
    }

    @Override
    protected void onPause() {
        feedData = false;
        super.onPause();
    }

    private void FeedStorePack(ArrayList<Integer> pack1, ArrayList<Integer> pack2, ArrayList<Integer> pack3,int start,int len){
        if (FrameLen == 0) {
            FrameLen = mHistoryECGView.getViewContainDataCount();
//            len = FrameLen*4;
            len = FrameLen * 24;
        }
        byte[] buf = new byte[len];
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            try {
                FileInputStream fis = new FileInputStream(mECGdiag.getEaddress());
                int skipbytes = (int)fis.skip(start);
                byte[] buffer1=new byte[4];
                byte[] buffer2=new byte[4];
                byte[] buffer3=new byte[4];

                int readByte = fis.read(buf,0,len);
                for(int i= 0;i<readByte;i+=24){
                    buffer1[0] = buf[i];
                    buffer1[1] = buf[i+1];
                    buffer1[2] = buf[i+2];
                    buffer1[3] = buf[i+3];
                    buffer2[0] = buf[i+4];
                    buffer2[1] = buf[i+5];
                    buffer2[2] = buf[i+6];
                    buffer2[3] = buf[i+7];
                    buffer3[0] = buf[i+8];
                    buffer3[1] = buf[i+9];
                    buffer3[2] = buf[i+10];
                    buffer3[3] = buf[i+11];
                    int data1 = DataConvert.bytesToInt(buffer1);
                    int data2 = DataConvert.bytesToInt(buffer2);
                    int data3 = DataConvert.bytesToInt(buffer3);

                    Log.w(TAG, "FeedStorePack" + " = " + data1 + " " + data2 + " " + data3);

                    pack1.add(data1);
                    pack2.add(data2);
                    pack3.add(data3);
                }
                fis.close();
            } catch (FileNotFoundException e) {
                Log.i(TAG,"FileNotFoundException ="+e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                Log.i(TAG,"IOException ="+e.toString());
                e.printStackTrace();
            }
        }

    }
    private void ShowHistoryData(int progress){
        if(ECG_BLOCK_1 == null){
            return;
        }
        float percent = (float)progress/(float)mMaxProgress;
        int start_ind = (int) (mIntDataLen*percent);
        ECG_BLOCK_1.clear();
        ECG_BLOCK_2.clear();
        ECG_BLOCK_3.clear();
//        testQRSPack(mProgressPack);
//        FeedStorePack(mProgressPack,start_ind*4,FrameLen*4);
        FeedStorePack(ECG_BLOCK_1,ECG_BLOCK_2,ECG_BLOCK_3,start_ind*24,FrameLen*24);

        mHistoryECGView.DrawData(ECG_BLOCK_1,ECG_BLOCK_2,ECG_BLOCK_3);
    }

    public void updateECGView(int msg,int progress){
        Message message = new Message();
        message.what = msg;
        message.obj = progress;
        handler.sendMessage(message);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        updateECGView(MSG_UPDATE_DATA,i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
