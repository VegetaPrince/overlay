package com.test.myapplication;

import android.app.Application;
import com.test.framwork.Sdk;

/**
 * @ClassName: App
 * @Description: java类作用描述
 * @Author: flying
 * @CreateDate: 2018/10/18 20:39
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Sdk.init(this);
    }
}
