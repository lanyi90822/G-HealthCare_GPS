package com.example.hp.stepcount.Service;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.hp.stepcount.Activity.ECGData;
import com.example.hp.stepcount.Activity.ECG_realtime;
import com.example.hp.stepcount.Activity.Fall_remand;
import com.example.hp.stepcount.Activity.MainMenu;
import com.example.hp.stepcount.Activity.Step_analyze;
import com.example.hp.stepcount.Activity.setting;
import com.example.hp.stepcount.Bluetooth.blueConnectThread;
import com.example.hp.stepcount.Bluetooth.blueConnectThreadG;
import com.example.hp.stepcount.Bluetooth.blueConnectedThread;
import com.example.hp.stepcount.Bluetooth.blueMethods;
import com.example.hp.stepcount.Common.DataConvert;
import com.example.hp.stepcount.Common.Data_store;
import com.example.hp.stepcount.Common.FileUtiles;
import com.example.hp.stepcount.Common.Gait_estimate;
import com.example.hp.stepcount.Common.TimeUtils;
import com.example.hp.stepcount.Common.filter;
import com.example.hp.stepcount.Data.data;
import com.example.hp.stepcount.Fall.Fall_Detection;
import com.example.hp.stepcount.Fall.Fall_Last;
import com.example.hp.stepcount.Fall.Fall_new;
import com.example.hp.stepcount.Http.DateUtils;
import com.example.hp.stepcount.Http.HttpTask;
import com.example.hp.stepcount.Http.HttpURLSend;
import com.example.hp.stepcount.Http.JSONTask;
import com.example.hp.stepcount.Http.MagicClient;
import com.example.hp.stepcount.Http.MagicClientImpl;
import com.example.hp.stepcount.Interface.BluetoothCB;
import com.example.hp.stepcount.Message.ECGdiag;
import com.example.hp.stepcount.NativeStore.MessageStore;
import com.example.hp.stepcount.R;
import com.example.hp.stepcount.SQLite.ECGdiagSQLiteOpertare;
import com.example.hp.stepcount.Stepcount.step;
import com.example.ecghr.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.baidu.mapapi.BMapManager.getContext;

public class stepcountservice extends Service implements BluetoothCB {
    private static final String TAG = "com.example.hp.stepcount.Service.stepcountservice";
    public static final String ACTION_STEP_CHANG = "com.example.lenovo.positioningsystem.stepcountservice.ACTION_STEP_CHANG";
    public static final String ACTION_LOCATE_CHANGE = "com.example.lenovo.positioningsystem.stepcountservice.ACTION_LOCATE_CHANGE";
    public static final String ACTION_GPS_CHANGE = "com.example.lenovo.positioningsystem.stepcountservice.ACTION_GPS_CHANGE";
    private float X_lateral = 0;
    private float Y_longitudinal = 0;
    private float Z_vertical = 0;
    private SensorManager sm;
    private double veluetemp;
    private filter mfilter = new filter();
    private SimpleDateFormat formatter;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    public static int step_count = 0;
    private NotificationManager notificationgmanager;

    private boolean M_data_get_state = false;
    private boolean G_data_get_state = false;
    private float temp_;
    private double temp_d;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00000");
    private Fall_Detection mFall_Detection = new Fall_Detection();
    public static boolean notifacation_state = true;

    private final static int msg_finddevice = 0;
    private final static int msg_notbondeddevice = 1;
    private final static int msg_connectdevice = 2;
    public final static int msg_manageconnected = 3;
    private final static int msg_phonework = 4;
    private final static int msg_blueonework = 5;
    private final static int msg_bluetwowork = 6;
    private final static int msg_bluethreework = 12;
    private final static int msg_bluefourwork = 15;
    private final static int msg_receivebluedata = 7;
    public final static int msg_hasrecieved = 8;
    private final static int msg_receivegps = 11;
    private final static int msg_energycomput = 13;
    private final static int msg_ecgdatastart = 14;
    private final static int msg_ecgdatastop = 9;
    private final static int msg_ecgdatasend = 10;
    private final static int msg_stepanalysestart = -1;
    private final static int msg_stepanalysestop = -2;
    private final static int msg_stepdatastore = -3;
    public final static int msg_locationon = -4;
    public final static int msg_locationoff = -5;
    public final static int msg_realecgshow = -6;

    private static int sampling_rate = 16;
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter;
    private static boolean BLUE_CONNECTED_state = false;
    // private static String BLUEDEVICE_ADTRESS = "34:81:F4:11:0C:14";
    // private static String BLUEDEVICE_NAME = "HOLUX GPSlim240";
    private static String BLUEDEVICE_NAME = "Dual-SPP";
    private static String BLUEDEVICE_ADTRESS = "null";
    private static BluetoothDevice DEVICE;
    private static final String strPin = "1234";
    private blueConnectedThread manageConnectedThread;
    private blueConnectThread manageConnectThread;
    private blueConnectThreadG manageConnectThreadG;
    public static boolean blueMethodchoose = true;
    private static boolean send_state = false;
    private static boolean phone_work_sate = true;
    private static double data_tem_; //智慧衣合重力加速度,g为单位
    public static int[] raw_data_temp;

