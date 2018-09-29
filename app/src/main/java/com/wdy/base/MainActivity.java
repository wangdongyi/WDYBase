package com.wdy.base;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.wdy.base.module.base.WDYBaseActivity;
import com.wdy.base.module.dialog.DialogAddress;
import com.wdy.base.module.dialog.DialogFailed;
import com.wdy.base.module.dialog.DialogMUtil;
import com.wdy.base.module.dialog.DialogSinge;
import com.wdy.base.module.dialog.DialogSuccess;
import com.wdy.base.module.listen.NoDoubleClickListener;
import com.wdy.base.module.permission.PMUtil;
import com.wdy.base.module.view.adresse.AddressPickerView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends WDYBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<String> pList = new ArrayList<>();
        pList.add(Manifest.permission.READ_PHONE_STATE);
        pList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        pList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        new PMUtil(this, pList, new PMUtil.OnPermissionBack() {
            @Override
            public void permissionBack(boolean grant) {
                if (!grant) {
                    // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。

                    DialogMUtil.getInstance().with(MainActivity.this, "提示", "应用缺少必要的权限！是否前去设置，打开所需要的权限。", new NoDoubleClickListener() {
                        @Override
                        protected void onNoDoubleClick(View v) {
                            DialogMUtil.Dismiss();
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            startActivity(intent);
                        }
                    });
                }
            }
        });
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

    public void DialogAddress(View view) {
        DialogAddress.getInstance().with(this, new AddressPickerView.OnAddressPickerSureListener() {
            @Override
            public void onSureClick(String address, String provinceCode, String cityCode, String districtCode) {
                DialogAddress.Dismiss();
                DialogMUtil.getInstance().with(MainActivity.this, "提示", address + "\n" + provinceCode + "\n" + cityCode, new NoDoubleClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {
                        DialogMUtil.Dismiss();
                    }
                });
            }
        });
    }
}
