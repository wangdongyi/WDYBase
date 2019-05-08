package com.wdy.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.PrintWriter;

/**
 * 作者：王东一
 * 创建时间：2019/1/23.
 */
public class Controller {
    public static boolean install(String apkPath, Context context) {
        // 判断是否有root权限
        if (hasRootPerssion()) {
            // 有root权限，利用静默安装实现
            return clientInstall(apkPath);
        } else {
            // 没有root权限，利用意图进行安装
            File file = new File(apkPath);
            if (!file.exists())
                return false;
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            context.startActivity(intent);
            return true;
        }
    }

    /**
     * 判断手机是否有root权限
     */
    public static boolean hasRootPerssion() {
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }

    /**
     * 静默安装
     */
    public static boolean clientInstall(String apkPath) {
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.println("chmod 777 " + apkPath);
            PrintWriter.println("export LD_LIBRARY_PATH=/vendor/lib:/system/lib");
            PrintWriter.println("pm install -r " + apkPath);
//          PrintWriter.println("exit");
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }

    /**
     * 将文件复制到system/app 目录
     *
     * @param apkPath 特别注意格式：该路径不能是：/storage/emulated/0/app/QDemoTest4.apk 需要是：/sdcard/app/QDemoTest4.apk
     * @return
     */
    public static boolean copy2SystemApp(String apkPath) {
        PrintWriter PrintWriter = null;
        Process process = null;
        String appName = "chetou.apk", cmd;

        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            cmd = "mount -o remount,rw -t yaffs2 /dev/block/mtdblock3 /system";
            Log.e("copy2SystemApp", cmd);
            PrintWriter.println(cmd);

            cmd = "cat " + apkPath + " > /system/app/" + appName;
            Log.e("copy2SystemApp", cmd);
            PrintWriter.println(cmd);

            cmd = "chmod 777 /system/app/" + appName + " -R";
            Log.e("copy2SystemApp", cmd);
            PrintWriter.println(cmd);

            cmd = "mount -o remount,ro -t yaffs2 /dev/block/mtdblock3 /system";
            Log.e("copy2SystemApp", cmd);
            PrintWriter.println(cmd);
            PrintWriter.println("reboot");  //重启
            PrintWriter.println("exit");
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }

    private static boolean returnResult(int value) {
        // 代表成功
        if (value == 0) {
            return true;
        } else if (value == 1) { // 失败
            return false;
        } else { // 未知情况
            return false;
        }
    }

}
