package com.wdy.base.module.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.wdy.base.module.R;
import com.wdy.base.module.listen.NoDoubleClickListener;
import com.wdy.base.module.util.CodeUtil;

/**
 * 作者：王东一
 * 创建时间：2018/9/3.
 */
public class DialogSuccess {
    @SuppressLint("StaticFieldLeak")
    private static DialogSuccess mInstance;
    private static AlertDialog mAlertDialog = null;
    private Context mContext;
    private String content;
    private View layout;
    private ViewHolder viewHolder;

    public static DialogSuccess getInstance() {
        if (mInstance == null) {
            synchronized (DialogSuccess.class) {
                if (mInstance == null) {
                    mInstance = new DialogSuccess();
                }
            }
        }
        return mInstance;
    }

    public void with(Context context, String content) {
        this.mContext = context;
        this.content = content;
        initDialog();
    }


    @SuppressLint("InflateParams")
    private void initDialog() {
        if (mAlertDialog == null) {
            LayoutInflater inflaterDl = LayoutInflater.from(mContext);
            layout = inflaterDl.inflate(R.layout.wdy_dialog_success, null);
            mAlertDialog = new AlertDialog.Builder(mContext, R.style.WDYDialog).create();
            viewHolder = new ViewHolder(layout);
            viewHolder.textView_content.setText(content);
            viewHolder.textView.setOnClickListener(new NoDoubleClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    Dismiss();
                }
            });
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
    

    public static class ViewHolder {
        View rootView;
        public TextView textView_content;
        public TextView textView;

        ViewHolder(View rootView) {
            this.rootView = rootView;
            this.textView_content = (TextView) rootView.findViewById(R.id.textView_content);
            this.textView = (TextView) rootView.findViewById(R.id.textView);
        }

    }

    private static void Dismiss() {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
    }

   
}
