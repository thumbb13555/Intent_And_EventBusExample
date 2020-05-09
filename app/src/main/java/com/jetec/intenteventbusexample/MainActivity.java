package com.jetec.intenteventbusexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btIntent, btEventIntent, btTest;
        btIntent = findViewById(R.id.button_Intent);
        btEventIntent = findViewById(R.id.button_IntentByEvent);
        btTest = findViewById(R.id.button_Test);
        EditText editText = findViewById(R.id.editText);

        /**以普通的Intent帶資料傳送*/
        btIntent.setOnClickListener((v) -> {
            new Thread(() -> {
                ArrayList<String> arrayList = makeArray(editText);
                if (arrayList == null) return;
                runOnUiThread(() -> {
                    Intent intent = new Intent(MainActivity.this, Activity2.class);
                    intent.putExtra("MyArray", arrayList);
                    startActivity(intent);
                });

            }).start();
        });
        /**以EventBus傳送資料*/
        btEventIntent.setOnClickListener(v -> {
            new Thread(() -> {
                ArrayList<String> arrayList = makeArray(editText);
                if (arrayList == null) return;

                GetEvent getEvent = new GetEvent();
                getEvent.setArrayList(arrayList);
                EventBus.getDefault().postSticky(getEvent);//要畫面之間傳資料請務必用postSticky
                runOnUiThread(() -> {
                    Intent intent = new Intent(MainActivity.this, Activity2.class);
                    startActivity(intent);
                });
            }).start();


        });
        /**測試EventBus在同一個Activity內的效果*/
        btTest.setOnClickListener(v -> {
            new Thread(() -> {
                ArrayList<String> arrayList = makeArray(editText);
                if (arrayList == null) return;
                runOnUiThread(() -> {
                    GetEvent getEvent = new GetEvent();
                    getEvent.setArrayList(arrayList);
                    EventBus.getDefault().post(getEvent);//在同個畫面傳資料可使用一般post
                });
            }).start();
        });
    }

    /**
     * 統一製作陣列
     */
    private ArrayList<String> makeArray(EditText editText) {
        if (editText.getText().toString().length() == 0) return null;
        ArrayList<String> arrayList = new ArrayList<>();
        int size = Integer.parseInt(editText.getText().toString());
        for (int i = 0; i < size; i++) {
            arrayList.add(String.valueOf(i));
        }
        return arrayList;
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
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void getEvent(GetEvent event) {
        Toast.makeText(this, "陣列長度：" + event.getArrayList().size(), Toast.LENGTH_SHORT).show();
    }


}
