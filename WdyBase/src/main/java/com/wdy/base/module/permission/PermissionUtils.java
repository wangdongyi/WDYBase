package com.wdy.base.module.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import com.wdy.base.module.dialog.DialogMUtil;
import com.wdy.base.module.listen.NoDoubleClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 作者：王东一
 * 创建时间：2020/7/8.
 */

public class PermissionUtils {
    private static final int CODE_REQUEST_PERMISSION = 900;

    private static int mRequestCode = CODE_REQUEST_PERMISSION;
    private static Map<Integer, PermissionGrant> callbackMap = new HashMap<>();

    public interface PermissionGrant {

        /**
         * 当所有权限的申请被用户同意之后,该方法会被调用
         */
        void onPermissionGranted();

        /**
         * 当权限申请中的某一个或多个权限,被用户否定了
         *
         * @param permissions  本次授权权限列表
         * @param grantResults 本次授权结果,0:授权成功 -1:拒绝授权
         */
        void onPermissionDenied(String[] permissions, int[] grantResults);
    }

    /**
     * 跳转到系统设置界面去开启权限
     *
     * @param context 上下文
     * @param message 提示内容
     */
    public static void openSystemSetting(final Context context, String message) {
        DialogMUtil.getInstance().with(context, "温馨提示", message, new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                startAppSettings(context);
                DialogMUtil.Dismiss();
            }
        });
    }

    /**
     * 启动应用的设置
     */
    private static void startAppSettings(Context context) {
        try {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getRequestCode() {
        mRequestCode++;
        return mRequestCode;
    }

    /************************       执行授权     *******************************/

    public static boolean checkPermission(final Object object, final int requestCode, String[] requestPermissions, String message) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Context context = getActivityOrFragmentContext(object);
            if (context == null) {
//                L.e("requestMultiPermissionsAll======>object 必须为 activity 或 fragment");
                return false;
            }
            //获取没有授权的权限
            final List<String> permissionList = getNoGrantedPermission(object, requestPermissions, false);
            //获取上次被拒权限列表
            final List<String> shouldRationalePermissionsList = getNoGrantedPermission(object, requestPermissions, true);

            if (permissionList.size() > 0) {
                executePermissionsRequest(object, permissionList.toArray(new String[0]), requestCode);
                return false;
            } else if (shouldRationalePermissionsList.size() > 0) {
                DialogMUtil.getInstance().with(context, "温馨提示", "应用缺少权限，是否重新去授权？", new NoDoubleClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {
                        DialogMUtil.Dismiss();
                        executePermissionsRequest(object, shouldRationalePermissionsList.toArray(new String[0]), requestCode);
                    }
                });
                return false;
            } else {
                for (String requestPermission : requestPermissions) {
                    int checkSelfPermission = PermissionChecker.checkSelfPermission(context, requestPermission);
                    if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                        if (!TextUtils.isEmpty(message)) {
                            openSystemSetting(context, message);
                        }
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 返回权限检查结果
     */
    private static void handleResult(int requestCode, String[] permissions, int[] grantResults) {
        if (callbackMap.containsKey(requestCode)) {
            PermissionGrant permissionGrant = callbackMap.get(requestCode);
            boolean hasPermission = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    hasPermission = false;
                    break;
                }
            }
            if (hasPermission) {
                assert permissionGrant != null;
                permissionGrant.onPermissionGranted();
            } else {
                assert permissionGrant != null;
                permissionGrant.onPermissionDenied(permissions, grantResults);
            }
            callbackMap.remove(requestCode);
        }
    }

    /**
     * 一次申请多个权限
     * object 必须为 activity 或 fragment
     */
    public static void requestMultiPermissionsAll(Object object, String[] requestPermissions, PermissionGrant grant) {

        int requestCode = getRequestCode();
        callbackMap.put(requestCode, grant);

        Context context = getActivityOrFragmentContext(object);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestMultiPermissionsM(object, requestCode, requestPermissions);
        } else {
            assert context != null;
            PackageManager pkm = context.getPackageManager();
            String packageName = context.getPackageName();

            int[] grantResults = new int[requestPermissions.length];
            for (int i = 0; i < requestPermissions.length; i++) {
                grantResults[i] = pkm.checkPermission(requestPermissions[i], packageName);
            }
            handleResult(requestCode, requestPermissions, grantResults);
        }
    }

    /**
     * 一次申请多个权限 M
     */
    private static void requestMultiPermissionsM(final Object object, final int requestCode, String[] requestPermissions) {

        //获取没有授权的权限
        final List<String> permissionList = getNoGrantedPermission(object, requestPermissions, false);
        //获取上次被拒权限列表
        final List<String> shouldRationalePermissionsList = getNoGrantedPermission(object, requestPermissions, true);

        if (permissionList.size() > 0) {//去授权
            executePermissionsRequest(object, permissionList.toArray(new String[0]), requestCode);
        } else if (shouldRationalePermissionsList.size() > 0) {
            DialogMUtil.getInstance().with(getActivityOrFragmentContext(object), "温馨提示", "应用缺少权限，是否重新去授权？", new NoDoubleClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    DialogMUtil.Dismiss();
                    executePermissionsRequest(object, shouldRationalePermissionsList.toArray(new String[0]), requestCode);
                }
            });
        } else {
            int[] grantResults = new int[requestPermissions.length];
            handleResult(requestCode, requestPermissions, grantResults);
        }
    }

    private static ArrayList<String> getNoGrantedPermission(Object object, String[] requestPermissions, boolean isShouldRationale) {
        ArrayList<String> permissions = new ArrayList<>();
        for (String requestPermission : requestPermissions) {
            int checkSelfPermission = PermissionChecker.checkSelfPermission(Objects.requireNonNull(getActivityOrFragmentContext(object)), requestPermission);

            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                boolean flag = shouldShowRequestPermissionRationale(object, requestPermission);
                if (flag) {
                    if (isShouldRationale) {
                        permissions.add(requestPermission);
                    }

                } else {
                    if (!isShouldRationale) {
                        permissions.add(requestPermission);
                    }
                }
            }
        }
        return permissions;
    }

    /**
     * activity回调处理授权
     *
     * @param requestCode  Need consistent with requestPermission
     * @param permissions 权限
     * @param grantResults 放回结果
     */
    public static void requestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        handleResult(requestCode, permissions, grantResults);
    }


    @TargetApi(23)
    private static Context getActivityOrFragmentContext(Object object) {
        if (object instanceof Activity) {
            return (Activity) object;
        } else if (object instanceof Fragment) {
            return ((Fragment) object).getContext();
        } else if (object instanceof android.app.Fragment) {
            return ((android.app.Fragment) object).getContext();
        }
        return null;
    }

    @TargetApi(23)
    private static void executePermissionsRequest(Object object, String[] permissions, int requestCode) {
        if (object instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) object, permissions, requestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).requestPermissions(permissions, requestCode);
        } else if (object instanceof android.app.Fragment) {
            ((android.app.Fragment) object).requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(23)
    private static boolean shouldShowRequestPermissionRationale(Object object, String perm) {
        if (object instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) object, perm);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else if (object instanceof android.app.Fragment) {
            return ((android.app.Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else {
            return false;
        }
    }
}
