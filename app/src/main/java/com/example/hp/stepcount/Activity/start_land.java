package com.example.hp.stepcount.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hp.stepcount.R;
import com.example.hp.stepcount.SQLite.UserSQLiteOpertare;

import static com.baidu.mapapi.BMapManager.getContext;

public class start_land extends AppCompatActivity {
    private Button land_button;
    private Button register_button;
    private EditText name_edit;
    private EditText password_edit;
    private UserSQLiteOpertare muserstore;
    private String name_get = "";
    private String psw_get = "";
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_land);
        sp = getSharedPreferences("sp_demo", Context.MODE_PRIVATE);
        editor = sp.edit();

//        if (!sp.getString("main_person","").equals("")){
//            Intent intent = new Intent();
//            intent.setClass(start_land.this,MainMenu.class);
//            startActivity(intent);
//        }

        muserstore = new UserSQLiteOpertare(getApplicationContext());
        view_init();

    }

    private void view_init() {
        name_edit = (EditText)findViewById(R.id.name_edit);
        password_edit = (EditText)findViewById(R.id.password_edit);
        land_button = (Button)findViewById(R.id.land_button);
        register_button = (Button)findViewById(R.id.register_button);
        land_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name_get = String.valueOf(name_edit.getText());
                psw_get = String.valueOf(password_edit.getText());
                if (name_get.equals("") || psw_get.equals("")){
                    Toast.makeText(getApplicationContext(),"请输入用户名和密码！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (muserstore.isExisteById(name_get)){
                    if (muserstore.selectById(name_get).getUpsw().equals(psw_get)){
                        editor.putString("main_person",name_get);
                        editor.commit();
                        Intent intent = new Intent();
                        intent.setClass(start_land.this,MainMenu.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(),"密码错误！",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"用户不存在！",Toast.LENGTH_SHORT).show();
                }
            }
        });
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_info mdlg = new register_info(start_land.this, R.style.AppTheme);
                mdlg.show();
            }
        });

        name_edit.setText("18810358133");
        password_edit.setText("18810358133");
    }







}