    public static boolean Raw_data_get_state = false;
    public static String file_nam = "null";
    public static boolean data_store_OFF = true;
    public static boolean start_store_state = false;
    private static boolean gps_locate_state = false;
    private static boolean step_count_state = false;

    private static long last_step_time; //记录开始步行时间
    private static boolean step_process_state = true; //是否处于步行状态

    private static int step_total_count = 0;
    private static int[] step_count_one = new int[2];
    private static int[] step_count_two = new int[2];
    private static int count__ = 3;
    private static int step_count__ = 3;
    private static int gps_count_ = 5;
    private static int eenergy_count = 120; //步行能量消耗计算间隔，1分钟计算一次
    public static int history_step_count = 0; //历史步数缓存
    private static float personal_index = 0; //个人身体指数缓存
    private static float step_energy = 0; //不行消耗能量缓存
    private static Fall_new mFall_new = new Fall_new();
    private static int fall_count = 0;
    private blueMethods BLUE_METHODS = new blueMethods();
    private static Fall_Last mFall_Last = new Fall_Last();

    public static double[] gps_data = new double[2];

    private MessageStore mMessageStore;

    private static MediaPlayer alarmMusic = null;
    private static Vibrator vibrator = null;
    public static PowerManager.WakeLock wakeLock = null;
    LocationManager locationManager = null;

    private Thread ecgthread = null;
    private static boolean ecg_send_state = false;
    private static boolean ecg_ischange_state = true;

    private static final int total_num = 2500;
    private static short[] Ecg_data_1 = new short[total_num];
    private static short[] Ecg_data_2 = new short[total_num];
    private static short[] Ecg_data_3 = new short[total_num];
    private static short[] gson_data_1 = new short[total_num];
    private static short[] gson_data_2 = new short[total_num];
    private static short[] gson_data_3 = new short[total_num];
    private static short[] total_data;
    private static int data_num = 0;
    HttpURLSend mHttpURLSend;
    private gait_thread mgait_thread;
    public final static String ACTION_GAIT_STAET = "com.example.lenovo.positioningsystem.stepcountservice.ACTION_GAIT_STAET";
    public final static String ACTION_GAIT_STOP = "com.example.lenovo.positioningsystem.stepcountservice.ACTION_GAIT_STOP";
    public final static String ACTION_GAIT_ERRO = "com.example.lenovo.positioningsystem.stepcountservice.ACTION_GAIT_ERRO";
    public final static String ACTION_LOCATION_CHANG = "com.example.lenovo.positioningsystem.stepcountservice.ACTION_LOCATION_CHANG";
    public final static String ACTION_DATA_CHANGE = "com.example.hp.stepcount.ACTION_DATA_CHANGE";
    private int[] voice_data = {R.raw.ready, R.raw.wait, R.raw.start, R.raw.stopandwait, R.raw.stop};
    private static boolean gait_gravity_state = false;
    private static boolean gait_gravity_change = false;
    private static boolean ECG_GSENSER_STORE = false;
    private static ArrayList<Integer> gait_sensor_list;
    public static double[] location_data = new double[2];
    private ArrayList<Integer> ECGBlock1;
    private ArrayList<Integer> ECGBlock2;
    private ArrayList<Integer> ECGBlock3;
    private int ECGBlock_count = 0;
    public static int real_heart_rate = 0;
    private ECGdiagSQLiteOpertare msqlite;
    private DataStroreThread mDataStroreThread = null;

