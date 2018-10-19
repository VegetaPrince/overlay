package com.test.framwork;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;

/**
 * @ClassName: App
 * @Description: java类作用描述
 * @Author: flying
 * @CreateDate: 2018/10/18 20:36
 */
public class Sdk {
   public static void init(Context context){
       ContextCompat.startForegroundService(context,new Intent(context, MyService.class));
   }
}
