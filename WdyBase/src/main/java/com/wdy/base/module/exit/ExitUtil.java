package com.wdy.base.module.exit;

import android.app.Activity;

/**
 * 作者：王东一
 * 创建时间：2018/8/28.
 */
public class ExitUtil {
    private static ExitUtil mInstance;
    public static ExitUtil getInstance() {
        if (mInstance == null) {
            synchronized (ExitUtil.class) {
                if (mInstance == null) {
                    mInstance = new ExitUtil();
                }
            }
        }
        return mInstance;
    }
    public void with() {

    }
}
