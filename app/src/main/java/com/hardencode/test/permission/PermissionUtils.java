package com.hardencode.test.permission;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/20.
 */

public class PermissionUtils {
    public static boolean isOverMarshmallow()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static void executeFailureMethod(Object object, int requestCode)
    {
        Method[] methods = object.getClass().getDeclaredMethods();
        for(Method method : methods)
        {
            PermissionFail permissionFail = method.getAnnotation(PermissionFail.class);
            if(null != permissionFail)
            {
                int code = permissionFail.requestCode();
                if(code == requestCode)
                {
                    executeMethod(object, method);
                }
            }

        }
    }

    public static void executeSucceedMethod(Object object, int requestCode)
    {
        Method[] methods = object.getClass().getDeclaredMethods();
        for(Method method : methods)
        {
           PermissionSucceed permissionSucceed = method.getAnnotation(PermissionSucceed.class);
           if(null != permissionSucceed)
           {
               int code = permissionSucceed.requestCode();
               if(code == requestCode)
               {
                   executeMethod(object, method);
               }
           }

        }
    }

    private static void executeMethod(Object object, Method method)
    {
        try {
            method.setAccessible(true);
            method.invoke(object);
        }catch (Exception e)
        {
            e.printStackTrace();
            Log.d("xx","Runtime permissions executeMethod exception class = " + object.getClass().getName()
             + " method name = " +method.getName());
        }
    }

    public static List<String> getDeniedPermissions(Context context, String... requestPermission)
    {
        List<String> deniedPermissions = new ArrayList<String >();
        for(String requestPer : requestPermission)
        {
            int code = ActivityCompat.checkSelfPermission(context, requestPer);
            if(code != PackageManager.PERMISSION_GRANTED)
            {
                deniedPermissions.add(requestPer);
            }
        }
        return deniedPermissions;
    }

    public static Activity getActivity(Object object)
    {
        if(object instanceof Activity)
        {
            return (Activity)object;
        }else if(object instanceof Fragment)
        {
            return ((Fragment)object).getActivity();
        }
        return null;
    }
}