    public stepcountservice() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        variable_Init();
        CUP_wake();
        service_Init();
        Thread_Init();
        handler.postDelayed(runnable, 1000);
        registerBlueReceiver();
        registermReceiver();
        location_register();

    }

    private void Thread_Init() {
        if (!stepcountthread.isAlive()) {
            stepcountthread.start();   //开始计步线程
        }
        if (falldetecthread.isAlive()) {
            falldetecthread.start();   //开始跌倒检测线程
        }
    }


    private void CUP_wake() {  //保持CPU不休眠
        if (wakeLock == null) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, stepcountservice.class.getName());
            if (wakeLock != null)
                wakeLock.acquire();
        }
    }

    private void variable_Init() {
        notificationgmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //获取系统时间,设置时间格式
        formatter = new SimpleDateFormat("HHmm");
        sp = getSharedPreferences("sp_demo", Context.MODE_PRIVATE);
        editor = sp.edit();
        mMessageStore = new MessageStore(sp);
        msqlite = new ECGdiagSQLiteOpertare(getContext());
        step_energy = sp.getFloat("person_energy", 0); //获取个人运动消耗
        history_step_count = sp.getInt("step_count", 0);
        if (history_step_count != 0) {
            step_count = history_step_count;
        }
        step_count_one[0] = 0;
        step_count_two[0] = 0;

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void service_Init() {
        //定义一个notification
        Notification.Builder notify = new Notification.Builder(stepcountservice.this);
        notify.setSmallIcon(R.drawable.miao);
        notify.setContentTitle("老人健康监护"); //设置标题
        notify.setContentText("前台service"); //消息内容
        notify.setWhen(System.currentTimeMillis()); //发送时间
        // Intent intent = new Intent(stepcountservice.this, MainMenu.class);
        // PendingIntent pendingintent = PendingIntent.getActivity(stepcountservice.this, 0, intent, 0);
        // notify.setContentIntent(pendingintent);
        Notification notification1 = notify.build();
        //notificationgmanager.notify(124, notification1);// 通过通知管理器发送通知
        //把该service创建为前台service
        startForeground(1, notification1);
    }

    private void registermReceiver() {
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(setting.ACTION_PHONE_WORK);
        filter1.addAction(setting.ACTION_BULEONE_WORK);
        filter1.addAction(setting.ACTION_BULETWO_WORK);
        filter1.addAction(setting.ACTION_BULETHREE_WORK);
        filter1.addAction(setting.ACTION_BULEFOUR_WORK);
//        filter1.addAction(ECGData.ACTION_WORK_START);
//        filter1.addAction(ECGData.ACTION_WORK_STOP);
        filter1.addAction(ECG_realtime.ACTION_BULE_START);
        filter1.addAction(ECG_realtime.ACTION_BULE_STOP);
        filter1.addAction(Step_analyze.ACTION_START_ANALYSE);
        registerReceiver(mReceiver, filter1); // Don't forget to unregister during onDestroy
    }

    private void registerBlueReceiver() {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(BluetoothReceiver, filter); // Don't forget to unregister during onDestroy
    }

    private void unregistermReceiver() {
        unregisterReceiver(mReceiver);
    }

    private void unregisterBlueReceiver() {
        unregisterReceiver(BluetoothReceiver);
    }

    private final BroadcastReceiver BluetoothReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //  Log.w(TAG,"device found");
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                if (IsTargetBluetoothFound(device)) {
                    // Log.w(TAG,"device is the target");
                    mBluetoothAdapter.cancelDiscovery();
                }
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                BLUE_CONNECTED_state = false;
                Toast.makeText(getApplicationContext(), "蓝牙已断开！请重新连接设备！", Toast.LENGTH_LONG).show();

            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                //Log.w(TAG,"device connected");
            }
        }
    };


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.w(TAG, "action" + "=" + action);
            if (setting.ACTION_PHONE_WORK.equals(action)) {
                mdealHandler.sendEmptyMessage(msg_phonework);
                //   Toast.makeText(getApplicationContext(),"ACTION_PHONE_WORK",Toast.LENGTH_LONG).show();
            }
            if (setting.ACTION_BULEONE_WORK.equals(action)) {
                mdealHandler.sendEmptyMessage(msg_blueonework);
                //  Toast.makeText(getApplicationContext(),"ACTION_BULEONE_WORK",Toast.LENGTH_LONG).show();
            }
            if (setting.ACTION_BULETWO_WORK.equals(action)) {
                mdealHandler.sendEmptyMessage(msg_bluetwowork);
                //   Toast.makeText(getApplicationContext(),"ACTION_BULETWO_WORK",Toast.LENGTH_LONG).show();
            }
            if (setting.ACTION_BULETHREE_WORK.equals(action)) {
                mdealHandler.sendEmptyMessage(msg_bluethreework);
                //   Toast.makeText(getApplicationContext(),"ACTION_BULETHREE_WORK",Toast.LENGTH_LONG).show();
            }
            if (setting.ACTION_BULEFOUR_WORK.equals(action)) {
                mdealHandler.sendEmptyMessage(msg_bluefourwork);
                //   Toast.makeText(getApplicationContext(),"ACTION_BULETHREE_WORK",Toast.LENGTH_LONG).show();
            }
            if (ECG_realtime.ACTION_BULE_START.equals(action)) {
                mdealHandler.sendEmptyMessage(msg_ecgdatastart);
            }
            if (ECG_realtime.ACTION_BULE_STOP.equals(action)) {
                mdealHandler.sendEmptyMessage(msg_ecgdatastop);
            }
            if (Step_analyze.ACTION_START_ANALYSE.equals(action)) {
                mdealHandler.sendEmptyMessage(msg_stepanalysestart);
            }
        }
    };


