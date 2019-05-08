package com.wdy.base.module.download;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 作者：王东一
 * 创建时间：2018/10/24.
 */
public class WDYDownloadService extends Service {
    public WDYDownloadService() {

    }

    /**
     * 安卓系统下载类
     **/
    private DownloadManager manager;
    /**
     * 接收下载完的广播
     **/
    private DownloadCompleteReceiver receiver;
    private String url = "";
    private String appName = "";
    private String DOWNLOAD_PATH = "/mnt/internal_sd/zmeng/";//下载路径，如果不定义自己的路径，6.0的手机不自动安装

    /**
     * 初始化下载器
     **/
    private void initDownManager() {
        manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        receiver = new DownloadCompleteReceiver();
        Uri uri = Uri.parse(url);
        DownloadManager.Request req = new DownloadManager.Request(uri);
        //设置网络状态下进行更新
        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        //下载中和下载完后都·显示通知栏
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //使用系统默认的下载路径 此处为应用内 /android/data/packages ,所以兼容7.0 8.0
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            req.setDestinationInExternalPublicDir(DOWNLOAD_PATH, appName);
        } else {
            req.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, appName);
        }
        //通知栏标题
        req.setTitle(appName);
        //通知栏描述信息
        req.setDescription("下载中");
        //设置类型为.apk
        req.setMimeType("application/vnd.android.package-archive");
        manager.enqueue(req);
        //注册下载广
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        url = intent.getStringExtra("downloadUrl");
        appName = intent.getStringExtra("downloadAppName");
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + DOWNLOAD_PATH + appName;
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            // 调用下载
            initDownManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {
        if (receiver != null)
            // 注销下载广播
            unregisterReceiver(receiver);
        super.onDestroy();
    }

    // 接受下载完成后的intent
    class DownloadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //判断是否下载完成的广播
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                //获取下载的文件id
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (manager.getUriForDownloadedFile(downId) != null) {
                    //自动安装apk
//                    installAPK(context, downId);
                    Uri downloadFileUri = manager.getUriForDownloadedFile(downId);
                    installApk(downloadFileUri.getPath());
                }
                //停止服务并关闭广播
                WDYDownloadService.this.stopSelf();
            }
        }

        private void installAPK(Context context, long downloadId) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + DOWNLOAD_PATH + appName);
                if (file.exists()) {
                    openFile(file, context);
                }
            } else {
                install(context, downloadId);
            }
        }

        /**
         * android7.0之后的更新
         * 通过隐式意图调用系统安装程序安装APK
         */
        public void install(Context context, long downloadId) {
            DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //根据id判断如果文件已经下载成功返回保存文件的路径
            assert dManager != null;
            Uri downloadFileUri = dManager.getUriForDownloadedFile(downloadId);
            Log.d("DownloadManager", "apk存储路径 : " + downloadFileUri);
            install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
            if (downloadFileUri != null) {
                if ((Build.VERSION.SDK_INT >= 24)) {
                    //判读版本是否在7.0以上
                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    //添加这一句表示对目标应用临时授权该Uri所代表的文件
                    //  不添加无法安装 或者出现解析包异常
                }
                context.startActivity(install);
            }
        }
    }

    /**
     * android6.0之后的升级更新
     * <p>
     * file
     * context
     */
    @SuppressLint("WrongConstant")
    public void openFile(File file, Context context) {
        Intent intent = new Intent();
        intent.addFlags(268435456);
        intent.setAction("android.intent.action.VIEW");
        String type = getMIMEType(file);
        intent.setDataAndType(Uri.fromFile(file), type);
        try {
            context.startActivity(intent);
        } catch (Exception var5) {
            var5.printStackTrace();
        }
    }

    public String getMIMEType(File var0) {
        String var1;
        String var2 = var0.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }

    public static String execCommand(String... command) {
        Process process = null;
        InputStream errIs = null;
        InputStream inIs = null;
        String result = "";
        try {
            process = new ProcessBuilder().command(command).start();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = -1;
            errIs = process.getErrorStream();
            while ((read = errIs.read()) != -1) {
                baos.write(read);
            }
            inIs = process.getInputStream();
            while ((read = inIs.read()) != -1) {
                baos.write(read);
            }
            result = new String(baos.toByteArray());
            inIs.close();
            errIs.close();
            process.destroy();
        } catch (IOException e) {
            Log.e("安装", e.getLocalizedMessage());
            result = e.getMessage();
        }
        return result;
    }

    public static void installApk(String filePath) {
        Log.e("静默安装开始", filePath);
        execCommand("pm", "install", "-r", filePath);
    }
}
