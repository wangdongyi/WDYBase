package com.wdy.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.wdy.base.activity.RefreshActivity;
import com.wdy.base.module.dialog.DialogFailed;
import com.wdy.base.module.dialog.DialogMUtil;
import com.wdy.base.module.dialog.DialogSinge;
import com.wdy.base.module.dialog.DialogSuccess;
import com.wdy.base.module.listen.NoDoubleClickListener;
import com.wdy.base.module.photopicker.utils.PhotoUtils;
import com.wdy.base.module.view.banner.BannerView;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;

import static com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION;


public class MainActivity extends AppCompatActivity {
    public final static String TAG = "WDY";
    private String url = "http://oss-cn-hangzhou.aliyuncs.com/zm-static/product-download/zhitou_all_develop_3_7_104.apk";
    private int x, y;
    public final String rootPowerCommand = "chmod 777 /dev/block/mmcblk0";
    private JZVideoPlayerStandardLoopVideo videoplayer;
    private String urlVideo = "https://video.pearvideo.com/mp4/adshort/20181207/cont-1487975-13333880_adpkg-ad_hd.mp4";
    private Handler handler;
    private OrientationSensorListener listener;
    private SensorManager sm;
    private Sensor sensor;
    private ZMImagePlay zmImagePlay;
    private ImageView image;
    private BannerView banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initView();
//        handler = new ChangeOrientationHandler(this);
//
//        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        listener = new OrientationSensorListener(handler);
//        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
//        WDYHttpBase.getInstance().getEasyAsyncHttp("url", new WDYHttpBase.OnWDYEasyHttpCallback() {
//            @Override
//            public void onFailure(IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Object response, String json) {
//
//            }
//        });
//        String extension = MimeTypeMap.getFileExtensionFromUrl(urlVideo);
//        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
//        Intent mediaIntent = new Intent(Intent.ACTION_VIEW);
//        mediaIntent.setDataAndType(Uri.parse(urlVideo), mimeType);
//        startActivity(mediaIntent);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        String path = "/mnt/internal_sd/bluetooth/m1.mp4";//该路径可以自定义
//        File file = new File(path);
//        if(file.exists()){
//            Uri uri = Uri.fromFile(file);
//            intent.setDataAndType(uri, "video/*");
//            startActivity(intent);
//        }else {
//            Toast.makeText(this,"没有找到视频",1).show();
//        }
/**
 * 方式一
 * */
//        if(new File("/system/bin/su").exists()||new File("/system/xbin/su").exists()){
//            Toast.makeText(this,"有root 权限！！！！！！！！！",1).show();;
//        }else {
//            Toast.makeText(this,"没有root 权限---------------",1).show();;
//        }
//        List<String> pList = new ArrayList<>();
//        pList.add(Manifest.permission.READ_PHONE_STATE);
//        pList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        pList.add(Manifest.permission.ACCESS_FINE_LOCATION);
//        new PMUtil(this, pList, new PMUtil.OnPermissionBack() {
//            @Override
//            public void permissionBack(boolean grant) {
//                if (!grant) {
//                    // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
//
//                    DialogMUtil.getInstance().with(MainActivity.this, "提示", "应用缺少必要的权限！是否前去设置，打开所需要的权限。", new NoDoubleClickListener() {
//                        @Override
//                        protected void onNoDoubleClick(View v) {
//                            DialogMUtil.Dismiss();
//                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                            intent.setData(Uri.parse("package:" + getPackageName()));
//                            startActivity(intent);
//                        }
//                    });
//                }
//            }
//        });
//        Intent intent = new Intent(this, VideoActivity.class);
//        startActivity(intent);
//        rootCommand();
//        installApk("/mnt/internal_sd/bluetooth/aaa.apk");
//        videoplayer.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                excuteSuCMD("/mnt/internal_sd/Android/data/com.wdy.base/files/Download/app.apk");
//            }
//        }, 5000);
//        playVideo();
//        playImage();

    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
//        sm.unregisterListener(listener);
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    protected void onResume() {
//        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
        super.onResume();
    }