//    final SensorEventListener myAccelerometerListener = new SensorEventListener() {
//        public void onSensorChanged(SensorEvent sensorEvent) {
//            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//                X_lateral = sensorEvent.values[0];
//                Y_longitudinal = sensorEvent.values[1];
//                Z_vertical = sensorEvent.values[2];
//
//                //步数清零
//                step_clear();
//                //计算和加速度
//                if (phone_work_sate) {
//                    veluetemp = (double) (X_lateral * X_lateral + Y_longitudinal * Y_longitudinal + Z_vertical * Z_vertical);
//                    temp_ = (float) Math.sqrt(veluetemp);
//                    //加速度滤波
//                    temp_ = mfilter.averagefilter11(mfilter.averagefilter9(temp_));
//                    temp_d = temp_;
//                    temp_ = (float) (temp_/9.8);
//
//                    Intent locatehassend = new Intent(ACTION_LOCATE_CHANGE);
//                    locatehassend.putExtra("temp_locate",temp_*8192.0);
//                    sendBroadcast(locatehassend);
//
//                    Log.w(TAG, "temp" + "=" + temp_);
//                    Log.w(TAG,"ddddd"+"="+veluetemp);
//                    G_data_get_state = true;
//                    M_data_get_state = true;
//                }
//            }
//        }
//        //复写onAccuracyChanged方法,精度变化时调用
//        public void onAccuracyChanged(Sensor sensor, int accuracy) {
//        }
//    };


    public android.os.Handler mdealHandler = new android.os.Handler() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case msg_hasrecieved:
                    Notification.Builder notify = new Notification.Builder(stepcountservice.this);
                    notify.setSmallIcon(R.drawable.notification);
                    notify.setTicker("老人健康监护消息通知");
                    notify.setContentTitle("老人跌倒提醒"); //设置标题
                    notify.setContentText("点击关闭提醒"); //消息内容
                    notify.setWhen(System.currentTimeMillis()); //发送时间
                    //  notify.setDefaults(Notification.DEFAULT_ALL); //设置默认的提示音，振动方式，灯光
                    notify.setAutoCancel(true);//打开程序后图标消失
                    Intent intent = new Intent(stepcountservice.this, Fall_remand.class);
                    PendingIntent pendingintent = PendingIntent.getActivity(stepcountservice.this, 0, intent, 0);
                    notify.setContentIntent(pendingintent);
                    Notification notification1 = notify.build();
                    notificationgmanager.notify(124, notification1);// 通过通知管理器发送通知
                    call_for_help(); //跌倒后打电话求救
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    PlaySound();
                    break;
                case msg_finddevice:
                    getBondDevices();
                    break;
                case msg_connectdevice:
                    if (blueMethodchoose) {
                        try {
                            manageConnectThreadG = new blueConnectThreadG(DEVICE, mdealHandler);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        manageConnectThreadG.start();
                    } else {
                        checkBondState(DEVICE);
                        try {
                            manageConnectThread = new blueConnectThread(DEVICE, mdealHandler);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        manageConnectThread.start();
                    }
                    break;
                case msg_notbondeddevice:
                    mBluetoothAdapter.startDiscovery();
                    break;
                case msg_manageconnected:
                    BLUE_CONNECTED_state = true;
                    send_state = false;
                    if (blueMethodchoose) {
                        manageConnectedThread = new blueConnectedThread(stepcountservice.this, manageConnectThreadG.getSocket());
                        manageConnectedThread.recive_state = true;
                        manageConnectedThread.start();
                    } else {
                        manageConnectedThread = new blueConnectedThread(stepcountservice.this, manageConnectThread.getSocket());
                        manageConnectedThread.recive_state = true;
                        manageConnectedThread.start();
                    }
                    Toast.makeText(getApplicationContext(), "蓝牙已连接。。。", Toast.LENGTH_LONG).show();
                    break;
                case msg_phonework:
                    manageConnectedThread.recive_state = false;

//                    phone_work_sate = true;
//                    manageConnectedThread.recive_state = false;
                    break;
                case msg_blueonework:
                    manageConnectedThread.recive_state = false;
                    BLUEDEVICE_ADTRESS = "00:0B:0D:94:96:9F";
                    blueMethodchoose = false;
                    getBondDevices();
                    break;
                case msg_bluetwowork:
                    manageConnectedThread.recive_state = false;
                    BLUEDEVICE_ADTRESS = "34:81:F4:11:0C:14";
                    blueMethodchoose = true;
                    getBondDevices();
                    break;
                case msg_bluethreework:
                    manageConnectedThread.recive_state = false;
                    BLUEDEVICE_ADTRESS = "34:81:F4:11:09:E4";
                    blueMethodchoose = true;
                    getBondDevices();
                    break;
                case msg_bluefourwork:
                    manageConnectedThread.recive_state = false;
                    BLUEDEVICE_ADTRESS = "34:81:F4:11:E2:DA"; //新设备蓝牙地址
                    blueMethodchoose = true;
                    getBondDevices();
                    break;
                case msg_receivebluedata:
                    G_data_get_state = true;  //计步线程数据标志
                    M_data_get_state = true;  //跌到检测线程数据标志
                    Raw_data_get_state = true;  //心电数据上传标志
                    gait_gravity_change = true; //步态加速度数据标志
                    ECG_GSENSER_STORE = true;

                    Log.w(TAG, "handleMessage" + " = " + raw_data_temp[6]);

                    Intent locatehassend = new Intent(ACTION_LOCATE_CHANGE);
                    locatehassend.putExtra("temp_locate", raw_data_temp[6]);
                    sendBroadcast(locatehassend);
                    break;
                case msg_receivegps:
                    Raw_data_get_state = true;
                    if (gps_locate_state) {
                        gps_locate_state = false;
                        Intent locatehassend1 = new Intent(ACTION_GPS_CHANGE);
                        locatehassend1.putExtra("temp_gps", gps_data);
                        sendBroadcast(locatehassend1);
                    }
                    break;
                case msg_energycomput:
                    int step_now = sp.getInt("step_count", 0);
                    int step_diff = step_now - history_step_count;
                    personal_index = sp.getFloat("person_index", 0); //获取个人身体指数
                    if (step_diff > 0 && personal_index != 0) {
                        long time_now = SystemClock.elapsedRealtime();
                        float time_diff = (float) ((time_now - last_step_time) / 60000.0 - 2);
                        step_energy = sp.getFloat("person_energy", 0);
                        step_energy = (float) (step_energy + (personal_index + 0.26 * step_diff / time_diff + 0.92 * time_diff) / 2.0);
                        editor.putFloat("person_energy", step_energy);
                        editor.commit();
                    }
                    break;
                case msg_ecgdatastart:
                    mDataStroreThread = new DataStroreThread();
                    mDataStroreThread.start();
/***************************************以下为数据发送，勿删**************************************/
//                    if (ecg_send_state == false) {
//                        Log.w(TAG, "httpsend_state" + " = " + "开始心电数据接收！");
//                        ECGBlock1 = new ArrayList<>();
//                        ECGBlock2 = new ArrayList<>();
//                        ECGBlock3 = new ArrayList<>();
//                        ecg_send_state = true;
//                        ecg_ischange_state = true;
//                        data_num = 0;
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                while (ecg_send_state) {
//                                    if (Raw_data_get_state && ecg_ischange_state) {
//                                        Raw_data_get_state = false;
//                                        real_heart_rate = ECGHRInterface.JNIHearRateByC(raw_data_temp[0]) / 2;
//                                        if (ECGBlock1.size() < 25) {
//                                            ECGBlock1.add(raw_data_temp[0]);
//                                            ECGBlock2.add(raw_data_temp[1]);
//                                            ECGBlock3.add(raw_data_temp[2]);
//                                        }else {
//                                            Intent ecgintent = new Intent(ACTION_DATA_CHANGE);
//                                            ecgintent.putIntegerArrayListExtra("ECGBlock1",ECGBlock1);
//                                            ecgintent.putIntegerArrayListExtra("ECGBlock2",ECGBlock2);
//                                            ecgintent.putIntegerArrayListExtra("ECGBlock3",ECGBlock3);
//                                            sendBroadcast(ecgintent);
//                                            Log.w(TAG, "ecg_raw_data_data=" + ECGBlock1.size());
//                                            ECGBlock1.clear();
//                                            ECGBlock2.clear();
//                                            ECGBlock3.clear();
//                                            Log.w(TAG, "ecg_raw_data_data=" + ECGBlock1.size());
//                                        }
//                                        data_num++;
//                                        Ecg_data_1[data_num - 1] = (short) raw_data_temp[0];
//                                        Ecg_data_2[data_num - 1] = (short) raw_data_temp[1];
//                                        Ecg_data_3[data_num - 1] = (short) raw_data_temp[2];
//                                        gson_data_1[data_num - 1] = (short) raw_data_temp[3];
//                                        gson_data_2[data_num - 1] = (short) raw_data_temp[4];
//                                        gson_data_3[data_num - 1] = (short) raw_data_temp[5];
//                                    }
//                                    if (data_num >= total_num && ecg_ischange_state) {
//                                        ecg_ischange_state = false;
//                                        mdealHandler.sendEmptyMessage(msg_ecgdatasend);
//                                    }
//                                }
//                            }
//                        }).start();
//                    }
                    break;
                case msg_ecgdatastop:
                    if (mDataStroreThread == null)
                        break;
                    if (mDataStroreThread.isAlive()){
                        mDataStroreThread.storestop();
                        try {
                            mDataStroreThread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
//                    ecg_send_state = false;
//                    mdealHandler.sendEmptyMessage(msg_ecgdatasend);
//                    Log.w(TAG, "httpsend_state" + " = " + "心电接收停止！");
                    break;
                case msg_ecgdatasend:
                    total_data = new short[data_num * 6];
                    System.arraycopy(Ecg_data_1, 0, total_data, 0, data_num);
                    System.arraycopy(Ecg_data_2, 0, total_data, data_num, data_num);
                    System.arraycopy(Ecg_data_3, 0, total_data, 2 * data_num, data_num);
                    System.arraycopy(gson_data_1, 0, total_data, 3 * data_num, data_num);
                    System.arraycopy(gson_data_2, 0, total_data, 4 * data_num, data_num);
                    System.arraycopy(gson_data_3, 0, total_data, 5 * data_num, data_num);
                    data_num = 0;
                    ecg_ischange_state = true;
                    Log.w(TAG, "httpsend_state" + " = " + "心电上传一次！");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mHttpURLSend = new HttpURLSend();
                            mHttpURLSend.addparams(6, total_data);
                            String result = mHttpURLSend.Http_Data_send();
                            Log.w(TAG, "httpsend_state" + " = " + result);
                        }
                    }).start();
                    break;
                case msg_realecgshow:
                    real_heart_rate = ECGHRInterface.JNIHearRateByC(raw_data_temp[0]) / 2;
                    if (ECGBlock1.size() < 25) {
                        ECGBlock1.add(raw_data_temp[0]);
                        ECGBlock2.add(raw_data_temp[1]);
                        ECGBlock3.add(raw_data_temp[2]);
                    } else {
                        Intent ecgintent = new Intent(ACTION_DATA_CHANGE);
                        ecgintent.putIntegerArrayListExtra("ECGBlock1", ECGBlock1);
                        ecgintent.putIntegerArrayListExtra("ECGBlock2", ECGBlock2);
                        ecgintent.putIntegerArrayListExtra("ECGBlock3", ECGBlock3);
                        sendBroadcast(ecgintent);
                        Log.w(TAG, "ecg_raw_data_data=" + ECGBlock1.size());
                        ECGBlock1.clear();
                        ECGBlock2.clear();
                        ECGBlock3.clear();
                        Log.w(TAG, "ecg_raw_data_data=" + ECGBlock1.size());
                    }
                    break;
                case msg_stepanalysestart:
                    mgait_thread = new gait_thread();
                    mgait_thread.start();
                    Log.w(TAG, "gait_sensor_process" + " = " + "开始步态加速度采集线程");
                    Intent gaitintent = new Intent(ACTION_GAIT_STAET);
                    sendBroadcast(gaitintent);
                    break;
                case msg_stepdatastore:
                    Log.w(TAG, "gait_sensor_process" + " = " + "开始步态加速度采集");
                    gait_gravity_state = true;
                    gait_sensor_list = new ArrayList<>();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (gait_gravity_state) {
                                if (gait_gravity_change) {
                                    gait_gravity_change = false;
                                    gait_sensor_list.add(raw_data_temp[3]);
                                }
                            }
                        }
                    }).start();
                    break;
                case msg_stepanalysestop:
                    /******************步态加速度数据处理*********************/
                    Log.w(TAG, "gait_sensor_process" + " = " + "步态加速度采集结束");
                    double ASI_Result = -1;
                    gait_gravity_state = false;
