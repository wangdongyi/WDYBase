package com.wdy.base;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.wdy.base.module.base.WDYBaseActivity;
import com.wdy.base.module.dialog.DialogAddress;
import com.wdy.base.module.dialog.DialogCity;
import com.wdy.base.module.dialog.DialogFailed;
import com.wdy.base.module.dialog.DialogMUtil;
import com.wdy.base.module.dialog.DialogSinge;
import com.wdy.base.module.dialog.DialogSuccess;
import com.wdy.base.module.dialog.DialogUtil;
import com.wdy.base.module.listen.NoDoubleClickListener;
import com.wdy.base.module.view.adresse.AddressPickerView;
import com.wdy.base.module.view.adresse.CityPickerView;


public class MainActivity extends WDYBaseActivity {

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
