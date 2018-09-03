package com.wdy.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wdy.base.module.dialog.DialogFailed;
import com.wdy.base.module.dialog.DialogMUtil;
import com.wdy.base.module.dialog.DialogSinge;
import com.wdy.base.module.dialog.DialogSuccess;
import com.wdy.base.module.dialog.DialogUtil;
import com.wdy.base.module.listen.NoDoubleClickListener;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void singeDialog(View view) {
        DialogSinge.getInstance().with(this, "这是一个单独的提示");
    }

    public void mDialog(View view) {
        DialogMUtil.getInstance().with(this, "提示", "这是一个Material风格的提示框", new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                DialogMUtil.Dismiss();
            }
        });
    }

    public void successDialog(View view) {
        DialogSuccess.getInstance().with(this, "成功\n这是一个Material风格的提示框");
    }

    public void failedDialog(View view) {
        DialogFailed.getInstance().with(this, "失败\n这是一个Material风格的提示框");
    }
}