    public void singeDialog(View view) {
        DialogSinge.getInstance().with(this, "这是一个单独的提示");
    }

    public void mDialog(View view) {
        DialogMUtil.getInstance().with(this, "提示", "这是一个Material风格的提示框，点击确定下载。", new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
//                Intent intentDownload = new Intent(MainActivity.this, DownloadService.class);
//                intentDownload.putExtra("downloadUrl", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1548407022489&di=c6fc90aca20f586b2cebe6d591848635&imgtype=0&src=http%3A%2F%2Fimg8.zol.com.cn%2Fbbs%2Fupload%2F22393%2F22392130.JPG");
//                intentDownload.putExtra("downloadAppName", "MM4.JPG");
//                startService(intentDownload);
//                Intent intentDownload = new Intent(MainActivity.this, WDYDownloadService.class);
//                intentDownload.putExtra("downloadUrl", url);
//                intentDownload.putExtra("downloadAppName", "zhitou_all_release_3_4_0.apk");
//                startService(intentDownload);
//                rootCommand();
//                installApk("/storage/emulated/0/aaa.apk");
//                excuteSuCMD("/mnt/internal_sd/Android/data/com.wdy.base/files/Download/app.apk");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 从Url中下载  下载至 Environment.getExternalStorageDirectory() 目录的update.apk文件 并安装
//                        execLinuxCommand();
//                        clientInstall("/mnt/internal_sd/bluetooth/test.apk");
//                        excuteSuCMD("/mnt/internal_sd/Android/data/com.wdy.base/files/Download/app.apk");
                    }
                }).start();
                PhotoUtils.showPhotoIntent(MainActivity.this, false, 1, 6, new PhotoUtils.OnPhotoBack() {
                    @Override
                    public void onBack(ArrayList<String> result) {
                        Log.e("图片返回", result.size() + "");
                    }
                });

                DialogMUtil.Dismiss();
            }
        });
    }

    private int point = 0;
    ArrayList<String> list = new ArrayList<>();


    private void playVideo() {
        String path = "/mnt/internal_sd/bluetooth/m1.mp4";
        String path2 = "/mnt/internal_sd/bluetooth/mm2.mp4";
        String path3 = "/mnt/internal_sd/bluetooth/mm3.mp4";
//        String path4 = "/mnt/internal_sd/bluetooth/m4.mp4";
//        String path5 = "/mnt/internal_sd/bluetooth/m5.mov";
        String path6 = "/mnt/internal_sd/bluetooth/m7.mp4";
        String path7 = "/mnt/internal_sd/bluetooth/m8.mp4";
        String path8 = "/mnt/internal_sd/bluetooth/m9.mp4";
        String path9 = "/mnt/internal_sd/bluetooth/m10.mp4";
        String path10 = "/mnt/internal_sd/bluetooth/m11.mp4";

        list.add(path);
        list.add(path2);
        list.add(path3);
//        list.add(path4);
//        list.add(path5);
        list.add(path6);
        list.add(path7);
        list.add(path8);
        list.add(path9);
        list.add(path10);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        String path = Environment.getExternalStorageDirectory().getPath()+ "/1.mp4";//该路径可以自定义
//        File file = new File(path);
//        Uri uri = Uri.fromFile(file);
//        intent.setDataAndType(uri, "video/*");
//        startActivity(intent);
//        loadVideoScreenshot(this, list.get(point), videoplayer.thumbImageView, 1000);
        videoplayer.setUp(list.get(point), "", Jzvd.SCREEN_WINDOW_NORMAL);
//        videoplayer.startVideo();
//        videoplayer.startWindowFullscreen();
        videoplayer.startVideo();
        videoplayer.setOnAutoCompletion(new JZVideoPlayerStandardLoopVideo.onAutoCompletion() {
            @Override
            public void onAutoCompletion() {
                change();
            }

            @Override
            public void onStateAutoComplete() {

            }
        });
    }

    private int pointImage = 0;
    ArrayList<String> listImage = new ArrayList<>();

    @SuppressLint("CheckResult")
    private void playImage() {
        listImage.add("/mnt/internal_sd/bluetooth/MM1.JPG");
        listImage.add("/mnt/internal_sd/bluetooth/MM2.JPG");
        listImage.add("/mnt/internal_sd/bluetooth/MM3.JPG");
        zmImagePlay = new ZMImagePlay(this, new ZMImagePlay.OnImageListen() {

            @Override
            public void onFinish() {
                pointImage++;
                if (pointImage >= listImage.size()) {
                    pointImage = 0;
                }
                SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        image.setImageDrawable(resource);
                        zmImagePlay.initTimer(10);
                    }
                };
//                GlideApp.with(MainActivity.this)
//                        .load(listImage.get(pointImage))
//                        .centerCrop()
//                        .into(simpleTarget);
            }
        });
        SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                image.setImageDrawable(resource);
                zmImagePlay.initTimer(10);
            }
        };
