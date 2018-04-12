package com.hardencode.test.permission;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.List;

/**
 * Created by Administrator on 2018/3/20.
 */

public class PermissionsHelper {
    private Object object;
    private int requestCode;
    private String[] mRequestPermission;

    private PermissionsHelper(Object object)
    {
        this.object = object;
    }

    public static PermissionsHelper with(Activity activity)
    {
        return new PermissionsHelper(activity);
    }

    public static PermissionsHelper with(Fragment fragment)
    {
        return new PermissionsHelper(fragment);
    }

    public  PermissionsHelper requestCode(int requestCode)
    {
        this.requestCode = requestCode;
        return this;
    }

    public PermissionsHelper requestPermission(String... permissions)
    {
        this.mRequestPermission = permissions;
        return this;
    }

    public void request()
    {
        if(!PermissionUtils.isOverMarshmallow())
        {
            //6.0以下直接执行
            PermissionUtils.executeSucceedMethod(object, requestCode);
        }
        else
        {
            Activity mActivity = PermissionUtils.getActivity(object);
            if(null == mActivity)
            {
                Log.e("xxx", "Runtimen permision object is not context");
                return;
            }
            List<String> deniedPermissions = PermissionUtils.getDeniedPermissions((Context)object, mRequestPermission);
            if(deniedPermissions.size() == 0)
            {
                PermissionUtils.executeSucceedMethod(object, requestCode);
            }else
            {
                ActivityCompat.requestPermissions(mActivity, deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            }
        }
    }

    public static void requestPermissionResult(Object object, int requestCode, String[] permissions)
    {
        Activity mActivity = PermissionUtils.getActivity(object);
        if(null == mActivity)
        {
            Log.e("ss", "Runtimen permision object is not context");
            return;
        }
        List<String> denies = PermissionUtils.getDeniedPermissions(mActivity, permissions);
        if(denies.size() == 0)
        {
            PermissionUtils.executeSucceedMethod(object, requestCode);
        }else
        {
            PermissionUtils.executeFailureMethod(object, requestCode);
        }
    }
}
