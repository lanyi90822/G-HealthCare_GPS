package com.example.hp.stepcount.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.hp.stepcount.Common.Data_store;
import com.example.hp.stepcount.NativeStore.MessageStore;
import com.example.hp.stepcount.R;

public class setting extends Activity {
    private static final String[] systemlist = new String[]{"蓝牙设备连接", "紧急号码设置", "个人信息设置"};
    private final String[] blueconnecte = new String[] { "无设备连接","Holux-GPS(0222)", "Dual-SPP(0C14)","Dual-SPP(09E4)","Dual-SPP(E2DA)"};
    public static final String ACTION_PHONE_WORK = "com.example.lenovo.caseapp.Activity.MainMeauActivity.ACTION_PHONE_WORK";
    public static final String ACTION_BULEONE_WORK = "com.example.lenovo.caseapp.Activity.MainMeauActivity.ACTION_BULEONE_WORK";
    public static final String ACTION_BULETWO_WORK = "com.example.lenovo.caseapp.Activity.MainMeauActivity.ACTION_BULETWO_WORK";
    public static final String ACTION_BULETHREE_WORK = "com.example.lenovo.caseapp.Activity.MainMeauActivity.ACTION_BULETHREE_WORK";
    public static final String ACTION_BULEFOUR_WORK = "com.example.lenovo.caseapp.Activity.MainMeauActivity.ACTION_BULEFOUR_WORK";
    private static int selectedBlueIndex = 0;
    private MessageStore mMessageStore;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sp = getSharedPreferences("sp_demo", Context.MODE_PRIVATE);
        mMessageStore = new MessageStore(sp);

        list_Init();

    }

    private void list_Init() {

        final ListView system_list = (ListView) findViewById(R.id.system_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, systemlist);
        system_list.setAdapter(adapter);
        system_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Dialog_Init();
                        break;
                    case 1:
                        LayoutInflater mylayoutInflater = LayoutInflater.from(setting.this);
                        final View myphoneView = mylayoutInflater.inflate(R.layout.system_phonenumber, null);
                        final EditText system_phone_num = (EditText) myphoneView.findViewById(R.id.system_phone);
                        system_phone_num.setText(mMessageStore.StringRead("adphonenume"));
                        AlertDialog.Builder numberphone = new AlertDialog.Builder(setting.this);
                        numberphone.setIcon(R.drawable.star);
                        numberphone.setTitle("紧急呼叫号码：");
                        numberphone.setView(myphoneView);
                        numberphone.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //**添加命令操作**//
                                mMessageStore.StringStore("adphonenume", String.valueOf(system_phone_num.getText()));
                                Toast.makeText(setting.this, "号码已设定为：" + String.valueOf(system_phone_num.getText()), Toast.LENGTH_SHORT).show();
                            }
                        });
                        numberphone.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        numberphone.create().show();
                        break;
                    case 2:
                        personal_info mdlg = new personal_info(setting.this, R.style.AppTheme);
                        mdlg.show();

                        break;
                }

            }
        });
    }

    private void Dialog_Init() {
        AlertDialog.Builder Mode1 = new AlertDialog.Builder(setting.this);
        Mode1.setTitle("设备选择：");
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


}
