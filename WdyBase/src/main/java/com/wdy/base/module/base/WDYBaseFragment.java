package com.wdy.base.module.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wdy.base.module.permission.PermissionUtils;
import com.wdy.base.module.util.ToastUtil;

/**
 * 作者：王东一
 * 创建时间：2019-05-14.
 */
public abstract class WDYBaseFragment extends Fragment {
    //懒加载
    protected boolean isViewShown = false;
    protected View main;
    protected boolean isBuild = false;
    protected Context WDYContext;
    private boolean isFirstLoad = true; // 是否第一次加载

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        WDYContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(WDYContext).inflate(getContentViewId(), null);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoad) {
            // 将数据加载逻辑放到onResume()方法中
            initData();
            isFirstLoad = false;
        }
    }

    /**
     * 设置布局资源id
     *
     * @return layout
     */
    protected abstract int getContentViewId();

    /**
     * 初始化视图
     *
     * @param view 父
     */
    protected void initView(View view) {

    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }


    protected void showToast(String content) {
        ToastUtil.getToast(WDYContext).showMiddleToast(content);
    }

    /**
     * 动态申请权限
     *
     * @param permissions 要申请的权限
     * @param grant       权限申请的回掉
     */
    public void getPermissions(String[] permissions, PermissionUtils.PermissionGrant grant) {
        PermissionUtils.requestMultiPermissionsAll(getActivity(), permissions, grant);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFirstLoad = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        WDYContext = null;
    }
}