//        GlideApp.with(this)
//                .load(listImage.get(0))
//                .centerCrop()
//                .into(simpleTarget);


    }

    private void change() {
        point++;
        if (point >= list.size()) {
            point = 0;
//            videoplayer.changeUrl(list.get(point), "", 0);
//            Jzvd.releaseAllVideos();
//            loadVideoScreenshot(this, list.get(point), videoplayer.thumbImageView, 1000);
//            videoplayer.changeUrl( list.get(point),"",1);

//            JZUtils.setRequestedOrientation(this, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//            videoplayer.clearSavedProgress(this, list.get(point));
        }
//        loadVideoScreenshot(this, list.get(point), videoplayer.thumbImageView, 1000);
//        videoplayer.setUp(list.get(point), "", Jzvd.SCREEN_WINDOW_NORMAL);
        videoplayer.changeUrl(list.get(point), "", 1);
//        videoplayer.startVideo();
        Log.e("播放位置", point + "");
    }

    public void cpAssertVideoToLocalPath() {
        try {
            InputStream myInput;
            OutputStream myOutput = new FileOutputStream("/mnt/internal_sd/bluetooth/m1.mp4");
            myInput = this.getAssets().open("m1.mp4");
            byte[] buffer = new byte[1024];
            int length = myInput.read(buffer);
            while (length > 0) {
                myOutput.write(buffer, 0, length);
                length = myInput.read(buffer);
            }

            myOutput.flush();
            myInput.close();
            myOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startApp(Context context) {
        execRootShellCmd("am start -S  " + context.getPackageName() + "/"
                + MainActivity.class.getCanonicalName() + " \n");
    }

    /**
     * context 上下文
     * uri 视频地址
     * imageView 设置image
     * frameTimeMicros 获取某一时间帧
     */
    @SuppressLint("CheckResult")
    public static void loadVideoScreenshot(final Context context, String uri, ImageView imageView, long frameTimeMicros) {
        RequestOptions requestOptions = RequestOptions.frameOf(frameTimeMicros);
        requestOptions.set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST);
        requestOptions.transform(new BitmapTransformation() {
            @Override
            protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
                return toTransform;
            }

            @Override
            public void updateDiskCacheKey(MessageDigest messageDigest) {
                try {
                    messageDigest.update((context.getPackageName() + "RotateTransform").getBytes("utf-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
//        Glide.with(context).load(uri).apply(requestOptions).into(imageView);
    }

    public void successDialog(View view) {
        DialogSuccess.getInstance().with(this, "成功\n这是一个Material风格的提示框");
    }

    public void failedDialog(View view) {
        DialogFailed.getInstance().with(this, "失败\n这是一个Material风格的提示框");
    }

    public void DialogAddress(View view) {

    }

    private void backHome() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    /**
     * 授权root用户权限
     */
    public void rootCommand() {
        File superuser = new File("/system/bin/superuser");

        if (superuser.exists()) {
            // return device to original state
            Process process = null;
            try {
                process = Runtime.getRuntime().exec("superuser");
                DataOutputStream os = new DataOutputStream(process.getOutputStream());
                os.writeBytes("mount -oremount,rw /dev/block/mtdblock3 /system\n");
                os.writeBytes("busybox cp /system/bin/superuser /system/bin/su\n");
                os.writeBytes("busybox chown 0:0 /system/bin/su\n");
                os.writeBytes("chmod 4755 /system/bin/su\n");
                os.writeBytes("rm /system/bin/superuser\n");
                os.writeBytes("exit\n");
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


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
            Log.e(TAG, e.getLocalizedMessage());
            result = e.getMessage();
        }
        return result;
    }

    public static void installApk(String filePath) {
        Log.e("安装文件", filePath);
        execCommand("pm", "install", "-r", filePath);
    }

    @SuppressLint("LongLogTag")
    protected int excuteSuCMD(String cmd) {
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.println("chmod 777 " + cmd);
            PrintWriter.println("export LD_LIBRARY_PATH=/vendor/lib:/system/lib");
            PrintWriter.println("pm install -r " + cmd);
            PrintWriter.println("exit");
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            Log.e("File.toString()====value=静默安装返回值===", "" + value);
            return (Integer) value;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    /*
    　　@pararm apkPath 等待安装的app全路径，如：/sdcard/app/app.apk
    **/
    private void clientInstall(String apkPath) {
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.println("chmod 777 " + apkPath);
            PrintWriter.println("export LD_LIBRARY_PATH=/vendor/lib:/system/lib");
            PrintWriter.println("pm install -r " + apkPath);
            // PrintWriter.println("exit");
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            Log.e(TAG, "静默安装返回值：" + value);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "安装apk出现异常");
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    private void execLinuxCommand() {
        String cmd = "sleep 120; am start -n 包名/包名.第一个Activity的名称";
        //Runtime对象
        Runtime runtime = Runtime.getRuntime();
        try {
            Process localProcess = runtime.exec("su");
            OutputStream localOutputStream = localProcess.getOutputStream();
            DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
            localDataOutputStream.writeBytes(cmd);
            localDataOutputStream.flush();
            Log.e(TAG, "设备准备重启");
        } catch (IOException e) {
            Log.e(TAG, "strLine:" + e.getMessage());
            e.printStackTrace();

        }
    }

    private void initView() {
        image = (ImageView) findViewById(R.id.image);
        banner = (BannerView) findViewById(R.id.banner);
    }

    public void installApk(Context context, String apkpath) {
        Log.d("qhj", "TestAppota MainActivity-->installApk---apkpath:" + apkpath);
        Intent it = new Intent(Intent.ACTION_VIEW);
        it.setDataAndType(Uri.parse("file://" + apkpath), "application/vnd.android.package-archive");
        // todo 针对不同的手机 以及 sdk 版本, 这里的 uri 地址可能有所不同
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 执行shell命令
     *
     * @param cmds
     * @return
     */
    private static boolean execRootShellCmd(String... cmds) {
        if (cmds == null || cmds.length == 0) {
            return false;
        }
        DataOutputStream dos = null;
        InputStream dis = null;
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("su");// 经过Root处理的android系统即有su命令
            dos = new DataOutputStream(p.getOutputStream());

            for (int i = 0; i < cmds.length; i++) {
                dos.writeBytes(cmds[i] + " \n");
            }
            dos.writeBytes("exit \n");

            int code = p.waitFor();

            return code == 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dos != null) {
                    dos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (dis != null) {
                    dis.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                if (p != null) {
                    p.destroy();
                    p = null;
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        return false;
    }

    public void moveRefreshTest(View view) {
        Intent intent = new Intent(MainActivity.this, RefreshActivity.class);
        startActivity(intent);
    }

}
