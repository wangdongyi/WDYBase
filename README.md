# WDYBase
我的基础包
[![](https://jitpack.io/v/wangdongyi/WDYBase.svg)](https://jitpack.io/#wangdongyi/WDYBase)
、、、
权限调用
//PMUtil(AppCompatActivity activity, List<String> permissions, OnPermissionBack onPermissionBack) 
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
、、、
