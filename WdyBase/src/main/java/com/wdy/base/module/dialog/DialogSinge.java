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
 * 创建时间：2018/8/29.
 */
public class DialogSinge {
    @SuppressLint("StaticFieldLeak")
    private static DialogSinge mInstance;
    private static AlertDialog mAlertDialog = null;
    private Context mContext;
    private String button = "确定";
    private String content;
    private String title = "提示";
    private View layout;
    private ViewHolder viewHolder;

    public static DialogSinge getInstance() {
        if (mInstance == null) {
            synchronized (DialogSinge.class) {
                if (mInstance == null) {
                    mInstance = new DialogSinge();
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

    public void with(Context context, String title, String content) {
        this.mContext = context;
        this.title = title;
        this.content = content;
        initDialog();
    }

    public void with(Context context, String title, String content, String button) {
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.button = button;
        initDialog();
    }

    @SuppressLint("InflateParams")
    private void initDialog() {
        if (mAlertDialog == null) {
            LayoutInflater inflaterDl = LayoutInflater.from(mContext);
            layout = inflaterDl.inflate(R.layout.wdy_dialog_singe, null);
            mAlertDialog = new AlertDialog.Builder(mContext, R.style.WDYDialog).create();
            viewHolder = new ViewHolder(layout);
            viewHolder.textView.setOnClickListener(new NoDoubleClickListener() {
                @Override
                protected void onNoDoubleClick(View view) {
                    Dismiss();
                }
            });
            viewHolder.textView_subtitle.setText(title);
            viewHolder.textView_content.setText(content);
            viewHolder.textView.setText(button);
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
        public TextView textView_subtitle;
        public TextView textView_content;
        public TextView textView;

        ViewHolder(View rootView) {
            this.rootView = rootView;
            this.textView_subtitle = (TextView) rootView.findViewById(R.id.textView_subtitle);
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
