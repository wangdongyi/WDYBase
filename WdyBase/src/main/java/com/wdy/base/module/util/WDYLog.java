package com.wdy.base.module.util;
//Created by 王东一 on 2016/11/1.


import android.util.Log;

public class WDYLog {
    private static int LOG_MAX_LENGTH = 2000;

    public static void e(String title, String msg) {
        int strLength = msg.length();
        int start = 0;
        int end = LOG_MAX_LENGTH;
        for (int i = 0; i < 100; i++) {
            if (strLength > end) {
                Log.e(title + i, msg.substring(start, end));
                start = end;
                end = end + LOG_MAX_LENGTH;
            } else {
                Log.e(title + i, msg.substring(start, strLength));
                break;
            }
        }
    }

    public static void i(String title, String msg) {

        int strLength = msg.length();
        int start = 0;
        int end = LOG_MAX_LENGTH;
        for (int i = 0; i < 100; i++) {
            if (strLength > end) {
                Log.i(title + i, msg.substring(start, end));
                start = end;
                end = end + LOG_MAX_LENGTH;
            } else {
                Log.i(title + i, msg.substring(start, strLength));
                break;
            }
        }

    }

    public static void d(String title, String msg) {

        int strLength = msg.length();
        int start = 0;
        int end = LOG_MAX_LENGTH;
        for (int i = 0; i < 100; i++) {
            if (strLength > end) {
                Log.d(title + i, msg.substring(start, end));
                start = end;
                end = end + LOG_MAX_LENGTH;
            } else {
                Log.d(title + i, msg.substring(start, strLength));
                break;
            }
        }

    }

    public static void w(String title, String msg) {

        Log.w(title, msg);
        int strLength = msg.length();
        int start = 0;
        int end = LOG_MAX_LENGTH;
        for (int i = 0; i < 100; i++) {
            if (strLength > end) {
                Log.w(title + i, msg.substring(start, end));
                start = end;
                end = end + LOG_MAX_LENGTH;
            } else {
                Log.w(title + i, msg.substring(start, strLength));
                break;
            }
        }

    }

    public static void v(String title, String msg) {
        int strLength = msg.length();
        int start = 0;
        int end = LOG_MAX_LENGTH;
        for (int i = 0; i < 100; i++) {
            if (strLength > end) {
                Log.v(title + i, msg.substring(start, end));
                start = end;
                end = end + LOG_MAX_LENGTH;
            } else {
                Log.v(title + i, msg.substring(start, strLength));
                break;
            }
        }
    }
}
