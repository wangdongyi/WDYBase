package com.wdy.base.module.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wdy.base.module.listen.WDYKeyboardShow;
import com.wdy.base.module.permission.OnPermissionBack;
import com.wdy.base.module.permission.PermissionUtils;
import com.wdy.base.module.status.StatusBarUtil;
import com.wdy.base.module.util.ActivityManage;
import com.wdy.base.module.util.CodeUtil;
import com.wdy.base.module.util.ToastUtil;

import java.util.List;

/**
 * 作者：王东一
 * 创建时间：2018/9/11.
 */
public class WDYBaseActivity extends AppCompatActivity {
    private View Main;
    private WDYKeyboardShow wdyKeyboardShow;
    protected Boolean isShowKeyboard = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManage.getInstance().addActivity(this);
        wdyInitKeyboard();
    }

    private void wdyInitKeyboard() {
        Main = findViewById(android.R.id.content);
        Main.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            Main.getWindowVisibleDisplayFrame(r);
            if (Main.getRootView().getHeight() - (r.bottom - r.top) > CodeUtil.dip2px(WDYBaseActivity.this, 100)) {
                isShowKeyboard = true;
                if (getWdyKeyboardShow() != null) {
                    getWdyKeyboardShow().isShow(true);
                }
            } else {
                isShowKeyboard = false;
                if (getWdyKeyboardShow() != null)
                    getWdyKeyboardShow().isShow(false);
            }
        });
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private WDYKeyboardShow getWdyKeyboardShow() {
        return wdyKeyboardShow;
    }

    public void setWdyKeyboardShow(WDYKeyboardShow wdyKeyboardShow) {
        this.wdyKeyboardShow = wdyKeyboardShow;
    }

    protected void showToast(String content) {
        ToastUtil.getToast(getActivity()).showMiddleToast(content);
    }

    protected Activity getActivity() {
        return this;
    }

    protected void isHomeActivity() {

    }


    /**
     * 关闭软键盘
     */
    protected void closeSoftKeyboard() {
        if (isShowKeyboard) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 动态申请权限
     *
     * @param permissions 要申请的权限
     * @param grant       权限申请的回掉
     */
    public void getPermissions(String[] permissions, PermissionUtils.PermissionGrant grant) {
        PermissionUtils.requestMultiPermissionsAll(getActivity(), permissions, grant);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeSoftKeyboard();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManage.getInstance().removeActivity(this);
    }
}
