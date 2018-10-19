package com.test.framwork;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * @ClassName: AppUtil
 * @Description: java类作用描述
 * @Author: flying
 * @CreateDate: 2018/10/18 19:22
 */
public class AppUtil {
    public static void startApp(Context context,String pkg){
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(pkg);
        if (intent == null) {
            System.out.println("APP not found!");
        }
        context.startActivity(intent);
    }
}
