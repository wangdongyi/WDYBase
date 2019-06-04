# WDYBase
[![](https://jitpack.io/v/wangdongyi/WDYBase.svg)](https://jitpack.io/#wangdongyi/WDYBase)
代码远程仓库 maven { url "https://raw.githubusercontent.com/wangdongyi/WDYBaseMeaven/master" }
implementation 'com.wdy.base.module:WDYBase:1.2.5'
我的基础包
## 权限调用
```
 PMUtil(AppCompatActivity activity, List<String> permissions, OnPermissionBack onPermissionBack)//构造函数
List<String> pList = new ArrayList<>();
        pList.add(Manifest.permission.READ_PHONE_STATE);
        pList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        pList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        new PMUtil(this, pList, new PMUtil.OnPermissionBack() {
            @Override
            public void permissionBack(boolean grant) {
                if (!grant) {
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
```
## 常用提示框
```
 DialogSinge.getInstance().with(this, "这是一个单独的提示");
 //
 DialogMUtil.getInstance().with(this, "提示", "这是一个Material风格的提示框", new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                DialogMUtil.Dismiss();
            }
        });
 //
 DialogSuccess.getInstance().with(this, "成功\n这是一个Material风格的提示框");
 //
 DialogFailed.getInstance().with(this, "失败\n这是一个Material风格的提示框");
 //
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
```
## 下载apk并安装
```
  Intent intentDownload = new Intent(MainActivity.this, WDYDownloadService.class);
  intentDownload.putExtra("downloadUrl", url);
  intentDownload.putExtra("downloadAppName", "zhitou_all_release_3_4_0.apk");
  startService(intentDownload);
```
