package com.jetec.intenteventbusexample;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class Activity2 extends AppCompatActivity {
    boolean eventSelect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        /**從Intent拿取資料*/
        Intent intent = getIntent();
        ArrayList<String> arrayList = intent.getStringArrayListExtra("MyArray");
        TextView textView = findViewById(R.id.textView);
        if (arrayList != null) {
            /**如果有接收到資料，將EventBus的資料設為不顯示*/
            eventSelect = true;
            textView.setText("陣列長度為："+arrayList.size());
        }
    }
    /**開啟EventBus監聽*/
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    /**關閉EventBus監聽*/
    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    /**由EventBus所取得到的監聽資料在此*/
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getThisActivityEvent(GetEvent event) {
        TextView textView = findViewById(R.id.textView);
        /**如果Intent沒有資料，將EventBus的資料設為顯示*/
        if (!eventSelect){
            textView.setText("陣列長度為(Event)："+event.getArrayList().size());
        }
    }
}
