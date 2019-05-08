package com.wdy.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;

import java.io.DataOutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;

import static com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION;

/**
 * 作者：王东一
 * 创建时间：2018/12/10.
 */
public class VideoActivity extends AppCompatActivity {
    private String urlVideo = "https://video.pearvideo.com/mp4/adshort/20181207/cont-1487975-13333880_adpkg-ad_hd.mp4";
    private JZVideoPlayerStandardLoopVideo videoplayer;
    private TextView text;
    private String pa;

    private List<ResolveInfo> apps = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initView();
    }

    private Handler myhander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                text.setText(pa);
            }
        }
    };

    @SuppressLint("WrongConstant")
    private void initView() {
        videoplayer = (JZVideoPlayerStandardLoopVideo) findViewById(R.id.videoplayer);
        videoplayer.setUp(urlVideo, "", Jzvd.SCREEN_WINDOW_NORMAL);
        loadVideoScreenshot(this, urlVideo, videoplayer.thumbImageView, 15000);
        videoplayer.startVideo();
        AudioMngHelper audioMngHelper = new AudioMngHelper(this);
        audioMngHelper.setAudioType(AudioManager.STREAM_MUSIC);
//        for (int i = 0; i < audioMngHelper.getSystemMaxVolume(); i++) {
//            audioMngHelper.addVoice100();
//        }


//        Jzvd.startFullscreen(this, JzvdStd.class,urlVideo,"");
//        Process p = null;
//        try {
//            p = Runtime.getRuntime().exec("ls");
//            String data = null;
//            BufferedReader ie = new BufferedReader(new InputStreamReader(p.getErrorStream()));
//            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
//            String error = null;
//            while ((error = ie.readLine()) != null
//                    && !error.equals("null")) {
//                data += error + "\n";
//            }
//            String line = null;
//            while ((line = in.readLine()) != null
//                    && !line.equals("null")) {
//                data += line + "\n";
//            }
//
//            Log.e("ls", data);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        execCmd("reboot");
//        PackageManager packageManager = getPackageManager();
//        String app="com.speedsoftware.rootexplorer";
//        if (checkPackInfo(app)) {
//            Intent intent = packageManager.getLaunchIntentForPackage(app);
//            startActivity(intent);
//        } else {
//            Toast.makeText(this, "没有安装" + app, 1).show();
//        }
//        text = (TextView) findViewById(R.id.text);
//        loadApps();
    }

    private void loadApps() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<PackageInfo> apps = getPackageManager().getInstalledPackages(0);
//        apps = getPackageManager().queryIntentActivities(intent, 0);
        //for循环遍历ResolveInfo对象获取包名和类名
        for (int i = 0; i < apps.size(); i++) {
            PackageInfo info = apps.get(i);
            String packageName = info.packageName;
            ApplicationInfo appInfo = info.applicationInfo;
            CharSequence name = appInfo.loadLabel(getPackageManager());
            if ("R.E管理器".equals(name)) {
                pa = packageName + "-" + name + "\n";
            }

        }
        myhander.sendEmptyMessage(1);

    }

    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            pa = pa + packageInfo.packageName + "\n";
            return pa;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检查包是否存在
     *
     * @param packname
     * @return
     */
    private boolean checkPackInfo(String packname) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(packname, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
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
        Glide.with(context).load(uri).apply(requestOptions).into(imageView);
    }

    public static boolean execCmd(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}
