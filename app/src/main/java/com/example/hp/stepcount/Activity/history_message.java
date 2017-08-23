package com.example.hp.stepcount.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.stepcount.Common.FileUtiles;
import com.example.hp.stepcount.Message.ECGdiag;
import com.example.hp.stepcount.R;
import com.example.hp.stepcount.SQLite.ECGdiagSQLiteOpertare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static com.baidu.mapapi.BMapManager.getContext;

public class history_message extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = history_message.class.getSimpleName();
    private static final int NOSELECT_STATE = -1;  // 表示未选中任何CheckBox
    private static ArrayList<String> message_list;
    private boolean isMultiSelect = false;// 是否处于多选状态
    private ListView mlist;
    private Button bt_cancel;
    private Button bt_delete;
    private TextView tv_sum;
    private LinearLayout linearLayout;
    private history_adapter adapter;
    private ECGdiagSQLiteOpertare mEcgoperate;
    private ArrayList<ECGdiag> mEcglist = null;
    private static HashMap<Integer, Boolean> isListCheckd;
    public static ECGdiag temp_ECGdiag = null;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_message);
        view_init();
    }

    private void view_init(){
        mlist = (ListView)findViewById(R.id.history_display);
        bt_cancel = (Button)findViewById(R.id.bt_cancel);
        bt_delete = (Button)findViewById(R.id.bt_delete);
        tv_sum = (TextView)findViewById(R.id.tv_sum);
        linearLayout = (LinearLayout)findViewById(R.id.checklinearlayout);
        linearLayout.setVisibility(View.INVISIBLE);
        bt_cancel.setOnClickListener(this);
        bt_delete.setOnClickListener(this);

        sp = getSharedPreferences("sp_demo", Context.MODE_PRIVATE);
        editor = sp.edit();
        message_list = new ArrayList<>();
        mEcgoperate = new ECGdiagSQLiteOpertare(getContext());

        String main_person = sp.getString("main_person", "");
        if (!main_person.equals("") && main_person.length() > 1)
            mEcglist = mEcgoperate.selectById(main_person);
        if (mEcglist != null){
            for (int i=0;i<mEcglist.size();i++){
                ECGdiag ecgtemp = mEcglist.get(i);
                message_list.add(ecgtemp.getEdate() + " " + ecgtemp.getEsign());
                Log.w(TAG, "view_init" + " = " + ecgtemp.getEsuggest());
            }
        }
        adapter = new history_adapter(history_message.this, message_list, NOSELECT_STATE);
        mlist.setAdapter(adapter);
    }

    private class history_adapter extends BaseAdapter {
        private ArrayList<String> list;
        private LayoutInflater inflater;
        private HashMap<Integer, Integer> isCheckBoxVisible;// 用来记录是否显示checkBox
        private HashMap<Integer, Boolean> isChecked;// 用来记录是否被选中
        private Context mContext;

        public history_adapter(Context context, ArrayList<String> list, int position) {
            mContext = context;
            inflater = LayoutInflater.from(context);
            this.list = list;
            isCheckBoxVisible = new HashMap<>();
            isChecked = new HashMap<>();
            // 如果处于多选状态，则显示CheckBox，否则不显示
            if (isMultiSelect) {
                for (int i=0;i < list.size();i++){
                    isCheckBoxVisible.put(i, CheckBox.VISIBLE);
                    isChecked.put(i,false);
                }
            } else {
                for (int i=0;i < list.size();i++){
                    isCheckBoxVisible.put(i, CheckBox.INVISIBLE);
                    isChecked.put(i,false);
                }
            }
            // 如果长按Item，则设置长按的Item中的CheckBox为选中状态
            if (isMultiSelect && position>=0) {
                isChecked.put(position,true);
            }
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            final ViewHolder viewHolder;
            if (convertView == null){
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.historymessage_item,null);
                viewHolder.tv_Name = (TextView)convertView.findViewById(R.id.tv_name);
                viewHolder.cb = (CheckBox)convertView.findViewById(R.id.cb_select);
                convertView.setTag(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final String str = list.get(i);
            viewHolder.tv_Name.setText(str);
            // 根据position设置CheckBox是否可见，是否选中
            viewHolder.cb.setChecked(isChecked.get(i));
            if (isCheckBoxVisible.get(i) == CheckBox.VISIBLE){
                viewHolder.cb.setVisibility(CheckBox.VISIBLE);
            } else {
                viewHolder.cb.setVisibility(CheckBox.INVISIBLE);
            }
            // ListView每一个Item的长按事件
            convertView.setOnLongClickListener(new onMyLongClick(i,list));
            /*
             * 在ListView中点击每一项的处理
             * 如果CheckBox未选中，则点击后选中CheckBox，并将数据添加到list_delete中
             * 如果CheckBox选中，则点击后取消选中CheckBox，并将数据从list_delete中移除
             */
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isMultiSelect) {
                        if (viewHolder.cb.isChecked()) {
                            viewHolder.cb.setChecked(false);
                            isListCheckd.put(i,false);
                        } else {
                            viewHolder.cb.setChecked(true);
                            isListCheckd.put(i,true);
                        }
                        tv_sum.setText("共选择了" + getmapcount(isListCheckd) + "项");
                    } else {
                        //显示结果
                        temp_ECGdiag = mEcglist.get(i);
                        Intent intent = new Intent();
                        intent.setClass(mContext,history_ecg_show.class);
                        mContext.startActivity(intent);
//                        dialog_Init(message_list.get(i), mEcglist.get(i).getEresult(), mEcglist.get(i).getEsuggest());
                    }
                }
            });
            return convertView;
        }

        class ViewHolder {
            public TextView tv_Name;
            public CheckBox cb;
        }

        class onMyLongClick implements View.OnLongClickListener {
            private int position;
            private ArrayList<String> list;

            public onMyLongClick(int position, ArrayList<String> list){
                this.position = position;
                this.list = list;
            }

            @Override
            public boolean onLongClick(View view) {
                isMultiSelect = true;
               /*******************标记长按选项*******************/
                isListCheckd = new HashMap<>();
                for (int i = 0; i < list.size(); i++)
                    isListCheckd.put(i, false);
                isListCheckd.put(position, true);
                linearLayout.setVisibility(View.VISIBLE);
                tv_sum.setText("共选择了" + getmapcount(isListCheckd) + "项");
                for (int i=0;i<list.size();i++) {
                    adapter.isCheckBoxVisible.put(i, CheckBox.VISIBLE);
                }
                // 根据position，设置ListView中对应的CheckBox为选中状态
                adapter = new history_adapter(history_message.this, list, position);
                mlist.setAdapter(adapter);
                return true;
            }
        }
    }


    private void dialog_Init(String item, String str1, String str2) {
        LayoutInflater mylayoutInflater = LayoutInflater.from(history_message.this);
        final View ecg_dialog = mylayoutInflater.inflate(R.layout.ecg_dialog_show, null);
        final TextView ecg_dialog_item = (TextView) ecg_dialog.findViewById(R.id.ecg_dialog_item);
        final TextView ecg_dialog_result = (TextView) ecg_dialog.findViewById(R.id.ecg_dialog_result);
        final TextView ecg_dialog_suggestion = (TextView) ecg_dialog.findViewById(R.id.ecg_dialog_suggestion);
        ecg_dialog_item.setText(item);
        ecg_dialog_result.setText(str1);
        ecg_dialog_suggestion.setText(str2);
        AlertDialog.Builder ecg_alertdialog = new AlertDialog.Builder(history_message.this);
        ecg_alertdialog.setView(ecg_dialog);
        ecg_alertdialog.setNegativeButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        ecg_alertdialog.create().show();
    }


    private int getmapcount(HashMap<Integer, Boolean> map) {
        int sum_count = 0;
        Set<Integer> keys = map.keySet();
        for (int key : keys)
            if (map.get(key) == true)
                sum_count++;
        return sum_count;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_cancel:
                isMultiSelect = false;// 退出多选模式
          /************************清除选中项*************************/
                isListCheckd = null;

                adapter = new history_adapter(history_message.this, message_list, NOSELECT_STATE);
                mlist.setAdapter(adapter);
                linearLayout.setVisibility(View.GONE);
            break;
            case R.id.bt_delete:
                isMultiSelect = false;
                // 将数据从list中移除
                for (int i=message_list.size()-1;i>=0;i--){
                    if (isListCheckd.get(i) == true){
                        FileUtiles.deleteFile(mEcglist.get(i).getEaddress());  //删除SD卡储存文件
                        mEcgoperate.deleteID(mEcglist.remove(i));  //删除数据库记录
                        message_list.remove(i);    //删除列表信息
                    }
                }
                isListCheckd = null;
                // 重新加载Adapter
                adapter = new history_adapter(history_message.this, message_list, NOSELECT_STATE);
                mlist.setAdapter(adapter);
                linearLayout.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }
}
