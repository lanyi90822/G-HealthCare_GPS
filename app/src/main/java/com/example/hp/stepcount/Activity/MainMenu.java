package com.example.hp.stepcount.Activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.LocationManager;
import android.media.Image;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.example.hp.stepcount.Common.Gait_estimate;
import com.example.hp.stepcount.Data.data;
import com.example.hp.stepcount.Interface.BluetoothCB;
import com.example.hp.stepcount.R;
import com.example.hp.stepcount.Service.stepcountservice;
import com.example.hp.stepcount.Thread.Rawdatastore_Thread;

import java.lang.reflect.Field;

public class MainMenu extends Activity {
    private static final String TAG = "com.example.hp.stepcount.Service.Mainmenue";
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter;
    private stepcountservice mstepcountservice;
    public static final String ACTION_START_RECORD ="com.example.lenovo.caseapp.Activity.MainMeauActivity.ACTION_START_READ" ;
    private final String[] blueconnecte = new String[] { "手机传感器","GPS蓝牙接收器(0222)", "Dual-SPP(0C14)","Dual-SPP(09E4)","Dual-SPP(E2DA)"};
    public static final String ACTION_PHONE_WORK = "com.example.lenovo.caseapp.Activity.MainMeauActivity.ACTION_PHONE_WORK";
    public static final String ACTION_BULEONE_WORK = "com.example.lenovo.caseapp.Activity.MainMeauActivity.ACTION_BULEONE_WORK";
    public static final String ACTION_BULETWO_WORK = "com.example.lenovo.caseapp.Activity.MainMeauActivity.ACTION_BULETWO_WORK";
    public static final String ACTION_BULETHREE_WORK = "com.example.lenovo.caseapp.Activity.MainMeauActivity.ACTION_BULETHREE_WORK";
    public static final String ACTION_BULEFOUR_WORK = "com.example.lenovo.caseapp.Activity.MainMeauActivity.ACTION_BULEFOUR_WORK";
    public static final String ACTION_CALL_HELP = "com.example.lenovo.caseapp.Activity.MainMeauActivity.ACTION_CALL_HELP";
    private static int selectedBlueIndex = 0;
    private static Rawdatastore_Thread mThread;
    private ImageButton step_count;
    private ImageButton fall_remand;
    private ImageButton sleep_monitor;
    private ImageButton offline_map;
    private ImageButton step_analyze;
    private ImageButton hear_wave;
    private ImageButton history_message;
    private static TextView data_show;
    private static String file_name_ = "Gsensor";
    private static boolean Storing_State = false;
    //private TextView mweb_locate;
    private ImageButton mlocate;
    private TextView ecg_locate;
    private ImageButton system_setting;
    LocationManager locationManager = null;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main_menu);

        locationInit();

        Intent intent = new Intent(MainMenu.this,stepcountservice.class);
        startService(intent);

        view_init();
        initBluetooth();
        registerMReceiver();

    }

    private void view_init(){

        step_count = (ImageButton)findViewById(R.id.step_count);
        step_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), Sport_monitor.class);
                startActivity(intent);
            }
        });
        fall_remand = (ImageButton)findViewById(R.id.fall_remand);
        fall_remand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), Fall_remand.class);
                startActivity(intent);
            }
        });
        sleep_monitor = (ImageButton)findViewById(R.id.sleep_monitor);
        sleep_monitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), sleep.class);
                startActivity(intent);
            }
        });

        data_show = (TextView)findViewById(R.id.data_show);
        data_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), drawdata.class);
                startActivity(intent);
            }
        });
        mlocate = (ImageButton)findViewById(R.id.locate);
        mlocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), locate.class);
                startActivity(intent);
            }
        });
        offline_map = (ImageButton)findViewById(R.id.offline_map);
        offline_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), offline_map.class);
                startActivity(intent);
            }
        });
        step_analyze = (ImageButton)findViewById(R.id.step_analyze);
        step_analyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), Step_analyze.class);
                startActivity(intent);
            }
        });

        ecg_locate = (TextView)findViewById(R.id.web_locate);
        ecg_locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainMenu.this,ECGData.class);
                startActivity(intent);
            }
        });

        system_setting = (ImageButton)findViewById(R.id.system_setting);
        system_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainMenu.this,setting.class);
                startActivity(intent);
            }
        });

        hear_wave = (ImageButton)findViewById(R.id.hear_wave);
        hear_wave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainMenu.this, ECG_realtime.class);
                startActivity(intent);
            }
        });

        history_message = (ImageButton)findViewById(R.id.history_message);
        history_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainMenu.this,history_message.class);
                startActivity(intent);
            }
        });
    }


    private void locationInit(){
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "请开启GPS导航...", Toast.LENGTH_SHORT).show();
            return;
        }
    }



    public void initBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "bluetooth not access", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    private void Dialog_Init() {
        AlertDialog.Builder Mode1 = new AlertDialog.Builder(MainMenu.this);
        Mode1.setTitle("工作模式：");
        Mode1.setSingleChoiceItems(blueconnecte, selectedBlueIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedBlueIndex = i;
            }
        });
        Mode1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //**添加命令操作**//

                if (selectedBlueIndex == 0){
                    Intent stepchange = new Intent(ACTION_PHONE_WORK);
                    sendBroadcast(stepchange);
                }
                if (selectedBlueIndex == 1){
                    Intent stepchange1 = new Intent(ACTION_BULEONE_WORK);
                    sendBroadcast(stepchange1);
                }
                if (selectedBlueIndex == 2){
                    Intent stepchange2 = new Intent(ACTION_BULETWO_WORK);
                    sendBroadcast(stepchange2);
                }
                if (selectedBlueIndex == 3){
                    Intent stepchange3 = new Intent(ACTION_BULETHREE_WORK);
                    sendBroadcast(stepchange3);
                }
                if (selectedBlueIndex == 4){
                    Intent stepchange4 = new Intent(ACTION_BULEFOUR_WORK);
                    sendBroadcast(stepchange4);
                }
            }
        });
        Mode1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        Mode1.create().show();
    }
    private void Dialog_Init_Store() {
        LayoutInflater smslayoutInflater = LayoutInflater.from(MainMenu.this);
        final View smsphoneView = smslayoutInflater.inflate(R.layout.system_setting, null);
        final EditText system_sms_num = (EditText)smsphoneView.findViewById(R.id.system_phone);
        final ImageView store_st_ = (ImageView)smsphoneView.findViewById(R.id.store_st_);
        final Button start_save = (Button)smsphoneView.findViewById(R.id.start_save);
        final Button stop_save = (Button)smsphoneView.findViewById(R.id.stop_save);
        if (Storing_State){
            store_st_.setImageResource(R.drawable.miao);
        }else{
            store_st_.setImageResource(R.drawable.blank_);
        }
        system_sms_num.setText(file_name_);
        final AlertDialog.Builder Mode1 = new AlertDialog.Builder(MainMenu.this);
        Mode1.setTitle("数据保存");
        Mode1.setView(smsphoneView);
        start_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mmname_ = String.valueOf(system_sms_num.getText());
                if (mmname_!=null)
                    file_name_ = mmname_;
                mThread.raw_recive_state = true;
                mThread = new Rawdatastore_Thread(file_name_);
                mThread.start();
                Storing_State = true;
                store_st_.setImageResource(R.drawable.miao);
                Toast.makeText(MainMenu.this, "开始保存", Toast.LENGTH_SHORT).show();
            }
        });

        stop_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mThread.raw_recive_state = false;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Storing_State = false;
                store_st_.setImageResource(R.drawable.blank_);
                Toast.makeText(MainMenu.this, "保存完成", Toast.LENGTH_SHORT).show();



            }
        });
        Mode1.create().show();
    }

    private void registerMReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(stepcountservice.ACTION_LOCATE_CHANGE);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
        }
    private void unregisterMReceiver(){
       unregisterReceiver(mReceiver);
       }

   private BroadcastReceiver mReceiver = new BroadcastReceiver() {
       @Override
       public void onReceive(Context context, Intent intent) {
           String action = intent.getAction();
           Log.w(TAG, "action" + "=" + action);
           if (stepcountservice.ACTION_LOCATE_CHANGE.equals(action)){
               int block = intent.getIntExtra("temp_locate",0);
               Log.w(TAG, "ssssss" + "=" + block);
               data_show.setText(String.valueOf(block));
           }
       }
   };

    private void SetMenubackgroudcolor() {
        getLayoutInflater().setFactory(new LayoutInflater.Factory() {
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                if(name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")
                        || name.equalsIgnoreCase("com.android.internal.view.menu.ListMenuItemView")){
                    try {
                        LayoutInflater f = getLayoutInflater();
                        final View view = f.createView( name, null, attrs );
                        new Handler().post( new Runnable() {
                            public void run () {
                                //view.setBackgroundResource( R.drawable.menu_backg);//设置背景图片
                                view.setBackgroundColor(Color.parseColor("#F6F9FE"));//设置背景色
                            }
                        });

                    } catch (InflateException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);

//        MenuItem item = menu.findItem(R.id.action_settings);
//        SpannableString spannableString = new SpannableString(item.getTitle());
//        spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spannableString.length(), 0);
//        spannableString.setSpan(new BackgroundColorSpan(Color.BLACK), 0, spannableString.length(), 0);
//        item.setTitle(spannableString);
//
//        MenuItem item1 = menu.findItem(R.id.store_settings);
//        SpannableString spannableString1 = new SpannableString(item1.getTitle());
//        spannableString1.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spannableString.length(), 0);
//        spannableString1.setSpan(new BackgroundColorSpan(Color.BLACK),0, spannableString.length(), 0);
//        item1.setTitle(spannableString1);
//
//        MenuItem item2 = menu.findItem(R.id.setting_settings);
//        SpannableString spannableString2 = new SpannableString(item2.getTitle());
//        spannableString2.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spannableString.length(), 0);
//        spannableString2.setSpan(new BackgroundColorSpan(Color.BLACK),0, spannableString.length(), 0);
//        item2.setTitle(spannableString2);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            Dialog_Init();
//            return true;
//        }
        if (id == R.id.store_settings) {
            Dialog_Init_Store();
            return true;
        }
//       if (id == R.id.setting_settings) {
//            Intent intent = new Intent();
//            intent.setClass(getApplicationContext(), setting.class);
//            startActivity(intent);
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        unregisterMReceiver();
        super.onDestroy();


    }
}