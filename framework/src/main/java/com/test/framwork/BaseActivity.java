package com.test.framwork;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * @ClassName: BaseActivity
 * @Description: 模拟系统framework基类
 * @Author: flying
 * @CreateDate: 2018/10/18 14:32
 */
public class BaseActivity extends Activity {
    private static final String TAG = BaseActivity.class.getSimpleName();

    private LinearLayout root;
    private boolean create = true;
    private View mainView;
    private WindowManager.LayoutParams p;
    private OverEntity overEntity = new OverEntity();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initBeforeContentView() {
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        root = new LinearLayout(this);
        setContentView(root);
        root.setBackgroundResource(android.R.color.transparent);
        root.getLayoutParams().width = d.getWidth();
        root.getLayoutParams().height = d.getHeight();
        p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = d.getHeight() * 4 / 5;
        p.width = d.getWidth() * 4 / 5;
    }

    @Override
    public void setContentView(View view) {
        if (root != null && root == view) {
            super.setContentView(root);
            return;
        }
        initBeforeContentView();
        mainView = view;
        root.addView(mainView);

        overEntity.mainView = mainView;
        overEntity.pkg = getPackageName();
        overEntity.onExitOverListener = onExitOverListener;
        super.setContentView(root);
    }

    @Override
    public <T extends View> T findViewById(int id) {
        return mainView.findViewById(id);
    }

    @Override
    public void setContentView(int layoutResID) {
        mainView = View.inflate(getApplicationContext(), layoutResID, null);
        setContentView(mainView);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        view.setLayoutParams(params);
        setContentView(root);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Log.v(TAG, "onNewIntent:" + getIntent().getStringExtra(Constant.KEY_OVER_MODEL));
        if (Constant.OVER_MODEL.equals(getIntent().getStringExtra(Constant.KEY_OVER_MODEL))) {
            ViewManager.getInstance().onPause(overEntity);
            Intent service = new Intent(getApplicationContext(), MyService.class);
            service.putExtra(Constant.OVER_MODEL_ENTER_APP_PKG, getPackageName());
            ContextCompat.startForegroundService(getApplicationContext(),service);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        root.removeAllViews();
        ViewManager.getInstance().onPause(overEntity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ViewManager.getInstance().onDestroy(overEntity);
    }

    public ViewManager.OnExitOverListener onExitOverListener = new ViewManager.OnExitOverListener() {
        @Override
        public void exitOver(View view) {
            mainView = view;
            root.addView(mainView);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        int w;
        int h;
        if (create) {
            create = false;
        } else {
            w = p.width;
            h = p.height;
            ViewGroup.LayoutParams layoutParams = mainView.getLayoutParams();
            layoutParams.height = h;
            layoutParams.width = w;
            mainView.setLayoutParams(layoutParams);
        }
    }

}
