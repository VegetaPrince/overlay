package com.test.framwork;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: ViewManager
 * @Description: java类作用描述
 * @Author: flying
 * @CreateDate: 2018/10/18 15:36
 */
public class ViewManager {
    private Map<View, OnExitOverListener> overMap = new HashMap<>();
    private OnEnterOverListener onEnterOverListener;

    private static final String TAG = ViewManager.class.getSimpleName();

    private ViewManager() {
    }

    private static class Inner {
        private static final ViewManager INSTANCE = new ViewManager();
    }

    public static ViewManager getInstance() {
        return Inner.INSTANCE;
    }

    public void enterOver(View view, OnExitOverListener onExitOverListener) {
        overMap.put(view, onExitOverListener);
        onEnterOverListener.enterOver(view);
    }

    public void setOnEnterOverListener(OnEnterOverListener listener) {
        Log.v(TAG, "setOnEnterOverListener");
        onEnterOverListener = listener;
    }


    public void exitOver(View view) {
        if (overMap.containsKey(view)) {
            overMap.get(view).exitOver(view);
        }
    }


    public interface OnExitOverListener {
        void exitOver(View view);
    }

    public interface OnEnterOverListener {
        void enterOver(View view);
    }
}
