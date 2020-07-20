package com.wdy.base.module.permission;

/**
 * 作者：王东一
 * 创建时间：2020/7/8.
 */

public interface OnPermissionBack {
    void onPermissionsGranted();

    void onPermissionsDenied();
}
