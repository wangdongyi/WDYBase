package com.wdy.base.module.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.wdy.base.module.R;
import com.wdy.base.module.util.CodeUtil;

/**
 * 作者：王东一 on 2016/4/5 14:51
 **/
public class DialogUtil {
    //等待提示对话框
    private static Dialog dialog = null;

    public static void show(Context mContext) {
        if (dialog == null && mContext != null) {
            dialog = new Dialog(mContext, R.style.DialogStyle);
            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(mContext).inflate(R.layout.loading_layout, null);
            view.setOnClickListener(v -> dialog.dismiss());
            dialog.setContentView(view);
            dialog.setCancelable(true);
            Window win = dialog.getWindow();
            assert win != null;
            win.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams params = win.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            params.y = CodeUtil.getScreenHeight(mContext);
            win.setAttributes(params);
            dialog.show();
        }

    }

    public static void show(Context mContext, boolean cancel) {
        if (dialog == null) {
            dialog = new Dialog(mContext, R.style.DialogStyle);
            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(mContext).inflate(R.layout.loading_layout, null);
            dialog.setContentView(view);
            dialog.setCancelable(cancel);
            Window win = dialog.getWindow();
            assert win != null;
            win.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams params = win.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            params.y = CodeUtil.getScreenHeight(mContext);
            win.setAttributes(params);
            dialog.show();
        }
    }

    public static void dismiss() {
        if (null != dialog) {
            dialog.dismiss();
            dialog = null;
        }
    }

}
