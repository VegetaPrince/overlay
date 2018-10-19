package com.test.framwork;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @ClassName: MyService
 * @Description: java类作用描述
 * @Author: flying
 * @CreateDate: 2018/10/18 15:23
 */
public class MyService extends Service implements View.OnClickListener {
    private static final String TAG = MyService.class.getSimpleName();
    private static final String CHANNEL_ID = "1";
    private static final CharSequence CHANNEL_NAME = "test";
    private WindowManager windowManager;
    private View overLayout;
    private WindowManager.LayoutParams windowParams;
    private RelativeLayout container;
    private OverEntity overEntity;

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
        Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID).build();
        startForeground(1, notification);


        windowParams = new WindowManager.LayoutParams();
        windowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        windowParams.format = PixelFormat.RGBA_8888;
        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
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
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        enterOver(intent);
        Log.v(TAG, "onStart");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
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
        Log.v(TAG, "exitOver");
        container.removeAllViews();
        windowManager.removeView(overLayout);
        overEntity.onExitOverListener.exitOver(overEntity.mainView);
    }

    public void enterOver(Intent intent) {
        String pkg = intent.getStringExtra(Constant.OVER_MODEL_ENTER_APP_PKG);
        if (TextUtils.isEmpty(pkg)) {
            return;
        }
        Log.v(TAG, "exitOver:" + pkg);

        overEntity = ViewManager.getInstance().getOverEntity(pkg);
        container.removeAllViews();
        container.addView(overEntity.mainView);
        windowManager.addView(overLayout, windowParams);
    }
}
