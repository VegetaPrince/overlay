package com.test.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.test.framwork.BaseActivity;
import com.test.framwork.Constant;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Activity实现：BaseActivity  里默认将窗口横向和纵向尺寸缩放比例为0.8
        setContentView(R.layout.activity_main);
        findViewById(R.id.hello).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Activity实现切换为悬浮窗实现
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra(Constant.KEY_OVER_MODEL, Constant.OVER_MODEL);
                startActivity(intent);
            }
        });
    }


}
