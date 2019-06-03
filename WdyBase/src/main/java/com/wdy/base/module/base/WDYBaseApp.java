package com.wdy.base.module.base;

import android.app.Application;

import com.wdy.base.module.util.WDYLog;

/**
 * 作者：王东一
 * 创建时间：2019-06-03.
 */
public class WDYBaseApp extends Application {
    private static WDYBaseApp instance;
    public static WDYBaseApp getInstance() {
        if (instance == null) {
            WDYLog.e("WDYBaseApp","[WDYBaseApp] instance is null.");
        }
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
