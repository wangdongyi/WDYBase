package com.wdy.base.module.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.wdy.base.module.R;
import com.wdy.base.module.util.CodeUtil;
import com.wdy.base.module.view.adresse.AddressPickerView;
import com.wdy.base.module.view.adresse.CityPickerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 作者：王东一
 * 创建时间：2018/9/6.
 */
public class DialogCity {
    @SuppressLint("StaticFieldLeak")
    private static DialogCity mInstance;
    private static AlertDialog mAlertDialog = null;
    private Context mContext;
    private View layout;
    private ViewHolder viewHolder;
    private CityPickerView.OnAddressPickerSureListener listener;

    public static DialogCity getInstance() {
        if (mInstance == null) {
            synchronized (DialogCity.class) {
                if (mInstance == null) {
                    mInstance = new DialogCity();
                }
            }
        }
        return mInstance;
    }

    public void with(Context context, CityPickerView.OnAddressPickerSureListener listener) {
        this.mContext = context;
        this.listener = listener;
        initDialog();
    }


    @SuppressLint("InflateParams")
    private void initDialog() {
        if (mAlertDialog == null) {
            LayoutInflater inflaterDl = LayoutInflater.from(mContext);
            layout = inflaterDl.inflate(R.layout.wdy_dialog_city, null);
            mAlertDialog = new AlertDialog.Builder(mContext, R.style.WDYBottomDialog).create();
            viewHolder = new ViewHolder(layout);
            viewHolder.apvAddress.setOnAddressPickerSure(listener);
        }
        mAlertDialog.show();
        WindowManager.LayoutParams lp = mAlertDialog.getWindow().getAttributes();
        lp.width = CodeUtil.getScreenWidth(mContext); //设置宽度
        lp.gravity = Gravity.CENTER;
        mAlertDialog.getWindow().setAttributes(lp);
        mAlertDialog.getWindow().setContentView(layout, lp);
        mAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        mAlertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mAlertDialog = null;
            }
        });
    }


    public static void Dismiss() {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
    }

    public static class ViewHolder {
        public View rootView;
        public CityPickerView apvAddress;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.apvAddress = (CityPickerView) rootView.findViewById(R.id.apvAddress);
        }

    }
}
