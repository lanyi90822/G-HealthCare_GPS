package com.example.hp.stepcount.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.stepcount.Message.User;
import com.example.hp.stepcount.NativeStore.MessageStore;
import com.example.hp.stepcount.R;
import com.example.hp.stepcount.SQLite.UserSQLiteOpertare;

public class personal_info extends Dialog {
    Context mContext;
    private final static String TAG = "com.example.lenovo.healthcaresystem.Activity";
    private Button confirm;
    private Button cancle;
    private EditText height;
    private EditText weight;
    private EditText name;
    private EditText yearold;
    private EditText phonenume;
    private TextView user_name;
    private String main_person_name = "";
    private static String m_name = "";
    private static String m_yearold = "";
    private static String m_height = "";
    private static String m_weight = "";
    private static String m_phonenume = "";
    private UserSQLiteOpertare muserstore;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public personal_info(Context context, int theme) {
        super(context, theme);
        mContext = context;
        View v = LayoutInflater.from(context).inflate(R.layout.activity_personal_info, null);
        setContentView(v);

        muserstore = new UserSQLiteOpertare(getContext());
        sp = mContext.getSharedPreferences("sp_demo", Context.MODE_PRIVATE);
        editor = sp.edit();

        height = (EditText)findViewById(R.id.height);
        weight = (EditText)findViewById(R.id.weight);
        confirm = (Button)findViewById(R.id.setting);
        cancle = (Button)findViewById(R.id.cancle);
        name = (EditText)findViewById(R.id.name);
        yearold = (EditText)findViewById(R.id.yearold);
        phonenume = (EditText)findViewById(R.id.phonenume);
        user_name = (TextView)findViewById(R.id.user_name);

        main_person_name = sp.getString("main_person","");
        user_name.setText(main_person_name);
        if (main_person_name.equals("")){
            Toast.makeText(getContext(), "没有用户正在使用，请先注册！", Toast.LENGTH_SHORT).show();
            dismiss();
        }

        get_massage();
        phonenume.setText(m_phonenume);
        name.setText(m_name);
        height.setText(m_height);
        weight.setText(m_weight);
        yearold.setText(m_yearold);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_name = String.valueOf(name.getText());
                m_height = String.valueOf(height.getText());
                m_weight = String.valueOf(weight.getText());
                m_yearold = String.valueOf(yearold.getText());
                m_phonenume = String.valueOf(phonenume.getText());

                if (m_name.equals("")|| m_height.equals("") || m_weight.equals("") || m_yearold.equals("") || m_phonenume.equals("")) {
                    Toast.makeText(getContext(), "信息不完整，请完善信息！", Toast.LENGTH_SHORT).show();
                } else {
                    User mUser = new User();
                    mUser.setUid(main_person_name);
                    mUser.setUpsw(m_phonenume);
                    mUser.setUname(m_name);
                    mUser.setUphone(m_phonenume);
                    mUser.setUage(m_yearold);
                    mUser.setUheigh(m_height);
                    mUser.setUweight(m_weight);

                    if (muserstore.isExisteById(main_person_name)) {
                        muserstore.updateAccount(mUser);
                    } else {
                        muserstore.insertAccount(mUser);
                    }

                    int hherght = Integer.valueOf(m_height);
                    int wweight = Integer.valueOf(m_weight);
                    editor.putFloat("person_index", (float) (0.43 * hherght + 0.57 * wweight - 108.44));
                    editor.commit();

                    Toast.makeText(getContext(), "信息已储存！", Toast.LENGTH_SHORT).show();
                    dismiss();


                }
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }


    private void get_massage(){
//        m_phonenume = sp.getString("main_person","");
        if (muserstore.isExisteById(main_person_name)){
            User mUser = muserstore.selectById(main_person_name);
            m_name = mUser.getUname();
            m_height = mUser.getUheigh();
            m_weight = mUser.getUweight();
            m_yearold = mUser.getUage();
            m_phonenume = mUser.getUphone();
        }
    }


}