//                     ASI_Result = Gait_estimate.ASI_Gait_Calculate(data.data4);
                    ASI_Result = Gait_estimate.ASI_Gait_Calculate(gait_sensor_list);
                    gait_sensor_list.clear();
                    Log.w(TAG, "gait_sensor_process" + " = " + ASI_Result);
                    if (ASI_Result == -1 || ASI_Result > 60) {
                        Intent gaitstopintent = new Intent(ACTION_GAIT_ERRO);
                        sendBroadcast(gaitstopintent);
                        break;
                    }
                    editor.putFloat("gait_estimate_result", (float) ASI_Result);
                    editor.commit();
                    Intent gaitstopintent = new Intent(ACTION_GAIT_STOP);
                    sendBroadcast(gaitstopintent);
                    break;
                case msg_locationon:
                    location_register();
                    break;
                case msg_locationoff:
                    location_unregister();
                    break;
                default:
                    break;
            }
        }
    };

    public void location_register() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10*1000, 0, NetworklocationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10*1000, 0, GPSlocationLisener);

        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

    }

    public void location_unregister() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(NetworklocationListener);
            locationManager.removeUpdates(GPSlocationLisener);
            locationManager = null;
        }

    }

    private static boolean location_GPS_NET = true;
    private static int location_count = 20;
    LocationListener NetworklocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null && location_GPS_NET) {
                location_data[0] = location.getLatitude();
                location_data[1] = location.getLongitude();
                Intent intent = new Intent(ACTION_LOCATION_CHANG);
                intent.putExtra("location_data",location_data);
                sendBroadcast(intent);
                Log.w(TAG, "onLocationChanged" + "GPS = " + location_data[0] + "   " + location_data[1]);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {}

        @Override
        public void onProviderEnabled(String s) {}

        @Override
        public void onProviderDisabled(String s) {}
    };

    LocationListener GPSlocationLisener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                location_count = 20;
                location_GPS_NET = false;
                location_data[0] = location.getLatitude();
                location_data[1] = location.getLongitude();
                Intent intent = new Intent(ACTION_LOCATION_CHANG);
                intent.putExtra("location_data",location_data);
                sendBroadcast(intent);
                Log.w(TAG, "onLocationChanged" + "GPS = " + location_data[0] + "   " + location_data[1]);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {}

        @Override
        public void onProviderEnabled(String s) {}

        @Override
        public void onProviderDisabled(String s) {}
    };

    private void getBondDevices() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                if (IsTargetBluetoothFound(device)) {
                    return;
                }
            }
        }
        mdealHandler.sendEmptyMessage(msg_notbondeddevice);
    }

    private boolean IsTargetBluetoothFound(BluetoothDevice device) {
        if (BLUEDEVICE_ADTRESS.equals(device.getAddress().toString())) {
            //  if(BLUEDEVICE_NAME.equals(device.getName().toString())) {
            DEVICE = device;
            send_state = true;
            Log.w(TAG, "BLUEDEVICE_NAME" + "=" + device.getName().toString());
            Log.w(TAG, "BLUEDEVICE_ADTRESS" + "=" + device.getAddress().toString());
            mdealHandler.sendEmptyMessage(msg_connectdevice);
            return true;
        } else {
            return false;
        }
    }

    private void checkBondState(BluetoothDevice device) {
        if (device.getBondState() != BluetoothDevice.BOND_BONDED) {//判断给定地址下的device是否已经配对
            try {
                BLUE_METHODS.autoBond(device, strPin);//设置pin值
                BLUE_METHODS.createBond(device);
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println("配对不成功");
            }
        }
    }

    public class DataStroreThread extends Thread {
        private boolean isStartstore = false;
        private ECGdiag tempECGdiag;
        private FileOutputStream moutStream = null;

        public DataStroreThread () {
            isStartstore = true;
            ECGBlock1 = new ArrayList<>();
            ECGBlock2 = new ArrayList<>();
            ECGBlock3 = new ArrayList<>();
            String time = TimeUtils.getString(System.currentTimeMillis(), "yyyy-MM-dd HH:mm");
            tempECGdiag = new ECGdiag();
            tempECGdiag.setEid(sp.getString("main_person", ""));
            tempECGdiag.setEdate(time);
            tempECGdiag.setEsign("心电测量数据");
            tempECGdiag.setEresult("暂无诊断信息");
            tempECGdiag.setEsuggest("暂无建议");
            tempECGdiag.setEaddress(FileUtiles.DATA_PATH + time + ".bin");
        }
        public void storestop() {
            isStartstore = false;
        }
        @Override
        public void run() {
            super.run();
            File file = new File(tempECGdiag.getEaddress());
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                moutStream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            while (isStartstore){
                if (ECG_GSENSER_STORE){
                    ECG_GSENSER_STORE = false;
                    if (raw_data_temp != null && raw_data_temp.length > 0) {
                        mdealHandler.sendEmptyMessage(msg_realecgshow);
                        try {
                            moutStream.write(DataConvert.intToBytes(raw_data_temp[0]));
                            moutStream.write(DataConvert.intToBytes(raw_data_temp[1]));
                            moutStream.write(DataConvert.intToBytes(raw_data_temp[2]));
                            moutStream.write(DataConvert.intToBytes(raw_data_temp[3]));
                            moutStream.write(DataConvert.intToBytes(raw_data_temp[4]));
                            moutStream.write(DataConvert.intToBytes(raw_data_temp[5]));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            try {
                moutStream.close();
                msqlite.insertAccount(tempECGdiag);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    //计步线程
    Thread stepcountthread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                if (M_data_get_state) {
                    M_data_get_state = false;

                    if (step.DetectorNewStep((float) (raw_data_temp[6] / 8192.0 * 15.0)) == 1) {
                        if (!step_count_state) {
                            step_total_count++;
                            count__ = 3;
                            if (step_total_count >= 9) {
                                /**************************记录步行能量消耗计算初始步态************************************/
                                if (step_process_state) {
                                    last_step_time = SystemClock.elapsedRealtime();
                                    step_process_state = false;
                                    history_step_count = step_count;
                                }
                                /*************************************************************************************/
                                step_count = step_count + 9;
                                step_count_state = true;
                                step_total_count = 0;
                            }
                        } else {
                            step_count__ = 3;
                            step_count++;
                            eenergy_count = 120;

                            editor.putInt("step_count", step_count);
                            editor.commit();
                            //检测到一步，向activity发送一次广播
                            Intent stepchange = new Intent(ACTION_STEP_CHANG);
                            sendBroadcast(stepchange);
                        }
                    }
                }
            }
        }
    });
    //跌倒检测线程
    Thread falldetecthread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                boolean fall_state = sp.getBoolean("remand_state", false);
                if (G_data_get_state && fall_state && notifacation_state) {
                    G_data_get_state = false;

                    double fall_data_temp = raw_data_temp[6] / 8192.0;

                    if (fall_data_temp > 5) {
                        mdealHandler.sendEmptyMessage(msg_hasrecieved);
                        notifacation_state = false;
                    }
                    double fall_result = mFall_Last.Fall_Remand(fall_data_temp);
                    if (fall_result >= 0.516) {
                        fall_count++;
                        if (fall_count > 10) {
                            fall_count = 0;
                            notifacation_state = false;
                            mdealHandler.sendEmptyMessage(msg_hasrecieved);
                        }
                    } else if (fall_result < 0.516 && notifacation_state) {
                        if (fall_count > 0)
                            fall_count--;
                    }

                }
            }
        }
    });


    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 1000);
            count__--;
            step_count__--;
            gps_count_--;
            //   eenergy_count --;
            Log.w(TAG, "count__" + "=" + count__);
            if (count__ == 0) {
                count__ = 3;
                step_total_count = 0;
            }
            if (step_count__ == 0) {
                step_count__ = 3;
                step_count_state = false;
            }
            if (gps_count_ == 0) {
                gps_count_ = 5;
                gps_locate_state = true;
            }
            if (eenergy_count > 0)
                eenergy_count--;
            if (eenergy_count == 0 && step_process_state == false) {
                step_process_state = true;
                mdealHandler.sendEmptyMessage(msg_energycomput);
            }
            if (location_count > 0)
                location_count--;
            if (location_count < 1)
                location_GPS_NET = true;

        }
    };

    private void call_for_help() {
        String phone_number = mMessageStore.StringRead("adphonenume");
        if (phone_number.equals("")) {
            Toast.makeText(stepcountservice.this, "紧急呼叫号码未设定！", Toast.LENGTH_SHORT).show();
        } else {
            // Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Data_store.ad_phonenumber));
            // startActivity(intent);
            Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone_number));
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(intent1);
            Log.w(TAG,"sdfge" + "  " + "dsafafkj");
            Toast.makeText(stepcountservice.this, "正在呼叫被监护人！", Toast.LENGTH_SHORT).show();
        }
    }


    // 播放默认铃声
    // 返回Notification id
    public void PlaySound() {
        alarmMusic = MediaPlayer.create(stepcountservice.this,R.raw.alarm);
        alarmMusic.setLooping(true);
        alarmMusic.start();
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        // 等待3秒，震动3秒，从第0个索引开始，一直循环
        vibrator.vibrate(new long[]{1000, 1000}, 0);
    }
