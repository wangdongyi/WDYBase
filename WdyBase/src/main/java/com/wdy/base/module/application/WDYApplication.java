package com.wdy.base.module.application;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.multidex.MultiDexApplication;

import com.wdy.base.module.util.ToastUtil;

/**
 * 作者：王东一
 * 创建时间：2018/6/14.
 */
public class WDYApplication extends MultiDexApplication {
    private static WDYApplication instance;

    private static ToastUtil toastUtil;

    /**
     * 单例，返回一个实例
     *
     * @return
     */
    public static WDYApplication getInstance() {
        if (instance == null) {

        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initApp();

    }

    public void initApp() {
        instance = this;

    }

    public static ToastUtil getToast() {
        if (toastUtil == null) {
            synchronized (ToastUtil.class) {
                if (toastUtil == null)
                    toastUtil = new ToastUtil(getInstance());
            }
        }
        return toastUtil;
    }

    //日志是否打开
    public static boolean isOpenLog() {
        return isApkInDebug(getInstance());
    }

    /**
     * 判断当前应用是否是debug状态
     */

    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
