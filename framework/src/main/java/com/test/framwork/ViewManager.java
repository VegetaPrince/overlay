package com.test.framwork;

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
    private Map<String, OverEntity> overEntityMap = new HashMap<>();

    private static final String TAG = ViewManager.class.getSimpleName();

    private ViewManager() {
    }

    private static class Inner {
        private static final ViewManager INSTANCE = new ViewManager();
    }

    public static ViewManager getInstance() {
        return Inner.INSTANCE;
    }

    public OverEntity getOverEntity(String pkg) {
        return overEntityMap.get(pkg);
    }

    public void onPause(OverEntity entity) {
        overEntityMap.put(entity.pkg, entity);
    }

    public void onDestroy(OverEntity entity) {
        overEntityMap.remove(entity.pkg);
    }


    public interface OnExitOverListener {
        void exitOver(View view);
    }

}
