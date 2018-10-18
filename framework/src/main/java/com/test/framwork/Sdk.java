package com.test.framwork;

import android.content.Context;
import android.content.Intent;

/**
 * @ClassName: App
 * @Description: java类作用描述
 * @Author: flying
 * @CreateDate: 2018/10/18 20:36
 */
public class Sdk {
   public static void init(Context context){
       context.startService(new Intent(context, MyService.class));

   }
}
