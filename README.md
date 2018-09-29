# WDYBase
[![](https://jitpack.io/v/wangdongyi/WDYBase.svg)](https://jitpack.io/#wangdongyi/WDYBase)
我的基础包
## 权限调用
、、、
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
、、、
