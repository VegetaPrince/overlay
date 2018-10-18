package com.test.framwork;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.*;
import android.widget.RelativeLayout;

/**
 * @ClassName: MyService
 * @Description: java类作用描述
 * @Author: flying
 * @CreateDate: 2018/10/18 15:23
 */
public class MyService extends Service implements View.OnClickListener, ViewManager.OnEnterOverListener {
    private static final String TAG = MyService.class.getSimpleName();
    private WindowManager windowManager;
    private View overLayout;
    private WindowManager.LayoutParams windowParams;
    private View mainView;
    RelativeLayout container;

    @Override
    public void onCreate() {
        super.onCreate();
        windowParams = new WindowManager.LayoutParams();
        windowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        windowParams.format = PixelFormat.RGBA_8888;
        windowParams.flags = WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_SPLIT_TOUCH;
//                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        windowParams.gravity = Gravity.LEFT | Gravity.TOP;
        windowParams.width = 800;
        windowParams.height = 800;
        windowParams.x = 250;
        windowParams.y = 250;

        windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        overLayout = LayoutInflater.from(this).inflate(R.layout.over_layout, null);
        container = overLayout.findViewById(R.id.container);
        overLayout.findViewById(R.id.close).setOnClickListener(this);
        overLayout.findViewById(R.id.max).setOnClickListener(this);
        overLayout.setOnTouchListener(new View.OnTouchListener() {
            private float lastX; //上一次位置的X.Y坐标
            private float lastY;
            private float nowX;  //当前移动位置的X.Y坐标
            private float nowY;
            private float tranX; //悬浮窗移动位置的相对值
            private float tranY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean ret = false;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 获取按下时的X，Y坐标
                        lastX = event.getRawX();
                        lastY = event.getRawY();
                        ret = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 获取移动时的X，Y坐标
                        nowX = event.getRawX();
                        nowY = event.getRawY();
                        // 计算XY坐标偏移量
                        tranX = nowX - lastX;
                        tranY = nowY - lastY;
                        // 移动悬浮窗
                        windowParams.x += tranX;
                        windowParams.y += tranY;
                        //更新悬浮窗位置
                        windowManager.updateViewLayout(overLayout, windowParams);
                        //记录当前坐标作为下一次计算的上一次移动的位置坐标
                        lastX = nowX;
                        lastY = nowY;
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return ret;
            }
        });
        ViewManager.getInstance().setOnEnterOverListener(MyService.this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.close) {
            exitOver();
        } else if (v.getId() == R.id.max) {
            exitOver();
            AppUtil.startApp(getApplicationContext(), getPackageName());
        }
    }

    private void exitOver() {
        container.removeAllViews();
        windowManager.removeView(overLayout);
        ViewManager.getInstance().exitOver(mainView);
    }

    @Override
    public void enterOver(View view) {
        mainView = view;
        container.removeAllViews();
        container.addView(mainView);
        windowManager.addView(overLayout, windowParams);
    }
}