//    public void StopSound(){
//        if (alarmMusic!=null){
//            alarmMusic.stop();
//            alarmMusic.release();
//            alarmMusic = null;
//            vibrator.cancel();
//            vibrator = null;
//        }
//    }


    private class gait_thread extends Thread{
        public gait_thread(){}
        @Override
        public void run() {
            super.run();
            thread_wait(1);
            control_remand(voice_data[0]);
            thread_wait(6);
            control_remand(voice_data[1]);
            thread_wait(3);
            /****************开始数据采集操作********************/
            mdealHandler.sendEmptyMessage(msg_stepdatastore);
            thread_wait(3);
            control_remand(voice_data[2]);
            thread_wait(35);
            control_remand(voice_data[3]);
            thread_wait(5);
            mdealHandler.sendEmptyMessage(msg_stepanalysestop);
            control_remand(voice_data[4]);
        }
    }


    private void thread_wait(int time){
        int ti = time * 1000;
        try {
            Thread.sleep(ti);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void control_remand(int id) {
        int id_voice = id;
        if (alarmMusic == null) {
            alarmMusic = MediaPlayer.create(stepcountservice.this, id_voice);
            alarmMusic.setLooping(false);
            alarmMusic.start();
        }else {
            alarmMusic.stop();
            alarmMusic.release();
            alarmMusic = null;

            alarmMusic = MediaPlayer.create(stepcountservice.this, id_voice);
            alarmMusic.setLooping(false);
            alarmMusic.start();
        }
    }
    public void StopSound(){
        if (null != alarmMusic) {
            alarmMusic.stop();
            alarmMusic.release();
            alarmMusic = null;
//            vibrator.cancel();
//            vibrator = null;
        }
        if (null != vibrator) {
            vibrator.cancel();
            vibrator = null;
        }
    }





    private void step_clear() {
        //23：55计步数据清零
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        if (str.equals("0000")) {
            editor.putInt("step_count", 0);
            editor.commit();
        }
    }
    private int score_count(double[] da) {
        int score_ = 0;
        for (int i = 0; i < da.length; i++) {
            if (da[i] > 0.516) {
                score_++;
            }
        }

        return score_;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        sm.unregisterListener(myAccelerometerListener);
        unregisterBlueReceiver();
        unregistermReceiver();
        StopSound();
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
        location_unregister();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.d(TAG, "MyService: onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }




    @Override
    public void ReceiveBlueData(int[] block) {
        raw_data_temp = block;


        Log.w(TAG, "ReceiveBlueData" + " = " + raw_data_temp[0] + "   " + raw_data_temp[1]);
        mdealHandler.sendEmptyMessage(msg_receivebluedata);
    }

    @Override
    public void ReceiveGPSData(double[] block) {
        gps_data = block;
        mdealHandler.sendEmptyMessage(msg_receivegps);
    }

}
