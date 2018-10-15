package com.wdy.base.module.permission;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wdy.base.module.dialog.DialogMUtil;
import com.wdy.base.module.listen.NoDoubleClickListener;

import java.util.ArrayList;
import java.util.List;

import wdy.life.library.LifeManager;
import wdy.life.library.PermissionListener;
import wdy.life.library.SupportActLifeListenerFragment;

/**
 * 作者：王东一
 * 创建时间：2018/9/29.
 */
public class PMUtil {
    private AppCompatActivity activity;
    private List<String> permissions;
    private OnPermissionBack onPermissionBack;
    private WDYHandler wdyHandler;

    public PMUtil(AppCompatActivity activity, List<String> permissions, OnPermissionBack onPermissionBack) {
        this.activity = activity;
        this.permissions = permissions;
        this.onPermissionBack = onPermissionBack;
        wdyHandler = new WDYHandler();
        bandLeft();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void bandLeft() {
        LifeManager.getInstance().ObserveActivity(activity, new PermissionListener() {
            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                if (requestCode == 1989) {
                    if (hasAllPermissionsGranted(grantResults))
                        wdyHandler.sendEmptyMessage(2015);
                    else {
                        wdyHandler.sendEmptyMessage(2016);
                    }
                }
            }

            @Override
            public void onCreate(Bundle bundle) {

            }

            @Override
            public void onStart() {
                startP();
            }

            @Override
            public void onResume() {

            }

            @Override
            public void onPause() {

            }

            @Override
            public void onStop() {

            }

            @Override
            public void onDestroy() {

            }

            @Override
            public void onActivityResult(int i, int i1, Intent intent) {

            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void startP() {
        List<String> lackedPermission = new ArrayList<String>();
        for (int i = 0; i < permissions.size(); i++) {
            if (!(activity.checkSelfPermission(permissions.get(i)) == PackageManager.PERMISSION_GRANTED)) {
                lackedPermission.add(permissions.get(i));
            }
        }
        // 权限都已经有了，那么直接调用SDK
        if (lackedPermission.size() == 0) {
            wdyHandler.sendEmptyMessage(2015);
        } else {
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            SupportActLifeListenerFragment fragment = LifeManager.getInstance().findFragment(activity.getFragmentManager());
            fragment.LifeRequestPermissions(requestPermissions, 1989);
        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @SuppressLint("HandlerLeak")
    class WDYHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2015:
                    onPermissionBack.permissionBack(true);
                    break;
                case 2016:
                    onPermissionBack.permissionBack(false);
                    break;
            }
        }
    }

    public interface OnPermissionBack {
        void permissionBack(boolean grant);
    }

    public void showOpenP() {
        DialogMUtil.getInstance().with(activity, "提示", "应用缺少必要的权限！请点击确定，打开所有的权限。", new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                DialogMUtil.Dismiss();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                activity.startActivity(intent);
            }
        });
    }
}
