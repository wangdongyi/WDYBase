package com.wdy.base.module.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.wdy.base.module.listen.WDYKeyboardShow;
import com.wdy.base.module.status.StatusBarUtil;
import com.wdy.base.module.util.ActivityManage;
import com.wdy.base.module.util.CodeUtil;

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
        Main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
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
            }
        });
    }

    private WDYKeyboardShow getWdyKeyboardShow() {
        return wdyKeyboardShow;
    }

    public void setWdyKeyboardShow(WDYKeyboardShow wdyKeyboardShow) {
        this.wdyKeyboardShow = wdyKeyboardShow;
    }

    protected Activity getActivity() {
        return this;
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
