package com.wdy.base;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.wdy.base.module.base.WDYBaseActivity;
import com.wdy.base.module.dialog.DialogAddress;
import com.wdy.base.module.dialog.DialogFailed;
import com.wdy.base.module.dialog.DialogMUtil;
import com.wdy.base.module.dialog.DialogSinge;
import com.wdy.base.module.dialog.DialogSuccess;
import com.wdy.base.module.dialog.DialogUtil;
import com.wdy.base.module.download.WDYDownloadService;
import com.wdy.base.module.listen.NoDoubleClickListener;
import com.wdy.base.module.permission.PMUtil;
import com.wdy.base.module.view.adresse.AddressPickerView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends WDYBaseActivity {
private String url="https://lcoriginal.s3.cn-north-1.amazonaws.com.cn/4454ffde-e010-4368-863c-fc14b5e37aad?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAOYYVHPTPAKZAWURQ%2F20181024%2Fcn-north-1%2Fs3%2Faws4_request&X-Amz-Date=20181024T034213Z&X-Amz-Expires=3600&X-Amz-Signature=be634f69fa02194eb9f39e6a81b29855326074436932fa6ee09f58f78605945b&X-Amz-SignedHeaders=host&response-content-disposition=attachment%3B%20filename%2A%3Dutf-8%27%27zhitou_all_release_3_4_0.apk";
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
        DialogUtil.show(this);
    }

    public void singeDialog(View view) {
        DialogSinge.getInstance().with(this, "这是一个单独的提示");
    }

    public void mDialog(View view) {
        DialogMUtil.getInstance().with(this, "提示", "这是一个Material风格的提示框，点击确定下载。", new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                Intent intentDownload = new Intent(MainActivity.this, WDYDownloadService.class);
                intentDownload.putExtra("downloadUrl", url);
                intentDownload.putExtra("downloadAppName", "zhitou_all_release_3_4_0.apk");
                startService(intentDownload);
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
